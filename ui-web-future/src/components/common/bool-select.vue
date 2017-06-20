<template>
  <div>
    <el-select v-model="innerId"  filterable clearable @change="handleChange" :disabled="disabled" >
      <el-option v-for="item in itemList"  :key="item" :label="item | bool2str" :value="item"></el-option>
    </el-select>
  </div>
</template>
<script>
  export default {
    props: ['value','disabled'],
    data() {
      return {
        innerId: this.value,
        itemList:[true, false],
      };
    },methods:{
      handleChange(newVal) {
        this.$emit('input', newVal);
      },
      setValue(val){

        if(this.innerId === val) {
          return;
        }
        this.innerId=val;
      }
    },created () {
      this.setValue(this.value);
    },watch: {
      value :function (newVal) {
          this.setValue(newVal);
      }
    }
  };
</script>
