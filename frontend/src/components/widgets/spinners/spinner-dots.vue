<template>
  <svg
    class="spinner"
    :style="`
      height: ${height}px
      width: ${width}px`"
    xmlns="http://www.w3.org/2000/svg"
    viewBox="0 0 50 50">
    <circle class="circle1" r="16" cy="64" cx="236" :fill="color" />
    <circle class="circle2" r="16" cy="64" cx="236" :fill="color" />
    <circle class="circle3" r="16" cy="64" cx="236" :fill="color" />
    <circle class="circle4" r="16" cy="64" cx="236" :fill="color" />
    <circle class="circle5" r="16" cy="64" cx="236" :fill="color" />
  </svg>
</template>

<script>
export default {
  name: 'spinner-circular',
  props: {
    height: {
      type: Number,
      default: 50,
    },
    width: {
      type: Number,
      default: 50,
    },
    color: {
      type: String,
      default: 'black',
    },
  },
};
</script>

<style scoped>
.circle1 {
  opacity: 0.001;
  transform: rotate3d(0, 0, 1, 225deg);
  transform-origin: 150px 150px;
  animation: Dots-_-rotate 5.5s linear infinite;
}

.circle2 {
  opacity: 0.001;
  transform: rotate3d(0, 0, 1, 225deg);
  transform-origin: 150px 150px;
  animation: Dots-_-rotate 5.5s linear infinite;
  animation-delay: 240ms;
}

.circle3 {
  opacity: 0.001;
  transform: rotate3d(0, 0, 1, 225deg);
  transform-origin: 150px 150px;
  animation: Dots-_-rotate 5.5s linear infinite;
  animation-delay: 480ms;
}

.circle4 {
  opacity: 0.001;
  transform: rotate3d(0, 0, 1, 225deg);
  transform-origin: 150px 150px;
  animation: Dots-_-rotate 5.5s linear infinite;
  animation-delay: 720ms;
}

.circle5 {
  opacity: 0.001;
  transform: rotate3d(0, 0, 1, 225deg);
  transform-origin: 150px 150px;
  animation: Dots-_-rotate 5.5s linear infinite;
  animation-delay: 960ms;
}

@keyframes Dots-_-rotate {
  0% {
    transform: rotate3d(0, 0, 1, 225deg);
    opacity: 1;
    animation-timing-function: ease-out;
  }

  7% {
    transform: rotate3d(0, 0, 1, 345deg);
    animation-timing-function: linear;
  }

  30% {
    transform: rotate3d(0, 0, 1, 455deg);
    animation-timing-function: ease-in-out;
  }

  39% {
    transform: rotate3d(0, 0, 1, 690deg);
    animation-timing-function: linear;
  }

  60% {
    transform: rotate3d(0, 0, 1, 815deg);
    opacity: 1;
    animation-timing-function: ease-out;
  }

  75% {
    transform: rotate3d(0, 0, 1, 945deg);
    animation-timing-function: ease-out;
  }

  76% {
    transform: rotate3d(0, 0, 1, 945deg);
    opacity: 0;
  }

  100% {
    transform: rotate3d(0, 0, 1, 945deg);
    opacity: 0;
  }
}
</style>
