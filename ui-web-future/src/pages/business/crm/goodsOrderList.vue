<template>
  <div>
    <head-tab :active="goodsOrderList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus">{{ $t('goodsOrderList.add') }}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">{{ $t('goodsOrderList.filter') }}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('goodsOrderList.filter')" v-model="formVisible" size="large" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="8">
              <el-form-item :label="formLabel.netType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.netType" clearable filterable :placeholder="$t('goodsOrderList.selectNetType')">
                  <el-option v-for="netType in formProperty.netTypes" :key="netType" :label="netType" :value="netType"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.businessId.label"  :label-width="formLabelWidth">
                <el-input v-model.number="formData.businessId" auto-complete="off" :placeholder="$t('goodsOrderList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.storeType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.storeType" clearable filterable :placeholder="$t('goodsOrderList.selectStoreType')">
                  <el-option v-for="(value,key) in formProperty.storeTypes" :key="key"  :label="key" :value="value"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.billDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker  v-model="formData.billDate" type="daterange" align="right" :placeholder="$t('goodsOrderList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.shipType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.shipType" clearable filterable :placeholder="$t('goodsOrderList.selectShopType')">
                  <el-option v-for="shipType in formProperty.shipTypes" :key="shipType" :label="shipType" :value="shipType"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.areaId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.areaId" clearable  filterable :placeholder="$t('goodsOrderList.inputKey')">
                  <el-option v-for="area in formProperty.areas":key="area.id"  :label="area.name" :value="area.id"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item :label="formLabel.shipDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker  v-model="formData.shipDate" type="daterange" align="right" :placeholder="$t('goodsOrderList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off"  :placeholder="$t('goodsOrderList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.storeId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.storeId" clearable filterable :placeholder="$t('goodsOrderList.inputKey')">
                  <el-option v-for="stores in formProperty.stores" :key="stores.id" :label="stores.name" :value="stores.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.createdBy.label" :label-width="formLabelWidth">
                <el-input v-model="formData.createdBy" auto-complete="off" :placeholder="$t('goodsOrderList.likeSearch')" ></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.outCode.label" :label-width="formLabelWidth">
                <el-input v-model="formData.outCode" auto-complete="off" :placeholder="$t('goodsOrderList.likeSearch')" ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.createdDate" type="daterange" align="right"  :placeholder="$t('goodsOrderList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.expressCodes.label" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="formData.expressCodes" auto-complete="off"  :placeholder="$t('goodsOrderList.multiEnterOrComma')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.businessIds.label" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="formData.businessIds" auto-complete="off"  :placeholder="$t('goodsOrderList.multiEnterOrComma')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.status.label" :label-width="formLabelWidth">
                <el-select v-model="formData.status" clearable filterable :placeholder="$t('goodsOrderList.selectStatus')">
                  <el-option v-for="status in formProperty.status":key="status"  :label="status" :value="status"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.remarks.label" :label-width="formLabelWidth">
                <el-input v-model="formData.remarks" auto-complete="off" :placeholder="$t('goodsOrderList.likeSearch')" ></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.expressCodes.label" :label-width="formLabelWidth">
                <el-input v-model="formData.expressCodes" auto-complete="off" :placeholder="$t('goodsOrderList.likeSearch')" ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{ $t('goodsOrderList.sure') }}</el-button>
        </div>
      </el-dialog>

      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" border v-loading="pageLoading" :element-loading-text="$t('goodsOrderList.loading')" @sort-change="sortChange" stripe border >
        <el-table-column type="expand">
          <template scope="props">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item :label="$t('goodsOrderList.billDate')">
                <span>{{ props.row.billDate }}</span>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.shipDate')">
                <span>{{ props.row.shipDate }}</span>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.outCode')">
                <span>{{ props.row.outCode }}</span>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.createdName')">
                <span>{{ props.row.created.fullName }}</span>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.isUseTicket')">
                <span>{{ props.row.isUseTicket  | bool2str}}</span>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.shipRemarks')">
                <span>{{ props.row.shipRemarks }}</span>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.number')">
                <span>{{ props.row.id }}</span>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.orderRemarks')">
                <span>{{ props.row.remarks }}</span>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.shopDeposit')">
                <span>{{ props.row.shop.goodsDeposit }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column  prop="businessId" :label="$t('goodsOrderList.businessId')" sortable width="150"></el-table-column>
        <el-table-column prop="createdDate" sortable :label="$t('goodsOrderList.createdDate')"></el-table-column>
        <el-table-column prop="billDate" :label="$t('goodsOrderList.billDate')"></el-table-column>
        <el-table-column prop="status" :label="$t('goodsOrderList.status')"></el-table-column>
        <el-table-column prop="shop.name" :label="$t('goodsOrderList.shop')" ></el-table-column>
        <el-table-column prop="shipType" :label="$t('goodsOrderList.shipType')"></el-table-column>
        <el-table-column prop="amount" :label="$t('goodsOrderList.amount')" ></el-table-column>
        <el-table-column prop="shopShouldGet" :label="$t('goodsOrderList.shopShouldGet')" ></el-table-column>
        <el-table-column prop="store.name" :label="$t('goodsOrderList.store')" ></el-table-column>
        <el-table-column prop="remarks" :label="$t('goodsOrderList.remarks')" ></el-table-column>
        <el-table-column prop="netType" :label="$t('goodsOrderList.netType')" ></el-table-column>
        <el-table-column prop="expressOrder.expressCodes" :label="$t('goodsOrderList.expressCodes')" ></el-table-column>
        <el-table-column prop="carrierCodes" :label="$t('goodsOrderList.carrierCodes')" ></el-table-column>
        <el-table-column prop="pullStatus" :label="$t('goodsOrderList.pullStatus')" ></el-table-column>
        <el-table-column fixed="right" :label="$t('goodsOrderList.operate')" width="140">
          <template scope="scope">
             <div v-for="action in scope.row.actionList" :key="action" class="action">
                <el-button size="small" @click.native="itemAction(scope.row.id,action)">{{action}}</el-button>
             </div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<style>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>

<script>
export default {
  data() {
    return {
      page:{},
      formData:{
        page:0,
        size:25,
        netType:"",
        businessId:"",
        storeType:"",
        billDate:"",
        billDateBTW:'',
        shipType:"",
        areaId:"",
        createdDate:"",
        createdDateBTW:'',
        expressCodes:"",
        businessIds:"",
        shipDate:"",
        shipDateBTW:'',
        shopName:"",
        storeId:"",
        createdBy:"",
        outCode:"",
        status:"",
        remarks:"",
      },
      formLabel:{
        netType:{label: this.$t("goodsOrderList.netType")},
        businessId:{label: this.$t("goodsOrderList.businessId")},
        storeType:{label: this.$t("goodsOrderList.storeType"), value:""},
        billDateBTW:{label: this.$t("goodsOrderList.billDate")},
        shipType:{label: this.$t("goodsOrderList.shipType")},
        areaId:{label: this.$t("goodsOrderList.office"), value:""},
        createdDateBTW:{label: this.$t("goodsOrderList.createdDate")},
        expressCodes:{label:this.$t("goodsOrderList.expressCodes")},
        businessIds:{label: this.$t("goodsOrderList.businessId")},
        shipDateBTW:{label: this.$t("goodsOrderList.shipDate")},
        shopName:{label: this.$t("goodsOrderList.shop")},
        storeId:{label: this.$t("goodsOrderList.store"),value:""},
        createdBy:{label: this.$t("goodsOrderList.createdBy")},
        outCode:{label: this.$t("goodsOrderList.outCode")},
        status:{label: this.$t("goodsOrderList.status")},
        remarks:{label: this.$t("goodsOrderList.remarks")},
      },
      pickerDateOption:util.pickerDateOption,
      formProperty:{},
      formLabelWidth: '120px',
      formVisible: false,
      pullStatusList:['空值','已推送','待发货','待开单'],
      pageLoading: false
    };
  },
  methods: {
    pageRequest() {
      this.pageLoading = true;
      this.formData.createdDateBTW=util.formatDateRange(this.formData.createdDate);
      this.formData.billDateBTW=util.formatDateRange(this.formData.billDate);
      this.formData.shipDateBTW=util.formatDateRange(this.formData.shipDate);
      this.formLabel.areaId.value=util.getLabel(this.formProperty.areas, this.formData.areaId);
      this.formLabel.storeId.value=util.getLabel(this.formProperty.stores, this.formData.storeId);

      axios.get('/api/crm/goodsOrder',{params:this.formData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        }) }, pageChange(pageNumber, pageSize) {
      this.formData.page = pageNumber;
      this.formData.size = pageSize;
      this.pageRequest();
    },sortChange(column) {
      this.formData.order=util.getOrder(column);
      this.formData.page=0;
      this.pageRequest();
    },search() {
      this.formVisible = false;
      this.pageRequest();
    },itemAdd(){
      this.$router.push({ name: 'goodsOrderForm'})
    },itemAction:function(id,action){
      if(action=="修改") {
        this.$router.push({ name: 'goodsOrderForm', query: { id: id }})
      }else if(action =="详细"){
        this.$router.push({ name: 'goodsOrderDetail', query: { id: id }})
      }else if(action=="开单"){
        this.$router.push({name:'goodsOrderBill',query:{id:id}})
      }else if(action =="发货"){
        this.$router.push({name:'goodsOrderShip',query:{id:id}})
      }else if(action =="快递单"){
        var newWindow = window.open('/#/crm/goodsOrderShipPrint?id=' + id, '', '');
        newWindow.print();
      }else if(action =="打印"){
        var newWindow = window.open('/#/crm/goodsOrderPrint?id=' + id, '', '');
        newWindow.print();
      }else if(action =="签收"){
        this.$router.push({name:'goodsOrderSign',query:{id:id}})
      }else if(action =="重发"){
        axios.get('/api/crm/goodsOrder/shipBack?id='+id).then((response)=> {
         this.$router.push({name:'goodsOrderList',query:util.getQuery("goodsOrderList")})
        });
      }else if(action =="销售退货"){
        this.$router.push({name:'goodsOrderSreturn',query:{id:id}})
      }else if(action == "删除"){
        axios.get('/api/crm/goodsOrder/delete',{params:{id:id}}).then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        })
      }
    }
 },created () {
    this.pageHeight = window.outerHeight -320;
    util.copyValue(this.$route.query,this.formData);
    axios.get('/api/crm/goodsOrder/getQuery').then((response) =>{
      this.formProperty=response.data;
      this.pageRequest();
    });
  }
};
</script>

