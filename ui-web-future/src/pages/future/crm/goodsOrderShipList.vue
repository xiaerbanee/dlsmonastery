<template>
  <div>
    <head-tab active="goodsOrderShipList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{ $t('goodsOrderShipList.filter') }}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('goodsOrderShipList.filter')" v-model="formVisible" size="large" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="8">
              <el-form-item :label="$t('goodsOrderShipList.netType')" :label-width="formLabelWidth">
                <el-select v-model="formData.netType" clearable filterable :placeholder="$t('goodsOrderShipList.selectNetType')">
                  <el-option v-for="netType in formData.extra.netTypeList" :key="netType" :label="netType" :value="netType"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShipList.businessId')"  :label-width="formLabelWidth">
                <el-input v-model.number="formData.businessId" auto-complete="off" :placeholder="$t('goodsOrderShipList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShipList.billDate')" :label-width="formLabelWidth">
                <date-range-picker  v-model="formData.billDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShipList.shipType')" :label-width="formLabelWidth">
                <el-select v-model="formData.shipType" clearable filterable :placeholder="$t('goodsOrderShipList.selectShopType')">
                  <el-option v-for="shipType in formData.extra.shipTypeList" :key="shipType" :label="shipType" :value="shipType"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShipList.office')" :label-width="formLabelWidth">
                <office-select v-model="formData.areaId" @afterInit="setSearchText"></office-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item :label="$t('goodsOrderShipList.shipDate')" :label-width="formLabelWidth">
                <date-range-picker  v-model="formData.shipDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShipList.shop')" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off"  :placeholder="$t('goodsOrderShipList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShipList.store')" :label-width="formLabelWidth">
                <depot-select v-model="formData.storeId"  type="store" category="store" @afterInit="setSearchText"></depot-select>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShipList.createdBy')" :label-width="formLabelWidth">
                <account-select v-model="formData.createdBy" @afterInit="setSearchText" ></account-select>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShipList.outCode')" :label-width="formLabelWidth">
                <el-input v-model="formData.outCode" auto-complete="off" :placeholder="$t('goodsOrderShipList.likeSearch')" ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item :label="$t('goodsOrderShipList.createdDate')" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDateRange"></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShipList.expressCodes')" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="formData.expressCodes" auto-complete="off"  :placeholder="$t('goodsOrderShipList.multiEnterOrComma')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShipList.businessId')" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="formData.businessIds" auto-complete="off"  :placeholder="$t('goodsOrderShipList.multiEnterOrComma')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShipList.status')" :label-width="formLabelWidth">
                <el-select v-model="formData.status" clearable filterable :placeholder="$t('goodsOrderShipList.selectStatus')">
                  <el-option v-for="status in formData.extra.statusList" :key="status"  :label="status" :value="status"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShipList.remarks')" :label-width="formLabelWidth">
                <el-input v-model="formData.remarks" auto-complete="off" :placeholder="$t('goodsOrderShipList.likeSearch')" ></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderShipList.expressCode')" :label-width="formLabelWidth">
                <el-input v-model="formData.expressCode" auto-complete="off" :placeholder="$t('goodsOrderShipList.likeSearch')" ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{ $t('goodsOrderShipList.sure') }}</el-button>
        </div>
      </search-dialog>

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
            <div class="action"  v-if="scope.row.enabled && (scope.row.status=='待发货')"><el-button   size="small" @click.native="itemAction(scope.row.id, 'storePrint')">出库单</el-button></div>
            <div class="action"  v-if="scope.row.enabled && (scope.row.status=='待发货')"><el-button   size="small" @click.native="itemAction(scope.row.id, 'expressPrint')">快递单</el-button></div>

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
      searchText:"",
      formData:{
          extra:{}
      },
      formLabelWidth: '120px',
      formVisible: false,
      pageLoading: false
    };
  },
  methods: {
    setSearchText() {
      this.$nextTick(function () {
        this.searchText = util.getSearchText(this.$refs.searchDialog);
      })
    },
    pageRequest() {
      this.pageLoading = true;
      this.setSearchText();
      var submitData = util.deleteExtra(this.formData);
      util.setQuery("goodsOrderShipList",submitData);
      axios.get('/api/ws/future/crm/goodsOrderShip?'+qs.stringify(submitData)).then((response) => {
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
        this.$router.push({name:'goodsOrderShip',query:{id:id}})
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
      }else if(action=="storePrint"){
        window.open("/#/future/crm/goodsOrderStorePrint?id="+id,",");
      }else if(action=="expressPrint"){
        window.open("/#/future/crm/goodsOrderExpressPrint?id="+id,",");
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

