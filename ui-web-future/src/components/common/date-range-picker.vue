<template>
  <div>
    <el-date-picker  v-model="innerDateRange" :disabled="disabled"  type="daterange"  :placeholder="$t('su_date_ranger_picker.selectDateRange')"  align="right"  @change="onChange" :picker-options="pickerDateOption"></el-date-picker>
  </div>
</template>
<script>
  export default {
    props: ['value','disabled'],
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
        this.$emit('input', newVal);
        return true;
      },setValue(val) {
        var range = [];
        if(util.isNotBlank(val)) {
          range[0] = new Date(val.split(" - ")[0].replace(/-/,"/"));
          range[1] = new Date(val.split(" - ")[1].replace(/-/,"/"));
        }
        this.innerDateRange =range;
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
