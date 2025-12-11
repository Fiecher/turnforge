window.CrudTable = {
    props: {
        title: String,
        headers: Array,
        items: Array,
        itemKey: {
            type: String,
            default: 'ID'
        },
        loading: Boolean,
        hideEdit: Boolean,
        hideDelete: Boolean
    },

    emits: ['create', 'update', 'delete', 'refresh'],

    data() {
        return {
            dialog: false,
            dialogDelete: false,

            editedIndex: -1,
            editedItem: {},

            formValid: false,

            search: '',
            searchOpen: false
        }
    },

    computed: {
        formTitle() {
            return this.editedIndex === -1
                ? `Создать: ${this.title}`
                : `Редактировать: ${this.title}`
        }
    },

    methods: {

        _readId(v) {
            if (v === null || v === undefined) return null;
            if (typeof v === 'number' && Number.isFinite(v)) return Number(v);
            if (typeof v === 'string') {
                const s = v.trim();
                if (/^\d+$/.test(s)) return Number(s);
            }
            if (typeof v === 'object') {
                if ('id' in v && (typeof v.id === 'number' || typeof v.id === 'string')) {
                    const n = Number(v.id);
                    if (!Number.isNaN(n)) return n;
                }
                if ('ID' in v && (typeof v.ID === 'number' || typeof v.ID === 'string')) {
                    const n = Number(v.ID);
                    if (!Number.isNaN(n)) return n;
                }
            }
            return null;
        },

        _readName(v) {
            if (v === null || v === undefined) return null;
            if (typeof v === 'string') return v;
            if (typeof v === 'object') {
                if ('name' in v && v.name) return String(v.name);
                if ('Name' in v && v.Name) return String(v.Name);
                if ('title' in v && v.title) return String(v.title);
                if ('Title' in v && v.Title) return String(v.Title);
            }
            return null;
        },

        _extractTrailingNumberFromString(str) {
            if (!str || typeof str !== 'string') return null;
            const m = str.trim().match(/(\d+)\s*$/);
            if (m) return Number(m[1]);
            return null;
        },

        _valuesToIds(fieldValues, list) {
            const out = [];

            if (!fieldValues) return [];
            const nameToId = new Map();
            if (Array.isArray(list)) {
                for (const e of list) {
                    const nm = this._readName(e);
                    const id = this._readId(e);
                    if (nm && id != null) nameToId.set(String(nm).toLowerCase(), Number(id));
                }
            }

            const pushIfValid = (x) => {
                if (x == null) return;
                const n = Number(x);
                if (!Number.isNaN(n)) out.push(n);
            };

            const arr = Array.isArray(fieldValues) ? fieldValues : [fieldValues];

            for (const v of arr) {
                const idFromObj = this._readId(v);
                if (idFromObj != null) {
                    pushIfValid(idFromObj);
                    continue;
                }

                if (typeof v === 'string') {
                    const trailing = this._extractTrailingNumberFromString(v);
                    if (trailing != null) {
                        pushIfValid(trailing);
                        continue;
                    }

                    const mapped = nameToId.get(v.toLowerCase());
                    if (mapped != null) {
                        pushIfValid(mapped);
                        continue;
                    }

                    for (const [nameLower, idVal] of nameToId.entries()) {
                        if (v.toLowerCase().includes(nameLower) || nameLower.includes(v.toLowerCase())) {
                            pushIfValid(idVal);
                            break;
                        }
                    }

                    continue;
                }

            }

            return Array.from(new Set(out.map(Number)));
        },

        normalizeRelations(item) {
            if (!item || typeof item !== 'object') return item;

            const lists = window.__globalEntityLists || {};

            const weaponsList = lists.weapons || window.weapons || [];
            const armorList = lists.armor || window.armor || [];
            const itemsList = lists.items || window.items || [];
            const traitsList = lists.traits || window.traits || [];
            const abilitiesList = lists.abilities || window.abilities || [];

            try {
                item.weapons = this._valuesToIds(item.weapons, weaponsList);
                item.armor = this._valuesToIds(item.armor, armorList);
                item.items = this._valuesToIds(item.items, itemsList);
                item.traits = this._valuesToIds(item.traits, traitsList);
                item.abilities = this._valuesToIds(item.abilities, abilitiesList);

                if (item.custom_skills) {
                    item.custom_skills = this._valuesToIds(item.custom_skills, []);
                } else if (item.skills) {
                    item.custom_skills = this._valuesToIds(item.skills, []);
                } else {
                    item.custom_skills = [];
                }
            } catch (e) {
            }

            return item;
        },

        toggleSearch() {
            this.searchOpen = !this.searchOpen

            if (this.searchOpen) {
                this.$nextTick(() => {
                    this.$refs.searchInput?.focus?.()
                })
            } else {
                this.search = ''
            }
        },

        closeSearch() {
            this.searchOpen = false
        },


        openEdit(item) {
            this.editItem(item);
        },

        editItem(item) {
            const actualItem = (item && item.raw) ? item.raw : item;

            if (!actualItem || !actualItem[this.itemKey]) {
                console.error("Попытка редактирования с неопределенным объектом или ID.", actualItem);
                return;
            }

            this.editedIndex = this.items.findIndex(i => i[this.itemKey] === actualItem[this.itemKey]);

            const normalized = this.normalizeRelations(JSON.parse(JSON.stringify(actualItem)));

            normalized.weapons = normalized.weapons || [];
            normalized.armor = normalized.armor || [];
            normalized.items = normalized.items || [];
            normalized.traits = normalized.traits || [];
            normalized.abilities = normalized.abilities || [];
            normalized.custom_skills = normalized.custom_skills || [];


            this.editedItem = Object.assign({}, normalized, { formTab: 'main' });
            this.dialog = true;
        },


        deleteItem(item) {
            const actualItem = (item && item.raw) ? item.raw : item;

            if (!actualItem || !actualItem[this.itemKey]) {
                console.error("Попытка удаления с неопределенным объектом или ID.", actualItem);
                return;
            }

            this.editedIndex = this.items.findIndex(i => i[this.itemKey] === actualItem[this.itemKey]);
            this.editedItem = Object.assign({}, actualItem)
            this.dialogDelete = true
        },

        deleteItemConfirm() {
            this.$emit('delete', this.editedItem)
            this.closeDelete()
        },

        close() {
            this.dialog = false
            this.$nextTick(() => {
                this.editedItem = {}
                this.editedIndex = -1
            })
        },

        closeDelete() {
            this.dialogDelete = false
            this.$nextTick(() => {
                this.editedItem = {}
                this.editedIndex = -1
            })
        },

        save() {
            if (!this.formValid) return

            if (this.editedIndex > -1) {
                this.$emit('update', this.editedItem)
            } else {
                this.$emit('create', this.editedItem)
            }

            this.close()
        },

        openCreate() {
            this.editedIndex = -1
            this.editedItem = {
                formTab: 'main',
                weapons: [],
                armor: [],
                items: [],
                traits: [],
                abilities: [],
                custom_skills: []
            }
            this.dialog = true
            this.formValid = false
        }
    },

    template: `
<v-card elevation="2">

    <v-card-title class="d-flex justify-space-between align-center">
        {{ title }}

        <div class="d-flex ga-2 align-center">

            <v-btn
                icon="mdi-refresh"
                variant="text"
                @click="$emit('refresh')"
            />

            <div class="d-flex align-center">

                <v-btn
                    v-if="!searchOpen"
                    icon="mdi-magnify"
                    variant="text"
                    @click="toggleSearch"
                />

                <v-text-field
                    v-else
                    ref="searchInput"
                    v-model="search"
                    density="compact"
                    hide-details
                    clearable
                    label="Поиск"
                    style="width:320px;"
                    @blur="closeSearch"
                />

            </div>

            <v-btn
                color="primary"
                prepend-icon="mdi-plus"
                @click="openCreate"
            >
                Создать
            </v-btn>

        </div>
    </v-card-title>


    <v-data-table
        :headers="headers"
        :items="items"
        :loading="loading"
        :search="search"
        :item-key="itemKey"
    >

        <template #item.actions="{ item }">

            <v-btn
                v-if="!hideEdit"
                icon="mdi-pencil"
                size="small"
                @click="editItem(item)"
            />

            <v-btn
                v-if="!hideDelete"
                icon="mdi-delete"
                size="small"
                color="red"
                @click="deleteItem(item)"
            />

        </template>

    </v-data-table>

</v-card>



<v-dialog v-model="dialog" max-width="700">
    <v-card>

        <v-card-title>{{ formTitle }}</v-card-title>

        <v-card-text>

            <v-form v-model="formValid">
                <slot name="form" :item="editedItem"></slot>
            </v-form>

        </v-card-text>

        <v-card-actions>

            <v-spacer></v-spacer>

            <v-btn variant="text" @click="close">
                Отмена
            </v-btn>

            <v-btn color="success" @click="save">
                Сохранить
            </v-btn>

        </v-card-actions>

    </v-card>
</v-dialog>



<v-dialog v-model="dialogDelete" max-width="400">

    <v-card>

        <v-card-title>
            Удалить запись?
        </v-card-title>

        <v-card-actions>

            <v-spacer></v-spacer>

            <v-btn variant="text" @click="closeDelete">
                Нет
            </v-btn>

            <v-btn color="red" @click="deleteItemConfirm">
                Да
            </v-btn>

        </v-card-actions>

    </v-card>

</v-dialog>
`
}
