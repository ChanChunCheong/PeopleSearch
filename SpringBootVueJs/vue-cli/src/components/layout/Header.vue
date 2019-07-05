<template>
  <header class="header">
    <h1>People Search Aggregator</h1>
    <div id="nav">
      <router-link to="/home">Home</router-link> |
      <router-link to="/searchResults">Search Results</router-link>
    </div>
  </header>
</template>

<script>
export default {
  name: 'Header'
}
</script>

<style scoped>
  .header {
    background: #333;
    color: #fff;
    text-align: center;
    padding: 10px;
  }
  .header a {
    color: #fff;
    padding-right: 5px;
    text-decoration: none;
  }
</style>
