<template>
  <div class="wrapper">
    <b-carousel
      id="carousel-1"
      v-model="slide"
      :interval="4000"
      controls
      indicators
      background="#ababab"
      img-width="1024"
      img-height="480"
      style="text-shadow: 1px 1px 2px #333;"
      @sliding-start="onSlideStart"
      @sliding-end="onSlideEnd"
    >
      <!-- Slides with custom text -->
      <b-carousel-slide>
        <img
          slot="img"
          class="d-block img-fluid w-100"
          src="https://picsum.photos/1024/480/?image=54"
          alt="image slot"
        >
        <!-- <AddTodo v-on:add-todo="addTodo"/> -->
      </b-carousel-slide>

      <!-- Slides with img slot -->
      <!-- Note the classes .d-block and .img-fluid to prevent browser default image alignment -->
      <b-carousel-slide>
        <img
          slot="img"
          class="d-block img-fluid w-100"
          src="https://picsum.photos/1024/480/?image=55"
          alt="image slot"
        >
        <h1>Hello world!</h1>
      </b-carousel-slide>
    </b-carousel>
  </div>
</template>

<script>
import AddTodo from '../AddTodo'

export default {
  name: 'Carousel',
  components: {
    AddTodo
  },
  data () {
    return {
      slide: 0,
      sliding: null
    }
  },
  methods: {
    onSlideStart (slide) {
      this.sliding = true
    },
    onSlideEnd (slide) {
      this.sliding = false
    },
    addTodo (newTodo) {
      // Send up to parent
      this.$emit('add-todo', newTodo)
    }
  }
}
</script>
