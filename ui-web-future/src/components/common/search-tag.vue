<template>
  <span>
    <span v-for="item of formCode">
      <el-tag type="primary" v-if="(formData[item] && formData[item] != '')" style="margin-left:5px;">
          {{formLabel[item].label}}：<span v-if="(formLabel[item].value  && formLabel[item].value != '')">{{formLabel[item].value}}</span><span v-else>{{formData[item]}}</span>
      </el-tag>
    </span>
  </span>
</template>
<script>
export default {
  props: ['formLabel','formData'],
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
