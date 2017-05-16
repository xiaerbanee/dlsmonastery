<template>
  <div>
    <el-date-picker  v-model="innerDateTime"  type="datetime"  align="right"  @change="onChange" ></el-date-picker>
  </div>
</template>
<script>
  export default {
    props: ['value'],
    data() {
      return {
        innerDateTime:null
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
          var datetime=null;
        if(util.isNotBlank(val)) {
          datetime=new Date(Date.parse(val.replace(/-/g, "/")))
        }
        this.innerDateTime =datetime;
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
