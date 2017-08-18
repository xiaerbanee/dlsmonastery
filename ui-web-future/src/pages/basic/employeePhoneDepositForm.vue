<template>
  <div>
    <head-tab active="employeePhoneDepositForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('employeePhoneDepositForm.employeeName')" prop="employeeId">
          <employee-select  v-model="inputForm.employeeId"></employee-select>
        </el-form-item>
        <el-form-item :label="$t('employeePhoneDepositForm.depotName')" prop="depotId">
          <depot-select v-model="inputForm.depotId" category="shop"></depot-select>
        </el-form-item>
        <el-form-item :label="$t('employeePhoneDepositForm.amount')" prop="amount">
          <el-input v-model.number="inputForm.amount"></el-input>
        </el-form-item>
        <el-form-item :label="$t('employeePhoneDepositForm.department')" prop="department">
          <el-select v-model="inputForm.department" clearable :placeholder="$t('employeePhoneDepositForm.inputKey')">
            <el-option v-for="item in inputForm.extra.departMentList" :key="item.fnumber" :label="item.ffullName" :value="item.fnumber"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('employeePhoneDepositForm.productName')" prop="productId">
          <product-select v-model="inputForm.productId" :hasIme=true></product-select>
        </el-form-item>
        <el-form-item :label="$t('employeePhoneDepositForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('employeePhoneDepositForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import employeeSelect from 'components/basic/employee-select'
  import productSelect from 'components/future/product-select'
  import depotSelect from 'components/future/depot-select'

    export default{
      components:{
        employeeSelect,
        productSelect,
        depotSelect
      },
      data(){
        return this.getData();
      },
      methods:{
        getData(){
          return{
            submitDisabled:false,
            formProperty:{},
            inputForm:{
              extra:{},
            },
            remoteLoading:false,
            formLabelWidth: '120px',
            rules: {
              depotId: [{ required: true, message: this.$t('employeePhoneDepositForm.prerequisiteMessage')}],
              depositAmount: [{ required: true, message: this.$t('employeePhoneDepositForm.prerequisiteMessage')},{ type: 'number', message: this.$t('employeePhoneDepositForm.inputLegalValue')}],
              productId: [{ required: true, message: this.$t('employeePhoneDepositForm.prerequisiteMessage')}],
              department: [{ required: true, message: this.$t('employeePhoneDepositForm.prerequisiteMessage')}],
              employeeId: [{ required: true, message: this.$t('employeePhoneDepositForm.prerequisiteMessage')}],
            }
          }
        },
        formSubmit(){
          this.submitDisabled = true;
          let form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/ws/future/basic/employeePhoneDeposit/save',qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
                this.$message(response.data.message);
                if(this.inputForm.create){
                  Object.assign(this.$data, this.getData());
                  this.initPage();
                }else{
                  util.closeAndBackToPage(this.$router,'employeePhoneDepositList')
                }
              }).catch(()=> {
                this.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },initPage(){
          axios.get('/api/ws/future/basic/employeePhoneDeposit/getForm').then((response)=>{
            this.inputForm = response.data;
          });
        }
      },created () {
        this.initPage();
      }
    }
</script>
