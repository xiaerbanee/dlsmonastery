<template>
  <div>
    <head-tab active="goodsOrderDetail"></head-tab>
    <div>
      <el-form :model="goodsOrder" ref="inputForm" :rules="rules" label-width="150px" class="form input-form">
        <el-row>
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
            <el-form-item v-if="carrierEdit" :label="$t('goodsOrderDetail.mallDepotName')+' : '" prop="outCode">
              <el-select v-model="formData.carrierShopId" filterable remote :remote-method="search" :loading="loading">
                <el-option v-for="item in shopList" :value="item.id" :key="item.id" :label="item.name"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item v-if="carrierEdit" :label="$t('goodsOrderDetail.carrierDetails')+' : '" prop="outCode">
              <el-input type="textarea" v-model="formData.detailJson" :rows="4"></el-input>
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
            <el-form-item :label="$t('goodsOrderDetail.expressOrder')+' : '" prop="expressOrder">
              {{expressOrder.expressCodes ? expressOrder.expressCodes : '' }}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.shipType')+' : '" prop="shipType">
              {{goodsOrder.shipType}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderDetail.purchaseInfo')+' : '" prop="purchaseInfo">
              {{goodsOrder.purchaseInfo}}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="carrierEdit">
          <el-button type="primary" class="saveBtn" icon="check" @click="formSubmit" :disable="submitDisabled">
            {{$t('goodsOrderDetail.save')}}
          </el-button>
        </el-row>
      </el-form>
      <div style="width:100%;height:50px;text-align:center;font-size:20px">{{$t('goodsOrderDetail.billDetail')}}</div>
      <el-table :data="goodsOrderDetailList" style="margin-top:5px;" stripe border>
        <el-table-column prop="productName" :label="$t('goodsOrderDetail.productName')" width="200"></el-table-column>
        <el-table-column prop="qty" :label="$t('goodsOrderDetail.qty')"></el-table-column>
        <el-table-column prop="realBillQty" :label="$t('goodsOrderDetail.billQty')"></el-table-column>
        <el-table-column prop="shippedQty" :label="$t('goodsOrderDetail.shippedQty')"></el-table-column>
        <el-table-column prop="price" :label="$t('goodsOrderDetail.price')"></el-table-column>
        <el-table-column :label="$t('goodsOrderDetail.operate')" :render-header="renderAction">
          <template scope="scope">
            <el-button size="small" class="clipBtn" type="success">{{$t('goodsOrderDetail.ime')}}</el-button>
            <el-button size="small" class="clipBtn" type="success" >{{$t('goodsOrderDetail.meid')}}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="width:100%;height:50px;text-align:center;font-size:20px" v-if="!carrierEdit">
        {{$t('goodsOrderDetail.shipDetail')}}
      </div>
      <el-table :data="goodsOrderImeList" style="margin-top:5px;" stripe border v-if="!carrierEdit">
        <el-table-column prop="productName" :label="$t('goodsOrderDetail.productName')" width="200"></el-table-column>
        <el-table-column prop="productImeIme" :label="$t('goodsOrderDetail.productIme')"></el-table-column>
        <el-table-column prop="productImeMeid" :label="$t('goodsOrderDetail.meid')"></el-table-column>
        <el-table-column prop="createdByName" :label="$t('goodsOrderDetail.createdByName')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('goodsOrderDetail.shipDate')"></el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  import clip from 'clipboard'
  new clip('.clipBtn')
  export default{
    data(){
      return this.getData();
    },
    methods: {
      getData(){
        return {
          carrierEdit: this.$route.query.carrierEdit,
          isCreate: this.$route.query.id == null,
          goodsOrder: {},
          expressOrder: {},
          goodsOrderDetailList: [],
          goodsOrderImeList: [],
          imeMap: [],
          meidMap: [],
          rules: {},
          activityEntity: {},
          fileList: [],
          formData: {
            id: this.$route.query.id,
            carrierShopId: "",
            detailJson: "",
          },
          loading: false,
          shopList: [],
          submitDisabled: false,
        }
      },
      renderAction(createElement) {
        return createElement(
          'a', {
            attrs: {
              class: 'el-button el-button--primary el-button--small'
            }, domProps: {
              innerHTML: this.$t('goodsOrderDetail.copy')
            }
          }
        );
      },
      search(query){
        if (query.length >= 2) {
          this.loading = true;
          axios.get('/api/ws/future/api/carrierShop/search', {params: {name: query}})
            .then((res) => {
              this.shopList = res.data;
              this.loading = false;
            })
            .catch((err) => {
              console.log(err);
            })
        }
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        let form = this.$refs.inputForm;
        form.validate((isValidate) => {
          if (isValidate) {
            axios.post('/api/ws/future/crm/goodsOrder/updateCarrierOrderDetail', qs.stringify(util.deleteExtra(this.formData)))
              .then((response) => {
                if (response.data.success) {
                  this.$message(response.data.message);
                  if (this.isCreate) {
                    Object.assign(this.$data, this.getData());
                    this.initPage();
                  } else {
                    this.$router.push({
                      name: 'goodsOrderShipList',
                      query: util.getQuery("goodsOrderShipList"),
                      params: {_closeFrom: true}
                    })
                  }
                } else {
                  this.submitDisabled = false;
                  this.$message({
                    showClose: true,
                    message: response.data.message,
                    type: 'error'
                  });
                }
              })
              .catch((err) => {
                that.submitDisabled = false;
              })
          }
        })
      }
    }, created(){
      axios.get('/api/ws/future/crm/goodsOrder/detail', {params: {id: this.$route.query.id}}).then((response) => {
        this.goodsOrder = response.data;
        console.log('data:',response.data);
        this.goodsOrderDetailList = response.data.goodsOrderDetailDtoList;
        this.goodsOrderImeList = response.data.goodsOrderImeDtoList;
        console.log('detailList:',this.goodsOrderDetailList);
        console.log('imeList:',this.goodsOrderImeList);
      });
      axios.get('/api/ws/future/crm/expressOrder/findByGoodsOrderId', {params: {goodsOrderId: this.$route.query.id}}).then((response) => {
        this.expressOrder = response.data;
      });
    }
  }
</script>
<style>
  .el-button.saveBtn {
    margin: 0 815px;
  }
</style>
