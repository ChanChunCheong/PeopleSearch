<template>
<div>
  <b-navbar v-bind:sticky="true" toggleable="lg" type="dark" variant="info">
    <b-navbar-brand href="#">People Search Aggregator</b-navbar-brand>

    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

    <b-collapse id="nav-collapse" is-nav>
      <b-navbar-nav>
        <b-nav-item to="/">Home</b-nav-item>
        <b-nav-item to="/searchResults">Search Results</b-nav-item>
      </b-navbar-nav>

      <!-- Right aligned nav items -->
      <b-navbar-nav class="ml-auto">
         <AddTodo v-on:add-todo="addTodo"/>
      </b-navbar-nav>
    </b-collapse>
  </b-navbar>
  <b-modal v-model="modalShow" centered v-bind:hide-footer="true" v-bind:hide-header="true" v-bind:no-close-on-backdrop="true">
    <div class="d-block text-center">
      <h3>Your search result is curently processing!</h3>
      <h4>Please wait for a moment</h4>
      <b-spinner label="Loading..."></b-spinner>
    </div>
  </b-modal>
</div>
</template>

<script>
import AddTodo from '../AddTodo'
import {AXIOS} from '../../http-common'
import {EventBus} from '../../main'
// import { platform } from 'os';

export default {
  name: 'Header',
  components: {
    AddTodo
  },
  data () {
    return {
      modalShow: false
    }
  },
  methods: {
    addTodo (newTodo) {
      // Send up to parent
      // this.$emit('add-todo', newTodo)
      var params = new FormData()
      const { name, location, keyWords, platforms, numPage, file } = newTodo
      var list = []
      var i
      for (i = 0; i < keyWords.length; i++) {
        list.push(keyWords[i].word)
      }
      params.append('name', name)
      params.append('location', location)
      params.append('numPage', numPage)
      params.append('keywords', list)
      params.append('platforms', platforms)
      params.append('file', file)
      console.log(name)
      console.log(numPage)
      console.log(location)
      console.log(list)
      console.log(platforms)
      console.log(file)
      AXIOS.post('/all', params, {headers: {'Content-Type': 'multipart/form-data'}})
        .then(res => {
          // this.todos = res.data
          if (this.$route.name === 'searchResults') {
            this.$router.go()
          } else {
            this.$router.push('/searchResults')
          }
        })
    },
    show () {
      this.modalShow = true
    },
    hide () {
      this.modalShow = false
    }
  },
  mounted () {
    EventBus.$on('before-request', this.show)
    EventBus.$on('after-response', this.hide)
  }
}
</script>
