<template>
  <div>
    <head-tab active="adApplyBillForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('adApplyBillForm.billType')" prop="billType">
          <el-select v-model="inputForm.billType" :placeholder="$t('adApplyBillForm.selectInput')" :clearable=true @change="onchange">
            <el-option v-for="billType in inputForm.extra.billTypes" :key="billType" :label="billType" :value="billType"></el-option>
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
      <el-table :data="filterAdApplyList"  stripe border>
        <el-table-column prop="shopName" :label="$t('adApplyBillForm.shopName')" ></el-table-column>
        <el-table-column prop="productCode" :label="$t('adApplyBillForm.productCode')" ></el-table-column>
        <el-table-column prop="productName" sortable :label="$t('adApplyBillForm.productName')" ></el-table-column>
        <el-table-column prop="createdDate" sortable :label="$t('adApplyBillForm.createdDate')"></el-table-column>
        <el-table-column prop="applyQty" :label="$t('adApplyBillForm.applyQty')+'('+totalApplyQty+')'" ></el-table-column>
        <el-table-column prop="confirmQty" sortable :label="$t('adApplyBillForm.confirmQty')+'('+totalConfirmQty+')'" ></el-table-column>
        <el-table-column prop="leftQty" :label="$t('adApplyBillForm.leftQty')+'('+totalLeftQty+')'" ></el-table-column>
        <el-table-column prop="nowBilledQty" :label="$t('adApplyBillForm.billQty')" >
          <template scope="scope">
            <el-input v-model="scope.row.nowBilledQty"></el-input>
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
      return this.getData();
    },
    methods:{
      getData(){
        return {
          isInit: false,
          submitDisabled: false,
          productOrShopName: "",
          filterAdApplyList:[],
          inputForm: {
              extra:{}
          },
          adApplyList:{},
          rules: {
            billType: [{required: true, message: this.$t('adApplyBillForm.prerequisiteMessage')}],
            billDate: [{required: true, message: this.$t('adApplyBillForm.prerequisiteMessage')}],
            expressCompanyId: [{required: true, message: this.$t('adApplyBillForm.prerequisiteMessage')}],
          },
          remoteLoading: false,
          totalApplyQty:"0",
          totalConfirmQty:"0",
          totalLeftQty:"0"
        };
      },
      formSubmit(){
        let that = this;
        that.submitDisabled = true;
        let form = that.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            let submitData = util.deleteExtra(this.inputForm);
            submitData.adApplyDetailForms = this.getProductForSubmit();
            axios.post('/api/ws/future/layout/adApply/billSave',qs.stringify(submitData,{allowDots:true})).then((response)=> {
              that.$message(response.data.message);
              if(response.data.success){
                Object.assign(this.$data, this.getData());
                this.initPage();
              }
            }).catch(function () {
              that.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },setProductList(list){
        this.adApplyList = list;
        this.searchDetail();
      },getProductForSubmit(){
          let tempList=new Array();
          for(let index in this.adApplyList){
            let detail=this.adApplyList[index];
            if(util.isNotBlank(detail.nowBilledQty)){
              tempList.push(detail);
            }
          }
          return tempList;
      },
      searchDetail(){
        var val=this.productOrShopName;
        if(!val){
          this.filterAdApplyList = this.adApplyList;
          this.getTotalQty(this.filterAdApplyList);
          return;
        }
        var tempList=new Array();
        for(var index in this.adApplyList){
          var detail=this.adApplyList[index];
          if(util.isNotBlank(detail.nowBilledQty)){
            tempList.push(detail)
          }
        }
        for(var index in this.adApplyList){
          var detail=this.adApplyList[index];
          if((util.contains(detail.shopName,val)||util.contains(detail.productName,val))&&util.isBlank(detail.nowBilledQty)){
            tempList.push(detail)
          }
        }
        this.filterAdApplyList = tempList;
        this.getTotalQty(this.filterAdApplyList);
      },getTotalQty(content){
        if(content == null){
          return;
        }
        let tempApplyQty = 0;
        let tempConfirmQty = 0;
        let tempLeftQty = 0;
        for(let index in content){
          let detail=content[index];
          if(util.isNotBlank(detail.applyQty)){
            tempApplyQty += detail.applyQty;
          }
          if(util.isNotBlank(detail.confirmQty)){
            tempConfirmQty += detail.confirmQty;
          }
          if(util.isNotBlank(detail.leftQty)){
            tempLeftQty += detail.leftQty;
          }
        }
        this.totalApplyQty = tempApplyQty;
        this.totalConfirmQty = tempConfirmQty;
        this.totalLeftQty = tempLeftQty;
      },onchange(){
          if(this.inputForm.billType == null){
              return;
          }
          axios.get('api/ws/future/layout/adApply/findAdApplyList',{params:{billType:this.inputForm.billType}}).then((response) =>{
              this.inputForm.storeId = response.data.storeId;
            this.setProductList(response.data.adApplyDtoList);
          });
      },
      initPage(){
        this.pageHeight = window.outerHeight -320;
        axios.get('api/ws/future/layout/adApply/getBillForm').then((response) =>{
          this.inputForm = response.data;
        });
      }
    },created () {
      this.initPage();
    }
  }
</script>
