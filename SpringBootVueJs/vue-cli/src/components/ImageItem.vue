<template>
  <div class="todo-item">
    <b-card
    no-body
    :img-src="image_url"
    img-alt="Image"
    img-top
    style="max-height: 20rem;"
    class="mb-2"
    >
    </b-card>
    <!-- <p>
      <input type="checkbox" v-on:change="markComplete" v-bind:checked="todo.completed">
      <button @click="$emit('del-todo', todo.id)" class="del">x</button>
      </p> -->
  </div>
</template>

<script>
export default {
  name: 'ImageItem',
  props: ['image_url'],
  methods: {
  }
}
</script>

<style scoped>
  .todo-item {
    background: #f4f4f4;
    padding: 10px;
    border-bottom: 1px #ccc dotted;
  }
  .is-complete {
    text-decoration: line-through;
  }
  .del {
    background: #ff0000;
    color: #fff;
    border: none;
    padding: 5px 9px;
    border-radius: 50%;
    cursor: pointer;
    float: right;
  }
</style>
