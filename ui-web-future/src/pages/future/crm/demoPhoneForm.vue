<template>
  <div>
    <head-tab active="demoPhoneForm"></head-tab>
    <div >
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('demoPhoneForm.productType')" prop="productType">
          <demo-phone-type v-model = "inputForm.demoPhoneTypeId"></demo-phone-type>
        </el-form-item>
        <el-form-item :label="$t('demoPhoneForm.shopName')" prop="shop">
          <depot-select v-model="inputForm.shopId" category="shop"></depot-select>
        </el-form-item>
        <el-form-item :label="$t('demoPhoneForm.employeeName')" prop="employeeId">
          <employee-select v-model="inputForm.employeeId"></employee-select>
        </el-form-item>
        <el-form-item :label="$t('demoPhoneForm.productIme')" prop="productIme">
          <el-select v-model="inputForm.productImeId" clearable filterable remote :placeholder="$t('demoPhoneForm.inputWord')" :remote-method="remoteProductIme" :loading="remoteLoading">
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
  import depotSelect from 'components/future/depot-select'
  import employeeSelect from 'components/basic/employee-select'
  import demoPhoneType from 'components/future/demo-phone-type-select'
  export default{
    components:{
        depotSelect,
        employeeSelect,
        demoPhoneType
    },
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        inputForm:{},
        submitData:{
          id:'',
          type:'',
          demoPhoneTypeId:'',
          shopId:'',
          employeeId:'',
          productImeId:'',
          remarks:'',
        },
        rules: {
          employeeId: [{ required: true, message: this.$t('demoPhoneForm.prerequisiteMessage')}],
          productIme: [{ required: true, message: this.$t('demoPhoneForm.prerequisiteMessage')}]
        },
        remoteLoading:false,
        productImes:[],
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            util.copyValue(this.inputForm,this.submitData);
            axios.post('/api/ws/future/crm/demoPhone/save', qs.stringify(this.submitData)).then((response)=> {
              this.$message(response.data.message);
            this.submitDisabled = false;
              if(this.isCreate){
                form.resetFields();
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
      },remoteProductIme(query) {
        if (query !== '' && query.length>=6 && this.inputForm.shopId) {
          this.remoteLoading = true;
          axios.get('/api/ws/future/crm/productIme/search',{params: {productIme:query,shopId:this.inputForm.shopId}}).then((response)  =>{
            this.productImes=response.data;
            this.remoteLoading = false;
          })
        } else {
          this.productImes = [];
        }
      },initPage(){
        axios.get('/api/ws/future/crm/demoPhone/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm = response.data;
        })
      }
    },activated () {
      if(!this.$route.query.headClick) {
        this.initPage();
      }
    }
  }
</script>

