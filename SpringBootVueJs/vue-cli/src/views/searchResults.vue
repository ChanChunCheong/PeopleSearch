<template>
  <div id="app">
    <Todos v-bind:todos="todos" v-on:del-todo="deleteTodo" />
    <b-spinner v-if = "showSpinner === true" label="Loading..."></b-spinner>
  </div>
</template>

<script>
import Todos from '../components/Todos'
import AddTodo from '../components/AddTodo'
import {AXIOS} from '../http-common'
import {EventBus} from '../main'

export default {
  name: 'Home',
  components: {
    Todos,
    AddTodo
  },
  data () {
    return {
      todos: [],
      showSpinner: false
    }
  },
  methods: {
    addTodo (newTodo) {
      var params = new URLSearchParams()
      const { name, location, keyword } = newTodo
      params.append('name', name)
      params.append('location', location)
      params.append('keyword', keyword)
      console.log(name)
      console.log(location)
      console.log(keyword)
      AXIOS.get('/all?' + params)
        .then(res => {
          this.todos = res.data
          console.log(res.data)
          console.log(params.toString())
        })
        .catch(err => console.log(err))
    },
    show () {
      console.log('show spinner')
      this.showSpinner = true
    },
    hide () {
      console.log('hide spinner')
      this.showSpinner = false
    }
  },
  mounted () {
    EventBus.$on('add-todo', (data) => {
      this.addTodo(data)
    })
    EventBus.$on('before-request', this.show)
    EventBus.$on('after-response', this.hide)
  },
  created () {
    EventBus.$on('EVENT_NAME', (data) => {
      console.log(data)
    })
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
