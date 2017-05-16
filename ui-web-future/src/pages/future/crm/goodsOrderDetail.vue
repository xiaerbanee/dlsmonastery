<template>
  <div>
    <head-tab active="goodsOrderDetail"></head-tab>
    <div>
      <el-form :model="detailForm" ref="inputForm" :rules="rules" label-width="150px"  class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderDetail.businessId')" prop="businessId">
              {{detailForm.businessId}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.storeName')" prop="storeId">
              {{detailForm.store==undefined?'':detailForm.store.name}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.shopName')" prop="shopId">
              {{detailForm.shop.name}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.billDate')" prop="billDate">
              {{detailForm.billDate}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.outCode')" prop="outCode">
              {{detailForm.outCode}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.carrierCodes')" prop="carrierCodes">
            {{detailForm.carrierCodes}}
          </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.carrierDetails')" prop="carrierDetails">
              {{detailForm.carrierDetails}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.remarks')" prop="remarks">
              {{detailForm.remarks}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderDetail.createdLoginName')" prop="createdBy">
              {{detailForm.created.loginName}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.createdDate')" prop="createdDate">
              {{detailForm.createdDate}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.isUseTicket')" prop="isUseTicket">
              {{detailForm.isUseTicket | bool2str}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.expressOrder')" prop="expressOrder">
              {{detailForm.expressOrder.expressCodes!=null?'':detailForm.expressOrder.expressCodes}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.shipType')" prop="shipType">
              {{detailForm.shipType}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.purchaseInfo')" prop="purchaseInfo">
              {{detailForm.purchaseInfo}}
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div style="width:100%;height:50px;text-align:center;font-size:20px">{{$t('goodsOrderDetail.billDetail')}}</div>
      <el-table :data="detailForm.goodsOrderDetailList" style="margin-top:5px;" border stripe border>
        <el-table-column  prop="product.name" :label="$t('goodsOrderDetail.productName')"  width="200"></el-table-column>
        <el-table-column prop="qty"  :label="$t('goodsOrderDetail.qty')"></el-table-column>
        <el-table-column prop="billQty" :label="$t('goodsOrderDetail.billQty')"></el-table-column>
        <el-table-column prop="price" :label="$t('goodsOrderDetail.price')"></el-table-column>
        <el-table-column :label="$t('goodsOrderDetail.operate')" :render-header="renderAction" >
          <template scope="scope">
            <el-button size="small" type="success" :text="imeMap[scope.row.productId]">{{$t('goodsOrderDetail.ime')}}</el-button>
            <el-button size="small" type="success" :text="meidMap[scope.row.productId]">{{$t('goodsOrderDetail.meid')}}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="width:100%;height:50px;text-align:center;font-size:20px">{{$t('goodsOrderDetail.shipDetail')}}</div>
      <el-table :data="detailForm.goodsOrderImeList" style="margin-top:5px;" border stripe border>
        <el-table-column  prop="product.name" :label="$t('goodsOrderDetail.productName')"    width="200"></el-table-column>
        <el-table-column prop="productIme.ime"  :label="$t('goodsOrderDetail.productIme')"  ></el-table-column>
        <el-table-column prop="productIme.meid" :label="$t('goodsOrderDetail.meid')"  ></el-table-column>
        <el-table-column prop="created.loginName" :label="$t('goodsOrderDetail.createdByName')"  ></el-table-column>
        <el-table-column prop="createdDate":label="$t('goodsOrderDetail.shipDate')"  ></el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        detailForm:{
          businessId:"",
          billDate:"",
          outCode:"",
          carrierCodes:"",
          carrierDetails:"",
          remarks:"",
          createdDate:"",
          isUseTicket:"",
          expressOrder:{expressCodes:""},
          shipType:"",
          purchaseInfo:"",
          shop:{name:''},
          store:{name:''},
          created:{loginName:''},
          goodsOrderDetailList:[],
          goodsOrderImeList:[]
        },
        zmd:'',
        imeMap:[],
        meidMap:[],
        rules: {},
        activityEntity:{},
        fileList:[]
      }
    },
    methods:{
      findOne(){
        axios.get('/api/crm/goodsOrder/detail',{params: {id:this.$route.query.id}}).then((response)=>{
        this.detailForm=response.data.goodsOrder;
        console.log(response.data.goodsOrder);
        if(response.data.goodsOrder.goodsOrderDetailList){
          this.detailForm.goodsOrderDetailList = response.data.goodsOrder.goodsOrderDetailList;
        }
        if(response.data.goodsOrder.goodsOrderImeList){
          this.detailForm.goodsOrderImeList = response.data.goodsOrder.goodsOrderImeList;
        }
        if(response.data.goodsOrder.store){
          this.detailForm.store = response.data.goodsOrder.store;
        }
        if(response.data.goodsOrder.shop){
          this.detailForm.shop = response.data.goodsOrder.shop;
        }
        if(response.data.goodsOrder.created){
          this.detailForm.created.loginName= response.data.goodsOrder.created.loginName;
        }
        this.zmd=response.data.zmd;
        this.imeMap=response.data.imeMap;
        this.meidMap=response.data.meidMap;
        })
      },renderAction(createElement) {
          return createElement(
            'a',{
               attrs: {
                class: 'el-button el-button--primary el-button--small'
              }, domProps: {
                innerHTML: this.$t('goodsOrderDetail.copy')
              }
            }
          );
        }
    },created(){
      axios.get('/api/ws/future/crm/goodsOrder/detail',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm = response.data;
      })

    }
  }
</script>
