<template>
  <div>
    <head-tab active="jobForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('jobForm.jobName')" prop="name">
          <el-input v-model="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('jobForm.permission')" prop="permission">
          <el-input v-model="inputForm.permission"></el-input>
        </el-form-item>
        <el-form-item :label="$t('jobForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('jobForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return {
        isCreate: this.$route.query.id == null,
        submitDisabled: false,
        inputForm:{},
        submitData: {
          id: this.$route.query.id,
          name: '',
          permission: '',
          remarks: ''
        },
        rules: {
          name: [{required: true, message: this.$t('jobForm.prerequisiteMessage')}],
          permission: [{required: true, message: this.$t('jobForm.prerequisiteMessage')}]
        },
      };
    },
    methods: {
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            util.copyValue(this.inputForm, this.submitData);
            axios.post('/api/basic/hr/job/save', qs.stringify(this.submitData)).then((response) => {
              this.$message(response.data.message);
              if (this.isCreate) {
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name: 'jobList', query: util.getQuery("jobList")})
              }
            });
          } else {
            this.submitDisabled = false;
          }
        })
      },
      handleCheckChange(data, checked, indeterminate) {
      },
    }, created(){
      axios.get('/api/basic/hr/job/findOne', {params: {id: this.$route.query.id}}).then((response) => {
        util.copyValue(response.data, this.inputForm);
      })
    }
  }
</script>
