<template>
  <div id="app">
    <Todos v-bind:todos="todos" v-on:del-todo="deleteTodo" />
  </div>
</template>

<script>
export default {
  name: 'searchResults'
//   components: {
//     Todos,
//   },
//   props: ['todos'],
//   data () {
//      return {
//       todos: []
//     }
//   },
//   methods: {
//   },
//   created () {
//     this.todos = todos
//   }
}
</script>

<style>
  /* * {
    box-sizing: border-box;
    margin: 0;
    padding: 0;
  }
  body {
    font-family: Arial, Helvetica, sans-serif;
    line-height: 1.4;
  }
  .btn {
    display: inline-block;
    border: none;
    background: #555;
    color: #fff;
    padding: 7px 20px;
    cursor: pointer;
  }
  .btn:hover {
    background: #666;
  } */
</style>
