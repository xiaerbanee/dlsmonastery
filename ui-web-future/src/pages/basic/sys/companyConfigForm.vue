<template>
  <div>
    <head-tab active="companyConfigForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('companyConfigForm.name')" prop="name">
          <el-input v-model="inputForm.name" :readonly="!isCreate"></el-input>
        </el-form-item>
         <el-form-item :label="$t('companyConfigForm.code')" prop="code">
           <el-input v-model="inputForm.code" :readonly="!isCreate"></el-input>
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
      data:function(){
        return this.getData();
      },
      methods:{
        getData(){
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            isCreate:true,
            inputForm:{
                extra:{}
            },
            rules: {
              value: [{ required: true, message: this.$t('companyConfigForm.prerequisiteMessage')}]
            }
          }
        },
        formSubmit(){
          var that = this;
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/basic/sys/companyConfig/save', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  Object.assign(this.$data,this.getData());
                  this.initPage();
                }else{
                  this.submitDisabled = false;
                  this.$router.push({name:'companyConfigList',query:util.getQuery("companyConfigList")})
                }
              }).catch(function () {
                that.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },initPage(){
            this.isCreate=this.$route.query.id==null;
            axios.get('/api/basic/sys/companyConfig/getForm').then((response)=> {
              this.inputForm = response.data;
              axios.get('/api/basic/sys/companyConfig/findOne', {params: {id: this.$route.query.id}}).then((response) => {
                util.copyValue(response.data,this.inputForm);
              })
            })
        }
      },created(){
        this.initPage();
      }
    }
</script>
