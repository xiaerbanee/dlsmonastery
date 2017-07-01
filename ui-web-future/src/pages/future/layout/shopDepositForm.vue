<template>
  <div>
    <head-tab active="shopDepositForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('shopDepositForm.shopName')" prop="shopId">
          <depot-select v-model ="inputForm.shopId"  category="directShop" @input="shopChanged"></depot-select>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.outBillType')" prop="outBillType" >
          <el-select v-model="inputForm.outBillType" clearable :placeholder="$t('shopDepositForm.inputKey')">
            <el-option v-for="item in inputForm.extra.outBillTypeList" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.department')" prop="departMent" >
          <el-select v-model="inputForm.departMent" clearable :placeholder="$t('shopDepositForm.inputKey')">
            <el-option v-for="item in inputForm.extra.departMentList" :key="item.fnumber" :label="item.ffullName" :value="item.fnumber"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.bank')" prop="bankId" v-if="inputForm.outBillType==='手工日记账'" >
          <bank-select v-model = "inputForm.bankId"></bank-select>
        </el-form-item>
        <el-form-item   :label="$t('shopDepositForm.billDate')" prop="billDate" >
          <date-picker  v-model="inputForm.billDate" ></date-picker>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.marketAmount')" prop="marketAmount" >
          <el-input v-model.number="inputForm.marketAmount"></el-input>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.imageAmount')" prop="imageAmount" >
          <el-input v-model.number="inputForm.imageAmount"></el-input>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.demoPhoneAmount')" prop="demoPhoneAmount" >
          <el-input v-model.number="inputForm.demoPhoneAmount"></el-input>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.remarks')" prop="remarks" >
          <el-input v-model="inputForm.remarks" type="textarea"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('shopDepositForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import bankSelect from 'components/future/bank-select';
  import depotSelect from 'components/future/depot-select'
    export default{
        components:{
          bankSelect,
          depotSelect,
        },
      data(){
        return this.getData();
      },
      methods:{
        getData(){
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            inputForm:{
              extra:{}
            },

            rules: {
              shopId: [{ required: true, message: this.$t('shopDepositForm.prerequisiteMessage')}],
              departMent: [{ required: true, message: this.$t('shopDepositForm.prerequisiteMessage')}],
              bankId: [{ required: true, message: this.$t('shopDepositForm.prerequisiteMessage')}],
              outBillType: [{ required: true, message: this.$t('shopDepositForm.prerequisiteMessage')}],
              billDate: [{ required: true, message: this.$t('shopDepositForm.prerequisiteMessage')}],
              marketAmount: [{ required: true, type: 'number', message: this.$t('shopDepositForm.prerequisiteAndNumberMessage')}],
              imageAmount: [{ required: true, type: 'number', message: this.$t('shopDepositForm.prerequisiteAndNumberMessage')}],
              demoPhoneAmount: [{ required: true,  type: 'number', message: this.$t('shopDepositForm.prerequisiteAndNumberMessage')}]
            }
          }
        },
        formSubmit(){
          this.submitDisabled = true;
          let form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/ws/future/layout/shopDeposit/save', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
                this.$message(response.data.message);
                this.submitDisabled = false;
                if(response.data.success) {
                  Object.assign(this.$data, this.getData());
                  this.initPage();
                }
              }).catch( () => {
                this.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },shopChanged(){
            if(!this.inputForm.shopId || this.inputForm.shopId === ''){
                return ;
            }
            axios.get('/api/ws/future/basic/depot/getDefaultDepartMent',{params:{depotId:this.inputForm.shopId}}).then((response)=>{
              this.inputForm.departMent=response.data;
            });
        },initPage(){
          //押金列表只能增加，不能修改
          axios.get('/api/ws/future/layout/shopDeposit/getForm').then((response)=>{
            this.inputForm = response.data;
            if(!this.isCreate){
              axios.get('/api/ws/future/layout/shopDeposit/findDto', {params: {id:this.$route.query.id}}).then((response)=>{
                util.copyValue(response.data, this.inputForm);
              });
            }
          });
        }
      },created () {
        this.initPage();
      }

    }
</script>
