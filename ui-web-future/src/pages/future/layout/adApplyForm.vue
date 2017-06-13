<template>
  <div>
    <head-tab active="adApplyForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('adApplyForm.billType')" prop="billType">
          <el-select v-model="inputForm.billType" :placeholder="$t('adApplyForm.selectInput')" :clearable=true @change="onchange">
            <el-option v-for="billType in inputForm.billTypes"  :key="billType" :label="billType" :value="billType"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item  :label="$t('adApplyForm.shopName')" prop="shopId">
          <depot-select v-if="this.inputForm.billType=='POP'" v-model="inputForm.shopId" category="popShop"></depot-select>
          <depot-select v-if="this.inputForm.billType=='配件赠品'" v-model="inputForm.shopId" category="directShop"></depot-select>
        </el-form-item>
        <el-form-item :label="$t('adApplyForm.shopType')" prop="shopTypeLabel" v-if="shopTypeLabel">
          {{shopTypeLabel}}
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
      <el-table :data="inputForm.productDtos"  stripe border>
        <el-table-column prop="code" :label="$t('adApplyForm.productCode')"></el-table-column>
        <el-table-column prop="applyQty" :label="$t('adApplyForm.applyQty')">
          <template scope="scope">
            <el-input v-model="scope.row.applyQty"></el-input>
          </template>
        </el-table-column>
        <el-table-column prop="name" :label="$t('adApplyForm.productName')"></el-table-column>
        <el-table-column prop="expiryDateRemarks" sortable :label="$t('adApplyForm.expiryDateRemarks')"></el-table-column>
        <el-table-column prop="remarks" sortable :label="$t('adApplyForm.remarks')"></el-table-column>
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
          shopTypeLabel:'',
          filterAdApplyList:[],
          filterApplyQty:[],
          productName:"",
          shops:{},
          inputForm:{},
          submitData:{
            billType:'',
            productDtos:[],
            applyQty:[],
            shopId:'',
            remarks:'',
          },
          rules: {
            billType: [{ required: true, message: this.$t('adApplyForm.prerequisiteMessage')}],
            shopId: [{ required: true, message: this.$t('adApplyForm.prerequisiteMessage')}]
          },
          remoteLoading:false
        }
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            util.copyValue(this.inputForm,this.submitData);
            var tempList=new Array();
            var qtyList=new Array();
            for(var index in this.inputForm.adApplyList){
              var detail=this.inputForm.adApplyList[index];
              if(util.isNotBlank(detail.applyQty)){
                tempList.push(detail);
                qtyList.push(detail.applyQty);
              }
            }
            this.submitData.productDtos=tempList;
            this.submitData.applyQty = qtyList;

            axios.post('/api/ws/future/layout/adApply/save',qs.stringify(this.submitData,{allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              Object.assign(this.$data, this.getData());
              if(!this.isCreate){
                this.$router.push({name:'adApplyList',query:util.getQuery("adApplyList")})
              }
            }).catch(function () {
              that.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
       searchDetail(){
         var val=this.productName;
         var tempList=new Array();
         var qtyList = new Array();
          for(var index in this.inputForm.adApplyList){
            var detail=this.inputForm.adApplyList[index];
            if(util.isNotBlank(detail.applyQty)){
              tempList.push(detail);
              qtyList.push(detail.applyQty);
             }
          }
         for(var index in this.inputForm.adApplyList){
           var detail=this.inputForm.adApplyList[index];
           if((val.length>=1 && util.contains(detail.product.name,val)) && util.isBlank(detail.applyQty)){
             tempList.push(detail);
             qtyList.push(detail.applyQty);
           }
         }
         this.filterAdApplyList = tempList;
         this.filterApplyQty = qtyList;
       },onchange(){
          this.submitDisabled = true;
          axios.get('api/ws/future/layout/adApply/getForm',{params:{billType:this.inputForm.billType}}).then((response) =>{
              this.inputForm.adApplyList = response.data;
              this.submitDisabled = false;
          });
      },initPage(){

      }
    },created () {
      this.initPage();
    },activated () {
      if(!this.$route.query.headClick || !this.isInit) {
        Object.assign(this.$data, this.getData());
        this.pageHeight = window.outerHeight -320;
        axios.get('api/ws/future/layout/adApply/getForm').then((response) =>{
          this.inputForm = response.data;
        });
      }
      this.isInit = true;
    }
  }
</script>
