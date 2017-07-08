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
        <span>{{$t('adApplyBillForm.billQty')}}</span>
        <el-input v-model.number="billGoodsSortQty" @change="billGoodsSort" style="width:200px;margin-right:10px"></el-input>
        <span>{{$t('adApplyBillForm.search')}}</span>
        <el-input v-model="productOrShopName" @change="searchDetail" :placeholder="$t('adApplyBillForm.inputKey')" style="width:200px;margin-right:10px"></el-input>
     </el-row>
      <el-table :data="filterAdApplyList"  stripe border>
        <el-table-column prop="shopName" :label="$t('adApplyBillForm.shopName')" ></el-table-column>
        <el-table-column prop="productCode" :label="$t('adApplyBillForm.productCode')" ></el-table-column>
        <el-table-column prop="productName" sortable :label="$t('adApplyBillForm.productName')" ></el-table-column>
        <el-table-column prop="createdDate" sortable :label="$t('adApplyBillForm.createdDate')"></el-table-column>
        <el-table-column prop="applyQty" :label="$t('adApplyBillForm.applyQty')+'('+totalApplyQty+')'" ></el-table-column>
        <el-table-column prop="confirmQty" sortable :label="$t('adApplyBillForm.confirmQty')+'('+totalConfirmQty+')'" ></el-table-column>
        <el-table-column prop="leftQty" :label="$t('adApplyBillForm.leftQty')+'('+totalLeftQty+')'" ></el-table-column>
        <el-table-column prop="nowBilledQty" :label="$t('adApplyBillForm.billQty')+'('+totalNowBilledQty+')'" >
          <template scope="scope">
            <el-input v-model.number="scope.row.nowBilledQty" @input="getTotalNowBilledQty()"></el-input>
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
          totalApplyQty:0,
          totalConfirmQty:0,
          totalLeftQty:0,
          totalNowBilledQty:0,
          billGoodsSortQty:"",
        };
      },
      formSubmit(){
        let validateMsg = this.customValidate();
        if(util.isNotBlank(validateMsg)){
          this.$alert(validateMsg);
          return;
        }

        this.submitDisabled = true;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            let submitData = util.deleteExtra(this.inputForm);
            submitData.adApplyDetailForms = this.getProductForSubmit();
            axios.post('/api/ws/future/layout/adApply/billSave',qs.stringify(submitData,{allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(response.data.success){
                Object.assign(this.$data, this.getData());
                this.initPage();
              }
            }).catch(()=> {
              this.submitDisabled = false;
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
          for(let index of this.adApplyList){
            if(util.isNotBlank(index.nowBilledQty)){
              tempList.push(index);
            }
          }
          return tempList;
      },customValidate(){
        let totalQty = 0;
        for(let index of this.filterAdApplyList){
          if(util.isBlank(index.nowBilledQty)){
            continue;
          }

          if(!Number.isInteger(index.nowBilledQty) || index.nowBilledQty < 0){
            return '门店：'+index.shopName+'的订货数不是一个大于等于0的整数';
          }

          totalQty += index.nowBilledQty;
        }
        if(totalQty<=0){
          return "总订货数要大于0";
        }
        return null;
      },getTotalNowBilledQty(){
        let tempTotalNowBilledQty = 0;
        for(let index of this.filterAdApplyList){
          if(util.isNotBlank(index.nowBilledQty)&&Number.isInteger(index.nowBilledQty)){
            tempTotalNowBilledQty += index.nowBilledQty;
          }
        }
        this.totalNowBilledQty = tempTotalNowBilledQty;
        //this.billGoodsSortQty = tempTotalNowBilledQty;
      },billGoodsSort(){
          let val = this.billGoodsSortQty;
          if(!val){
              for(let index of this.filterAdApplyList){
                  index.nowBilledQty = "";
              }
          }
          let tempVal = val;
          for(let index of this.filterAdApplyList){
              if(tempVal >0){
                if(index.leftQty<tempVal){
                  index.nowBilledQty = index.leftQty;
                  tempVal = tempVal - index.leftQty;
                }else{
                  index.nowBilledQty = tempVal;
                  tempVal = 0;
                }
              }else{
                  index.nowBilledQty = "";
              }

          }
          this.getTotalNowBilledQty();
      },
      searchDetail(){
        let val=this.productOrShopName;
        if(!val){
          this.filterAdApplyList = this.adApplyList;
          this.getTotalQty(this.filterAdApplyList);
          return;
        }
        let tempList=new Array();
        for(let index of this.adApplyList){
          if(util.isNotBlank(index.nowBilledQty)){
            tempList.push(index)
          }
        }
        for(let index of this.adApplyList){
          if((util.contains(index.shopName,val)||util.contains(index.productName,val)||util.contains(index.productCode,val))&&util.isBlank(index.nowBilledQty)){
            tempList.push(index)
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
        for(let index of content){
          if(util.isNotBlank(index.applyQty)){
            tempApplyQty += index.applyQty;
          }
          if(util.isNotBlank(index.confirmQty)){
            tempConfirmQty += index.confirmQty;
          }
          if(util.isNotBlank(index.leftQty)){
            tempLeftQty += index.leftQty;
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
          this.billGoodsSortQty = '';
          this.totalNowBilledQty = 0;
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
