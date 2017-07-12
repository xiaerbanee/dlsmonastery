<template>
  <div>
    <el-radio-group v-model="innerBool" @change="onChange" :disabled="disabled">
      <el-radio :label=true>{{$t('depotForm.true')}}</el-radio>
      <el-radio :label=false>{{$t('depotForm.false')}}</el-radio>
    </el-radio-group>
  </div>
</template>
<script>
  export default {
    props: ['value','disabled'],
    data() {
      return {
        innerBool:false
      }
    },
    methods:{
      onChange( newVal) {
        this.$emit('input', newVal);
        return true;
      },setValue(val) {
        this.innerBool=val
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
