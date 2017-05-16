<template>
  <div>
    <head-tab active="goodsOrderList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus">{{ $t('goodsOrderList.add') }}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">{{ $t('goodsOrderList.filter') }}</el-button>
        <search-tag  :formData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('goodsOrderList.filter')" v-model="formVisible" size="large" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="8">
              <el-form-item :label="formLabel.netType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.netType" clearable filterable :placeholder="$t('goodsOrderList.selectNetType')">
                  <el-option v-for="netType in formData.netTypeList" :key="netType" :label="netType" :value="netType"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.businessId.label"  :label-width="formLabelWidth">
                <el-input v-model.number="formData.businessId" auto-complete="off" :placeholder="$t('goodsOrderList.likeSearch')"></el-input>
              </el-form-item>

              <el-form-item :label="formLabel.billDateRange.label" :label-width="formLabelWidth">
                <date-range-picker  v-model="formData.billDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="formLabel.shipType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.shipType" clearable filterable :placeholder="$t('goodsOrderList.selectShopType')">
                  <el-option v-for="shipType in formData.shipTypeList" :key="shipType" :label="shipType" :value="shipType"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.areaId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.areaId" clearable  filterable :placeholder="$t('goodsOrderList.inputKey')">
                  <el-option v-for="area in formData.areaList":key="area.id"  :label="area.name" :value="area.id"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item :label="formLabel.shipDateRange.label" :label-width="formLabelWidth">
                <date-range-picker  v-model="formData.shipDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off"  :placeholder="$t('goodsOrderList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.storeId.label" :label-width="formLabelWidth">
                <depot-select v-model="formData.storeId"  type="STORE"></depot-select>
              </el-form-item>
              <el-form-item :label="formLabel.createdBy.label" :label-width="formLabelWidth">
                <account-select v-model="formData.createdBy"  ></account-select>
              </el-form-item>
              <el-form-item :label="formLabel.outCode.label" :label-width="formLabelWidth">
                <el-input v-model="formData.outCode" auto-complete="off" :placeholder="$t('goodsOrderList.likeSearch')" ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item :label="formLabel.createdDateRange.label" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDateRange"></date-range-picker>
              </el-form-item>
              <el-form-item :label="formLabel.expressCodes.label" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="formData.expressCodes" auto-complete="off"  :placeholder="$t('goodsOrderList.multiEnterOrComma')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.businessIds.label" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="formData.businessIds" auto-complete="off"  :placeholder="$t('goodsOrderList.multiEnterOrComma')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.status.label" :label-width="formLabelWidth">
                <el-select v-model="formData.status" clearable filterable :placeholder="$t('goodsOrderList.selectStatus')">
                  <el-option v-for="status in formData.statusList" :key="status"  :label="status" :value="status"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.remarks.label" :label-width="formLabelWidth">
                <el-input v-model="formData.remarks" auto-complete="off" :placeholder="$t('goodsOrderList.likeSearch')" ></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.expressCode.label" :label-width="formLabelWidth">
                <el-input v-model="formData.expressCode" auto-complete="off" :placeholder="$t('goodsOrderList.likeSearch')" ></el-input>
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
                <span>{{ props.row.createdByName }}</span>
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
                <span>{{ props.row.totalShopGoodsDepositAmount }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column  prop="businessId" :label="$t('goodsOrderList.businessId')" sortable width="150"></el-table-column>
        <el-table-column prop="createdDate" sortable :label="$t('goodsOrderList.createdDate')"></el-table-column>
        <el-table-column prop="billDate" :label="$t('goodsOrderList.billDate')"></el-table-column>
        <el-table-column prop="status" :label="$t('goodsOrderList.status')"></el-table-column>
        <el-table-column prop="shopName" :label="$t('goodsOrderList.shop')" ></el-table-column>
        <el-table-column prop="shipType" :label="$t('goodsOrderList.shipType')"></el-table-column>
        <el-table-column prop="amount" :label="$t('goodsOrderList.amount')" ></el-table-column>
        <el-table-column prop="shopShouldGet" :label="$t('goodsOrderList.shopShouldGet')" ></el-table-column>
        <el-table-column prop="storeName" :label="$t('goodsOrderList.store')" ></el-table-column>
        <el-table-column prop="remarks" :label="$t('goodsOrderList.remarks')" ></el-table-column>
        <el-table-column prop="netType" :label="$t('goodsOrderList.netType')" ></el-table-column>
        <el-table-column prop="expressOrderExpressCodes" :label="$t('goodsOrderList.expressCodes')" ></el-table-column>
        <el-table-column prop="pullStatus" :label="$t('goodsOrderList.pullStatus')" ></el-table-column>
        <el-table-column fixed="right" :label="$t('goodsOrderList.operate')" width="80">
          <template scope="scope">
            <el-button  type="text"  size="small"v-permit="'crm:goodsOrder:view'" @click.native="itemAction(scope.row.id, 'view')">{{$t('goodsOrderList.detail')}}</el-button>

            <el-button  v-if="scope.row.enabled && scope.row.status=='待开单'" type="text"  size="small"v-permit="'crm:goodsOrder:bill'" @click.native="itemAction(scope.row.id, 'bill')">{{$t('goodsOrderList.bill')}}</el-button>
            <el-button  v-if="scope.row.enabled && scope.row.status=='待开单'" type="text"  size="small"v-permit="'crm:goodsOrder:edit'" @click.native="itemAction(scope.row.id, 'edit')">{{$t('goodsOrderList.edit')}}</el-button>
            <el-button  v-if="scope.row.enabled && (scope.row.status=='待开单' || scope.row.status=='待发货')" type="text"  size="small"v-permit="'crm:goodsOrder:delete'" @click.native="itemAction(scope.row.id, 'delete')">{{$t('goodsOrderList.delete')}}</el-button>
            <el-button  v-if="scope.row.enabled && scope.row.status=='待发货' " type="text"  size="small"v-permit="'crm:goodsOrder:ship'" @click.native="itemAction(scope.row.id, 'ship')">{{$t('goodsOrderList.ship')}}</el-button>
            <el-button  v-if="scope.row.enabled && scope.row.status=='待发货' && scope.row.isSreturn" type="text"  size="small"v-permit="'crm:goodsOrder:edit'" @click.native="itemAction(scope.row.id, 'sreturn')">{{$t('goodsOrderList.sreturn')}}</el-button>
            <el-button  v-if="scope.row.enabled && scope.row.status=='待签收'" type="text"  size="small"v-permit="'crm:goodsOrder:edit'" @click.native="itemAction(scope.row.id, 'sign')">{{$t('goodsOrderList.sign')}}</el-button>
            <el-button  v-if="scope.row.enabled && scope.row.status=='待签收'" type="text"  size="small"v-permit="'crm:goodsOrder:shipBack'" @click.native="itemAction(scope.row.id, 'shipBack')">{{$t('goodsOrderList.shipBack')}}</el-button>
            <el-button  v-if="scope.row.isPrint" type="text"  size="small"v-permit="'crm:goodsOrder:print'" @click.native="itemAction(scope.row.id, 'print')">{{$t('goodsOrderList.print')}}</el-button>
            <el-button  v-if="!scope.row.isPrint" type="text" style="color:red;"  size="small"v-permit="'crm:goodsOrder:print'" @click.native="itemAction(scope.row.id, 'print')">{{$t('goodsOrderList.print')}}</el-button>
            <el-button  v-if="scope.row.isShipPrint" type="text" size="small"v-permit="'crm:goodsOrder:print'" @click.native="itemAction(scope.row.id, 'shipPrint')">{{$t('goodsOrderList.shipPrint')}}</el-button>
            <el-button  v-if="!scope.row.isShipPrint" type="text" style="color:red;"   size="small"v-permit="'crm:goodsOrder:print'" @click.native="itemAction(scope.row.id, 'shipPrint')">{{$t('goodsOrderList.shipPrint')}}</el-button>

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
  import depotSelect from 'components/future/depot-select'
  import accountSelect from 'components/basic/account-select'
  export default{
    components:{
      depotSelect,
      accountSelect,
    },
    data() {
    return {
      page:{},
      formData:{},
      submitData:{
        page:0,
        size:25,
        netType:"",
        businessId:"",
        billDateRange:'',
        shipType:"",
        areaId:"",
        createdDateRange:'',
        expressCodes:"",
        expressCode:"",
        businessIds:"",
        shipDateRange:'',
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
        billDateRange:{label: this.$t("goodsOrderList.billDate")},
        shipType:{label: this.$t("goodsOrderList.shipType")},
        areaId:{label: this.$t("goodsOrderList.office"), value:""},
        createdDateRange:{label: this.$t("goodsOrderList.createdDate")},
        expressCodes:{label:this.$t("goodsOrderList.expressCodes")},
        expressCode:{label:this.$t("goodsOrderList.expressCode")},
        businessIds:{label: this.$t("goodsOrderList.businessId")},
        shipDateRange:{label: this.$t("goodsOrderList.shipDate")},
        shopName:{label: this.$t("goodsOrderList.shop")},
        storeId:{label: this.$t("goodsOrderList.store"),value:""},
        createdBy:{label: this.$t("goodsOrderList.createdBy")},
        outCode:{label: this.$t("goodsOrderList.outCode")},
        status:{label: this.$t("goodsOrderList.status")},
        remarks:{label: this.$t("goodsOrderList.remarks")},
      },
      formLabelWidth: '120px',
      formVisible: false,
      pullStatusList:['空值','已推送','待发货','待开单'],
      pageLoading: false
    };
  },
  methods: {
    pageRequest() {
      this.pageLoading = true;
      util.copyValue(this.formData,this.submitData);
      util.setQuery("goodsOrderList",this.submitData);
      axios.get('/api/ws/future/crm/goodsOrder?'+qs.stringify(this.submitData)).then((response) => {
        this.page = response.data;
        this.pageLoading = false;
      })
    },pageChange(pageNumber,pageSize) {
      this.formData.page = pageNumber;
      this.formData.size = pageSize;
      this.pageRequest();
    },sortChange(column) {
      this.formData.sort=util.getSort(column);
      this.formData.page=0;
      this.pageRequest();
    },search() {
      this.formVisible = false;
      this.pageRequest();
    },itemAdd(){
      this.$router.push({ name: 'goodsOrderForm'})
    },itemAction:function(id,action){
      if(action=="edit") {
        this.$router.push({ name: 'goodsOrderForm', query: { id: id }})
      }else if(action =="detail"){
        this.$router.push({ name: 'goodsOrderDetail', query: { id: id }})
      }else if(action=="bill"){
        this.$router.push({name:'goodsOrderBill',query:{id:id}})
      }else if(action =="ship"){
        this.$router.push({name:'goodsOrderShip',query:{id:id}})
      }else if(action =="shipPrint"){
        var newWindow = window.open('/#/crm/goodsOrderShipPrint?id=' + id, '', '');
        newWindow.print();
      }else if(action =="print"){
        var newWindow = window.open('/#/crm/goodsOrderPrint?id=' + id, '', '');
        newWindow.print();
      }else if(action =="sign"){
        this.$router.push({name:'goodsOrderSign',query:{id:id}})
      }else if(action =="shipBack"){
        axios.get('/api/crm/goodsOrder/shipBack?id='+id).then((response)=> {
         this.$router.push({name:'goodsOrderList',query:util.getQuery("goodsOrderList")})
        });
      }else if(action =="sreturn"){
        this.$router.push({name:'goodsOrderReturn',query:{id:id}})
      }else if(action == "delete"){
        axios.get('/api/ws/future/crm/goodsOrder/delete',{params:{id:id}}).then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        })
      }
    }
 },created () {

    var that = this;
    that.pageHeight = window.outerHeight -320;
    axios.get('/api/ws/future/crm/goodsOrder/getQuery').then((response) =>{
      that.formData=response.data;
      util.copyValue(that.$route.query,that.formData);
      that.pageRequest();
    });
  }
};
</script>

