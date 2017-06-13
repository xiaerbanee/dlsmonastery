<template>
  <div>
    <head-tab active="shopDepositList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopDeposit:edit'">{{$t('shopDepositList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:shopDeposit:view'">{{$t('shopDepositList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('shopDepositList.filter')" v-model="formVisible" size="tiny" class="search-form" ref="searchDialog"  z-Index="1500">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('shopDepositList.shopName')" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off" :placeholder="$t('shopDepositList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('shopDepositList.type')" :label-width="formLabelWidth">
                <el-select v-model="formData.type" filterable clearable :placeholder="$t('shopDepositList.inputKey')">
                  <el-option v-for="item in formData.extra.typeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('shopDepositList.createdBy')" :label-width="formLabelWidth">
                <account-select v-model="formData.createdBy" @afterInit="setSearchText"></account-select>
              </el-form-item>
              <el-form-item :label="$t('shopDepositList.createdDate')" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('shopDepositList.remarks')" :label-width="formLabelWidth">
                <el-input v-model="formData.remarks" auto-complete="off" :placeholder="$t('shopDepositList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('shopDepositList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('shopDepositList.loading')" @sort-change="sortChange" stripe border>

        <el-table-column fixed prop="id" :label="$t('shopDepositList.billCode')" sortable width="100"></el-table-column>
        <el-table-column prop="shopName" column-key="shopId"  :label="$t('shopDepositList.shopName')" sortable></el-table-column>
        <el-table-column prop="shopAreaName" :label="$t('shopDepositList.areaName')" ></el-table-column>
        <el-table-column prop="shopOfficeName" :label="$t('shopDepositList.officeName')" ></el-table-column>
        <el-table-column prop="type" :label="$t('shopDepositList.type')" sortable></el-table-column>
        <el-table-column prop="amount" :label="$t('shopDepositList.amount')" sortable></el-table-column>
        <el-table-column prop="leftAmount" :label="$t('shopDepositList.leftAmount')" sortable></el-table-column>
        <el-table-column prop="outCode" :label="$t('shopDepositList.outCode')" sortable></el-table-column>
        <el-table-column prop="createdByName" column-key="createdBy"  :label="$t('shopDepositList.createdBy')" sortable></el-table-column>
        <el-table-column prop="createdDate" :label="$t('shopDepositList.createdDate')" sortable></el-table-column>
        <el-table-column prop="lastModifiedByName" column-key="lastModifiedBy" :label="$t('shopDepositList.lastModifiedBy')" sortable></el-table-column>
        <el-table-column prop="lastModifiedDate" :label="$t('shopDepositList.lastModifiedDate')" sortable></el-table-column>
        <el-table-column prop="remarks" :label="$t('shopDepositList.remarks')" sortable></el-table-column>
        <el-table-column prop="locked" :label="$t('shopDepositList.locked')" width="100">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" :label="$t('shopDepositList.enabled')" width="100">
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'primary' : 'danger'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>

      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import accountSelect from 'components/basic/account-select'

  export default{
    components:{
      accountSelect,
    },
    data() {
      return {
        page:{},
        pageLoading:false,
        formData:{
            extra:{},
        },
        initPromise:{},
        searchText:"",
        formLabelWidth: '120px',
        formVisible: false,
      };
    },
    methods: {
      setSearchText(){
        this.$nextTick(function () {
          this.searchText = util.getSearchText(this.$refs.searchDialog);
        })
      },
      pageRequest() {
        this.pageLoading = true;
        this.setSearchText();
        let submitData = util.deleteExtra(this.formData);
        util.setQuery("shopDepositList",submitData);

        axios.get('/api/ws/future/crm/shopDeposit?'+qs.stringify(submitData)).then((response) => {
          this.page = response.data;
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
        this.$router.push({ name: 'shopDepositForm'})
      }
    },created () {
      let that = this;
      that.pageHeight = window.outerHeight -320;
      this.initPromise = axios.get('/api/ws/future/crm/shopDeposit/getQuery').then((response) =>{
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

