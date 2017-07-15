<template>
  <div>
    <el-date-picker  v-model="innerDate" :editable=false  :disabled="disabled"  align="left"  @change="onChange" format="yyyy-MM-dd"></el-date-picker>
  </div>
</template>
<script>
  export default {
    props: ['value','disabled'],
    data() {
      return {
        innerDate:null
      }
    },
    methods:{
      onChange( newVal) {
        if(!newVal) {
            newVal = "";
        }
        this.$emit('input', newVal);
        return true;
      },setValue(val) {
        var date = null;
        if(util.isNotBlank(val)) {
          date = new Date(val.replace(/-/,"/"));
        }
        this.innerDate =date;
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
