<template>
  <span>
    <span v-for="item of formCode">
      <el-tag type="primary" v-if="(childFormData[item] && childFormData[item] != '')" style="margin-left:5px;">
          {{childFormLabel[item].label}}：<span v-if="(childFormLabel[item].value  && childFormLabel[item].value != '')">{{childFormLabel[item].value}}</span><span v-else>{{childFormData[item]}}</span>
      </el-tag>
    </span>
  </span>
</template>
<script>
export default {
  props: ['formLabel','formData'],
  data() {
    return {
      childFormLabel : this.formLabel,
      childFormData:this.formData
    };
  },computed: {
    formCode: function () {
      var formCode = new Array();
      for(var key in this.childFormLabel) {
        formCode.push(key);
      }
      return formCode;
    }
  }
};
</script>
