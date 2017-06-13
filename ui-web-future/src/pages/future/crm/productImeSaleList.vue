<template>
  <div>
    <head-tab active="productImeSaleList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="sale" icon="plus" v-permit="'crm:productImeSale:edit'">{{$t('productImeSaleList.sale')}}</el-button>
        <el-button type="primary" @click="saleBack" icon="minus"  v-permit="'crm:productImeSale:back'">{{$t('productImeSaleList.saleBack')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:productImeSale:view'">{{$t('productImeSaleList.filter')}}</el-button>
        <el-button type="primary" @click="exportData"  v-permit="'crm:productImeSale:view'">{{$t('productImeSaleList.export')}}</el-button>

        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('productImeSaleList.filter')" v-model="formVisible" size="small" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="8">
              <el-form-item :label="$t('productImeSaleList.employeeName')" :label-width="formLabelWidth">
                <employee-select v-model="formData.employeeId" @afterInit="setSearchText"></employee-select>
              </el-form-item>
              <el-form-item :label="$t('productImeSaleList.createdDate')" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('productImeSaleList.isBack')" :label-width="formLabelWidth">
                <bool-select v-model="formData.isBack"></bool-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item :label="$t('productImeSaleList.shopName')" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off"  :placeholder="$t('productImeSaleList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('productImeSaleList.ime')" :label-width="formLabelWidth">
                <el-input v-model="formData.ime" auto-complete="off"  :placeholder="$t('productImeSaleList.inputImeTail')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('productImeSaleList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('productImeSaleList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="shopName" column-key="shopId" :label="$t('productImeSaleList.saleShopName')" width="180" sortable></el-table-column>
        <el-table-column prop="productImeIme"  :label="$t('productImeSaleList.ime')" ></el-table-column>
        <el-table-column prop="productImeProductName" :label="$t('productImeSaleList.productName')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('productImeSaleList.saleDate')" sortable></el-table-column>
        <el-table-column prop="employeeName" column-key="employeeId" :label="$t('productImeSaleList.employeeName')" sortable></el-table-column>
        <el-table-column prop="isBack" :label="$t('productImeSaleList.isBack')" width="70">
          <template scope="scope">
            <el-tag :type="scope.row.isBack ? 'primary' : 'danger'">{{scope.row.isBack | bool2str}}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>

  import boolSelect from 'components/common/bool-select'
  import employeeSelect from 'components/basic/employee-select'
  export default{
    components:{
      boolSelect,
      employeeSelect,
    },
    data() {
      return {
        pageLoading: false,
        pageHeight:600,
        page:{},
        searchText:"",
        formData:{
            extra:{}
        },
        initPromise:{},
        formLabelWidth: '120px',
        formVisible: false,
        loading:false
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
        util.setQuery("productImeSaleList",submitData);
        axios.get('/api/ws/future/crm/productImeSale?'+qs.stringify(submitData)).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        }).catch(()=>{
          this.pageLoading = false;
        });
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
      },exportData(){
        util.confirmBeforeExportData(this).then(() => {
          axios.get('/api/ws/future/crm/productImeSale/export',{params:this.submitData}).then((response)=> {
            window.location.href="/api/general/sys/folderFile/download?id="+response.data;
          });
        }).catch(()=>{});

      },sale(){
        this.$router.push({ name: 'productImeSaleForm'});
      },saleBack(){
        this.$router.push({ name: 'productImeSaleBackForm'});
      },itemAction:function(id,action){
        if(action==="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/crm/productImeSale/delete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
              this.pageRequest();
            });
          }).catch(()=>{});
        }
      }
    },created () {

      let that = this;
      that.pageHeight = window.outerHeight -320;
      this.initPromise=axios.get('/api/ws/future/crm/productImeSale/getQuery').then((response) =>{
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

