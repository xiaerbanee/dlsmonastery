<template>
  <div>
    <head-tab active="goodsOrderShipList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{ $t('goodsOrderShipList.filter') }}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('goodsOrderShipList.filter')" v-model="formVisible" size="large" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="8">
              <el-form-item :label="formLabel.netType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.netType" clearable filterable :placeholder="$t('goodsOrderShipList.selectNetType')">
                  <el-option v-for="netType in formData.netTypeList" :key="netType" :label="netType" :value="netType"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.businessId.label"  :label-width="formLabelWidth">
                <el-input v-model.number="formData.businessId" auto-complete="off" :placeholder="$t('goodsOrderShipList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.billDateRange.label" :label-width="formLabelWidth">
                <date-range-picker  v-model="formData.billDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="formLabel.shipType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.shipType" clearable filterable :placeholder="$t('goodsOrderShipList.selectShopType')">
                  <el-option v-for="shipType in formData.shipTypeList" :key="shipType" :label="shipType" :value="shipType"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.areaId.label" :label-width="formLabelWidth">
                <!--<el-select v-model="formData.areaId" clearable  filterable :placeholder="$t('goodsOrderShipList.inputKey')">
                  <el-option v-for="area in formData.areaList":key="area.id"  :label="area.name" :value="area.id"></el-option>
                </el-select>-->
                <office-select v-model="formData.areaId"></office-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item :label="formLabel.shipDateRange.label" :label-width="formLabelWidth">
                <date-range-picker  v-model="formData.shipDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off"  :placeholder="$t('goodsOrderShipList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.storeId.label" :label-width="formLabelWidth">
                <depot-select v-model="formData.storeId"  type="store" category="store"></depot-select>
              </el-form-item>
              <el-form-item :label="formLabel.createdBy.label" :label-width="formLabelWidth">
                <account-select v-model="formData.createdBy"  ></account-select>
              </el-form-item>
              <el-form-item :label="formLabel.outCode.label" :label-width="formLabelWidth">
                <el-input v-model="formData.outCode" auto-complete="off" :placeholder="$t('goodsOrderShipList.likeSearch')" ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item :label="formLabel.createdDateRange.label" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDateRange"></date-range-picker>
              </el-form-item>
              <el-form-item :label="formLabel.expressCodes.label" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="formData.expressCodes" auto-complete="off"  :placeholder="$t('goodsOrderShipList.multiEnterOrComma')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.businessIds.label" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="formData.businessIds" auto-complete="off"  :placeholder="$t('goodsOrderShipList.multiEnterOrComma')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.status.label" :label-width="formLabelWidth">
                <el-select v-model="formData.status" clearable filterable :placeholder="$t('goodsOrderShipList.selectStatus')">
                  <el-option v-for="status in formData.statusList" :key="status"  :label="status" :value="status"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.remarks.label" :label-width="formLabelWidth">
                <el-input v-model="formData.remarks" auto-complete="off" :placeholder="$t('goodsOrderShipList.likeSearch')" ></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.expressCode.label" :label-width="formLabelWidth">
                <el-input v-model="formData.expressCode" auto-complete="off" :placeholder="$t('goodsOrderShipList.likeSearch')" ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{ $t('goodsOrderShipList.sure') }}</el-button>
        </div>
      </el-dialog>

      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" border v-loading="pageLoading" :element-loading-text="$t('goodsOrderShipList.loading')" @sort-change="sortChange" stripe border >
        <el-table-column column-key="id" prop="businessId" :label="$t('goodsOrderShipList.businessId')" sortable width="150"></el-table-column>
        <el-table-column prop="createdDate" sortable :label="$t('goodsOrderShipList.createdDate')"></el-table-column>
        <el-table-column prop="status" :label="$t('goodsOrderShipList.status')"></el-table-column>
        <el-table-column prop="shopName" :label="$t('goodsOrderShipList.shop')" ></el-table-column>
        <el-table-column prop="shipType" :label="$t('goodsOrderShipList.shipType')"></el-table-column>
        <el-table-column prop="amount" :label="$t('goodsOrderShipList.amount')" ></el-table-column>
        <el-table-column prop="storeName" :label="$t('goodsOrderShipList.store')" ></el-table-column>
        <el-table-column prop="remarks" :label="$t('goodsOrderShipList.remarks')" ></el-table-column>
        <el-table-column prop="netType" :label="$t('goodsOrderShipList.netType')" ></el-table-column>
        <el-table-column prop="expressOrderExpressCodes" :label="$t('goodsOrderShipList.expressCodes')" ></el-table-column>
        <el-table-column prop="pullStatus" :label="$t('goodsOrderShipList.pullStatus')" ></el-table-column>
        <el-table-column fixed="right" :label="$t('goodsOrderShipList.operate')" width="160">
          <template scope="scope">
            <div class="action"><el-button size="small" v-permit="'crm:goodsOrder:view'" @click.native="itemAction(scope.row.id, 'detail')">{{$t('goodsOrderShipList.detail')}}</el-button></div>
            <div class="action"  v-if="scope.row.enabled && scope.row.status=='待发货'"><el-button size="small" @click.native="itemAction(scope.row.id, 'ship')">{{$t('goodsOrderShipList.ship')}}</el-button></div>
            <div class="action"  v-if="scope.row.enabled && (scope.row.status=='待签收')"><el-button   size="small" @click.native="itemAction(scope.row.id, 'sign')">{{$t('goodsOrderShipList.sign')}}</el-button></div>
            <div class="action"  v-if="scope.row.enabled && (scope.row.status=='待签收')"><el-button   size="small" @click.native="itemAction(scope.row.id, 'shipBack')">{{$t('goodsOrderShipList.shipBack')}}</el-button></div>
            <div class="action"  v-if="scope.row.enabled && (scope.row.status=='待发货' || scope.row.status=='待签收')"><el-button   size="small" @click.native="itemAction(scope.row.id, 'mallOrder')">{{$t('goodsOrderShipList.mallOrder')}}</el-button></div>
            <div class="action"  v-if="scope.row.enabled && (scope.row.status=='待发货')"><el-button   size="small" @click.native="itemAction(scope.row.id, 'sreturn')">{{$t('goodsOrderShipList.sreturn')}}</el-button></div>
            <div class="action"  v-if="scope.row.enabled && (scope.row.status=='待发货')"><el-button   size="small" @click.native="itemAction(scope.row.id, 'delete')">{{$t('goodsOrderShipList.delete')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>

<script>
  import officeSelect from 'components/basic/office-select'
  import depotSelect from 'components/future/depot-select'
  import accountSelect from 'components/basic/account-select'
  export default{
    components:{
      officeSelect,
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
        sort:"id,DESC",
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
        netType:{label: this.$t("goodsOrderShipList.netType")},
        businessId:{label: this.$t("goodsOrderShipList.businessId")},
        billDateRange:{label: this.$t("goodsOrderShipList.billDate")},
        shipType:{label: this.$t("goodsOrderShipList.shipType")},
        areaId:{label: this.$t("goodsOrderShipList.office"), value:""},
        createdDateRange:{label: this.$t("goodsOrderShipList.createdDate")},
        expressCodes:{label:this.$t("goodsOrderShipList.expressCodes")},
        expressCode:{label:this.$t("goodsOrderShipList.expressCode")},
        businessIds:{label: this.$t("goodsOrderShipList.businessId")},
        shipDateRange:{label: this.$t("goodsOrderShipList.shipDate")},
        shopName:{label: this.$t("goodsOrderShipList.shop")},
        storeId:{label: this.$t("goodsOrderShipList.store"),value:""},
        createdBy:{label: this.$t("goodsOrderShipList.createdBy")},
        outCode:{label: this.$t("goodsOrderShipList.outCode")},
        status:{label: this.$t("goodsOrderShipList.status")},
        remarks:{label: this.$t("goodsOrderShipList.remarks")},
      },
      formLabelWidth: '120px',
      formVisible: false,
      pageLoading: false
    };
  },
  methods: {
    pageRequest() {
      this.pageLoading = true;
      util.copyValue(this.formData,this.submitData);
      util.setQuery("goodsOrderList",this.submitData);
      axios.get('/api/ws/future/crm/goodsOrderShip?'+qs.stringify(this.submitData)).then((response) => {
        this.page = response.data;
        this.pageLoading = false;
      })
    },pageChange(pageNumber,pageSize) {
      this.formData.page = pageNumber;
      this.formData.size = pageSize;
      this.pageRequest();
    },sortChange(column) {
      console.log(column);
      this.formData.sort=util.getSort(column);
      this.formData.page=0;
      this.pageRequest();
    },search() {
      this.formVisible = false;
      this.pageRequest();
    },itemAction:function(id,action){
      if(action=="detail") {
        this.$router.push({ name: 'goodsOrderDetail', query: { id: id }})
      }else if(action =="ship"){
        //todo
      }else if(action=="sign"){
        //todo
      }else if(action =="shipBack"){
        //todo
      }else if(action == "mallOrder"){
        //todo
      }else if(action =="sreturn"){
        //todo
      }else if(action =="delete"){
       //todo
      }
    }
 },created () {
    var that = this;
    that.pageHeight = window.outerHeight -320;
    axios.get('/api/ws/future/crm/goodsOrderShip/getQuery').then((response) =>{
      that.formData=response.data;
      util.copyValue(that.$route.query,that.formData);
      that.pageRequest();
    });
  }
};
</script>

