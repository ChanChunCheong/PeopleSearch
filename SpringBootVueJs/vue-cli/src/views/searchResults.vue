<template>
  <div id="app">
     <div>
      <b-form-select v-model="sessionID" :options="options" size="sm" class="mt-3"></b-form-select>
      <div class="mt-3">Selected: <strong>{{ sessionID }}</strong></div>
    </div>
    <b-tabs pills content-class="mt-3" justified>
      <b-tab title="Twitter" active>
        <Todos v-bind:todos="todos_Twitter" v-bind:sessionID="sessionID"/>
      </b-tab>
      <b-tab title="Facebook">
        <!-- change the todos to something else  -->
        <Todos v-bind:todos="todos_Facebook" v-bind:sessionID="sessionID"/>
      </b-tab>
    </b-tabs>
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
      sessionID: null,
      options: [
        { value: 'null', text: 'Please select an option' }
      ],
      todos_Twitter: [],
      todos_Facebook: []
    }
  },
  methods: {
  },
  created () {
    AXIOS.get('/initSession/')
      .then(res => {
        var list = []
        var i
        for (i = 0; i < res.data.length; i++) {
          const { name, sessionID } = res.data[i]
          var session = {}
          session.value = sessionID
          session.text = name
          list.push(session)
        }
        this.options = list
        this.sessionID = res.data[res.data.length - 1].sessionID
        console.log(this.sessionID)
      })
      .catch(err => console.log(err))
    AXIOS.get('/initTwitter/')
      .then(res => {
        this.todos_Twitter = res.data
        console.log(res.data)
      })
      .catch(err => console.log(err))
    AXIOS.get('/initFacebook/')
      .then(res => {
        this.todos_Facebook = res.data
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
