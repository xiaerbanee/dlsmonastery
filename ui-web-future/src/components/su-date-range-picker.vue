<template>
  <div>
    <el-date-picker  v-model="innerDateRange"  type="daterange" align="right" :placeholder="$t('su_date_ranger_picker.selectDateRange')"   @change="onChange" :picker-options="pickerDateOption"></el-date-picker>
  </div>
</template>
<script>
  export default {
    props: {
      value: {
        required: true
      }
    },
    data() {
      return {
        pickerDateOption : util.pickerDateOption,
        innerDateRange:this.value,
      };
    },

    methods:{
      onChange( newVal) {
        this.$emit('input', newVal);
        return true;
      },
    }
  };
</script>
