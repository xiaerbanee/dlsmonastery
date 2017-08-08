<template>
  <div>
    <su-select v-model="innerId" :searchByKeyMethod="searchByKeyMethod" :searchByIdsMethod="searchByIdsMethod" labelProp="fullName" :remote="remote" :multiple="multiple" :disabled="disabled"  @input="handleInput"  @afterInit="handleAfterInit">
    </su-select>
  </div>
</template>
<script>
  export default {
    props: {
      value: {
        required: true
      },
      multiple: Boolean,
      disabled: Boolean,
      remote: {
        type: Boolean,
        default: true
      },
      officeRuleName: String,
    },
    data() {
      return {
        innerId : this.value,
        searchByKeyMethod : (query)=> axios.get('/api/basic/sys/office/search',{params:{officeRuleName:this.officeRuleName, name:query}}),
        searchByIdsMethod : (val)=> axios.get('/api/basic/sys/office/findByIds?idStr='+ val),
      };
    } ,methods:{
      handleInput(newVal) {
        this.$emit('input', newVal);
      }, handleAfterInit() {
        this.$emit('afterInit');
      }
    },watch: {
      value :function (newVal) {
        this.innerId = newVal;
      }
    }
  }
</script>
