<template>
  <div>
    <head-tab active="shopAllotForm"></head-tab>
    <div>
      <el-form :model="shopAllot" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <template>
          <su-alert  type="danger" :text="errMsg"> </su-alert>
        </template>
        <el-form-item :label="$t('shopAllotForm.fromShop')" prop="fromShopId">
            <depot-select :disabled="!isCreate" category="directShop" v-model="shopAllot.fromShopId"  @input="refreshProductListIfNeeded" ></depot-select>
        </el-form-item>
        <el-form-item :label="$t('shopAllotForm.toShop')" prop="toShopId">
          <depot-select :disabled="!isCreate"  category="directShop" v-model="shopAllot.toShopId"   @input="refreshProductListIfNeeded"></depot-select>
        </el-form-item>
        <el-form-item :label="$t('shopAllotForm.remarks')" prop="remarks">
          <el-input type="textarea" v-model="shopAllot.remarks"></el-input>
        </el-form-item>

        <div v-if="shopAllot.fromShopId && shopAllot.toShopId ">
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('shopAllotForm.save')}}</el-button>
        </el-form-item>
        <el-input v-model="productName" @change="searchDetail" :placeholder="$t('shopAllotForm.selectTowKey')" style="width:200px;"></el-input>
        <el-table  :data="filterShopAllotDetailList"  style="margin-top:5px;"   stripe border >
          <el-table-column prop="productName" :label="$t('shopAllotForm.productName')"></el-table-column>
          <el-table-column prop="qty" :label="$t('shopAllotForm.qty')">
            <template scope="scope">
              <input type="text" v-model="scope.row.qty" class="el-input__inner"/>
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
            shopAllot:{},
            shopAllotDetailList:[],
            filterShopAllotDetailList:[],
            errMsg:'',
            submitData:{
              id:'',
              fromShopId:'',
              toShopId:'',
              remarks:'',
              shopAllotDetailList:[]
            },
            rules: {
              fromShopId: [{ required: true, message: this.$t('shopAllotForm.prerequisiteMessage')}],
              toShopId: [{ required: true, message: this.$t('shopAllotForm.prerequisiteMessage')}]
            }
          }
        },
        formSubmit(){
          var that = this;
          let form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              this.submitDisabled = true;
              this.initSubmitDataBeforeSubmit();
              axios.post('/api/ws/future/crm/shopAllot/save', qs.stringify(this.submitData, {allowDots:true})).then((response)=> {
                this.$message(response.data.message);

                if(response.data.success) {
                  if (!this.isCreate) {
                    this.submitDisabled = false;
                    this.$router.push({name: 'shopAllotList', query: util.getQuery("shopAllotList")});

                  }else{
                    Object.assign(this.$data, this.getData());
                    this.initPage();
                  }
                }
              }).catch(() => {
                  that.submitDisabled = false;
              });
            }
          })
        }, initSubmitDataBeforeSubmit(){
          this.submitData.id = this.$route.query.id;
          this.submitData.fromShopId = this.shopAllot.fromShopId;
          this.submitData.toShopId = this.shopAllot.toShopId;
          this.submitData.remarks = this.shopAllot.remarks;

          let tempList=[];
        for(let shopAllotDetail of this.shopAllotDetailList){

          if(util.isNotBlank(shopAllotDetail.id) || util.isNotBlank(shopAllotDetail.qty)){
            tempList.push(shopAllotDetail);
          }
        }
        this.submitData.shopAllotDetailList = tempList;
      }
      ,refreshProductListIfNeeded(){
          //只有新增的时候才会刷新产品列表，修改的时候不能修改fromShop和toShop，所以也就不需要再次加载产品列表
        if(this.shopAllot.fromShopId && this.shopAllot.toShopId && !this.$route.query.id){
          axios.get('/api/ws/future/crm/shopAllot/findDetailListForNew',{params:{fromShopId:this.shopAllot.fromShopId,toShopId:this.shopAllot.toShopId}}).then((response)=>{
            if(!response.data.success){
              this.errMsg=response.data.errMsg;
            }else{
              this.errMsg='';
              this.setShopAllotDetailList(response.data.shopAllotDetailList);
            }
          })
        }
        return true;
      },setShopAllotDetailList(list){
          this.shopAllotDetailList = list;
          this.searchDetail();
      },searchDetail(){
        let val=this.productName;
        let tempList=[];
        for(let shopAllotDetail of this.shopAllotDetailList){
          if(util.isNotBlank(shopAllotDetail.qty)){
            tempList.push(shopAllotDetail);
          }
        }
        for(let shopAllotDetail of this.shopAllotDetailList){
          if(util.contains(shopAllotDetail.productName, val) && util.isBlank(shopAllotDetail.qty)){
            tempList.push(shopAllotDetail);
          }
        }
        this.filterShopAllotDetailList = tempList;
      },initPage(){
          if(this.$route.query.id){
            axios.get('/api/ws/future/crm/shopAllot/findDetailListForEdit',{params: {shopAllotId:this.$route.query.id}}).then((response)=>{
              this.setShopAllotDetailList(response.data);
            });
          }
          axios.get('/api/ws/future/crm/shopAllot/findDto',{params: {id:this.$route.query.id}}).then((response)=>{
            this.shopAllot = response.data;
          });
        }
    },created () {

          this.initPage();


    }
  }
</script>
