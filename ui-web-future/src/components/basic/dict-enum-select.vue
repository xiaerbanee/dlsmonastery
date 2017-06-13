<template>
  <div>
    <el-select v-model="innerId"  filterable :clearable=true @change="handleChange" :multiple="multiple" :disabled="disabled" >
      <el-option v-for="item in itemList"  :key="item.value" :label="item.value" :value="item.value"></el-option>
    </el-select>
  </div>
</template>
<script>
  export default {
    props: ['value','category','multiple','disabled'],
    data() {
      return {
        innerId: this.value,
        itemList : [],
      };
    },methods:{
      handleChange(newVal) {
        this.$emit('input', newVal);
      },
      setValue(val){
          this.innerId=val;
      }
    },created () {
      axios.get('/api/basic/sys/dictEnum/findByCategory?category=' + this.category).then((response)=>{
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
