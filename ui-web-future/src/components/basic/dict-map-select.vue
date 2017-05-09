<template>
  <div>
    <el-select v-model="innerId"  filterable :clearable=true @change="handleChange">
      <el-option v-for="item in itemList"  :key="item.value" :label="item.name" :value="item.value"></el-option>
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
      },setValue(val) {
        this.innerId=val;
        this.remoteLoading = true;
      }
    },created () {
      axios.get('/api/basic/sys/dictMap/findByCategory?category=' + this.category).then((response)=>{
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
