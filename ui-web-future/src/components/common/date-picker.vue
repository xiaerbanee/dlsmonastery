<template>
  <div>
    <el-date-picker  v-model="innerDate"  align="right"  @change="onChange" :picker-options="pickerDateOption"></el-date-picker>
  </div>
</template>
<script>
  export default {
    props: ['value'],
    data() {
      return {
        pickerDateOption : util.pickerDateOption,
        innerDate:null
      }
    },
    methods:{
      onChange( newVal) {
        if(!newVal) {
            newVal = "";
        }
        this.$emit('change', newVal);
        return true;
      }
    },created () {
        this.innerDate =new Date(this.value.replace(/-/,"/"));;
    }
  };
</script>
