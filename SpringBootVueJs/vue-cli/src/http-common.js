import axios from 'axios'
import {EventBus} from '../src/main'

const HTTP = axios.create({
  baseURL: `http://localhost:8090`,
  headers: {
    'Access-Control-Allow-Origin': 'http://localhost:8080'
  }
})

// before a request is made start the nprogress
HTTP.interceptors.request.use(config => {
  EventBus.$emit('before-request')
  return config
})

// before a response is returned stop nprogress
HTTP.interceptors.response.use(response => {
  EventBus.$emit('after-response')
  return response
})

export const AXIOS = HTTP
