<template>
  <div>
    <head-tab active="productImeUploadList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:productImeUpload:edit'">{{$t('productImeUploadList.upload')}}</el-button>
        <el-button type="primary" @click="itemBack" icon="minus" v-permit="'crm:productImeUpload:edit'">{{$t('productImeUploadList.back')}}</el-button>
        <el-button type="primary" @click="batchUpload"  icon="plus"  v-permit="'crm:productImeUpload:edit'">{{$t('productImeUploadList.batchUpload')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:productImeUpload:view'">{{$t('productImeUploadList.filter')}}</el-button>
        <el-button type="primary" @click="exportData"  v-permit="'crm:productImeUpload:view'">{{$t('productImeUploadList.export')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('productImeUploadList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" label-width="120px">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('productImeUploadList.officeId')">
                <office-select v-model="formData.officeId" @afterInit="setSearchText"></office-select>
              </el-form-item>
              <el-form-item :label="$t('productImeUploadList.month')">
                <month-picker  v-model="formData.month" ></month-picker>
              </el-form-item>
              <el-form-item :label="$t('productImeUploadList.createdDate')">
                <date-range-picker v-model="formData.createdDateRange"></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('productImeUploadList.shopName')">
                <el-input v-model="formData.shopName" :placeholder="$t('productImeUploadList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('productImeUploadList.imeOrMeids')">
                <el-input  type="textarea" v-model="formData.imeOrMeids"  :placeholder="$t('productImeUploadList.imeOrMeidsMultiLine')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="exportData()" v-permit="'crm:productImeUpload:view'">{{$t('productImeUploadList.export')}}</el-button>
          <el-button type="primary" @click="search()">{{$t('productImeUploadList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('productImeUploadList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="month" :label="$t('productImeUploadList.month')" width="180" ></el-table-column>
        <el-table-column prop="shopName" :label="$t('productImeUploadList.updateShopName')"  ></el-table-column>
        <el-table-column prop="productImeIme" :label="$t('productImeUploadList.ime')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('productImeUploadList.updateDate')"></el-table-column>
        <el-table-column prop="employeeName" :label="$t('productImeUploadList.employeeName')"></el-table-column>
        <el-table-column prop="status"  :label="$t('productImeUploadList.status')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.status=='已通过' ? 'primary' : 'danger'">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>

      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>

  import officeSelect from 'components/basic/office-select'
  import monthPicker from 'components/common/month-picker'

  export default{
    components:{
      officeSelect,
      monthPicker,
    },
    data() {
      return {
        pageLoading: false,
        page:{},
        initPromise:{},
        searchText:"",
        formData:{
            extra:{}
        },
        selects:[],
        formVisible: false,

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
        util.setQuery("productImeUploadList",submitData);
        axios.get('/api/ws/future/crm/productImeUpload',{params:submitData}).then((response) => {
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
      },itemAdd(){
        this.$router.push({ name: 'productImeUploadForm'});
      },itemBack(){
        this.$router.push({ name: 'productImeUploadBackForm'});
      },batchUpload(){
        this.$router.push({ name: 'productImeBatchUploadForm'});
      },exportData(){
        util.confirmBeforeExportData(this).then(() => {
          axios.get('/api/ws/future/crm/productImeUpload/export',{params:util.deleteExtra(this.formData)}).then((response)=> {
            window.location.href="/api/general/sys/folderFile/download?id="+response.data;
          });
        }).catch(()=>{});
      }
    },created () {
      this.initPromise = axios.get('/api/ws/future/crm/productImeUpload/getQuery').then((response) =>{
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

