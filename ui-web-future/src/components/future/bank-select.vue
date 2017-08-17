<template>
  <div>
    <su-select v-model="innerId" :searchByKeyMethod="searchByKeyMethod" :searchByIdsMethod="searchByIdsMethod" :remote="remote" :multiple="multiple" :disabled="disabled"  @input="handleInput"  @afterInit="handleAfterInit">
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
      remote: Boolean,
      includeCash: Boolean,
    },
    data() {
      return {
        innerId : this.value,
        searchByKeyMethod : (query)=> axios.get('/api/ws/future/basic/bank/search', {params:{key:query, includeCash : this.includeCash}}),
        searchByIdsMethod : (val)=> axios.get('/api/ws/future/basic/bank/findByIds?ids='+ val),
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
