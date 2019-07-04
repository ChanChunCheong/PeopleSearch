<template>
  <div>
    <form @submit.prevent="addTodo">
      <input type="text" v-model="name" name="name" placeholder="Name">
      <input type="text" v-model="location" name="location" placeholder="Location">
      <input type="text" v-model="keyword" name="keyword" placeholder="Keyword">
      <input type="submit" value="Search" class="btn">
    </form>
  </div>
</template>

<script>
// import uuid from 'uuid';
export default {
  name: 'AddTodo',
  data () {
    return {
      name: '',
      location: '',
      keyword: ''
    }
  },
  methods: {
    addTodo () {
      const newTodo = {
        name: this.name,
        location: this.location,
        keyword: this.keyword
      }
      // Send up to parent
      this.$emit('add-todo', newTodo)
      this.name = ''
      this.location = ''
      this.keyword = ''
    }
  }
}
</script>

<style scoped>
  form {
    display: flex;
  }
  input[type="text"] {
    flex: 10;
    padding: 5px;
  }
  input[type="submit"] {
    flex: 2;
  }
</style>
