<script setup>
import { ref, onMounted, nextTick, computed } from 'vue'
import CrudTable from '@/components/crudtable.js'
import api from '@/api'

const tab = ref('characters')

const characters = ref([])
const abilitiesList = ref([])
const traitsList = ref([])
const weaponsList = ref([])
const armorList = ref([])
const itemsList = ref([])
const sizes = ref([
  { label: 'Tiny', value: 'TINY' },
  { label: 'Small', value: 'SMALL' },
  { label: 'Medium', value: 'MEDIUM' },
  { label: 'Large', value: 'LARGE' },
  { label: 'Huge', value: 'HUGE' },
  { label: 'Gargantuan', value: 'GARGANTUAN' }
])

const loading = ref(false)
const crudRef = ref(null)

const headers = [
  { title: 'ID', key: 'id' },
  { title: 'Имя', key: 'name' },
  { title: 'Класс', key: 'class' },
  { title: 'Уровень', key: 'level' },
  { title: 'Раса', key: 'race' },
  { title: 'Возраст', key: 'age' },
  { title: '💬', key: 'actions', sortable: false }
]

const classOptions = ref(['Воин', 'Маг', 'Плут', 'Жрец', 'Бард'])
const abilityNames = ref(['Strength', 'Dexterity', 'Constitution', 'Intelligence', 'Wisdom', 'Charisma'])

function readId(obj) {
  if (!obj) return null
  if (typeof obj === 'number') return obj
  if (typeof obj === 'string' && /^\d+$/.test(obj)) return Number(obj)
  if (typeof obj === 'object') {
    if ('id' in obj && (typeof obj.id === 'number' || (typeof obj.id === 'string' && /^\d+$/.test(obj.id)))) {
      return Number(obj.id)
    }
    if ('ID' in obj && (typeof obj.ID === 'number' || (typeof obj.ID === 'string' && /^\d+$/.test(obj.ID)))) {
      return Number(obj.ID)
    }
  }
  return null
}

function readName(obj) {
  if (!obj) return null
  if (typeof obj === 'string') return obj
  if (typeof obj === 'object') {
    if ('name' in obj && obj.name) return String(obj.name)
    if ('Name' in obj && obj.Name) return String(obj.Name)
    if ('title' in obj && obj.title) return String(obj.title)
    if ('Title' in obj && obj.Title) return String(obj.Title)
  }
  return null
}


function convertFieldToIds(character, list, fieldName) {
  const raw = character[fieldName]

  if (!raw) {

    character[fieldName] = []
    return
  }

  const nameToId = new Map()
  for (const e of (list || [])) {
    const nm = readName(e)
    const id = readId(e)
    if (nm && id != null) nameToId.set(String(nm), Number(id))
  }

  const ids = []

  if (Array.isArray(raw)) {
    for (const v of raw) {
      const maybeId = readId(v)
      if (maybeId != null) {
        ids.push(Number(maybeId))
        continue
      }

      if (typeof v === 'string') {
        const trimmed = v.trim()
        if (/^\d+$/.test(trimmed)) {
          ids.push(Number(trimmed))
          continue
        }

        const mapped = nameToId.get(trimmed)
        if (mapped != null) {
          ids.push(Number(mapped))
          continue
        }

        continue
      }
    }
  } else {
    const maybeId = readId(raw)
    if (maybeId != null) ids.push(Number(maybeId))
    else if (typeof raw === 'string') {
      const mapped = nameToId.get(raw.trim())
      if (mapped != null) ids.push(Number(mapped))
    }
  }


  character[fieldName] = Array.from(new Set(ids.map(Number)))
}

function normalizeCharacterRelations(character) {
  convertFieldToIds(character, weaponsList.value, 'weapons')
  convertFieldToIds(character, armorList.value, 'armor')
  convertFieldToIds(character, itemsList.value, 'items')
  convertFieldToIds(character, traitsList.value, 'traits')
  convertFieldToIds(character, abilitiesList.value, 'abilities')

  if (!character.custom_skills) {
    character.custom_skills = []
  } else {
    const cs = []
    for (const v of character.custom_skills) {
      const maybeId = readId(v)
      if (maybeId != null) cs.push(Number(maybeId))
      else if (typeof v === 'string' && /^\d+$/.test(v.trim())) cs.push(Number(v.trim()))
    }
    character.custom_skills = Array.from(new Set(cs))
  }
}

function serializeCharacterForSend(char) {
  const safe = { ...char }
  const toNumArray = (arr) => {
    if (!arr) return []
    if (!Array.isArray(arr)) return []
    return arr.map(v => {
      const n = readId(v)
      if (n != null) return Number(n)
      if (typeof v === 'string' && /^\d+$/.test(v.trim())) return Number(v.trim())
      return null
    }).filter(x => x !== null)
  }

  safe.weapons = toNumArray(char.weapons)
  safe.armor = toNumArray(char.armor)
  safe.items = toNumArray(char.items)
  safe.traits = toNumArray(char.traits)
  safe.abilities = toNumArray(char.abilities)
  safe.custom_skills = toNumArray(char.custom_skills)

  return safe
}

async function fetchAllRelatedData() {
  try {
    abilitiesList.value = await api.getAbilitiesList()
    traitsList.value = await api.getTraitsList()
    weaponsList.value = await api.getWeaponsList()
    armorList.value = await api.getArmorList()
    itemsList.value = await api.getItemsList()

    abilitiesList.value = Array.isArray(abilitiesList.value) ? abilitiesList.value : []
    traitsList.value = Array.isArray(traitsList.value) ? traitsList.value : []
    weaponsList.value = Array.isArray(weaponsList.value) ? weaponsList.value : []
    armorList.value = Array.isArray(armorList.value) ? armorList.value : []
    itemsList.value = Array.isArray(itemsList.value) ? itemsList.value : []
  } catch (e) {
    console.error("Не удалось загрузить списки связей:", e);
    abilitiesList.value = []
    traitsList.value = []
    weaponsList.value = []
    armorList.value = []
    itemsList.value = []
  }
}

async function fetchCharacters() {
  loading.value = true
  try {
    const chars = await api.getCharacters() || []
    chars.forEach(c => {
      normalizeCharacterRelations(c)

      if (!c.weapons) c.weapons = []
      if (!c.armor) c.armor = []
      if (!c.items) c.items = []
      if (!c.traits) c.traits = []
      if (!c.abilities) c.abilities = []
      if (!c.custom_skills) c.custom_skills = []
    })

    characters.value = chars
  } catch (e) {
    console.error("Ошибка загрузки персонажей:", e)
    characters.value = []
  } finally {
    loading.value = false
  }
}

async function createCharacter(char) {
  const payload = serializeCharacterForSend(char)
  await api.createCharacter(payload)
  await fetchCharacters()
}

async function updateCharacter(char) {
  const payload = serializeCharacterForSend(char)
  await api.updateCharacter(payload)
  await fetchCharacters()
}

async function deleteCharacter(char) {
  const id = (char && (char.id ?? char.ID)) ? (char.id ?? char.ID) : char
  await api.deleteCharacter(id)
  await fetchCharacters()
}

async function createNewCharacter() {
  tab.value = 'characters'
  await nextTick()
  crudRef.value?.openCreate?.()
}

onMounted(async () => {
  await fetchAllRelatedData()
  await fetchCharacters()
});
</script>

<template>

  <v-container>

    <v-row>
      <v-col cols="12" md="4">
        <v-card class="pa-4">
          <h3>Персонажи</h3>

          <v-btn
              color="success"
              block
              prepend-icon="mdi-plus"
              @click="createNewCharacter"
              class="mt-4"
          >
            Создать нового
          </v-btn>
        </v-card>
      </v-col>
    </v-row>

  </v-container>


  <v-window v-model="tab">

    <v-window-item value="characters">

      <crud-table
          ref="crudRef"
          title="Персонажи"
          :headers="headers"
          :items="characters"
          :loading="loading"
          item-key="id"
          @refresh="fetchCharacters"
          @create="createCharacter"
          @update="updateCharacter"
          @delete="deleteCharacter"
      >

        <template #form="{ item }">

          <v-tabs v-model="item.formTab" color="primary" density="compact">
            <v-tab value="main">Основное</v-tab>
            <v-tab value="stats">Характеристики</v-tab>
            <v-tab value="inventory">Инвентарь</v-tab>
            <v-tab value="features">Особенности</v-tab>
          </v-tabs>

          <v-window v-model="item.formTab" class="pa-4">

            <v-window-item value="main">
              <v-container>
                <v-row>
                  <v-col cols="12" md="6">
                    <v-text-field
                        v-model="item.name"
                        label="Имя персонажа"
                        :rules="[v => !!v || 'Имя обязательно']"
                        required
                    />
                  </v-col>
                  <v-col cols="12" md="6">
                    <v-select
                        v-model="item.class"
                        :items="classOptions"
                        label="Класс"
                        :rules="[v => !!v || 'Класс обязателен']"
                        required
                    />
                  </v-col>

                  <v-col cols="12" md="4">
                    <v-text-field
                        v-model.number="item.level"
                        type="number"
                        label="Уровень"
                        min="1"
                        required
                    />
                  </v-col>
                  <v-col cols="12" md="4">
                    <v-text-field v-model="item.race" label="Раса" />
                  </v-col>
                  <v-col cols="12" md="4">
                    <v-text-field v-model.number="item.age" type="number" label="Возраст" />
                  </v-col>

                  <v-col cols="12" md="4">
                    <v-select
                        v-model="item.size"
                        :items="sizes"
                        label="Размер"
                        item-title="label"
                        item-value="value"
                        clearable
                    />
                  </v-col>
                  <v-col cols="12" md="4">
                    <v-text-field v-model="item.subclass" label="Подкласс" />
                  </v-col>
                  <v-col cols="12" md="4">
                    <v-text-field v-model="item.background" label="Предыстория" />
                  </v-col>

                  <v-col cols="12">
                    <v-textarea v-model="item.description" label="Описание/Биография" rows="3" />
                  </v-col>
                </v-row>
              </v-container>
            </v-window-item>

            <v-window-item value="stats">
              <v-container>
                <v-row>
                  <v-col v-for="stat in abilityNames" :key="stat" cols="4">
                    <v-text-field
                        v-model.number="item[stat.toLowerCase()]"
                        :label="stat"
                        type="number"
                        min="0"
                        required
                    />
                  </v-col>
                  <v-col cols="12">
                    <v-text-field v-model="item.spellcasting_ability" label="Базовая характеристика заклинаний" />
                  </v-col>
                </v-row>
              </v-container>
            </v-window-item>

            <v-window-item value="inventory">
              <v-container>
                <v-row>
                  <v-col cols="12">
                    <v-text-field
                        v-model.number="item.money"
                        label="Золото (Money)"
                        type="number"
                        min="0"
                        required
                    />
                  </v-col>

                  <v-col cols="12" md="6">
                    <v-select
                        v-model="item.weapons"
                        :items="weaponsList"
                        item-title="name"
                        item-value="id"
                        label="Оружие"
                        multiple
                        chips
                        clearable
                    />
                  </v-col>
                  <v-col cols="12" md="6">
                    <v-select
                        v-model="item.armor"
                        :items="armorList"
                        item-title="name"
                        item-value="id"
                        label="Броня"
                        multiple
                        chips
                        clearable
                    />
                  </v-col>
                  <v-col cols="12">
                    <v-select
                        v-model="item.items"
                        :items="itemsList"
                        item-title="name"
                        item-value="id"
                        label="Предметы (Инвентарь)"
                        multiple
                        chips
                        clearable
                    />
                  </v-col>
                </v-row>
              </v-container>
            </v-window-item>

            <v-window-item value="features">
              <v-container>
                <v-row>
                  <v-col cols="12">
                    <v-select
                        v-model="item.traits"
                        :items="traitsList"
                        item-title="name"
                        item-value="id"
                        label="Черты"
                        multiple
                        chips
                        clearable
                    />
                  </v-col>
                  <v-col cols="12">
                    <v-select
                        v-model="item.abilities"
                        :items="abilitiesList"
                        item-title="name"
                        item-value="id"
                        label="Способности / Заклинания"
                        multiple
                        chips
                        clearable
                    />
                  </v-col>
                </v-row>
              </v-container>
            </v-window-item>
          </v-window>
        </template>

      </crud-table>

    </v-window-item>

  </v-window>

</template>
