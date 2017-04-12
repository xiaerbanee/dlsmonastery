<template>
  <div>
    <head-tab :active="$t('dealerForm.dealerForm') "></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('dealerForm.loginName')" prop="loginName">
          <el-input v-model.number="inputForm.loginName"></el-input>
        </el-form-item>
        <el-form-item :label="$t('dealerForm.name')" prop="name">
          <el-input v-model="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('dealerForm.mobilePhone')" prop="mobilePhone">
          <el-input v-model="inputForm.mobilePhone"></el-input>
        </el-form-item>
        <el-form-item :label="$t('dealerForm.password')" prop="password">
          <el-input v-model="inputForm.password"></el-input>
        </el-form-item>
        <el-form-item :label="$t('dealerForm.confirmPassword')" prop="confirmPassword">
        <el-input v-model="inputForm.confirmPassword"></el-input>
      </el-form-item>
        <el-form-item :label="$t('dealerForm.remarks')" prop="remarks">
        <el-input v-model="inputForm.remarks"></el-input>
      </el-form-item>
        <el-form-item>
          <el-button type="primary"  :disabled="submitDisabled" @click="formSubmit()">{{$t('dealerForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        formProperty:{},
        inputForm:{
          id:'',
          loginName:'',
          name:'',
          mobilePhone:'',
          password:'',
          confirmPassword:'',
          remarks:'',
        },
        rules: {
          loginName: [{ required: true, message: this.$t('dealerForm.prerequisiteMessage')}],
          name: [{ required: true, message: this.$t('dealerForm.prerequisiteMessage')}],
          mobilePhone: [{ required: true, message: this.$t('dealerForm.prerequisiteMessage')}]
        }
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.get('/api/crm/dealer/save', qs.stringify(this.inputForm)).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'dealerList',query:util.getQuery("dealerList")})
              }
            });
          }else{
            this.submitDisabled = false;
          }
        })
      }
    },created(){
      if(!this.isCreate){
        axios.get('/api/crm/dealer/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          util.copyValue(response.data,this.inputForm);
        })
      }
    }
  }
</script>

