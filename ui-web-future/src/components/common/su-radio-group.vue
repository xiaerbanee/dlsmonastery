<template>
  <div>
    <el-radio-group v-model="innerBool" @change="onChange">
      <el-radio :label=1>{{$t('depotForm.true')}}</el-radio>
      <el-radio :label=0>{{$t('depotForm.false')}}</el-radio>
    </el-radio-group>
  </div>
</template>
<script>
  export default {
    props: ['value'],
    data() {
      return {
        innerBool:null
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
        var bool=null
        if(util.isNotBlank(val)){
            bool=parseInt(val)
          this.innerBool=bool;

        }
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
