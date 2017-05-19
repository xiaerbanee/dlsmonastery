<template>
  <span>
    <span v-for="item of formCode">
      <el-tag type="primary" v-if="(submitData[item] && submitData[item] != '')" style="margin-left:5px;">
          {{formLabel[item].label}}：<span v-if="(formLabel[item].value  && formLabel[item].value != '')">{{formLabel[item].value}}</span><span v-else>{{submitData[item]}}</span>
      </el-tag>
    </span>
  </span>
</template>
<script>
export default {
  props: ['formLabel','submitData'],
  data() {
    return {
    };
  },computed: {
    formCode: function () {
      var formCode = new Array();
      for(var key in this.formLabel) {
        formCode.push(key);
      }
      return formCode;
    }
  }
};
</script>
