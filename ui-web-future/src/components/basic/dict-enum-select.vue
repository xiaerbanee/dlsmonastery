<template>
  <div>
    <el-select v-model="innerId"  filterable :clearable=true @change="handleChange">
      <el-option v-for="item in itemList"  :key="item.value" :label="item.value" :value="item.value"></el-option>
    </el-select>
  </div>
</template>
<script>
  export default {
    props: ['value','category'],
    data() {
      return {
        innerId:this.value,
        itemList : []
      };
    },methods:{
        handleChange(newVal) {
        this.$emit('input', newVal);
      }
    },created () {
      axios.get('/api/basic/sys/dictEnum/findByCategory?id=' + this.category).then((response)=>{
        this.itemList=response.data;
      })
      this.setValue(this.value);
    },watch: {
      value :function (newVal) {
        this.setValue(newVal);
      }
    }
  };
</script>
