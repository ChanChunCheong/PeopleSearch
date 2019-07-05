import Vue from 'vue'
import Router from 'vue-router'
import Home from '../views/Home.vue'
import searchResults from '../views/searchResults.vue'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/home',
      name: 'home',
      component: Home
    },
    {
      path: '/searchResults',
      name: 'searchResults',
      component: searchResults
    }
  ]
})
