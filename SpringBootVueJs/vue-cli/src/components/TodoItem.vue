<template>
  <div class="todo-item" v-bind:class="{'is-complete':todo.completed}">
    <p>Name: {{todo.name}} </p>
    <p>Link: {{todo.link}} </p>
    <p>Location: {{todo.location}} </p>
    <p>Date joined: {{todo.dateJoined}} </p>
    <p>Description: {{todo.description}} </p>
    <p>
      <input type="checkbox" v-on:change="markComplete" v-bind:checked="todo.completed">
      <button @click="$emit('del-todo', todo.id)" class="del">x</button>
      </p>
  </div>
</template>

<script>
export default {
  name: 'TodoItem',
  props: ['todo'],
  methods: {
    markComplete () {
      this.todo.completed = !this.todo.completed
    }
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
