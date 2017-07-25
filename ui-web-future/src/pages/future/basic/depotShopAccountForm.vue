<template>
  <div>
    <head-tab active="depotShopAccountForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form-item label="选择门店" prop="depotId">
              <depot-select v-model="inputForm.depotId" category="shop" @input="shopSelectd"></depot-select>
            </el-form-item>
            <el-form-item label="账户绑定" prop="accountIds" >
              <account-select v-model="inputForm.accountIds" multiple="multiple"></account-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled"  @click="formSubmit()">保存</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="选择账户" prop="accountId">
              <account-select v-model="inputForm.accountId" category="shop" @input="accountSelectd"></account-select>
            </el-form-item>
            <el-form-item label="门店绑定" prop="depotIdList" >
              <depot-select v-model="inputForm.depotIdList" multiple="multiple" category="shop"></depot-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>

<script>
  import depotSelect from 'components/future/depot-select';
  import accountSelect from 'components/basic/account-select';
  export default{
    components:{depotSelect,accountSelect},
    data(){
      return this.getData();
    },
    methods:{
      getData(){
        return{
          submitDisabled:false,
          remoteLoading:false,
          inputForm:{
            extra:{}
          },
          rules: {
          },
        }
      },
      formSubmit(){
        var that=this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/basic/depotShop/saveDepotAccount', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
              this.$message(response.data.message);
              if(response.data.success) {
                  this.$router.push({name: 'depotShopList', query: util.getQuery("depotShopList"), params:{_closeFrom:true}});
              }
            }).catch(() => {
              that.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },shopSelectd(){
          this.inputForm.accountId=null;
          this.inputForm.depotIdList=new Array();
          axios.get('/api/ws/future/basic/depotShop/findAccountIdsByDepotId',{params: {depotId:this.inputForm.depotId}}).then((response)=> {
            this.inputForm.accountIds = response.data;
          });
      },accountSelectd(){
        this.inputForm.depotId=null;
        this.inputForm.accountIds=new Array();
        axios.get('/api/ws/future/basic/depotShop/findDepotIdsByAccountId',{params: {accountId:this.inputForm.accountId}}).then((response)=> {
          this.inputForm.depotIdList = response.data;
        });
      },initPage(){
        axios.get('/api/ws/future/basic/depotShop/getAccountForm').then((response)=>{
          this.inputForm = response.data;
        });
      }
    },created(){
      this.initPage();
    }
  }
</script>
