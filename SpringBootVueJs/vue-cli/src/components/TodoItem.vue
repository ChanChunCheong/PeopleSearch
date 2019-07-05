<template>
  <div class="todo-item" v-bind:class="{'is-complete':todo.completed}">
    <b-card
    no-body
    :img-src="todo.imgSrc"
    img-alt="Image"
    img-top
    style="max-width: 20rem;"
    class="mb-2"
    >
      <h4 slot="header">{{todo.name}}</h4>
      <b-card-body>
        <b-card-title class="mb-2">
          <p v-if="todo.location.trim().length === 0">-Nil-</p>
          <p v-else>{{todo.location}}</p>
        </b-card-title>
        <b-card-sub-title class="mb-2">{{todo.dateJoined}}</b-card-sub-title>
        <b-card-text>
          {{todo.description}}
        </b-card-text>
        <!-- <b-button v-bind:href="todo.baselink" variant="primary">@{{todo.link}}</b-button> -->
      </b-card-body>
      <div slot="footer"><b-button v-bind:href="todo.baselink" variant="primary">@{{todo.link}}</b-button></div>
    </b-card>
    <!-- <p>
      <input type="checkbox" v-on:change="markComplete" v-bind:checked="todo.completed">
      <button @click="$emit('del-todo', todo.id)" class="del">x</button>
      </p> -->
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
