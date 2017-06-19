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
      return this.getData()
    },
    methods:{
      getData() {
        return{
          isCreate:this.$route.query.id == null,
          submitDisabled:false,
          remoteLoading:false,
          inputForm:{
            extra:{}
          },
          rules: {
            name: [{ required: true, message: this.$t('bankForm.prerequisiteMessage')}]
          }
        }
      },
      formSubmit(){
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.submitDisabled = true;
            axios.post('/api/ws/future/basic/bank/save',qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
              this.$message(response.data.message);
              if(response.data.success) {
                if (this.isCreate) {
                  Object.assign(this.$data,this.getData());
                  this.initPage();
                }else {
                  this.submitDisabled = false;
                  this.$router.push({name: 'bankList', query: util.getQuery("bankList")})
                }
              }
            }).catch(() => {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        });
      },initPage(){
        axios.get('/api/ws/future/basic/bank/getForm').then((response)=>{
          this.inputForm = response.data;
          if(!this.isCreate){
            axios.get('/api/ws/future/basic/bank/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
              util.copyValue(response.data,this.inputForm);
            });
          }
        });
      }
    },created () {
      this.initPage();
    }
  }
</script>
