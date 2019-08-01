<template>
  <div>
    <b-container>
    <b-card-group deck>
    <div v-bind:key="todo.id" v-for="todo in todos">
      <TodoItem v-if="todo.sessionID === sessionID" v-bind:todo="todo" />
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
  props: ['todos', 'sessionID']
}
</script>

<style scoped>
.card-group{
      display: flex;
      flex: 1 1 auto;
      flex-direction: column;
      justify-content: space-between;
}
</style>
