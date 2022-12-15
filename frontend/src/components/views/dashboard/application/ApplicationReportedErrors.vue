<template>
  <table>
    <caption>Reports</caption>
    <thead>
    <tr>
      <th>Email</th>
      <th>Treść</th>
      <th>Media</th>
      <th>Status</th>
      <th>Edytuj</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="msg in feedback" :key="msg.id">
      <td>{{ msg.senderEmail }}</td>
      <td>{{ msg.content }}</td>
      <td class="media-cell">
        <div v-for="media in msg.media" :key="media.name">
          <img
            v-if="media.type === 'picture'"
            :src="media.mediaSrc"
            class="media-content"
            alt=""
            @click="openMediaDialog(media)"
          />
          <div v-else class="media-content-video" @click="openMediaDialog(media)">
          </div>
        </div>
      </td>
      <td>
              <span :class="['status', statusColor(msg.status)]">
                {{ msg.status }}
              </span>
      </td>
      <td class="edit" @click="editStatus(msg)">
        <pencil/>
      </td>
    </tr>
    </tbody>
  </table>
  <el-dialog v-model="dialogData.isOpen" title="Edytuj status">
    <div class="dialog">
      <select v-model="selectedStatus">
        <option disabled value="">Wybierz status</option>
        <option>ODEBRANE</option>
        <option>ROZWIĄZANE</option>
        <option>PILNE</option>
      </select>
      <div>
        <button type="button" @click="updateStatus" class="btn btn__primary">Zapisz</button>
        <button type="button" @click="dialogData.isOpen = false" class="btn btn__outline">Wróć
        </button>
      </div>
    </div>
  </el-dialog>
  <el-dialog v-model="mediaDialog.isOpen" :title="mediaDialog.media?.name">
    <div class="media-dialog">
      <img v-if="mediaDialog.media.type === 'picture'" :src="mediaDialog.media.mediaSrc" alt=""/>
      <div>
        <button type="button" @click="mediaDialog.isOpen = false" class="btn btn__outline">Zamknij
        </button>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import {Pencil} from 'mdue';
import {ElDialog} from 'element-plus';

const testData = [
  {
    id: 1,
    senderEmail: 'email-1@gmail.com',
    content:
      'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled',
    status: 'ODEBRANE',
    media: [
      {
        type: 'picture',
        name: 'media name',
        mediaSrc:
          'https://images.unsplash.com/photo-1610337673044-720471f83677?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=666&q=80',
      },
      {
        type: 'picture',
        name: 'media name',
        mediaSrc:
          'https://images.unsplash.com/photo-1584824486509-112e4181ff6b?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1500&q=80',
      },
    ],
  },
  {
    id: 2,
    senderEmail: 'email-2@gmail.com',
    content:
      'it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset',
    status: 'ROZWIĄZANE',
    media: [
      {
        type: 'video',
        name: 'video media name',
        mediaSrc:
          'https://player.vimeo.com/external/368932864.sd.mp4?s=7afd048bc19e8442bab5adbfe20021c09f7a605c&profile_id=139&oauth2_token_id=57447761',
      },
      {
        type: 'picture',
        name: 'media name',
        mediaSrc:
          'https://images.unsplash.com/photo-1584824486509-112e4181ff6b?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1500&q=80',
      },
    ],
  },
  {
    id: 3,
    senderEmail: 'email-3@gmail.com',
    content:
      'It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. ',
    status: 'PILNE',
    media: [
      {
        type: 'picture',
        name: 'media name',
        mediaSrc:
          'https://images.unsplash.com/photo-1610337673044-720471f83677?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=666&q=80',
      },
      {
        type: 'picture',
        name: 'media name',
        mediaSrc:
          'https://images.unsplash.com/photo-1584824486509-112e4181ff6b?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1500&q=80',
      },
      {
        type: 'picture',
        name: 'media name',
        mediaSrc:
          'https://images.unsplash.com/photo-1584824486509-112e4181ff6b?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1500&q=80',
      },
    ],
  },
];

export default {
  data() {
    return {
      feedback: testData,
      selectedStatus: '',
      dialogData: {
        isOpen: false,
        updatedStatusId: null,
      },
      mediaDialog: {
        isOpen: false,
        media: null,
      },
    };
  },
  components: {
    Pencil,
    ElDialog,
  },
  methods: {
    statusColor(status) {
      if (status === 'ODEBRANE') return 'status-orange';
      if (status === 'ROZWIĄZANE') return 'status-green';
      return 'status-red';
    },
    editStatus(msg) {
      this.dialogData.isOpen = true;
      this.dialogData.updatedStatusId = msg.id;
    },
    openMediaDialog(media) {
      this.mediaDialog.isOpen = true;
      this.mediaDialog.media = media;
    },
    updateStatus() {
      this.dialogData.isOpen = false;
      // ...
      // this.dialogData.updatedStatusId -> id zedytowanej wiadomości
      // this.selectedStatus -> Nowa wartość statusu: 'ODEBRANE' itp.
    },
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

    th:nth-child(3),
    th:nth-child(4) {
      padding: 0 2rem;
    }
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

    td {
      &:nth-child(1) {
        min-width: 15rem;
      }

      &:nth-child(3) {
        min-width: 10rem;
        padding: 0 2rem;
      }
    }
  }
}

.edit {
  padding: 0 2rem;
  font-size: 1.7rem;

  svg {
    cursor: pointer;
  }
}

.status {
  color: #fff;
  padding: 0.25rem 1rem;
  border-radius: 5rem;
  font-size: 1rem;
  background: orange;
}

.status-green {
  background: green;
}

.status-red {
  background: red;
}

.btn {
  padding: 0.5rem 3.5rem;
  color: #fff;
  border: none;
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

.dialog {
  display: flex;
  flex-direction: column;
  gap: 2rem;

  div {
    display: flex;
    gap: 1rem;
  }
}

.media-dialog {
  img,
  video {
    width: 100%;
  }
}

.media-cell {
  margin: 1rem 0;
  display: flex;
  align-items: center;
  gap: 1rem;
}

.media-content-video {
  position: relative;
  cursor: pointer;

  &::after {
    content: '';
    position: absolute;
    z-index: 10;
    background: url('@/assets/images/play.svg') center / 2.5rem 2.5rem no-repeat;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
  }
}

.media-content {
  cursor: pointer;
  height: 8rem;
}
</style>
