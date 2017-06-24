<template>
  <div>
    <head-tab active="shopAllotForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <template>
          <su-alert  type="danger" :text="errMsg"> </su-alert>
        </template>
        <el-form-item :label="$t('shopAllotForm.fromShop')" prop="fromShopId">
            <depot-select :disabled="!isCreate" category="directShop" v-model="inputForm.fromShopId"  @input="refreshProductListIfNeeded" ></depot-select>
        </el-form-item>
        <el-form-item :label="$t('shopAllotForm.toShop')" prop="toShopId">
          <depot-select :disabled="!isCreate"  category="directShop" v-model="inputForm.toShopId"   @input="refreshProductListIfNeeded"></depot-select>
        </el-form-item>
        <el-form-item :label="$t('shopAllotForm.remarks')" prop="remarks">
          <el-input type="textarea" v-model="inputForm.remarks"></el-input>
        </el-form-item>

        <div v-if="inputForm.fromShopId && inputForm.toShopId">
          <el-form-item>
            <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('shopAllotForm.save')}}</el-button>
          </el-form-item>
          <el-input v-model="productName" @change="searchDetail" :placeholder="$t('shopAllotForm.selectTowKey')" style="width:200px;"></el-input>
          <el-table  :data="filterShopAllotDetailList"  style="margin-top:5px;"   stripe border >
            <el-table-column prop="productName" :label="$t('shopAllotForm.productName')"></el-table-column>
            <el-table-column prop="qty" :label="$t('shopAllotForm.qty')">
              <template scope="scope">
                <el-input v-model.number="scope.row.qty" ></el-input>
              </template>
            </el-table-column>

          </el-table>
        </div >
      </el-form>
    </div>
  </div>
</template>
<script>
  import depotSelect from 'components/future/depot-select'

  export default{
    components:{
      depotSelect,
    },
      data(){
        return this.getData();
      },
      methods:{
        getData(){
          return{
            isCreate:(this.$route.query.id==null || this.$route.query.id==''),
            submitDisabled:false,
            productName:'',
            filterShopAllotDetailList:[],
            errMsg:'',
            inputForm:{
                extra:{},
            },

            rules: {
              fromShopId: [{ required: true, message: this.$t('shopAllotForm.prerequisiteMessage')}],
              toShopId: [{ required: true, message: this.$t('shopAllotForm.prerequisiteMessage')}]
            }
          }
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

              let submitData =util.deleteExtra(this.inputForm);
              submitData.shopAllotDetailList = this.getSubmitDetailList();

              axios.post('/api/ws/future/crm/shopAllot/save', qs.stringify(submitData, {allowDots:true})).then((response)=> {
                this.$message(response.data.message);
                this.submitDisabled = false;
                if(response.data.success) {
                  if (this.isCreate) {
                    Object.assign(this.$data, this.getData());
                    this.initPage();
                  }else{
                    this.$router.push({name: 'shopAllotList', query: util.getQuery("shopAllotList"), params:{_closeFrom:true}});
                  }
                }
              }).catch(() => {
                  this.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        }, getSubmitDetailList(){

          let tempList=[];
          for(let shopAllotDetail of this.inputForm.shopAllotDetailList){

            if(util.isNotBlank(shopAllotDetail.id) || (Number.isInteger(shopAllotDetail.qty) &&shopAllotDetail.qty>0 )){
              tempList.push(shopAllotDetail);
            }
          }
          return tempList;
      },customValidate(){
          let totalQty = 0;
          for(let shopAllotDetail of this.inputForm.shopAllotDetailList){
            if(util.isBlank(shopAllotDetail.qty)){
              continue;
            }

            if(!Number.isInteger(shopAllotDetail.qty) || shopAllotDetail.qty < 0){
              return '产品：'+shopAllotDetail.productName+'的调拨数不是一个大于等于0的整数';
            }

            totalQty += shopAllotDetail.qty;
          }
          if(totalQty<=0){
            return "总调拨数要大于0";
          }
          return null;
        }
      ,refreshProductListIfNeeded(){
          //只有新增的时候才会刷新产品列表，修改的时候不能修改fromShop和toShop，所以也就不需要再次加载产品列表
        if(util.isNotBlank(this.inputForm.fromShopId) && util.isNotBlank(this.inputForm.toShopId) && util.isBlank(this.$route.query.id)){
          axios.get('/api/ws/future/crm/shopAllot/findDetailListForNew',{params:{fromShopId:this.inputForm.fromShopId,toShopId:this.inputForm.toShopId}}).then((response)=>{
            if(response.data.success){
              this.errMsg='';
              this.setShopAllotDetailList(response.data.shopAllotDetailList);
            }else{
              this.errMsg=response.data.errMsg;
            }
          })
        }
        return true;
      },setShopAllotDetailList(list){
          this.inputForm.shopAllotDetailList = list;
          this.searchDetail();
      },searchDetail(){
        let val=this.productName;
        let tempList=[];
        for(let shopAllotDetail of this.inputForm.shopAllotDetailList){
          if(util.isNotBlank(shopAllotDetail.qty)){
            tempList.push(shopAllotDetail);
          }
        }
        for(let shopAllotDetail of this.inputForm.shopAllotDetailList){
          if(util.contains(shopAllotDetail.productName, val) && util.isBlank(shopAllotDetail.qty)){
            tempList.push(shopAllotDetail);
          }
        }
        this.filterShopAllotDetailList = tempList;
      },initPage(){
          axios.get('/api/ws/future/crm/shopAllot/getForm').then((response)=>{
            this.inputForm = response.data;
            if(util.isNotBlank(this.$route.query.id)){

              axios.get('/api/ws/future/crm/shopAllot/findDetailListForEdit',{params: {shopAllotId:this.$route.query.id}}).then((response)=>{
                this.setShopAllotDetailList(response.data);
              });
              axios.get('/api/ws/future/crm/shopAllot/findDto',{params: {id:this.$route.query.id}}).then((response)=>{
                  this.inputForm.id = response.data.id;
                  this.inputForm.fromShopId = response.data.fromShopId;
                  this.inputForm.toShopId = response.data.toShopId;
                  this.inputForm.remarks = response.data.remarks;
              });
            }
          });
        }
    },created () {
      this.initPage();
    }
  }
</script>
