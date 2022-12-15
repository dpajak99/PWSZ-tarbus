<template>
  <div class="scroll-container">
    <slot name="content" />
  </div>
</template>

<script>
export default {
  name: 'TheScrollContainer',
};
</script>

<style lang="scss">
.scroll-container {
  width: 100%;
  height: 100%;
  overflow-y: auto;

  &::-webkit-scrollbar {
    width: 5px;
  }

  &::-webkit-scrollbar-track {
    background-color: rgba(0, 0, 0, 0);
    border-radius: 10px;
  }

  &::-webkit-scrollbar-track:hover {
    background-color: rgba(0, 0, 0, 0.1);
    border-radius: 10px;
  }

  &::-webkit-scrollbar-thumb {
    border-radius: 10px;
    background-color: rgb(188, 192, 196);
  }
}
</style>
