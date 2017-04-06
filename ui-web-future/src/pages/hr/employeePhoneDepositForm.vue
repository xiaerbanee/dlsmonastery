<template>
  <div>
    <head-tab :active="$t('employeePhoneDepositForm.employeePhoneDepositForm') "></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('employeePhoneDepositForm.employeeName')" prop="employeeId">
          <el-select v-model="inputForm.employeeId" filterable remote :placeholder="$t('employeePhoneDepositForm.inputWord')" :remote-method="remoteEmployee" :loading="remoteLoading" :clearable=true>
            <el-option v-for="employee in employees" :key="employee.id" :label="employee.name" :value="employee.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('employeePhoneDepositForm.depotName')" prop="depotId">
          <el-select v-model="inputForm.depotId" filterable remote :placeholder="$t('employeePhoneDepositForm.inputWord')" :remote-method="remoteDepot" :loading="remoteLoading" :clearable=true>
            <el-option v-for="depot in depots" :key="depot.id" :label="depot.name" :value="depot.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('employeePhoneDepositForm.amount')" prop="amount">
          <el-input v-model.number="inputForm.amount"></el-input>
        </el-form-item>
        <el-form-item :label="$t('employeePhoneDepositForm.department')" prop="department">
          <el-select v-model="inputForm.department">
            <el-option v-for="item in formProperty.departments"  :key="item.fnumber" :label="item.fname" :value="item.fnumber"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('employeePhoneDepositForm.productName')" prop="productId">
          <el-select v-model="inputForm.productId" filterable remote :placeholder="$t('employeePhoneDepositForm.inputWord')" :remote-method="remoteProduct" :loading="remoteLoading" :clearable=true>
            <el-option v-for="product in products" :key="product.id" :label="product.name" :value="product.id"></el-option>
          </el-select>
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
    export default{
      data(){
          return{
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
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.get('/api/hr/employeePhoneDeposit/save',{params:this.inputForm}).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'employeePhoneDepositList',query:util.getQuery("employeePhoneDepositList")})
                }
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },remoteProduct(query) {
          if (query !== '') {
            this.remoteLoading = true;
            axios.get('/api/crm/product/search',{params:{name:query}}).then((response)=>{
              this.products=response.data;
              this.remoteLoading = false;
            })
          } else {
            this.products = [];
          }
        },remoteDepot(query) {
          if (query !== '') {
            this.remoteLoading = true;
            axios.get('/api/crm/depot/search',{params:{name:query}}).then((response)=>{
              this.depots=response.data;
              this.remoteLoading = false;
            })
            if(this.inputForm.depotId!==''){
              axios.get('/api/crm/shopGoodsDeposit/searchDepartMent',{params:{shopId:this.inputForm.depotId}}).then((response)=>{
                this.inputForm.department=response.data.departMent;
              })
            }
          } else {
            this.depots = [];
          }
        },remoteEmployee(query) {
          if (query !== '') {
            this.remoteLoading = true;
            axios.get('/api/hr/employee/search',{params:{key:query}}).then((response)=>{
              this.employees=response.data;
              this.remoteLoading = false;
            })
          } else {
            this.depots = [];
          }
        }
      },created(){
        axios.get('/api/hr/employeePhoneDeposit/getFormProperty').then((response)=>{
          this.formProperty=response.data;
        });
        if(!this.isCreate){
          axios.get('/api/hr/employeePhoneDeposit/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
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
    }
</script>
