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
        innerBool:0
      }
    },
    methods:{
      onChange( newVal) {
        this.$emit('input', newVal);
        return true;
      },setValue(val) {
        if(util.isNotBlank(val)){
            if(val==true){
              this.innerBool=1
            }else if(val==false){
              this.innerBool=0
            }
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
