<template>
  <div>
    <head-tab active="shopGoodsDepositForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('shopGoodsDepositForm.shopName')" prop="shopId" >
              <el-select v-model="inputForm.shopId"  filterable remote :placeholder="$t('shopGoodsDepositForm.inputWord')" :remote-method="remoteDepot" :loading="remoteLoading" :clearable=true >
                <el-option v-for="depot in depots" :key="depot.id" :label="depot.name" :value="depot.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('shopGoodsDepositForm.bank')" prop="bankId" >
              <el-select v-model="inputForm.bankId"  filterable clearable  :placeholder="$t('shopGoodsDepositForm.inputWord')" >
                <el-option v-for="item in formProperty.banks" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('shopGoodsDepositForm.amount')" prop="amount">
              <el-input  v-model="inputForm.amount"></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopGoodsDepositForm.department')" prop="departMent">
              <el-select v-model="inputForm.departMent" >
                <el-option v-for="item in formProperty.departMents" :key="item.fnumber"  :label="item.fname" :value="item.fnumber"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('shopGoodsDepositForm.remarks')" prop="remarks" >
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()" v-if="inputForm.imeStr !==''">{{$t('shopGoodsDepositForm.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
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
            depots:[],
            remoteLoading:false,
            inputForm:{
              shopId:'',
              bankId:"",
              amount:"",
              departMent:"",
              remarks:''
            },
            rules: {
              shopId: [{ required: true, message: this.$t('shopGoodsDepositForm.prerequisiteMessage')}],
              bankId: [{ required: true, message: $t('shopGoodsDepositForm.prerequisiteMessage')}]
            },
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/crm/shopGoodsDeposit/save',qs.stringify(this.inputForm)).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'定金收款',query:util.getQuery("定金收款")})
                }
              }).catch(function () {
                this.submitDisabled = false;
              });
            }
          })
        },remoteDepot(query){
          if (query !== '') {
            this.remoteLoading = true;
            axios.get('/api/crm/depot/search',{params:{name:query,category:"SHOP"}}).then((response)=>{
              this.depots=response.data;
              this.remoteLoading = false;
            })
          }
          if(this.inputForm.shopId!==''){
            axios.get('/api/crm/shopGoodsDeposit/searchDepartMent',{params:{shopId:this.inputForm.shopId}}).then((response)=>{
              this.inputForm.departMent=response.data.departMent;
            })
          }
        }
      },created(){
        axios.get('/api/crm/shopGoodsDeposit/getFormProperty').then((response)=>{
          this.formProperty = response.data;
        });
        if(!this.isCreate){
          axios.get('/api/crm/shopGoodsDeposit/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
            if(response.data.shop != null){
              this.depots = new Array(response.data.shop)
            }
          })
        }
      }
    }
</script>
