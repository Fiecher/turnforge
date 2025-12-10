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

            this.editedItem = Object.assign({}, actualItem, { formTab: 'main' });
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
            this.editedItem = { formTab: 'main' }
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