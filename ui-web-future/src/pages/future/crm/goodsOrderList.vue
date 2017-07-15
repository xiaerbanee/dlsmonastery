<template>
  <div>
    <head-tab active="goodsOrderList"></head-tab>
    <div>
      <el-row>
        <el-button v-permit="'crm:goodsOrder:edit'" type="primary" @click="itemAdd" icon="plus">{{ $t('goodsOrderList.add') }}</el-button>
        <el-button v-permit="'crm:goodsOrderShip:mallOrder'" type="primary" @click="mallAdd" icon="plus">{{ $t('goodsOrderList.mallAdd') }}</el-button>
        <el-button v-permit="'crm:goodsOrder:view'" type="primary" @click="formVisible = true" icon="search">{{ $t('goodsOrderList.filter') }}</el-button>
        <el-dropdown @command="handleCommand">
          <el-button type="primary">
            {{$t('goodsOrderList.more')}}<i class="el-icon-caret-bottom el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item v-permit="'crm:goodsOrder:batchAdd'" command="batchAdd">{{$t('goodsOrderList.batchAdd')}}</el-dropdown-item>
            <el-dropdown-item v-permit="'api:carrierOrder:view'" command="carrierOrder">{{$t('goodsOrderList.carrierOrder')}}</el-dropdown-item>
            <el-dropdown-item v-permit="'crm:goodsOrder:view'" command="goodsOrderImeList">{{$t('goodsOrderList.goodsOrderImeList')}}</el-dropdown-item>
            <el-dropdown-item v-permit="'crm:goodsOrder:view'" command="exportData">{{$t('goodsOrderList.exportData')}}</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <span  v-html="searchText"></span>
      </el-row>
      <el-dialog  title="状态修改" v-model="detailVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="detailData" label-width="120px">
          <el-form-item label="状态" prop="value">
            <el-select v-model="detailData.pullStatus" filterable clearable placeholder="请选择">
              <el-option v-for="status in formData.extra.pullStatusList" :key="status" :label="status" :value="status"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="快递单号" prop="value">
            <el-input  type="textarea" :rows="5" placeholder="请输入内容" v-model="detailData.expressOrderExpressCodes"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="formSubmit()">确定</el-button>
        </div>
      </el-dialog>
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible=false"  :title="$t('goodsOrderList.filter')" v-model="formVisible" size="large" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" label-width="80px">
          <el-row :gutter="2">
            <el-col :span="8">
              <el-form-item :label="$t('goodsOrderList.netType')" >
                <el-select v-model="formData.netType" clearable filterable :placeholder="$t('goodsOrderList.selectNetType')">
                  <el-option v-for="netType in formData.extra.netTypeList" :key="netType" :label="netType" :value="netType"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.businessId')">
                <el-input  v-model="formData.businessId" :placeholder="$t('goodsOrderList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.billDate')">
                <date-range-picker  v-model="formData.billDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.shipType')">
                <el-select v-model="formData.shipType" clearable filterable :placeholder="$t('goodsOrderList.selectShopType')">
                  <el-option v-for="shipType in formData.extra.shipTypeList" :key="shipType" :label="shipType" :value="shipType"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.office')">
                <office-select v-model="formData.areaId" @afterInit="setSearchText"></office-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item :label="$t('goodsOrderList.shipDate')">
                <date-range-picker  v-model="formData.shipDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.shop')">
                <el-input v-model="formData.shopName" :placeholder="$t('goodsOrderList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.store')">
                <depot-select v-model="formData.storeIdList" category="store" multiple @afterInit="setSearchText"></depot-select>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.createdBy')">
                <account-select v-model="formData.createdBy" @afterInit="setSearchText"  ></account-select>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.outCode')">
                <el-input v-model="formData.outCode" :placeholder="$t('goodsOrderList.likeSearch')" ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item :label="$t('goodsOrderList.createdDate')">
                <date-range-picker v-model="formData.createdDateRange"></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.expressCodes')">
                <el-input type="textarea" v-model="formData.expressCodes" :placeholder="$t('goodsOrderList.multiEnterOrComma')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.businessId')">
                <el-input type="textarea" v-model="formData.businessIds" :placeholder="$t('goodsOrderList.multiEnterOrComma')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.status')">
                <el-select v-model="formData.status" clearable filterable :placeholder="$t('goodsOrderList.selectStatus')">
                  <el-option v-for="status in formData.extra.statusList" :key="status" :label="status" :value="status"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.remarks')">
                <el-input v-model="formData.remarks" :placeholder="$t('goodsOrderList.likeSearch')" ></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.expressCode')">
                <el-input v-model="formData.expressCode" :placeholder="$t('goodsOrderList.likeSearch')" ></el-input>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderList.lxMallOrder')">
                <bool-select v-model="formData.lxMallOrder"></bool-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{ $t('goodsOrderList.sure') }}</el-button>
        </div>
      </search-dialog>

      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :row-class-name="tableRowClassName" :element-loading-text="$t('goodsOrderList.loading')" @sort-change="sortChange" stripe border >
        <el-table-column column-key="id" prop="formatId" :label="$t('goodsOrderList.businessId')" sortable></el-table-column>
        <el-table-column prop="createdDate" sortable :label="$t('goodsOrderList.createdDate')"></el-table-column>
        <el-table-column prop="billDate" :label="$t('goodsOrderList.billDate')"></el-table-column>
        <el-table-column prop="status" :label="$t('goodsOrderList.status')"></el-table-column>
        <el-table-column prop="shopName" :label="$t('goodsOrderList.shop')"></el-table-column>
        <el-table-column prop="shipType" :label="$t('goodsOrderList.shipType')"></el-table-column>
        <el-table-column prop="amount" sortable :label="$t('goodsOrderList.amount')" ></el-table-column>
        <el-table-column prop="shopShouldGetAfterBill" sortable :label="$t('goodsOrderList.shopShouldGet')"></el-table-column>
        <el-table-column prop="storeName" :label="$t('goodsOrderList.store')" ></el-table-column>
        <el-table-column prop="remarks" :label="$t('goodsOrderList.remarks')" ></el-table-column>
        <el-table-column prop="expressOrderExpressCodes" :label="$t('goodsOrderList.expressCodes')" ></el-table-column>
        <el-table-column prop="carrierOrderCodes" :label="$t('goodsOrderList.carrierOrderCodes')" ></el-table-column>
        <el-table-column prop="pullStatus" :label="$t('goodsOrderList.pullStatus')" ></el-table-column>
        <el-table-column :label="$t('goodsOrderList.operate')" width="160">
          <template scope="scope">
            <div class="action"><el-button size="small" v-permit="'crm:goodsOrder:view'" @click.native="itemAction(scope.row.id, 'detail')">{{$t('goodsOrderList.detail')}}</el-button></div>
            <div class="action"  v-if="scope.row.enabled && scope.row.status=='待开单'" v-permit="'crm:goodsOrder:bill'" ><el-button size="small" @click.native="itemAction(scope.row.id, 'bill')">{{$t('goodsOrderList.bill')}}</el-button></div>
            <div class="action"  v-if="scope.row.enabled && scope.row.status=='待开单'"  v-permit="'crm:goodsOrder:edit'" ><el-button size="small" @click.native="itemAction(scope.row.id, 'edit')">{{$t('goodsOrderList.edit')}}</el-button></div>
            <div class="action"  v-permit="'crm:goodsOrder:print'"><el-button :style="scope.row.print ? '' : 'color:#ff0000;' "  size="small" @click.native="itemAction(scope.row.id, 'print')">出库单</el-button></div>
            <div class="action"  v-if="canDel(scope.row)" v-permit="'crm:goodsOrder:delete'"><el-button   size="small" @click.native="itemAction(scope.row.id, 'delete')">{{$t('goodsOrderList.delete')}}</el-button></div>
            <div class="action"  v-permit="'crm:goodsOrder:print'"><el-button :style="scope.row.shipPrint ? '' : 'color:#ff0000;' " size="small" @click.native="itemAction(scope.row.id, 'shipPrint')">快递单</el-button></div>
            <div class="action"  v-permit="'crm:goodsOrder:print'"><el-button size="small" @click.native="editPullStatus(scope.row)">商城状态</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<style>
  .el-table .danger-row,.el-table .el-table__row--striped.danger-row>td {
    background: #FF8888 !important;
  }
  .el-table .danger-row:hover>td{
    background: #FF8888 !important;
  }
</style>
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
      detailData:{},
      initPromise:{},
      pageHeight:600,
      searchText:"",
      detailVisible:false,
      formVisible: false,
      pageLoading: false
    };
  },
  methods: {
    setSearchText() {
      this.$nextTick(function () {
        this.searchText = util.getSearchText(this.$refs.searchDialog);
      })
    },canDel(row){
       return row.enabled && (row.status==='待开单' || (row.status==='待发货' && util.isPermit('crm:goodsOrder:bill')));
    },
    tableRowClassName(row, index) {
      if (row.shopShouldGetAfterBill<= 0 && row.status==='待开单' && _.trim(row.shopCode) !== '') {
        return "danger-row";
      }else{
        return "";
      }
    },
    pageRequest() {
      this.pageLoading = true;
      this.setSearchText();
      let submitData = util.deleteExtra(this.formData);
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
      this.formData.sort=util.getSort(column);
      this.formData.page=0;
      this.pageRequest();
    },search() {
      this.formVisible = false;
      this.pageRequest();
    },itemAdd(){
      this.$router.push({ name: 'goodsOrderForm'});
    },mallAdd(){
      this.$router.push({ name: 'goodsOrderMallForm'});
    },itemAction:function(id,action){
      if(action==="edit") {
        this.$router.push({ name: 'goodsOrderForm', query: { id: id }})
      }else if(action ==="detail"){
        this.$router.push({ name: 'goodsOrderDetail', query: { id: id }})
      }else if(action==="bill"){
        this.$router.push({name:'goodsOrderBill',query:{id:id}})
      }else if(action ==="sign"){
        this.$router.push({name:'goodsOrderSign',query:{id:id}})
      }else if(action === "delete"){
        util.confirmBeforeDelRecord(this).then(() => {
          axios.get('/api/ws/future/crm/goodsOrder/delete', {params: {id: id}}).then((response) => {
            this.$message(response.data.message);
            this.pageRequest();
          });
        }).catch(()=>{});
      }else if(action === "print"){
        window.open('/#/future/crm/goodsOrderPrint?id=' + id);
      }else if(action === "shipPrint"){
         window.open('/#/future/crm/goodsOrderShipPrint?id=' + id);
      }
    },editPullStatus(row){
      this.detailVisible=true;
      this.detailData=JSON.parse(JSON.stringify(row));
    },
    formSubmit(){
      axios.post('/api/ws/future/crm/goodsOrder/updatePullStatus', qs.stringify(this.detailData)).then((response)=> {
        this.$message(response.data.message);
        this.detailVisible=false;
        this.pageRequest();
      })
    }, handleCommand(command) {
      if(command==="batchAdd"){
        this.$router.push({ name: 'goodsOrderBatchAdd'});
      }else if(command==="carrierOrder"){
        this.$router.push({name:'carrierOrderList'});
      }else if(command==="goodsOrderImeList"){
        this.$router.push({ name: 'goodsOrderImeList'});
      }else if(command==="exportData"){
        util.confirmBeforeExportData(this).then(() => {
          window.location.href='/api/ws/future/crm/goodsOrder/export?'+qs.stringify(util.deleteExtra(this.formData));
        }).catch(()=>{});
      }
    }
 },created () {
       this.pageHeight = 0.75*window.innerHeight;
      this.initPromise=axios.get('/api/ws/future/crm/goodsOrder/getQuery').then((response) =>{
        this.formData=response.data;
        util.copyValue(this.$route.query,this.formData);
      });
  },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
};
</script>

