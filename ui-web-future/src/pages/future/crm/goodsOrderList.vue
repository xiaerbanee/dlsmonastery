<template>
  <div>
    <head-tab active="goodsOrderList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus">{{ $t('goodsOrderList.add') }}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">{{ $t('goodsOrderList.filter') }}</el-button>
        <el-button type="primary" @click="itemMallAdd" icon="plus">商城订单</el-button>
        <span  v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('goodsOrderList.filter')" v-model="formVisible" size="large" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="8">
              <el-form-item :label="$t('goodsOrderList.netType')" :label-width="formLabelWidth">
                <el-select v-model="formData.netType" clearable filterable :placeholder="$t('goodsOrderList.selectNetType')">
                  <el-option v-for="netType in formData.extra.netTypeList" :key="netType" :label="netType" :value="netType"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.businessId')"  :label-width="formLabelWidth">
                <el-input v-model="formData.businessId" auto-complete="off" :placeholder="$t('goodsOrderList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.billDate')" :label-width="formLabelWidth">
                <date-range-picker  v-model="formData.billDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.shipType')" :label-width="formLabelWidth">
                <el-select v-model="formData.shipType" clearable filterable :placeholder="$t('goodsOrderList.selectShopType')">
                  <el-option v-for="shipType in formData.extra.shipTypeList" :key="shipType" :label="shipType" :value="shipType"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.office')" :label-width="formLabelWidth">
                <office-select v-model="formData.areaId" @afterInit="setSearchText"></office-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item :label="$t('goodsOrderList.shipDate')" :label-width="formLabelWidth">
                <date-range-picker  v-model="formData.shipDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.shop')" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off"  :placeholder="$t('goodsOrderList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.store')" :label-width="formLabelWidth">
                <depot-select v-model="formData.storeId"  type="store" category="store" @afterInit="setSearchText"></depot-select>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.createdBy')" :label-width="formLabelWidth">
                <account-select v-model="formData.createdBy" @afterInit="setSearchText"  ></account-select>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.outCode')" :label-width="formLabelWidth">
                <el-input v-model="formData.outCode" auto-complete="off" :placeholder="$t('goodsOrderList.likeSearch')" ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item :label="$t('goodsOrderList.createdDate')" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDateRange"></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.expressCodes')" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="formData.expressCodes" auto-complete="off"  :placeholder="$t('goodsOrderList.multiEnterOrComma')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.businessId')" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="formData.businessIds" auto-complete="off"  :placeholder="$t('goodsOrderList.multiEnterOrComma')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.status')" :label-width="formLabelWidth">
                <el-select v-model="formData.status" clearable filterable :placeholder="$t('goodsOrderList.selectStatus')">
                  <el-option v-for="status in formData.extra.statusList" :key="status" :label="status" :value="status"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.remarks')" :label-width="formLabelWidth">
                <el-input v-model="formData.remarks" auto-complete="off" :placeholder="$t('goodsOrderList.likeSearch')" ></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.expressCode')" :label-width="formLabelWidth">
                <el-input v-model="formData.expressCode" auto-complete="off" :placeholder="$t('goodsOrderList.likeSearch')" ></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.lxMallOrder')" :label-width="formLabelWidth">
                <bool-select v-model="formData.lxMallOrder"></bool-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{ $t('goodsOrderList.sure') }}</el-button>
        </div>
      </search-dialog>

      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" border v-loading="pageLoading" :element-loading-text="$t('goodsOrderList.loading')" @sort-change="sortChange" stripe border >
        <el-table-column column-key="id" prop="formatId" :label="$t('goodsOrderList.businessId')" sortable width="150"></el-table-column>
        <el-table-column prop="createdDate" sortable :label="$t('goodsOrderList.createdDate')"></el-table-column>
        <el-table-column prop="billDate" :label="$t('goodsOrderList.billDate')"></el-table-column>
        <el-table-column prop="status" :label="$t('goodsOrderList.status')"></el-table-column>
        <el-table-column prop="shopName" :label="$t('goodsOrderList.shop')" ></el-table-column>
        <el-table-column prop="shipType" :label="$t('goodsOrderList.shipType')"></el-table-column>
        <el-table-column prop="amount" :label="$t('goodsOrderList.amount')" ></el-table-column>
        <el-table-column prop="shopShouldGetAfterBill" :label="$t('goodsOrderList.shopShouldGet')" ></el-table-column>
        <el-table-column prop="storeName" :label="$t('goodsOrderList.store')" ></el-table-column>
        <el-table-column prop="remarks" :label="$t('goodsOrderList.remarks')" ></el-table-column>
        <el-table-column prop="netType" :label="$t('goodsOrderList.netType')" ></el-table-column>
        <el-table-column prop="expressOrderExpressCodes" :label="$t('goodsOrderList.expressCodes')" ></el-table-column>
        <el-table-column prop="pullStatus" :label="$t('goodsOrderList.pullStatus')" ></el-table-column>
        <el-table-column fixed="right" :label="$t('goodsOrderList.operate')" width="160">
          <template scope="scope">
            <div class="action"><el-button size="small"v-permit="'crm:goodsOrder:view'" @click.native="itemAction(scope.row.id, 'detail')">{{$t('goodsOrderList.detail')}}</el-button></div>
            <div class="action"  v-if="scope.row.enabled && scope.row.status=='待开单'" v-permit="'crm:goodsOrder:bill'" ><el-button size="small" @click.native="itemAction(scope.row.id, 'bill')">{{$t('goodsOrderList.bill')}}</el-button></div>
            <div class="action"  v-if="scope.row.enabled && scope.row.status=='待开单'"  v-permit="'crm:goodsOrder:edit'" ><el-button size="small" @click.native="itemAction(scope.row.id, 'edit')">{{$t('goodsOrderList.edit')}}</el-button></div>
            <div class="action"  v-if="scope.row.enabled && (scope.row.status=='待开单' || scope.row.status=='待发货')" v-permit="'crm:goodsOrder:delete'"><el-button   size="small" @click.native="itemAction(scope.row.id, 'delete')">{{$t('goodsOrderList.delete')}}</el-button></div>
            <div class="action"  v-permit="'crm:goodsOrder:print'"><el-button size="small" @click.native="itemAction(scope.row.id, 'ship')">出库单</el-button></div>
            <div class="action"  v-permit="'crm:goodsOrder:print'"><el-button size="small" @click.native="itemAction(scope.row.id, 'expressPrint')">快递单</el-button></div>
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
  import boolSelect from 'components/common/bool-select'
  export default{
    components:{
      officeSelect,
      depotSelect,
      accountSelect,
      boolSelect,
    },
    data() {
    return {
      page:{},
      formData:{
          extra:{}
      },
      initPromise:{},
      searchText:"",
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
      util.setQuery("goodsOrderList",submitData);
      axios.get('/api/ws/future/crm/goodsOrder?'+qs.stringify(submitData)).then((response) => {
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
    },itemAdd(){
      this.$router.push({ name: 'goodsOrderForm'})
    },itemMallAdd(){
      this.$router.push({ name: 'goodsOrderMallForm'})
    },itemAction:function(id,action){
      if(action=="edit") {
        this.$router.push({ name: 'goodsOrderForm', query: { id: id }})
      }else if(action =="detail"){
        this.$router.push({ name: 'goodsOrderDetail', query: { id: id }})
      }else if(action=="bill"){
        this.$router.push({name:'goodsOrderBill',query:{id:id}})
      }else if(action =="sign"){
        this.$router.push({name:'goodsOrderSign',query:{id:id}})
      }else if(action == "delete"){
        util.confirmBeforeDelRecord(this).then(() => {
          axios.get('/api/ws/future/crm/goodsOrder/delete', {params: {id: id}}).then((response) => {
          this.$message(response.data.message);
          this.pageRequest();
      })
      }).catch(()=>{});
      }else if(action=="ship"){
        var newWindow=window.open('/#/future/crm/goodsOrderPrint?id=' + id);
      }else if(action=="expressPrint"){
         window.open('/#/future/crm/goodsOrderShipPrint?id=' + id);
      }
    }
 },created () {
    var that = this;
    that.pageHeight = window.outerHeight -320;
      this.initPromise=axios.get('/api/ws/future/crm/goodsOrder/getQuery').then((response) =>{
      that.formData=response.data;
      util.copyValue(that.$route.query,that.formData);
    });
  },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
};
</script>

