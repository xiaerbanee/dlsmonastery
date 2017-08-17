<template>
  <div>
    <head-tab :active="$t('afterSaleImeAllotList.afterSaleImeAllotList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary"@click="formVisible = true" icon="search">{{$t('afterSaleImeAllotList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible = false" :title="$t('afterSaleImeAllotList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData"  :label-width="formLabelWidth">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="this.$t('afterSaleImeAllotList.fromDepot')">
                <el-input v-model="formData.fromDepotName" auto-complete="off" :placeholder="$t('afterSaleImeAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="this.$t('afterSaleImeAllotList.toDepot')" >
                <el-input v-model="formData.toDepotName" auto-complete="off" :placeholder="$t('afterSaleImeAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('afterSaleImeAllotList.createdBy')">
                <el-input v-model="formData.createdBy" auto-complete="off" :placeholder="$t('afterSaleImeAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('afterSaleImeAllotList.createdDate')" >
                <date-range-picker v-model="formData.createdDate"></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('afterSaleImeAllotList.afterSaleBill')" >
                <el-input type="textarea" v-model="formData.afterSaleId" auto-complete="off" :placeholder="$t('afterSaleImeAllotList.blankOrComma')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('afterSaleImeAllotList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('afterSaleImeAllotList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('afterSaleImeAllotList.bill')"sortable ></el-table-column>
        <el-table-column prop="afterSale.id" :label="$t('afterSaleImeAllotList.afterSaleBill')" sortable></el-table-column>
        <el-table-column prop="productIme.ime" :label="$t('afterSaleImeAllotList.ime')" sortable></el-table-column>
        <el-table-column prop="fromDepot.name" :label="$t('afterSaleImeAllotList.fromDepot')"></el-table-column>
        <el-table-column prop="toDepot.name" :label="$t('afterSaleImeAllotList.toDepot')"></el-table-column>
        <el-table-column prop="created.loginName" :label="$t('afterSaleImeAllotList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('afterSaleImeAllotList.createdDate')" sortable></el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        searchText:"",
        page:{},
        formData:{
          extra:{}
        },
        initPromise:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
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
        util.setQuery("dictEnumList",submitData);
        axios.get('/api/crm/afterSaleImeAllot',{params:submitData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.pageNumber = pageNumber;
        this.formData.pageSize = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getSort(column);
        this.formData.pageNumber=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      }
    },created () {
       this.pageHeight = 0.75*window.innerHeight;
      this.initPromise = axios.get('/api/ws/future/crm/afterSale/getQuery').then((response) =>{
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

