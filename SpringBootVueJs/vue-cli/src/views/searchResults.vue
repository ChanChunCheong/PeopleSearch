<template>
  <div id="app">
    <Todos v-bind:todos="todos" v-on:del-todo="deleteTodo" />
  </div>
</template>

<script>
import Todos from '../components/Todos'
import AddTodo from '../components/AddTodo'
import {AXIOS} from '../http-common'

export default {
  name: 'Home',
  components: {
    Todos,
    AddTodo
  },
  data () {
    return {
      todos: []
    }
  },
  methods: {
    // addTodo (newTodo) {
    //   var params = new URLSearchParams()
    //   const { name, location, keyword } = newTodo
    //   params.append('name', name)
    //   params.append('location', location)
    //   params.append('keyword', keyword)
    //   console.log(name)
    //   console.log(location)
    //   console.log(keyword)
    //   AXIOS.get('/all?' + params)
    //     .then(res => {
    //       this.todos = res.data
    //       console.log(res.data)
    //       console.log(params.toString())
    //     })
    //     .catch(err => console.log(err))
    // },
  },
  created () {
    AXIOS.get('/init/')
      .then(res => {
        this.todos = res.data
        console.log(res.data)
      })
      .catch(err => console.log(err))
  }
}
</script>

<style>
  * {
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
  }
</style>
