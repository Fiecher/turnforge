const { createApp, ref, reactive, computed, onMounted, getCurrentInstance } = Vue
const { createVuetify } = Vuetify

const api = window.API

const app = createApp({
    components: { CrudTable },

    setup() {
        const { proxy } = getCurrentInstance()

        const tab = ref('dashboard')
        const loading = ref(false)
        const token = ref(localStorage.getItem('authToken') || null)

        let parsedUser = null
        try {
            const rawUserInfo = localStorage.getItem('user_info')
            if (rawUserInfo && rawUserInfo !== 'null') {
                parsedUser = JSON.parse(rawUserInfo)
            }
        } catch (e) {
            parsedUser = null
        }
        const user = ref(parsedUser)
        const loginValid = ref(false)
        const auth = reactive({ login: '', password: '' })

        const abilities = ref([])
        const weapons = ref([])
        const characters = ref([])
        const armor = ref([])
        const items = ref([])
        const traits = ref([])

        const snackbar = reactive({ show: false, text: '', color: 'success' })
        const rules = { required: value => !!value || 'Поле обязательно для заполнения' }
        const stats = reactive({ abilities: 0, weapons: 0, characters: 0, armor: 0, items: 0, traits: 0 })

        const headers = {
            abilities: [
                { title: 'ID', key: 'ID' },
                { title: 'Название', key: 'name' },
                { title: 'Урон', key: 'damage' },
                { title: 'Описание', key: 'description' },
                { title: '', key: 'actions' }
            ],
            weapons: [
                { title: 'ID', key: 'ID' },
                { title: 'Название', key: 'name' },
                { title: 'Урон', key: 'damage' },
                { title: 'Описание', key: 'description' },
                { title: '', key: 'actions' }
            ],
            characters: [
                { title: 'ID', key: 'ID' },
                { title: 'Имя', key: 'name' },
                { title: 'Класс', key: 'characterClass' },
                { title: 'Уровень', key: 'level' },
                { title: '', key: 'actions' }
            ],
            armor: [
                { title: 'ID', key: 'ID' },
                { title: 'Название', key: 'name' },
                { title: 'AC', key: 'AC' },
                { title: 'Тип', key: 'type' },
                { title: '', key: 'actions' }
            ],
            items: [
                { title: 'ID', key: 'ID' },
                { title: 'Название', key: 'name' },
                { title: 'Описание', key: 'description' },
                { title: 'Цена', key: 'price' },
                { title: '', key: 'actions' }
            ],
            traits: [
                { title: 'ID', key: 'ID' },
                { title: 'Название', key: 'name' },
                { title: 'Описание', key: 'description' },
                { title: '', key: 'actions' }
            ]
        }

        const isLoggedIn = computed(() => !!token.value)

        const controlledRequest = async (path, method = 'GET', data = null, skipAuthCheck = false) => {
            loading.value = true
            try {
                const currentToken = localStorage.getItem('authToken')
                const response = await api.request(path, method, data, currentToken)
                return response.data
            } catch (error) {
                const status = error.response?.status
                const message = error.response?.data?.message || error.message || 'Ошибка сервера'
                if (!skipAuthCheck && (status === 401 || status === 403)) {
                    logout()
                    showNotify('Необходима повторная авторизация', 'error')
                }
                else if (!skipAuthCheck || (status !== 404 && status !== 401 && status !== 403)) {
                    showNotify(message, 'error')
                }
                throw error
            } finally {
                loading.value = false
            }
        }

        function showNotify(text, color = 'success') {
            snackbar.text = text
            snackbar.color = color
            snackbar.show = true
        }

        async function login() {
            if (!auth.login || !auth.password) return
            try {
                const data = await controlledRequest('/users/login', 'POST', auth, true)
                token.value = data.token
                user.value = data.user || null
                localStorage.setItem('authToken', data.token)
                localStorage.setItem('user_info', JSON.stringify(data.user || null))
                snackbar.show = false
                showNotify('Успешный вход!')
                await fetchAllData()
            } finally {
                auth.password = ''
            }
        }

        async function register() {
            if (!auth.login || !auth.password) return
            try {
                await controlledRequest('/users/register', 'POST', auth, true)
                showNotify('Регистрация успешна! Теперь выполните вход.')
            } finally {
                auth.password = ''
            }
        }

        function logout() {
            token.value = null
            user.value = null
            localStorage.removeItem('authToken')
            localStorage.removeItem('user_info')
            showNotify('Вы вышли из системы')
        }

        async function fetchAbilities() {
            const res = await controlledRequest('/abilities');
            abilities.value = Array.isArray(res) ? res.map(w => ({ ...w, id: w.ID })) : [];
            stats.abilities = abilities.value.length
        }
        async function fetchWeapons() {
            const res = await controlledRequest('/weapons');
            weapons.value = Array.isArray(res) ? res.map(w => ({ ...w, id: w.ID })) : [];
            stats.weapons = weapons.value.length
        }
        async function fetchCharacters() {
            const res = await controlledRequest('/characters')
            characters.value = Array.isArray(res) ? res : []
            stats.characters = characters.value.length
        }
        async function fetchArmor() {
            const res = await controlledRequest('/armor');
            armor.value = Array.isArray(res) ? res.map(w => ({ ...w, id: w.ID })) : [];
            stats.armor = armor.value.length
        }
        async function fetchItems() {
            const res = await controlledRequest('/items');
            items.value = Array.isArray(res) ? res.map(w => ({ ...w, id: w.ID })) : [];
            stats.items = items.value.length
        }
        async function fetchTraits() {
            const res = await controlledRequest('/traits');
            traits.value = Array.isArray(res) ? res.map(w => ({ ...w, id: w.ID })) : [];
            stats.traits = traits.value.length
        }

        async function fetchAllData() {
            if (!isLoggedIn.value) return
            await Promise.all([
                fetchAbilities(),
                fetchWeapons(),
                fetchCharacters(),
                fetchArmor(),
                fetchItems(),
                fetchTraits()
            ])

            // <- вставить сюда:
            window.__globalEntityLists = {
                weapons: weapons.value,
                armor: armor.value,
                items: items.value,
                traits: traits.value,
                abilities: abilities.value
            };
        }

        const crudMethods = ['Ability', 'Weapon', 'Armor', 'Item', 'Trait']
        crudMethods.forEach(type => {
            const list = type.toLowerCase() + 's'
            const cap = type.charAt(0).toUpperCase() + type.slice(1)
            app.config.globalProperties[`create${cap}`] = async function(item) {
                await controlledRequest(`/${list}`, 'POST', item)
                showNotify(`${type} создан`)
                await eval(`fetch${cap}s`)()
            }
            app.config.globalProperties[`update${cap}`] = async function(item) {
                await controlledRequest(`/${list}/${item.ID}`, 'PUT', item)
                showNotify(`${type} обновлён`)
                await eval(`fetch${cap}s`)()
            }
            app.config.globalProperties[`delete${cap}`] = async function(item) {
                await controlledRequest(`/${list}/${item.ID}`, 'DELETE')
                showNotify(`${type} удалён`)
                await eval(`fetch${cap}s`)()
            }
        })

        const extractIdFromRelation = (item) => {
            if (!item) return null

            if (typeof item === 'number') {
                return item
            }

            if (typeof item === 'string') {
                const parts = item.split('_')
                const lastPart = parts[parts.length - 1]
                const n = Number(lastPart)
                if (!Number.isNaN(n) && n > 0) {
                    return n
                }

                const simpleNum = Number(item)
                if (!Number.isNaN(simpleNum) && simpleNum > 0) {
                    return simpleNum
                }
            }

            if (typeof item === 'object') {
                const val = item.id || item.ID || item.weaponId || item.armorId || item.itemId || item.traitId || item.abilityId || item.skillId || item.characterId
                return val ? Number(val) : null
            }
            return null
        }


        function startEditCharacter(character) {
            tab.value = 'characters'

            const prepared = JSON.parse(JSON.stringify(character))

            const extractIds = (list) => {
                if (!Array.isArray(list)) return []
                return list
                    .map(extractIdFromRelation)
                    .filter(id => id !== null && !isNaN(id))
            }

            prepared.weapons = extractIds(prepared.weapons)
            prepared.armor = extractIds(prepared.armor)
            prepared.items = extractIds(prepared.items)
            prepared.traits = extractIds(prepared.traits)
            prepared.abilities = extractIds(prepared.abilities)
            prepared.custom_skills = extractIds(prepared.custom_skills || prepared.skills)

            if (prepared.size) {
                prepared.size = String(prepared.size).toUpperCase()
            }

            setTimeout(() => {
                if (proxy.$refs.charactersCrud?.openEdit) {
                    proxy.$refs.charactersCrud.openEdit(prepared)
                }
            }, 100)
        }

        function startCreateCharacter() {
            tab.value = 'characters'
            setTimeout(() => { if (proxy.$refs.charactersCrud) proxy.$refs.charactersCrud.openCreate() }, 50)
        }

        function prepareCharacterPayload(character) {
            const toIds = (arr) => {
                if (!Array.isArray(arr)) return []

                const ids = arr
                    .map(extractIdFromRelation)
                    .filter(id => id != null && id > 0 && !isNaN(id))
                    .map(id => Number(id))

                return [...new Set(ids)]
            }

            return {
                name: character.name?.trim() || 'Без имени',
                characterClass: character.characterClass || '',
                level: Number(character.level) || 1,
                race: character.race || '',
                age: character.age != null ? Number(character.age) : null,
                size: (character.size || 'MEDIUM').toUpperCase(),
                subclass: character.subclass || '',
                background: character.background || '',
                description: character.description || '',

                strength: Number(character.strength) || 0,
                dexterity: Number(character.dexterity) || 0,
                constitution: Number(character.constitution) || 0,
                intelligence: Number(character.intelligence) || 0,
                wisdom: Number(character.wisdom) || 0,
                charisma: Number(character.charisma) || 0,
                spellcasting_ability: character.spellcasting_ability || '',
                money: Number(character.money) || 0,

                weapons: toIds(character.weapons),
                armor: toIds(character.armor),
                items: toIds(character.items),
                traits: toIds(character.traits),
                abilities: toIds(character.abilities),
                custom_skills: toIds(character.custom_skills || character.skills)
            }
        }

        async function updateCharacter(character) {
            if (!character.ID) {
                return
            }

            const payload = prepareCharacterPayload(character)

            try {
                await controlledRequest(`/characters/${character.ID}`, 'PUT', payload)
                showNotify('Персонаж обновлён')
                await fetchCharacters()
            } catch (err) {
                console.error('Ошибка обновления персонажа:', err)
            }
        }

        async function createCharacter(character) {
            const payload = prepareCharacterPayload(character)
            delete payload.id
            await controlledRequest('/characters', 'POST', payload)
            showNotify('Персонаж создан')
            await fetchCharacters()
        }

        async function deleteCharacter(item) {
            await controlledRequest(`/characters/${item.ID}`, 'DELETE')
            showNotify('Персонаж удален')
            await fetchCharacters()
        }

        onMounted(() => { if (isLoggedIn.value) fetchAllData() })

        async function createAbility(item) { await controlledRequest('/abilities', 'POST', item); showNotify('Способность создана'); await fetchAbilities() }
        async function updateAbility(item) { await controlledRequest(`/abilities/${item.ID}`, 'PUT', item); showNotify('Способность обновлена'); await fetchAbilities() }
        async function deleteAbility(item) { await controlledRequest(`/abilities/${item.ID}`, 'DELETE'); showNotify('Способность удалена'); await fetchAbilities() }

        async function createWeapon(item) { await controlledRequest('/weapons', 'POST', item); showNotify('Оружие создано'); await fetchWeapons() }
        async function updateWeapon(item) { await controlledRequest(`/weapons/${item.ID}`, 'PUT', item); showNotify('Оружие обновлено'); await fetchWeapons() }
        async function deleteWeapon(item) { await controlledRequest(`/weapons/${item.ID}`, 'DELETE'); showNotify('Оружие удалено'); await fetchWeapons() }

        async function createArmor(item) { await controlledRequest('/armor', 'POST', item); showNotify('Броня создана'); await fetchArmor() }
        async function updateArmor(item) { await controlledRequest(`/armor/${item.ID}`, 'PUT', item); showNotify('Броня обновлена'); await fetchArmor() }
        async function deleteArmor(item) { await controlledRequest(`/armor/${item.ID}`, 'DELETE'); showNotify('Броня удалена'); await fetchArmor() }

        async function createItem(item) { await controlledRequest('/items', 'POST', item); showNotify('Предмет создан'); await fetchItems() }
        async function updateItem(item) { await controlledRequest(`/items/${item.ID}`, 'PUT', item); showNotify('Предмет обновлён'); await fetchItems() }
        async function deleteItem(item) { await controlledRequest(`/items/${item.ID}`, 'DELETE'); showNotify('Предмет удалён'); await fetchItems() }

        async function createTrait(item) { await controlledRequest('/traits', 'POST', item); showNotify('Черта создана'); await fetchTraits() }
        async function updateTrait(item) { await controlledRequest(`/traits/${item.ID}`, 'PUT', item); showNotify('Черта обновлена'); await fetchTraits() }
        async function deleteTrait(item) { await controlledRequest(`/traits/${item.ID}`, 'DELETE'); showNotify('Черта удалена'); await fetchTraits() }

        return {
            tab, loading, auth, loginValid, isLoggedIn, user,
            abilities, weapons, characters, armor, items, traits,
            stats, snackbar, rules, headers,
            login, register, logout,
            fetchAbilities, fetchWeapons, fetchCharacters, fetchArmor, fetchItems, fetchTraits,
            createAbility, updateAbility, deleteAbility,
            createWeapon, updateWeapon, deleteWeapon,
            createCharacter, updateCharacter, deleteCharacter,
            createArmor, updateArmor, deleteArmor,
            createItem, updateItem, deleteItem,
            createTrait, updateTrait, deleteTrait,
            startCreateCharacter, startEditCharacter
        }
    }
})

app.component('crud-table', window.CrudTable)

const vuetify = createVuetify()
app.use(vuetify)
app.mount('#app')