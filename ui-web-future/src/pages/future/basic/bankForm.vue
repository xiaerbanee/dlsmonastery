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
            isInit:false,
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            remoteLoading:false,
            inputForm:{},
            submitData:{
              id:'',
              name:'',
              accountIdList:"",
              remarks:''
            },
            rules: {
              name: [{ required: true, message: this.$t('bankForm.prerequisiteMessage')}]
            }
          }
      },
        formSubmit(){
          var that = this;
          let form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              this.submitDisabled = true;
              util.copyValue(this.inputForm,this.submitData);
              axios.post('/api/ws/future/basic/bank/save',qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                Object.assign(this.$data, this.getData());
                if(response.data.success) {
                  if (!this.isCreate) {
                    this.$router.push({name: 'bankList', query: util.getQuery("bankList")})
                  }
                }
              }).catch(() => {
                that.submitDisabled = false;
              });
            }
          });
        }
    },activated () {
      if(!this.$route.query.headClick || !this.isInit) {
          axios.get('/api/ws/future/basic/bank/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            this.inputForm=response.data;
        });
        }
      this.isInit = true;
      }
    }
</script>
