<template>
  <div>
    <head-tab active="bankForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('bankForm.name')" prop="name">
          <el-input v-model="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('bankForm.account')" prop="accountIdList">
          <el-select v-model="inputForm.accountIdList" filterable remote multiple :placeholder="$t('bankForm.inputWord')" :remote-method="remoteAccount" :loading="remoteLoading" >
            <el-option v-for="item in accounts" :key="item.id" :label="item.fullName" :value="item.id"></el-option>
          </el-select>
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
    export default{
      data(){
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            formProperty:{},
            accounts:[],
            remoteLoading:false,
            inputForm:{
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
              axios.post('/api/ws/future/basic/bank/save',qs.stringify(this.inputForm)).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'bankList',query:util.getQuery("bankList")})
                }
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },remoteAccount(query) {
          if (query !== '') {
            this.remoteLoading = true;
            axios.get('/api/hr/account/search',{params:{key:query}}).then((response)=>{
              this.accounts=response.data;
              this.remoteLoading = false;
            })
          } else {
            this.accounts = [];
          }
        }
      },created(){
        if(!this.isCreate){
          axios.get('/api/ws/future/basic/bank/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
            if(response.data.accountList!=null&&response.data.accountList.length>0){
              this.accounts=response.data.accountList;
              this.inputForm.accountIdList=util.getIdList(this.accounts);
            }
          })
        }
      }
    }
</script>
