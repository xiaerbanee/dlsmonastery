<template>
  <div>
    <head-tab active="demoPhoneTypeForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('demoPhoneTypeForm.name')" prop="name">
          <el-input  v-model.number="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('demoPhoneTypeForm.productType')" prop="productTypeIdList">
          <el-select v-model="inputForm.productTypeIdList" multiple filterable remote :placeholder="$t('demoPhoneTypeForm.inputWord')" :remote-method="remoteProduct" :loading="remoteLoading" :clearable=true>
            <el-option v-for="productType in productTypes" :key="productType.id" :label="productType.name" :value="productType.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('demoPhoneTypeForm.limitQty')" prop="limitQty">
          <el-input  v-model="inputForm.limitQty"></el-input>
        </el-form-item>
        <el-form-item :label="$t('demoPhoneTypeForm.applyEndDate')" prop="applyEndDate">
          <el-date-picker v-model="inputForm.applyEndDate" :placeholder="$t('demoPhoneTypeForm.selectDate')"></el-date-picker>
        </el-form-item>
        <el-form-item :label="$t('demoPhoneTypeForm.renewEndDate')" prop="renewEndDate">
          <el-date-picker v-model="inputForm.renewEndDate" :placeholder="$t('demoPhoneTypeForm.selectDate')"></el-date-picker>
        </el-form-item>
        <el-form-item :label="$t('demoPhoneTypeForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('demoPhoneTypeForm.save')}}</el-button>
        </el-form-item>
        <el-table :data="inputForm.demoPhoneTypeOfficeList"  style="margin-top:5px;"   stripe border>
          <el-table-column prop="officeName" :label="$t('demoPhoneTypeForm.officeName')"></el-table-column>
          <el-table-column prop="officeTaskPoint" :label="$t('demoPhoneTypeForm.taskPoint')"></el-table-column>
          <el-table-column prop="qty" :label="$t('demoPhoneTypeForm.qty')">
            <template scope="scope">
              <el-input  v-model="scope.row.qty"></el-input>
            </template>
          </el-table-column>
        </el-table>
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
          name:'',
          productTypeIdList:[],
          limitQty:'',
          applyEndDate:'',
          renewEndDate:'',
          remarks:'',
          demoPhoneTypeOfficeList:[]
        },
        remoteLoading:false,
        productTypes:[],
        rules: {
          name: [{ required: true, message: this.$t('demoPhoneTypeForm.prerequisiteMessage')}],
          productType: [{ required: true, message: this.$t('demoPhoneTypeForm.prerequisiteMessage')}],
          applyEndDate: [{ required: true, message: this.$t('demoPhoneTypeForm.prerequisiteMessage')}]
        },
        pickerDateOption:util.pickerDateOption
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        this.inputForm.applyEndDate=util.formatLocalDate( this.inputForm.applyEndDate)
        this.inputForm.renewEndDate=util.formatLocalDate( this.inputForm.renewEndDate)
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/crm/demoPhoneType/save', qs.stringify(this.inputForm, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
            this.submitDisabled = false;
              if(this.isCreate){
                form.resetFields();
              } else {
                this.$router.push({name:'demoPhoneTypeList',query:util.getQuery("demoPhoneTypeList")})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },remoteProduct(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/ws/future/crm/demoPhoneType/search',{params:{name:query}}).then((response)=>{
            this.productTypes = response.data;
            this.remoteLoading = false;
          });
        }
      },initPage(){
        axios.get('/api/ws/future/crm/demoPhoneType/getForm',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm = response.data;
          this.inputForm.demoPhoneTypeOfficeList=response.data.demoPhoneTypeOfficeList;
          this.inputForm.productTypeIdList=util.getIdList(response.data.productTypeList)
          if(response.data.productTypeList!=null && response.data.productTypeList.length >0){
            this.productTypes = response.data.productTypeList;
            this.inputForm.productIdList = util.getIdList(this.products);
          }
        });
      }
    },created(){
      this.initPage();
    },activated () {
      if(!this.$route.query.headClick) {
        this.initPage();
      }
    }
  }
</script>

