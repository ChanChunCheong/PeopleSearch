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
        <b-form-group id="input-group-2" label="Keyword:" label-for="input-2">
          <b-form-input
            id="input-2"
            v-model="keyword"
            name="keyword"
            required
            placeholder="Enter keyword"
          ></b-form-input>
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
        <b-form-group label="Social Media Platforms:">
          <b-form-select
            id="input-4"
            v-model="selected"
            :options="options"
            name="platforms"
            required
          ></b-form-select>
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
      keyword: '',
      locations: CountryList.getNames(),
      selected: '', // Must be an array reference!
      options: [
        { text: 'Twitter', value: 'twitter' },
        { text: 'Facebook', value: 'facebook' }
      ]
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
        keyword: this.keyword
      }
      // Send up to parent
      this.$emit('add-todo', newTodo)
      console.log('emitted from addtodo')
      this.name = ''
      this.location = ''
      this.keyword = ''
      this.$nextTick(() => {
        this.$refs.modal.hide()
      })
    },
    resetModal () {
      // Reset our form values
      this.name = ''
      this.keyword = ''
      this.nameState = null
      this.location = null
    },
    handleOk (bvModalEvt) {
      // Prevent modal from closing
      bvModalEvt.preventDefault()
      // Trigger submit handler
      this.addTodo()
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
</style>
