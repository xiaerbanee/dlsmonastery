<template>
  <div>
    <head-tab active="demoPhoneForm"></head-tab>
    <div >
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('demoPhoneForm.productType')" prop="productType">
          {{inputForm.productTypeNames}}
        </el-form-item>
          <el-form-item :label="$t('demoPhoneForm.shopName')" prop="shop">
            <el-select v-model="inputForm.shop" clearable filterable remote :placeholder="$t('demoPhoneForm.selectKeyShow20time')" :remote-method="remoteDepot" :loading="remoteLoading">
              <el-option v-for="shop in shops" :key="shop.id" :label="shop.name" :value="shop.id"></el-option>
            </el-select>
          </el-form-item>
        <el-form-item :label="$t('demoPhoneForm.employeeName')" prop="employeeId">
          <el-select v-model="inputForm.employeeId" clearable filterable remote :placeholder="$t('demoPhoneForm.inputWord')" :remote-method="remoteEmployee" :loading="remoteLoading">
            <el-option v-for="employee in employees" :key="employee.id" :label="employee.name" :value="employee.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('demoPhoneForm.productIme')" prop="productIme">
          <el-select v-model="inputForm.productIme" clearable filterable remote :placeholder="$t('demoPhoneForm.inputWord')" :remote-method="remoteProductIme" :loading="remoteLoading">
            <el-option v-for="productIme in productImes" :key="productIme.id" :label="productIme.ime" :value="productIme.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('demoPhoneForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled"  @click="formSubmit()">{{$t('demoPhoneForm.save')}}</el-button>
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
          id:'',
          type:'',
          shopName:'',
          employeeId:'',
          productIme:'',
          remarks:'',
        },
        rules: {
          employeeId: [{ required: true, message: this.$t('demoPhoneForm.prerequisiteMessage')}],
          mobilePhone: [{ required: true, message: this.$t('demoPhoneForm.prerequisiteMessage')}]
        },
        remoteLoading:false,
        employees:[],
        shops:[],
        productImes:[],
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/crm/demoPhone/save', qs.stringify(this.inputForm)).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'demoPhoneList',query:util.getQuery("demoPhoneList")})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },remoteDepot(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/crm/depot/search', {params: {name: query, type: "SHOP"}}).then((response)=> {
            this.shops = response.data;
            this.remoteLoading = false;
          })
        } else {
          this.shops = [];
        }
      },remoteEmployee(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/hr/employee/search',{params:{key:query}}).then((response)=>{
            this.employees=response.data;
            console.log(response.data)
            this.remoteLoading = false;
          })
        } else {
          this.employees = [];
        }
      },remoteProductIme(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/crm/productIme/search',{params:{imeStr:query}}).then((response)=>{
            this.productImes=response.data;
            this.remoteLoading = false;
          })
        } else {
          this.productImes = [];
        }
      },
    },created(){
      axios.get('/api/crm/demoPhone/getForm').then((response)=>{
        this.formProperty=response.data;
      });
      if(!this.isCreate){
        axios.get('/api/crm/demoPhone/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          util.copyValue(response.data,this.inputForm);
          console.log(response.data);
        this.employees=new Array(response.data.employee);
        this.inputForm.employeeId=response.data.employeeId;
        })
      }
    }
  }
</script>

