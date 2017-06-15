<template>
  <div>
    <head-tab active="employeePhoneDepositForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('employeePhoneDepositForm.employeeName')" prop="employeeId">
          <employee-select  v-model="inputForm.employeeId"></employee-select>
        </el-form-item>
        <el-form-item :label="$t('employeePhoneDepositForm.depotName')" prop="depotId">
          <depot-select v-model="inputForm.depotId"></depot-select>
        </el-form-item>
        <el-form-item :label="$t('employeePhoneDepositForm.amount')" prop="amount">
          <el-input v-model.number="inputForm.amount"></el-input>
        </el-form-item>
        <el-form-item :label="$t('employeePhoneDepositForm.department')" prop="department">
          <office-select v-model="inputForm.department"></office-select>
        </el-form-item>
        <el-form-item :label="$t('employeePhoneDepositForm.productName')" prop="productId">
          <product-select v-model="inputForm.productId"></product-select>
        </el-form-item>
        <el-form-item :label="$t('employeePhoneDepositForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('employeePhoneDepositForm.save')}}</el-button>
        </el-form-item>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import employeeSelect from 'components/basic/employee-select'
  import productSelect from 'components/future/product-select'
  import officeSelect from 'components/basic/office-select'
  import depotSelect from 'components/future/depot-select'

    export default{
      components:{
        employeeSelect,
        productSelect,
        officeSelect,
        depotSelect
      },
      data(){
        return this.getData();
      },
      methods:{
        getData(){
          return{
            isInit:false,
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            formProperty:{},
            inputForm:{
              id:this.$route.query.id,
              employeeId:'',
              depotId:'',
              amount:2000,
              productId:'',
              department:'',
              status:''
            },
            remoteLoading:false,
            depots:[],
            products:[],
            employees:[],
            formLabelWidth: '120px',
            rules: {
              depotId: [{ required: true, message: this.$t('expressOrderList.prerequisiteMessage')}],
              depositAmount: [{ required: true, message: this.$t('expressOrderList.prerequisiteMessage')},{ type: 'number', message: this.$t('expressOrderList.inputLegalValue')}],
              productId: [{ required: true, message: this.$t('expressOrderList.prerequisiteMessage')}],
              department: [{ required: true, message: this.$t('expressOrderList.prerequisiteMessage')}],
              employeeId: [{ required: true, message: this.$t('expressOrderList.prerequisiteMessage')}],
            }
          }
        },
        formSubmit(){
          var that = this;
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.get('/api/basic/hr/employeePhoneDeposit/save',{params:this.inputForm}).then((response)=> {
                this.$message(response.data.message);
                Object.assign(this.$data, this.getData());
                if(!this.isCreate){
                  this.$router.push({name:'employeePhoneDepositList',query:util.getQuery("employeePhoneDepositList")})
                }
              }).catch(function () {
                that.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        }
      },activated () {
        if(!this.$route.query.headClick || !this.isInit) {
          axios.get('/api/basic/hr/employeePhoneDeposit/getForm').then((response)=>{
            this.formProperty=response.data;
          });
          if(!this.isCreate){
            axios.get('/api/basic/hr/employeePhoneDeposit/getForm',{params: {id:this.$route.query.id}}).then((response)=>{
              util.copyValue(response.data,this.inputForm);
              if(response.data.depot!=null){
                this.depots=new Array(response.data.depot)
              }
              if(response.data.product!=null){
                this.products=new Array(response.data.product)
              }
            })
          }
        }
        this.isInit = true;
      }
    }
</script>
