<template>
  <div class="todo-item">
    <b-card
    no-body
    :img-src="todo.imgSrc"
    img-alt="Image"
    img-fluid="w-100"
    img-top
    style="min-width: 12rem; max-width: 12rem;"
    class="mb-2"
    >
      <h4 id="header" slot="header">{{todo.name}}</h4>
      <b-card-body >
        <b-card-title class="mb-2">
          <p v-if="todo.location.trim().length === 0">-Nil-</p>
          <p v-else>{{todo.location}}</p>
        </b-card-title>
        <b-card-sub-title class="mb-2">{{todo.dateJoined}}</b-card-sub-title>
        <b-card-sub-title class="mb-2">Score: {{todo.totalScore}}</b-card-sub-title>
        <b-card-text v-if = "todo.platform === 'Twitter'">
          {{todo.description}}
        </b-card-text>
        <b-card-text v-else>
          {{todo.education}}
          {{todo.work}}
        </b-card-text>
      </b-card-body>
      <div slot="footer">
        <b-button id="footer" v-bind:href="todo.baselink" variant="primary">
          @{{todo.link}}
        </b-button>
        <ImagesExtra v-bind:todo="todo"/>
      </div>
    </b-card>
  </div>
</template>

<script>
import ImagesExtra from './ImagesExtra'

export default {
  name: 'TodoItem',
  components: {
    ImagesExtra
  },
  props: ['todo'],
  methods: {
  }
}
</script>

<style scoped>
.card {
    height: 85vh;
}
.card-body{
  overflow: hidden;
  text-overflow: "ellipsis";
}
#header{
  font-size:large;
}
#footer{
  margin:2px;
  flex: 1 0 100%;
  display: flex;
}
.mb-2 {
  font-size: small;
}
  /* .todo-item {
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
  } */
</style>
