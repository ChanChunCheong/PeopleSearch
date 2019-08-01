<template>
  <div>
      <b-button @click="modalShow = !modalShow">Images</b-button>
        <b-modal
        v-model="modalShow"
        title="Additional Images"
        >
            <b-card-group deck>
            <div v-bind:key="image_url" v-for="image_url in todo.image_urls">
            <ImageItem v-bind:image_url="image_url"/>
            </div>
            </b-card-group>
        </b-modal>
  </div>
</template>

<script>
import ImageItem from './ImageItem'
export default {
  name: 'ImagesExtra',
  components: {
    ImageItem
  },
  data () {
    return {
      modalShow: false
    }
  },
  props: ['todo']
}
</script>
