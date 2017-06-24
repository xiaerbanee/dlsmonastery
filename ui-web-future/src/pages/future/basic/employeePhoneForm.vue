<template>
  <div>
    <head-tab active="employeePhoneForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('employeePhoneForm.employeeName')" prop="employeeName">
          <el-input v-model.number="inputForm.employeeName" :readonly="true"></el-input>
        </el-form-item>
        <el-form-item :label="$t('employeePhoneForm.depotName')" prop="depotId">
          <depot-select v-model="inputForm.depotId" category="shop"></depot-select>
        </el-form-item>
        <el-form-item :label="$t('employeePhoneForm.depositAmount')" prop="depositAmount">
          <el-input v-model.number="inputForm.depositAmount"></el-input>
        </el-form-item>
        <el-form-item :label="$t('employeePhoneForm.uploadTime')" :label-width="formLabelWidth">
          <date-picker  v-model="inputForm.uploadTime" ></date-picker>
        </el-form-item>
        <el-form-item :label="$t('employeePhoneForm.productName')" prop="productId">
          <product-select v-model="inputForm.productId"></product-select>
        </el-form-item>
        <el-form-item :label="$t('employeePhoneForm.jobPrice')" prop="jobPrice">
          <el-input v-model.number="inputForm.jobPrice"></el-input>
        </el-form-item>
        <el-form-item :label="$t('employeePhoneForm.retailPrice')" prop="retailPrice">
          <el-input v-model.number="inputForm.retailPrice"></el-input>
        </el-form-item>
        <el-form-item :label="$t('employeePhoneForm.imeStr')" prop="imeStr">
          <el-input v-model="inputForm.imeStr"></el-input>
        </el-form-item>
        <el-form-item :label="$t('employeePhoneForm.status')" prop="status">
          <el-select v-model="inputForm.status"  :placeholder="$t('employeePhoneForm.inputWord')" :clearable=true>
            <el-option v-for="item in inputForm.extra.statusList" :key="item" :label="item" :value="item"></el-option>
          </el-select>
          <el-form-item v-permit="'hr:employeePhone:remarks'" label="备注" prop="remarks">
            <el-input v-model="inputForm.remarks"></el-input>
          </el-form-item>
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
      return this.getData()
    },
    methods:{
      getData() {
        return{
          submitDisabled:false,
          inputForm:{
            extra:{}
          },
          remoteLoading:false,
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
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/basic/employeePhone/save',qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
              this.$message(response.data.message);
              if(this.inputForm.create){
                  Object.assign(this.$data, this.getData());
                  this.initPage();
              }else{
                this.$router.push({name:'employeePhoneList',query:util.getQuery("employeePhoneList"), params:{_closeFrom:true}})
              }
              }).catch(function () {
              that.submitDisabled = false;
              });
          }else{
            this.submitDisabled = false;
       }
      })
      },initPage(){
        axios.get('/api/ws/future/basic/employeePhone/getForm').then((response)=>{
          this.inputForm = response.data;
          console.log(this.inputForm)
          axios.get('/api/ws/future/basic/employeePhone/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
          });
        });
      }
    },created () {
      this.initPage();
    }
  }
</script>
