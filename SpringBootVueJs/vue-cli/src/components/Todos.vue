<template>
  <div>
    <b-container>
    <b-card-group deck>
    <div v-bind:key="todo.id" v-for="todo in todos">
      <TodoItem v-bind:todo="todo" v-on:del-todo="$emit('del-todo', todo.id)" />
    </div>
    </b-card-group>
    </b-container>
  </div>
</template>

<script>
import TodoItem from './TodoItem.vue'
export default {
  name: 'Todos',
  components: {
    TodoItem
  },
  props: ['todos']
}
</script>

<style scoped>
</style>
