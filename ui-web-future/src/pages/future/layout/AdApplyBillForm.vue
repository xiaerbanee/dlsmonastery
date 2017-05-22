<template>
  <div>
    <head-tab active="adApplyBillForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('adApplyBillForm.billType')" prop="billType">
          <el-select v-model="inputForm.billType" :placeholder="$t('adApplyBillForm.selectInput')" :clearable=true @change="onchange">
            <el-option v-for="billType in inputForm.billTypes" :key="billType" :label="billType" :value="billType"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('adApplyBillForm.billDate')" prop="billDate">
          <date-picker  v-model="inputForm.billDate"></date-picker>
        </el-form-item>
        <el-form-item :label="$t('adApplyBillForm.expressCompany')"  prop="expressCompanyId">
          <express-company-select v-model="inputForm.expressCompanyId"></express-company-select>
        </el-form-item>
        <el-form-item :label="$t('adApplyBillForm.billRemarks')"  prop="billRemarks">
          <el-input v-model="inputForm.remarks" type="textarea"></el-input>
        </el-form-item>
        <el-form-item :label="$t('adApplyBillForm.financeQty')" >
          <depot-select v-model="inputForm.storeId" category="store" :disabled="true"></depot-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('adApplyBillForm.save')}}</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="20" style="margin-bottom:20px;float:right">
        <span>{{$t('adApplyBillForm.search')}}</span>
       <el-input v-model="productOrShopName" @change="searchDetail" :placeholder="$t('adApplyBillForm.inputTowKey')" style="width:200px;margin-right:10px"></el-input>
     </el-row>
      <el-table :data="inputForm.adApplyDtos"  stripe border>
        <el-table-column prop="shopName" :label="$t('adApplyBillForm.shopName')" ></el-table-column>
        <el-table-column prop="productCode" :label="$t('adApplyBillForm.productCode')" ></el-table-column>
        <el-table-column prop="productName" sortable :label="$t('adApplyBillForm.productName')" ></el-table-column>
        <el-table-column prop="createdDate" sortable :label="$t('adApplyBillForm.createdDate')"></el-table-column>
        <el-table-column prop="applyQty" :label="$t('adApplyBillForm.applyQty')" ></el-table-column>
        <el-table-column prop="confirmQty" sortable :label="$t('adApplyBillForm.confirmQty')" ></el-table-column>
        <el-table-column prop="leftQty" :label="$t('adApplyBillForm.leftQty')" ></el-table-column>
        <el-table-column prop="billQty" :label="$t('adApplyBillForm.billQty')" >
          <template scope="scope">
            <el-input v-model="scope.row.billQty"></el-input>
          </template>
        </el-table-column>
        <el-table-column prop="storeQty" sortable :label="$t('adApplyBillForm.financeQty')" ></el-table-column>
        <el-table-column prop="price2" :label="$t('adApplyBillForm.price')" ></el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  import expressCompanySelect from 'components/future/express-company-select';
  import depotSelect from 'components/future/depot-select';
  export default{
    components:{expressCompanySelect,depotSelect},
    data(){
      return{
        submitDisabled:false,
        productOrShopName:"",
        filterAdApplyList:[],
        formProperty:{},
        applyQtys:"",
        leftQtys:"",
        confirmQtys:"",
        inputForm:{},
        submitData:{
          billType:'',
          billDate:'',
          expressCompanyId:'',
          billRemarks:'',
          billQtys:[],
          adApplyList:[],
        },
        rules: {
          billType: [{ required: true, message: this.$t('adApplyBillForm.prerequisiteMessage')}],
          billDate: [{ required: true, message: this.$t('adApplyBillForm.prerequisiteMessage')}],
          expressCompanyId: [{ required: true, message: this.$t('adApplyBillForm.prerequisiteMessage')}],
        },
        remoteLoading:false
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/crm/adApply/billSave',qs.stringify(this.inputForm,{allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'adApplyList',query:util.getQuery("adApplyList")})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
      searchDetail(){
        var val=this.productOrShopName;
        var tempList=new Array();
        for(var index in this.inputForm.adApplyDtos){
          var detail=this.inputForm.adApplyDtos[index];
          if(util.contains(detail.shopName,val)||util.contains(detail.productName,val)||util.isBlank(val)){
            tempList.push(detail)
          }
        }
        console.log(tempList);
        this.inputForm.adApplyDtos = tempList;
      },onchange(){
          axios.get('api/ws/future/layout/adApply/getBillForm',{params:{billType:this.inputForm.billType}}).then((response) =>{
            this.inputForm = response.data;
          });
      },getShopTypeLabel(id) {
        var arr = this.shops;
        if(id){
          for(var i in arr){
            if(arr[i].id === id){
              this.shopTypeLabel =  arr[i].typeLabel;
              return;
            }
          }
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      axios.get('api/ws/future/layout/adApply/getBillForm').then((response) =>{
        this.inputForm = response.data;
      });
    }
  }
</script>
