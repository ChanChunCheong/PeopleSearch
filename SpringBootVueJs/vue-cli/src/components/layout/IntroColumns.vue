<template>
  <div>
    <br>
    <b-container fluid class="bv-example-row">
      <b-row>
        <b-col sm="4" class="">
            <b-card
                img-src="https://d2v9y0dukr6mq2.cloudfront.net/video/thumbnail/uh59Wh0/tools-icon-cartoon-illustration-hand-drawn-animation-transparent_nji91ic__F0004.png"
                img-alt="Image"
                img-top
                tag="article"
                style="max-width: 20rem;"
                class="mx-auto"
                border-variant="light"
            >
                <b-card-title class="text-center">
                Tools
                </b-card-title>
                <b-card-text>
                People Search Agregator is build using Vue.js, SpringBoot, Elasticseach, Scrapy and Selenium.
                </b-card-text>
            </b-card>
        </b-col>
        <b-col sm="4">
            <b-card
                img-src="https://d2v9y0dukr6mq2.cloudfront.net/video/thumbnail/mCz7hqj/videoblocks-magnifying-glass-search-icon-in-and-out-animation-loop-blue_hsny6k8ym_thumbnail-full04.png"
                img-alt="Image"
                img-top
                tag="article"
                style="max-width: 20rem;"
                class="mx-auto"
                border-variant="light"
            >
                <b-card-title class="text-center">
                Search Features
                </b-card-title>
                <b-card-text>
                People Search Agregator is able to filter out the required person profile through image recognition, keyword search, name and location matching
                </b-card-text>
            </b-card>
        </b-col>
        <b-col sm="4">
            <b-card
                img-src="https://images.pond5.com/social-media-platforms-their-logos-footage-054998368_prevstill.jpeg"
                img-alt="Image"
                img-top
                tag="article"
                style="max-width: 20rem;"
                class="mx-auto"
                border-variant="light"
            >
                <b-card-title class="text-center">
                Social Media Platforms
                </b-card-title>
                <b-card-text>
                The searchable platforms currently are Twitter and Facebook. However, the application aims to cover other platforms like Linkedin and Instagram in the future.
                </b-card-text>
            </b-card>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
export default {
  name: 'IntroColumns',
  components: {
  },
  data () {
    return {
    }
  },
  methods: {
  }
}
</script>
