<template>
  <div class="wrapper">
    <b-carousel
      id="carousel-1"
      v-model="slide"
      :interval="4000"
      indicators
      background="#ffffff"
      style="text-shadow: 1px 1px 2px #333;"
      @sliding-start="onSlideStart"
      @sliding-end="onSlideEnd"
    >
      <!-- Slides with custom text -->
      <b-carousel-slide
      >
        <img
          slot="img"
          class="d-block img-fluid w-100"
          src="../../pictures/socialmedia4Text.jpg"
          alt="image slot"
        >
      </b-carousel-slide>
      <b-carousel-slide>
        <img
          slot="img"
          class="d-block img-fluid w-100"
          src="../../pictures/socialmedia3.jpg"
          alt="image slot"
        >
      </b-carousel-slide>

      <!-- Slides with img slot -->
      <!-- Note the classes .d-block and .img-fluid to prevent browser default image alignment -->
    </b-carousel>
  </div>
</template>

<script>
import AddTodo from '../AddTodo'
import {EventBus} from '../../main'

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
  },
  mounted () {
    EventBus.$on('add-todo', (data) => {
      this.addTodo(data)
    })
  }
}
</script>
