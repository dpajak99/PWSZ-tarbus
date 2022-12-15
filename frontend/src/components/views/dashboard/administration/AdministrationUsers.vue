<template>
  <table>
    <caption>Administracja</caption>
    <thead>
      <tr>
        <th />
        <th>Imię</th>
        <th>Nazwisko</th>
        <th>Email</th>
        <th>Role</th>
        <th>Edytuj</th>
        <th>
          <button type="button" @click="openEditDialog(emptyUserModel)" class="btn btn__primary float-right">
            Dodaj użytkownika
          </button>
        </th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="user in users" :key="user.id">
        <td>
          <the-profile-image
            :userId="user.id"
            class="profile-image-small"
          />
        </td>
        <td>{{ user.firstName }}</td>
        <td>{{ user.lastName }}</td>
        <td>{{ user.email }}</td>
        <td>
          <ul>
            <li v-for="role in user.roles" :key="role.id">
              {{ role.name.slice(5).toLowerCase() }}
            </li>
          </ul>
        </td>
        <td colspan="2">
          <button type="button" @click="openEditDialog(user)" class="btn btn__primary">Edytuj</button>&nbsp;
          <button type="button" @click="openEditPasswordDialog(user)" class="btn btn__primary">
            Zmień hasło
          </button>
        </td>
      </tr>
    </tbody>
  </table>
  <el-dialog v-model="dialogData.isOpen" title="Edycja użytkownika">
    <div class="edit-form">
      <label for="firstName">Imię</label>
      <input type="text" id="firstName" v-model="dialogData.currentUser.firstName" required />
      <label for="name">Nazwisko</label>
      <input type="text" id="name" v-model="dialogData.currentUser.lastName" required />
      <label for="email">Email</label>
      <input type="email" id="email" v-model="dialogData.currentUser.email" required />
      <div class="roles">
        <div>
          <input
            type="checkbox"
            name="role"
            id="role-user"
            :value="{ id: 1, name: 'ROLE_USER' }"
            v-model="dialogData.currentUser.roles"
          />
          <label for="role-user">ROLE_USER</label>
        </div>
        <div>
          <input
            type="checkbox"
            name="role"
            id="role-admin"
            :value="{ id: 2, name: 'ROLE_ADMIN' }"
            v-model="dialogData.currentUser.roles"
          />
          <label for="role-admin">ROLE_ADMIN</label>
        </div>
      </div>
      <div>
        <button type="button" @click="dialogData.isOpen = false" class="btn btn__outline">Wróć</button>
        <button type="button" @click="saveForm" class="btn btn__primary">Zapisz</button>
      </div>
    </div>
  </el-dialog>
  <el-dialog v-model="passwordDialogData.isOpen" title="Zmiana hasła">
    <div class="edit-form">
      <label for="email">Nowe hasło</label>
      <input type="text" id="password" v-model="passwordDialogData.currentUser.password" required />
      <div>
        <button type="button" @click="passwordDialogData.isOpen = false" class="btn btn__outline">Wróć</button>
        <button type="button" @click="savePasswordForm" class="btn btn__primary">Zapisz</button>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import { ElDialog } from 'element-plus';
import UsersService from '../../../../services/administration/users.service';
import TheProfileImage from '../../../layouts/TheProfileImage.vue';

export default {
  data() {
    return {
      emptyUserModel: {
        firstName: null,
        lastName: null,
        email: null,
        roles: [],
      },
      dialogData: {
        isOpen: false,
        currentUser: null,
      },
      passwordDialogData: {
        isOpen: false,
        currentUser: null,
      },
      users: [],
    };
  },
  components: {
    ElDialog,
    TheProfileImage,
  },
  methods: {
    savePasswordForm() {
      this.passwordDialogData.isOpen = false;
      UsersService.update(this.passwordDialogData.currentUser)
        .then(() => {
          this.fetchUsers();
        })
        .catch((err) => console.log(err));
    },
    saveForm() {
      // SEND NEW USER DATA
      this.dialogData.isOpen = false;
      UsersService.update(this.dialogData.currentUser)
        .then(() => {
          this.fetchUsers();
        })
        .catch((err) => console.log(err));
    },
    openEditDialog(user) {
      this.dialogData.isOpen = true;
      this.dialogData.currentUser = { ...user };
    },
    openEditPasswordDialog(user) {
      this.passwordDialogData.isOpen = true;
      this.passwordDialogData.currentUser = { ...user };
    },
    fetchUsers() {
      UsersService.getUsers()
        .then((response) => {
          this.users = response.data;
        })
        .catch((err) => console.log(err));
    },
  },
  beforeMount() {
    this.fetchUsers();
  },
};
</script>

<style lang="scss" scoped>
@import '@/assets/styles/_variables.scss';

table {
  margin: 3rem;
  min-width: calc(100% - 3rem);
  border-collapse: collapse;
  text-align: left;

  thead {
    color: $color-gray-medium;
    font-weight: 300;
    font-size: 1rem;
  }

  th,
  td {
    padding: 1.5rem 0;
  }

  tbody {
    color: $font-color-dark;
    font-weight: 500;
    font-size: 1.2rem;

    tr {
      border-top: 1px solid $color-gray;
    }
  }
}

ul {
  list-style: none;
  display: flex;
  gap: 1rem;

  li {
    text-transform: capitalize;
  }
}

.float-right {
 float: right;
}

.btn {
  padding: 0.5rem 3.5rem;
  color: #fff;
  border-radius: 0.5rem;
  cursor: pointer;
  transition: all 0.3s;

  &__primary {
    border: none;
    background-color: $color-primary;

    &:hover {
      background-color: lighten($color-primary, 10%);
    }
  }
  &__outline {
    border: 2px solid $color-primary;
    background: none;
    color: #000;

    &:hover {
      color: #fff;
      background-color: $color-primary;
    }
  }
}

.edit-form {
  display: flex;
  flex-direction: column;
  color: $color-gray-medium;
  font-weight: 300;
  font-size: 1.2rem;
  align-items: flex-start;

  input {
    margin-top: 0.5rem;
    margin-bottom: 2rem;
    padding: 0.5rem;
    min-width: 20rem;
    border-radius: 0.5rem;
    border: 1px solid $color-gray-dark;
  }

  div {
    display: flex;
    gap: 1rem;

    input {
      padding: 0;
      margin: 0;
      min-width: 0;
    }
  }

  .roles {
    margin-bottom: 2rem;
    display: flex;
    flex-direction: column;
    gap: 1rem;
  }
}
</style>

<style lang="scss">
</style>
