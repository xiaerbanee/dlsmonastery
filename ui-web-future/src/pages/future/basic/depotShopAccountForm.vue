<template>
  <div>
    <head-tab active="depotShopAccountForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="选择门店" prop="shopId">
              <depot-select v-model="inputForm.shopId" category="shop" @input="shopSelectd"></depot-select>
            </el-form-item>
            <el-form-item label="账户绑定" prop="accountIds" v-if="isShopSelect">
              <account-select v-model="inputForm.accountIds" multiple="multiple"></account-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled"  @click="formSubmit()">保存</el-button>
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
          isShopSelect:false,
          submitDisabled:false,
          remoteLoading:false,
          inputForm:{
            extra:{}
          },
          rules: {
            shopId: [{ required: true, message: this.$t('shopImageForm.prerequisiteMessage')}]
          },
        }
      },
      formSubmit(){
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
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },shopSelectd(){
          this.isShopSelect = true;
          axios.get('/api/ws/future/basic/depotShop/findAccountIdsByDepotId',{params: {depotId:this.inputForm.shopId}}).then((response)=> {
            this.inputForm.accountIds = response.data;
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
