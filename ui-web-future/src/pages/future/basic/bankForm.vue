<template>
  <div>
    <head-tab active="bankForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('bankForm.name')" prop="name">
          <el-input v-model="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('bankForm.account')" prop="accountIdList">
          <account-select  v-model="inputForm.accountIdList" multiple="multiple"></account-select>
        </el-form-item>
        <el-form-item :label="$t('bankForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('bankForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import accountSelect from 'components/basic/account-select'
  export default{
    components:{
      accountSelect
    },
      data(){
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            remoteLoading:false,
            inputForm:{},
            submitData:{
              id:this.$route.query.id,
              name:'',
              accountIdList:"",
              remarks:''
            },
            rules: {
              name: [{ required: true, message: this.$t('bankForm.prerequisiteMessage')}]
            }
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              util.copyValue(this.inputForm,this.submitData)
              axios.post('/api/ws/future/basic/bank/save',qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'bankList',query:util.getQuery("bankList")})
                }
              }).catch(function () {
                this.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        }
      },created(){
          axios.get('/api/ws/future/basic/bank/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            this.inputForm=response.data;
          })
      }
    }
</script>
