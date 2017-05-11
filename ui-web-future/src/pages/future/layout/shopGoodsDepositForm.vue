<template>
  <div>
    <head-tab active="shopGoodsDepositForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('shopGoodsDepositForm.shopName')" prop="shopId" >
              <depot-select  :disabled="!isCreate" category="SHOP" v-model="inputForm.shopId"  @input="shopChanged"></depot-select>
            </el-form-item>
            <el-form-item :label="$t('shopGoodsDepositForm.bank')" prop="bankId" >
              <bank-select v-model="inputForm.bankId"></bank-select>
            </el-form-item>
            <el-form-item :label="$t('shopGoodsDepositForm.amount')" prop="amount">
              <el-input  v-model="inputForm.amount"></el-input>
            </el-form-item>
            <el-form-item v-if="isCreate" :label="$t('shopGoodsDepositForm.department')" prop="departMent">
              <el-select v-model="inputForm.departMent" >
                <el-option v-for="item in inputForm.departMents" :key="item.fnumber"  :label="item.fname" :value="item.fnumber"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('shopGoodsDepositForm.remarks')" prop="remarks" >
              <el-input type="textarea" v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()" >{{$t('shopGoodsDepositForm.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<script>

  import depotSelect from 'components/future/depot-select'

  import bankSelect from 'components/future/bank-select'

  export default{
    components:{
      depotSelect,
      bankSelect
    },

      data(){
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            inputForm:{},
            submitData:{
              shopId:'',
              bankId:"",
              amount:"",
              departMent:"",
              remarks:'',
            },

            rules: {
              shopId: [{ required: true, message: this.$t('shopGoodsDepositForm.prerequisiteMessage')}],
              bankId: [{ required: true, message: this.$t('shopGoodsDepositForm.prerequisiteMessage')}],
            },
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              util.copyValue(this.inputForm,this.submitData);
              axios.post('/api/ws/future/crm/shopGoodsDeposit/save', qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'shopGoodsDepositList',query:util.getQuery("shopGoodsDepositList")})
                }
              }).catch(function () {
                this.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },shopChanged(){
          axios.get('/api/ws/future/crm/shopGoodsDeposit/findForm',{params: {shopId:this.inputForm.shopId}}).then((response)=>{
            this.inputForm = response.data;
          })

        }
      },created(){
        axios.get('/api/ws/future/crm/shopGoodsDeposit/findForm',{params: {id: this.$route.query.id}}).then((response)=>{
          this.inputForm = response.data;
        })
      }
    }
</script>
