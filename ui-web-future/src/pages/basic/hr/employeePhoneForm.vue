<template>
  <div>
    <head-tab active="accountList"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('accountList.employeeName')" prop="employeeName">
          <el-input v-model.number="inputForm.employeeName" readonly></el-input>
        </el-form-item>
        <el-form-item :label="$t('accountList.depotName')" prop="depotId">
          <depot-select v-model="inputForm.depotId"></depot-select>
        </el-form-item>
        <el-form-item :label="$t('accountList.depositAmount')" prop="depositAmount">
          <el-input v-model.number="inputForm.depositAmount"></el-input>
        </el-form-item>
        <el-form-item :label="$t('accountList.uploadTime')" :label-width="formLabelWidth">
          <el-date-picker  v-model="inputForm.uploadTime" type="datetime" align="left" :placeholder="$t('employeePhoneForm.selectDate')" format="yyyy-MM-dd hh:mm:ss" ></el-date-picker>
        </el-form-item>
        <el-form-item :label="$t('accountList.productName')" prop="productId">
          <product-select v-model="inputForm.productId"></product-select>
        </el-form-item>
        <el-form-item :label="$t('accountList.jobPrice')" prop="jobPrice">
          <el-input v-model.number="inputForm.jobPrice"></el-input>
        </el-form-item>
        <el-form-item :label="$t('accountList.retailPrice')" prop="retailPrice">
          <el-input v-model.number="inputForm.retailPrice"></el-input>
        </el-form-item>
        <el-form-item :label="$t('accountList.imeStr')" prop="imeStr">
          <el-input v-model.number="inputForm.imeStr"></el-input>
        </el-form-item>
        <el-form-item :label="$t('accountList.status')" prop="status">
          <el-select v-model="inputForm.status" filterable remote :placeholder="$t('employeePhoneForm.inputWord')" :clearable=true>
            <el-option v-for="item in formProperty.status" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"  :disabled="submitDisabled" @click="formSubmit()">{{$t('employeePhoneForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import productSelect from 'components/future/product-select'
  import depotSelect from 'components/future/depot-select'

  export default{
      components:{productSelect,depotSelect},
      data(){
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            formProperty:{},
            inputForm:{
              id:this.$route.query.id,
              employeeName:'',
              depotId:'',
              depositAmount:'',
              uploadTime:'',
              productId:'',
              jobPrice:'',
              retailPrice:'',
              imeStr:'',
              status:''
            },
            remoteLoading:false,
            depots:[],
            products:[],
            formLabelWidth: '120px',
            rules: {
              depot: [{ required: true, message: this.$t('employeePhoneForm.prerequisiteMessage')}],
              depositAmount: [{ required: true, message: this.$t('employeePhoneForm.prerequisiteMessage')},{ type: 'number', message: this.$t('employeePhoneForm.inputLegalValue')}],
              uploadTime: [{ required: true, message: this.$t('employeePhoneForm.prerequisiteMessage')}],
              product: [{ required: true, message: this.$t('employeePhoneForm.prerequisiteMessage')}],
              jobPrice: [{ required: true, message: this.$t('employeePhoneForm.prerequisiteMessage')},{ type: 'number', message: this.$t('employeePhoneForm.inputLegalValue')}],
              retailPrice: [{ required: true, message: this.$t('employeePhoneForm.prerequisiteMessage')},{ type: 'number', message: this.$t('employeePhoneForm.inputLegalValue')}],
            }
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
            this.inputForm.uploadTime=util.formatLocalDateTime(this.inputForm.uploadTime)
              axios.get('/api/basic/hr/employeePhone/save',{params:this.inputForm}).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'employeePhoneList',query:util.getQuery("employeePhoneList")})
                }
              }).catch(function () {
                this.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        }
      },created(){
        axios.get('/api/basic/hr/employeePhone/getForm').then((response)=>{
          this.formProperty=response.data;
        });
        if(!this.isCreate){
          axios.get('/api/basic/hr/employeePhone/getForm',{params: {id:this.$route.query.id}}).then((response)=>{
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
