<template>
  <div>
    <head-tab active="adApplyForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('adApplyForm.billType')" prop="billType">
          <el-select v-model="inputForm.billType" :placeholder="$t('adApplyForm.selectInput')" :clearable=true @change="onchange">
            <el-option v-for="billType in inputForm.extra.billTypes"  :key="billType" :label="billType" :value="billType"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item  :label="$t('adApplyForm.shopName')" prop="shopId">
          <depot-select v-if="this.inputForm.billType=='POP'" v-model="inputForm.shopId" category="popShop"></depot-select>
          <depot-select v-if="this.inputForm.billType=='配件赠品'" v-model="inputForm.shopId" category="directShop"></depot-select>
        </el-form-item>
        <el-form-item :label="$t('adApplyForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks" type="textarea"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('adApplyForm.save')}}</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="20" style="margin-bottom:20px;float:right">
        <span>{{$t('adApplyForm.search')}}</span>
        <el-input v-model="productName" @change="searchDetail" :placeholder="$t('adApplyForm.inputTowKey')" style="width:200px;margin-right:10px"></el-input>
      </el-row>
      <el-table :data="filterProduct"  stripe border>
        <el-table-column prop="code" :label="$t('adApplyForm.productCode')"></el-table-column>
        <el-table-column prop="applyQty" :label="$t('adApplyForm.applyQty')">
          <template scope="scope">
            <el-input v-model="scope.row.applyQty"></el-input>
          </template>
        </el-table-column>
        <el-table-column prop="name" :label="$t('adApplyForm.productName')"></el-table-column>
        <el-table-column prop="expiryDateRemarks" :label="$t('adApplyForm.expiryDateRemarks')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('adApplyForm.remarks')"></el-table-column>
        <el-table-column prop="price2" :label="$t('adApplyForm.price2')"></el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  import depotSelect from 'components/future/depot-select';
  export default{
    components:{
      depotSelect
    },
    data(){
      return this.getData();
    },
    methods:{
      getData(){
        return{
          isInit:false,
          submitDisabled:false,
          filterProduct:[],
          product:{},
          productName:"",
          inputForm:{
              extra:{},
          },
          rules: {
            billType: [{ required: true, message: this.$t('adApplyForm.prerequisiteMessage')}],
            shopId: [{ required: true, message: this.$t('adApplyForm.prerequisiteMessage')}]
          },
          remoteLoading:false
        }
      },
      formSubmit(){
        this.submitDisabled = true;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            let submitData = util.deleteExtra(this.inputForm);
            submitData.productAdForms = this.getProductForSubmit();
            axios.post('/api/ws/future/layout/adApply/save',qs.stringify(submitData,{allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(response.data.success){
                Object.assign(this.$data, this.getData());
                this.initPage();
              }
            }).catch( ()=> {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
       searchDetail(){
         let val=this.productName;
         if(!val){
             this.filterProduct = this.inputForm.productAdForms;
             return;
         }
         let tempList=[];
          for(let index of this.inputForm.productAdForms){
            if(util.isNotBlank(index.applyQty)){
              tempList.push(index);
             }
          }
         for(let index of this.inputForm.productAdForms){
           if(util.contains(index.name,val)&&util.isBlank(index.applyQty)){
             tempList.push(index);
           }
         }
         this.filterProduct = tempList;
       },setProductList(list){
            this.inputForm.productAdForms = list;
            this.searchDetail();
       },getProductForSubmit(){
        let tempList=new Array();
        for(let index in this.inputForm.productAdForms){
          let detail=this.inputForm.productAdForms[index];
          if(util.isNotBlank(detail.applyQty)){
            tempList.push(detail);
          }
        }
        return tempList;
      }, onchange(){
          axios.get('api/ws/future/basic/product/findAdProductAndAllowOrder',{params:{billType:this.inputForm.billType}}).then((response) =>{
            this.setProductList(response.data);
          });
       },initPage(){
        this.pageHeight = window.outerHeight -320;
        axios.get('api/ws/future/layout/adApply/getForm').then((response) =>{
          this.inputForm = response.data;
        });
       }
    },created () {
      this.initPage();
    }
  }
</script>
