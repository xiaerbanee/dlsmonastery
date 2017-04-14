<template>
  <div>
    <head-tab :active="$t('companyConfigForm.companyConfigForm') "></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('companyConfigForm.name')" prop="name">
          <el-input v-model="inputForm.name" readonly></el-input>
        </el-form-item>
         <el-form-item :label="$t('companyConfigForm.code')" prop="code">
           <el-input v-model="inputForm.code" readonly></el-input>
         </el-form-item>
        <el-form-item :label="$t('companyConfigForm.value')" prop="value">
          <el-input v-model="inputForm.value"></el-input>
        </el-form-item>
        <el-form-item :label="$t('companyConfigForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"  :disabled="submitDisabled" @click="formSubmit()">{{$t('companyConfigForm.save')}}</el-button>
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
            inputForm:{
              id:'',
              name:'',
              code:'',
              value:'',
              remarks:''
            },
            rules: {
              value: [{ required: true, message: this.$t('companyConfigForm.prerequisiteMessage')}]
            }
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/basic/sys/companyConfig/save', qs.stringify(this.inputForm)).then((response)=> {
                this.$message(response.data.message);
                 if(this.isCreate){
                    form.resetFields();
                    this.submitDisabled = false;
                 } else {
                    this.$router.push({name:'companyConfigList',query:util.getQuery("companyConfigList")})
                }
              });
            }else{
              this.submitDisabled = false;
            }
          })
        }
      },created(){
        if(!this.isCreate){
          axios.get('/api/basic/sys/companyConfig/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
          })
        }
      }
    }
</script>
