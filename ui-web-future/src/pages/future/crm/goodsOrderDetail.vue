<template>
  <div>
    <head-tab active="goodsOrderDetail"></head-tab>
    <div>
      <el-form :model="goodsOrder" ref="inputForm" :rules="rules" label-width="150px"  class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderDetail.businessId')+' : '" prop="businessId">
              {{goodsOrder.businessId}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.storeName')+' : '" prop="storeId">
              {{goodsOrder.storeName}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.shopName')+' : '" prop="shopId">
              {{goodsOrder.shopName}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.billDate')+' : '" prop="billDate">
              {{goodsOrder.billDate}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.outCode')+' : '" prop="outCode">
              {{goodsOrder.outCode}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.remarks')+' : '" prop="remarks">
              {{goodsOrder.remarks}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderDetail.createdLoginName')+' : '" prop="createdBy">
              {{goodsOrder.createdByName}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.createdDate')+' : '" prop="createdDate">
              {{goodsOrder.createdDate}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.isUseTicket')+' : '" prop="isUseTicket">
              {{goodsOrder.isUseTicket | bool2str}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.expressOrder')+' : '"  prop="expressOrder">
              {{expressOrder.expressCodes? expressOrder.expressCodes :'' }}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.shipType')+' : '" prop="shipType">
              {{goodsOrder.shipType}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.purchaseInfo')+' : '" prop="purchaseInfo">
              {{goodsOrder.purchaseInfo}}
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div style="width:100%;height:50px;text-align:center;font-size:20px">{{$t('goodsOrderDetail.billDetail')}}</div>
      <el-table :data="goodsOrderDetailList" style="margin-top:5px;" border stripe border>
        <el-table-column  prop="productName" :label="$t('goodsOrderDetail.productName')"  width="200"></el-table-column>
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
      <el-table :data="goodsOrderImeList" style="margin-top:5px;" border stripe border>
        <el-table-column  prop="productName" :label="$t('goodsOrderDetail.productName')"    width="200"></el-table-column>
        <el-table-column prop="productImeIme"  :label="$t('goodsOrderDetail.productIme')"  ></el-table-column>
        <el-table-column prop="productImeMeid" :label="$t('goodsOrderDetail.meid')"  ></el-table-column>
        <el-table-column prop="createdByName" :label="$t('goodsOrderDetail.createdByName')"  ></el-table-column>
        <el-table-column prop="createdDate":label="$t('goodsOrderDetail.shipDate')"  ></el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        goodsOrder:{},
        expressOrder:{},
        goodsOrderDetailList:[],
        goodsOrderImeList:[],
        imeMap:[],
        meidMap:[],
        rules: {},
        activityEntity:{},
        fileList:[]
      }
    },
    methods:{
      renderAction(createElement) {
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
        this.goodsOrder = response.data;
        this.goodsOrderDetailList = response.data.goodsOrderDetailDtoList;
        this.goodsOrderImeList = response.data.goodsOrderImeDtoList;
      });
      axios.get('/api/ws/future/crm/expressOrder/findByGoodsOrderId',{params: {goodsOrderId:this.$route.query.id}}).then((response)=>{
        this.expressOrder = response.data;
      });
    }
  }
</script>
