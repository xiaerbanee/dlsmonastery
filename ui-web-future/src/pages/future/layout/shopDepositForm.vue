<template>
  <div>
    <head-tab active="shopDepositForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('shopDepositForm.shopName')" prop="shopId">
          <su-depot v-model ="inputForm.shopId"  type="shop" @input="changeDepartment">         </su-depot>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.outBillType')" prop="outBillType" >
          <el-select v-model="inputForm.outBillType" filterable :placeholder="$t('shopDepositForm.inputKey')">
            <el-option v-for="item in inputForm.outBillTypeList" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.department')" prop="department" >
          <office-select v-model = "inputForm.department" ></office-select>
        </el-form-item>
        <el-form-item :label="$t('shopDepositForm.bank')" prop="bankId" v-if="inputForm.outBillType==='手工日记账'" >
          <bank-select v-model = "inputForm.bankId"></bank-select>
        </el-form-item>
        <el-form-item   :label="$t('shopDepositForm.billDate')" prop="billDate" >
          <el-date-picker  v-model="inputForm.billDate" type="date" align="left" :placeholder="$t('bankInForm.selectDate')" format="yyyy-MM-dd" ></el-date-picker>
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
  import officeSelect from 'components/basic/office-select'
    export default{
        components:{
          bankSelect,
          officeSelect
        },
      data(){
          return{
            submitDisabled:false,
            inputForm:{},
            submitData:{
              id:'',
              shopId:'',
              departMent:'',
              amount:'',
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
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              util.copyValue(this.inputForm,this.submitData);
              axios.post('/api/ws/future/crm/shopDeposit/save', qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                form.resetFields();
                this.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })

        },changeDepartment(){
           var shopId=this.inputForm.shopId;
            axios.get('/api/ws/future/crm/shopDeposit/getDepartmentByShopId',{params:{shopId:shopId}}).then((response)=>{
              this.inputForm.departMent=response.data.fname;
            })
        }
      }, created(){
        axios.get('/api/ws/future/crm/shopDeposit/findForm',{params: {id:this.$route.query.id}}).then((response)=>{
              this.inputForm = response.data;
              console.log(this.inputForm)
          })
      }
    }
</script>
