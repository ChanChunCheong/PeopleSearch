<template>
<div>
  <b-navbar toggleable="lg" type="dark" variant="info">
    <b-navbar-brand href="#">People Search Agregator</b-navbar-brand>

    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

    <b-collapse id="nav-collapse" is-nav>
      <b-navbar-nav>
        <b-nav-item to="/home">Home</b-nav-item>
        <b-nav-item to="/searchResults">Search Results</b-nav-item>
      </b-navbar-nav>

      <!-- Right aligned nav items -->
      <b-navbar-nav class="ml-auto">
         <AddTodo v-on:add-todo="addTodo"/>
      </b-navbar-nav>
    </b-collapse>
  </b-navbar>
</div>
</template>

<script>
import AddTodo from '../AddTodo'
import {EventBus} from '../../main'

export default {
  name: 'Header',
  components: {
    AddTodo
  },
  data () {
    return {
    }
  },
  methods: {
    addTodo (newTodo) {
      // Send up to parent
      // this.$emit('add-todo', newTodo)
      EventBus.$emit(
        'add-todo', newTodo)
    },
    emitMethod () {
      EventBus.$emit(
        'EVENT_NAME', 'Hello')
    }
  }
}
</script>
