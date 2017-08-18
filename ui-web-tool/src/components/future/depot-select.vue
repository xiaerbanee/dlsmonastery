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
      remote: {
        type: Boolean,
        default: true
      },
      category: {
        type: String,
        default: 'depot'
      },
      includeHidden: Boolean,
    },

    data() {
      return {
        innerId : this.value,
        searchByKeyMethod : (query)=> axios.get("/api/ws/future/basic/depot/" + this.category, {params:{name:query, includeHidden : this.includeHidden }}),
        searchByIdsMethod : (val)=> axios.get('/api/ws/future/basic/depot/findByIds?idStr=' + val),
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
