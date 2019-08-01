<template>
  <div>
    <b-button v-b-modal.modal-2>Search</b-button>
    <b-modal
      id="modal-2"
      ref="modal"
      title="Submit Search"
      @show="resetModal"
      @hidden="resetModal"
      @ok="handleOk"
    >
      <b-form  ref="form" @submit.stop.prevent="addTodo" @reset="resetModal">
        <b-form-group
          id="input-group-1"
          :state="nameState"
          label="Name: "
          label-for="input-1"
          description="Please enter full name for accurate result"
        >
          <b-form-input
            id="input-1"
            :state="nameState"
            v-model="name"
            name="name"
            required
            placeholder="Enter name"
          ></b-form-input>
        </b-form-group>
        <b-button id="add" variant="success" @click="addKeywordFields">
          Add Keyword
        </b-button>
        <b-form-group v-for="keyword in keyWords" v-bind:key="keyword.id" id="input-group-2">
          <div class="flex">
          <b-form-input
            id="input-2"
            v-model="keyword.word"
            name="keyword"
            required
            placeholder="Enter keyword"
          >
          </b-form-input>
          <b-button id="del" variant="danger" @click="deleteKeywordFields">
            X
          </b-button>
          </div>
        </b-form-group>

        <b-form-group id="input-group-3" label="Location:" label-for="input-3">
          <b-form-select
            id="input-3"
            v-model="location"
            name="location"
            :options="locations"
            required
          ></b-form-select>
        </b-form-group>
        <b-form-group label="Number of pages to scrape:">
          <b-form-radio-group
            id="radio-group-1"
            v-model="numPage"
            :options="numofPage"
            name="numPage"
          ></b-form-radio-group>
        </b-form-group>
        <b-form-group label="Social media platforms:">
           <b-form-checkbox-group
            id="input-4"
            v-model="platforms"
            :options="options"
            name="platforms"
          ></b-form-checkbox-group>
        </b-form-group>
        <b-form-group label="Image of Person:">
          <b-form-file
            accept=".jpg"
            v-model="file"
            :state="Boolean(file)"
            placeholder="Choose a file..."
            drop-placeholder="Drop file here..."
            enctype="multipart/form-data"
          ></b-form-file>
          <div class="mt-3">Selected file: {{ file ? file.name : '' }}</div>
        </b-form-group>
       </b-form>
     </b-modal>
  </div>
</template>

<script>
import CountryList from 'country-list'
// import uuid from 'uuid';
export default {
  name: 'AddTodo',
  data () {
    return {
      name: '',
      nameState: null,
      location: '',
      keyWords: [{word: ''}],
      locations: CountryList.getNames(),
      platforms: '', // Must be an array reference!
      options: [
        { text: 'Twitter', value: 'twitter' },
        { text: 'Facebook', value: 'facebook' }
      ],
      numPage: '', // Must be an array reference!
      numofPage: [
        { text: 'one', value: '1' },
        { text: 'two', value: '2' },
        { text: 'max', value: '100' }
      ],
      file: null
    }
  },
  methods: {
    checkFormValidity () {
      const valid = this.$refs.form.checkValidity()
      this.nameState = valid ? 'valid' : 'invalid'
      return valid
    },
    addTodo () {
      if (!this.checkFormValidity()) {
        return
      }
      const newTodo = {
        name: this.name,
        location: this.location,
        keyWords: this.keyWords,
        platforms: this.platforms,
        numPage: this.numPage,
        file: this.file
      }
      // Send up to parent
      this.$emit('add-todo', newTodo)
      console.log('emitted from addtodo')
      this.name = ''
      this.location = ''
      this.keyWords = ''
      this.file = ''
      this.numPage = ''
      this.platforms = ''
      this.$nextTick(() => {
        this.$refs.modal.hide()
      })
    },
    resetModal () {
      // Reset our form values
      this.name = ''
      this.keyWords = [{word: ''}]
      this.nameState = null
      this.location = null
    },
    handleOk (bvModalEvt) {
      // Prevent modal from closing
      bvModalEvt.preventDefault()
      // Trigger submit handler
      this.addTodo()
    },
    addKeywordFields () {
      this.keyWords.push({
        word: ''
      })
    },
    deleteKeywordFields (index) {
      this.keyWords.splice(index, 1)
    }
  }
}
</script>

<style scoped>
  /* form {
    display: flex;
  } */
  input[type="text"] {
    flex: 10;
    padding: 5px;
  }
  input[type="submit"] {
    flex: 2;
  }
  .flex {
    display: flex;
  }
  #add {
      background-color:green;
  }
  #del {
    background-color: red;
  }
  /* .btn {
    display: inline-block;
    border: none;
    padding: 7px 20px;
    cursor: pointer;
  } */
</style>
