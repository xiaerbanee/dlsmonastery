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
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible=false" :title="$t('productImeSaleList.filter')" v-model="formVisible" size="medium" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" :label-width="formLabelWidth">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="$t('productImeSaleList.employeeName')">
                <employee-select v-model="formData.employeeId" @afterInit="setSearchText"></employee-select>
              </el-form-item>
              <el-form-item :label="$t('productImeSaleList.createdDate')" >
                <date-range-picker v-model="formData.createdDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('productImeSaleList.isBack')">
                <bool-select v-model="formData.isBack"></bool-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="$t('productImeSaleList.shopName')">
                <el-input v-model="formData.shopName" :placeholder="$t('productImeSaleList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('productImeSaleList.ime')">
                <el-input v-model="formData.ime" :placeholder="$t('productImeSaleList.inputImeTail')"></el-input>
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
        pageHeight: 600,
        page:{},
        initPromise:{},
        searchText:"",
        formData:{
            extra:{}
        },
        formVisible: false,
        formLabelWidth:"28%"
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
          window.location.href='/api/ws/future/crm/productImeSale/export?'+qs.stringify(util.deleteExtra(this.formData));
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
       this.pageHeight = 0.75*window.innerHeight;
      this.initPromise = axios.get('/api/ws/future/crm/productImeSale/getQuery').then((response) =>{
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

