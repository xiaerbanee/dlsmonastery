<template>
  <div>
    <head-tab active="adGoodsOrderList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:adGoodsOrder:edit'">{{$t('adGoodsOrderList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:adGoodsOrder:view'">{{$t('adGoodsOrderList.filter')}}</el-button>
        <el-button type="primary" @click="itemDetailList"  v-permit="'crm:adGoodsOrder:view'">{{$t('adGoodsOrderList.itemDetailList')}}</el-button>
        <el-button type="primary" @click="exportData"  v-permit="'crm:adGoodsOrder:view'">{{$t('adGoodsOrderList.export')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false"
         :title="$t('adGoodsOrderList.filter')" v-model="formVisible" size="medium" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" label-width="120px">
          <el-row :gutter="12">
            <el-col :span="12">
              <el-form-item :label="$t('adGoodsOrderList.createdDate')">
                <date-range-picker v-model="formData.createdDateRange"></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderList.billType')">
                <el-select v-model="formData.billType" filterable clearable :placeholder="$t('adGoodsOrderList.inputKey')">
                  <el-option v-for="item in formData.extra.billTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderList.remarks')">
                <el-input v-model="formData.remarks" :placeholder="$t('adGoodsOrderList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderList.createdBy')">
                <account-select v-model="formData.createdBy" @afterInit="setSearchText"></account-select>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderList.parentId')">
                <el-input v-model="formData.parentId" :placeholder="$t('adGoodsOrderList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderList.hasDeposit')">
                <bool-select v-model="formData.hasDeposit"></bool-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="$t('adGoodsOrderList.areaName')">
                <el-select v-model="formData.shopAreaId" multiple filterable clearable >
                  <el-option v-for="item in formData.extra.areaList" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderList.storeName')">
                <el-select v-model="formData.storeId" clearable filterable>
                  <el-option v-for="item in formData.extra.adStoreList" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderList.shopName')">
                <el-input v-model="formData.shopName" auto-complete="off" :placeholder="$t('adGoodsOrderList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderList.billDate')">
                <date-range-picker v-model="formData.billDateRange"></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderList.processStatus')">
                <process-status-select v-model="formData.processStatus" type="AdGoodsOrder" multiple="multiple" @afterInit="setSearchText"></process-status-select>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderList.orderCode')" >
                <el-input type="textarea" v-model="formData.idStr" :placeholder="$t('adGoodsOrderList.blankOrComma')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('adGoodsOrderList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('adGoodsOrderList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="formatId" column-key="id" :label="$t('adGoodsOrderList.orderCode')" sortable></el-table-column>
        <el-table-column prop="createdDate" :label="$t('adGoodsOrderList.createdDate')" sortable></el-table-column>
        <el-table-column prop="billDate" :label="$t('adGoodsOrderList.billDate')" sortable></el-table-column>
        <el-table-column prop="billType" :label="$t('adGoodsOrderList.type')" sortable></el-table-column>
        <el-table-column prop="outCode" :label="$t('adGoodsOrderList.outCode')" sortable></el-table-column>
        <el-table-column prop="processStatus" :label="$t('adGoodsOrderList.processStatus')" sortable></el-table-column>
        <el-table-column prop="storeName" column-key="storeId" :label="$t('adGoodsOrderList.storeName')" sortable></el-table-column>
        <el-table-column prop="shopOfficeName" :label="$t('adGoodsOrderList.officeName')" ></el-table-column>
        <el-table-column prop="shopAreaName" :label="$t('adGoodsOrderList.areaName')" ></el-table-column>
        <el-table-column prop="depotShopAreaType" :label="$t('adGoodsOrderList.areaType')" ></el-table-column>
        <el-table-column prop="shopName" column-key="shopId" :label="$t('adGoodsOrderList.shopName')" sortable></el-table-column>
        <el-table-column prop="amount" :label="$t('adGoodsOrderList.amount')"  sortable></el-table-column>
        <el-table-column prop="expressOrderExpressCodes" :label="$t('adGoodsOrderList.expressCodes')" ></el-table-column>
        <el-table-column prop="remarks" :label="$t('adGoodsOrderList.remarks')"></el-table-column>
        <el-table-column prop="createdByName" column-key="createdBy" :label="$t('adGoodsOrderList.createdBy')" sortable></el-table-column>
        <el-table-column fixed="right" :label="$t('adGoodsOrderList.operation')">
          <template scope="scope">
            <div class="action" v-permit="'crm:adGoodsOrder:view'"><el-button size="small" @click.native="itemAction(scope.row.id,'detail')">{{$t('adGoodsOrderList.detail')}}</el-button></div>
            <div class="action" v-if="scope.row.auditable&&scope.row.processStatus.indexOf('审核')>0" v-permit="'crm:adGoodsOrder:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'audit')">{{$t('adGoodsOrderList.audit')}}</el-button></div>
            <div class="action" v-if="scope.row.auditable&&scope.row.processStatus.indexOf('开单')>0" v-permit="'crm:adGoodsOrder:bill'"><el-button size="small" @click.native="itemAction(scope.row.id,'bill')">{{$t('adGoodsOrderList.bill')}}</el-button></div>
            <div class="action" v-if="scope.row.auditable&&scope.row.processStatus.indexOf('发货')>0" v-permit="'crm:adGoodsOrder:ship'"><el-button size="small" @click.native="itemAction(scope.row.id,'ship')">{{$t('adGoodsOrderList.ship')}}</el-button></div>
            <div class="action" v-if="scope.row.auditable&&scope.row.processStatus.indexOf('签收')>0" v-permit="'crm:adGoodsOrder:sign'"><el-button size="small" @click.native="itemAction(scope.row.id,'sign')">{{$t('adGoodsOrderList.sign')}}</el-button></div>
            <div class="action" v-if="scope.row.editable" v-permit="'crm:adGoodsOrder:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'edit')">{{$t('adGoodsOrderList.edit')}}</el-button></div>
            <div class="action" v-if="scope.row.editable" v-permit="'crm:adGoodsOrder:delete'"><el-button size="small" @click.native="itemAction(scope.row.id,'delete')">{{$t('adGoodsOrderList.delete')}}</el-button></div>
            <div class="action" v-permit="'crm:adGoodsOrder:print'"><el-button :style="scope.row.print ? '' : 'color:#ff0000;' " size="small" @click.native="itemAction(scope.row.id,'print')">{{$t('adGoodsOrderList.print')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import accountSelect from 'components/basic/account-select';
  import processStatusSelect from 'components/general/process-status-select'
  import boolSelect from 'components/common/bool-select'
  export default {
    components:{
      accountSelect,
      processStatusSelect,
      boolSelect
    },
    data() {
      return {
        pageLoading: false,
        page:{},
        searchText:'',
        formData:{
            extra:{},
        },
        show:false,
        initPromise:{},
        formVisible: false,
        pageHeight:600,
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
        let submitData = util.deleteExtra(this.formData);
        util.setQuery("adGoodsOrderList",submitData);
        axios.get('/api/ws/future/layout/adGoodsOrder?'+qs.stringify(submitData)).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        });
      }, pageChange(pageNumber, pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      }, sortChange(column) {
        this.formData.sort = util.getSort(column);
        this.formData.page = 0;
        this.pageRequest();
      }, search() {
        this.formVisible = false;
        this.pageRequest();
      }, itemAdd(){
        this.$router.push({name: 'adGoodsOrderForm'});
      }, itemDetailList(){
        this.$router.push({name: 'adGoodsOrderDetailList'});
      }, itemAction: function (id, action) {
        if (action === "edit") {
          this.$router.push({name: 'adGoodsOrderForm', query: {id: id}})
        } else if (action === "detail") {
          this.$router.push({name: 'adGoodsOrderDetail', query: {id: id, action: "detail"}})
        } else if (action === "audit") {
          this.$router.push({name: 'adGoodsOrderDetail', query: {id: id, action: "audit"}})
        } else if (action === "bill") {
          this.$router.push({name: 'adGoodsOrderBill', query: {id: id}})
        } else if (action === "ship") {
          this.$router.push({name: 'adGoodsOrderShip', query: {id: id}})
        } else if (action === "sign") {
          this.$router.push({name: 'adGoodsOrderSign', query: {id: id}})
        } else if (action === "print") {
          window.open('/#/future/layout/adGoodsOrderPrint?id='+id, '', '');
        } else if (action === "delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/layout/adGoodsOrder/delete', {params: {id: id}}).then((response) => {
              this.$message(response.data.message);
              this.pageRequest();
            });
          }).catch(()=>{});
        }
      },exportData(){
        util.confirmBeforeExportData(this).then(() => {
          window.location.href='/api/ws/future/layout/adGoodsOrder/export?'+qs.stringify(util.deleteExtra(this.formData));
        }).catch(()=>{});
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      this.initPromise = axios.get('/api/ws/future/layout/adGoodsOrder/getQuery').then((response) =>{
        this.formData=response.data;
        util.copyValue(this.$route.query,this.formData);
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
  }
</script>

