<template>
  <div>
    <el-date-picker  v-model="innerMonth"  align="left"  type="month"  @change="onChange" format="yyyy-MM" ></el-date-picker>
  </div>
</template>
<script>
  export default {
    props: ['value'],
    data() {
      return {
        innerMonth:null
      }
    },
    methods:{
      onChange( newVal) {
        if(!newVal) {
            newVal = "";
        }
        console.log(typeof newVal);
        console.log( newVal);
        this.$emit('input', newVal);
        return true;
      },setValue(val) {
        var month = null;
        if(util.isNotBlank(val)) {
          //month = new Date(val.replace(/-/,"/"));
        }
        this.innerMonth =val;
      }
    },created () {
       this.setValue(this.value);
    },watch: {
      value :function (val) {
          this.setValue(val);
      }
    }
  };
</script>
