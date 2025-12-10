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

/
async function fetchAllRelatedData() {
  try {
    abilitiesList.value = await api.getAbilitiesList()
    traitsList.value = await api.getTraitsList()
    weaponsList.value = await api.getWeaponsList()
    armorList.value = await api.getArmorList()
    itemsList.value = await api.getItemsList()
  } catch (e) {
    console.error("Не удалось загрузить списки связей:", e);
  }
}

async function fetchCharacters() {
  loading.value = true
  characters.value = await api.getCharacters()
  loading.value = false
}

async function createCharacter(char) {
  await api.createCharacter(char)
  await fetchCharacters()
}

async function updateCharacter(char) {
  await api.updateCharacter(char)
  await fetchCharacters()
}

async function deleteCharacter(char) {
  await api.deleteCharacter(char.id)
  await fetchCharacters()
}

async function createNewCharacter() {
  tab.value = 'characters'
  await nextTick()
  crudRef.value?.openCreate()
}

onMounted(() => {
  fetchCharacters();
  fetchAllRelatedData();
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