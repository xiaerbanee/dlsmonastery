<template>
  <div>
    <head-tab active="shopDepositForm"></head-tab>
    <div>
      <el-form :model="shopDeposit" ref="inputForm" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('shopDepositForm.shopName')" prop="shopId">
          <depot-select v-model ="shopDeposit.shopId"  category="directShop" @input="shopChanged"></depot-select>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.outBillType')" prop="outBillType" >
          <el-select v-model="outBillType" clearable :placeholder="$t('shopDepositForm.inputKey')">
            <el-option v-for="item in inputProperty.outBillTypeList" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.department')" prop="departMent" >
          <office-select v-model = "departMent" ></office-select>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.bank')" prop="bankId" v-if="outBillType==='手工日记账'" >
          <bank-select v-model = "shopDeposit.bankId"></bank-select>
        </el-form-item>
        <el-form-item   :label="$t('shopDepositForm.billDate')" prop="billDate" >
          <date-picker  v-model="shopDeposit.billDate" ></date-picker>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.marketAmount')" prop="marketAmount" >
          <el-input v-model.number="marketAmount"></el-input>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.imageAmount')" prop="imageAmount" >
          <el-input v-model.number="imageAmount"></el-input>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.demoPhoneAmount')" prop="demoPhoneAmount" >
          <el-input v-model.number="demoPhoneAmount"></el-input>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.remarks')" prop="remarks" >
          <el-input v-model="shopDeposit.remarks" type="textarea"></el-input>
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
  import officeSelect from 'components/basic/office-select'
  import depotSelect from 'components/future/depot-select'
    export default{
        components:{
          bankSelect,
          officeSelect,
          depotSelect,
        },
      data(){
        return this.getData();
      },
      methods:{
        getData(){
          return{
            isInit:false,
            submitDisabled:false,
            inputProperty:{},
            shopDeposit:{},
            marketAmount:0,
            imageAmount:0,
            demoPhoneAmount:0,
            outBillType:'',
            departMent:'',
            submitData:{
              shopId:'',
              departMent:'',
              billDate:'',
              outBillType:'',
              bankId:'',
              marketAmount:'',
              imageAmount:'',
              demoPhoneAmount:'',
              remarks:'',
            },
            rules: {
              shopId: [{ required: true, message: this.$t('shopDepositForm.prerequisiteMessage')}],
              departMent: [{ required: true, message: this.$t('shopDepositForm.prerequisiteMessage')}],
              outBillType: [{ required: true, message: this.$t('shopDepositForm.prerequisiteMessage')}],
              marketAmount: [{ type: 'number', message: this.$t('shopDepositForm.inputLegalValue')}],
              imageAmount: [{ type: 'number', message: this.$t('shopDepositForm.inputLegalValue')}],
              demoPhoneAmount: [{ type: 'number', message: this.$t('shopDepositForm.inputLegalValue')}]
            }
          }
        },
        formSubmit(){
          var that = this;
          let form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              this.submitDisabled = true;
              util.copyValue(this.shopDeposit,this.submitData);
              this.submitData.marketAmount = this.marketAmount;
              this.submitData.imageAmount = this.imageAmount;
              this.submitData.demoPhoneAmount = this.demoPhoneAmount;
              this.submitData.outBillType = this.outBillType;
              this.submitData.departMent = this.departMent;

              axios.post('/api/ws/future/crm/shopDeposit/save', qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                this.submitDisabled = false;
                if(response.data.success) {
                  Object.assign(this.$data, this.getData());
                }
              }).catch( () => {
                that.submitDisabled = false;
              });
            }
          })
        },shopChanged(){
            if(!this.shopDeposit.shopId || this.shopDeposit.shopId === ''){
                return ;
            }
            axios.get('/api/ws/future/crm/shopDeposit/getDefaultDepartMent',{params:{shopId:this.shopDeposit.shopId}}).then((response)=>{
              this.shopDeposit.departMent=response.data;
            });
        }
      },activated () {
        if(!this.$route.query.headClick || ! this.isInit) {
          axios.get('/api/ws/future/crm/shopDeposit/getForm').then((response)=>{
            this.inputProperty = response.data;
          });

          //押金列表只能增加，不能修改
          axios.get('/api/ws/future/crm/shopDeposit/findDto').then((response)=>{
            this.shopDeposit = response.data;
          });
        }
        this.isInit = true;
      }
    }
</script>
