<template>
  <div>
    <el-date-picker  v-model="innerDateRange"  type="daterange" align="right"  @change="onChange" :picker-options="pickerDateOption"></el-date-picker>
  </div>
</template>
<script>
  export default {
    props: ['dateRange'],
    data() {
      return {
        pickerDateOption : util.pickerDateOption,
        innerDateRange:[]
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
        var range = [];
        if(util.isNotBlank(this.dateRange)) {
          range[0] = new Date(this.dateRange.split(" - ")[0].replace(/-/,"/"));
          range[1] = new Date(this.dateRange.split(" - ")[1].replace(/-/,"/"));
        }
        this.innerDateRange =range;
    }
  };
</script>
