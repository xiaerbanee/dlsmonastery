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
            isInit:false,
            submitDisabled:false,
            isCreate:true,
            inputForm:{},
            submitData:{
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
        formSubmit(){
          var that = this;
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              util.copyValue(this.inputForm,this.submitData);
              axios.post('/api/basic/sys/companyConfig/save', qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                Object.assign(this.$data,this.getData());
                if(!this.isCreate){
                  this.$router.push({name:'companyConfigList',query:util.getQuery("companyConfigList")})
                }
              }).catch(function () {
                that.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        }
      },activated () {
        if(!this.$route.query.headClick || !this.isInit) {
          Object.assign(this.$data,this.getData());
          this.isCreate=this.$route.query.id==null
          axios.get('/api/basic/sys/companyConfig/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            this.inputForm = response.data;
          })
        }
        this.isInit = true;
      }
    }
</script>
