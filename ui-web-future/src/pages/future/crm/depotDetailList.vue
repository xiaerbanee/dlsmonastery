<template>
  <div>
    <head-tab active="depotDetailList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:depotDetail:view'">{{$t('depotDetailList.filter')}}</el-button>
        <el-button type="primary"  @click="exportData" v-permit="'crm:depotDetail:view'">{{$t('depotDetailList.export')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="upload2" v-permit="'crm:depotDetail:edit'">{{$t('depotDetailList.syn')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('depotDetailList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" >
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('depotDetailList.shopName')" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off" :placeholder="$t('depotDetailList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('depotDetailList.productName')" :label-width="formLabelWidth">
                <el-input v-model="formData.productName" auto-complete="off" :placeholder="$t('depotDetailList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('depotDetailList.hasIme')" :label-width="formLabelWidth">
                <bool-select  v-model="formData.hasIme"></bool-select>
              </el-form-item>
              <el-form-item :label="$t('depotDetailList.isSame')" :label-width="formLabelWidth">
                <bool-select  v-model="formData.isSame"></bool-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('depotDetailList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('depotDetailList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="depotName" :label="$t('depotDetailList.shopName')" ></el-table-column>
        <el-table-column prop="productName" :label="$t('depotDetailList.productName')" ></el-table-column>
        <el-table-column prop="hasIme" :label="$t('depotDetailList.hasIme')" width="150" sortable>
          <template scope="scope">
            <el-tag :type="scope.row.hasIme ? 'primary' : 'danger'">{{scope.row.hasIme | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="outQty" :label="$t('depotDetailList.outQty')" sortable></el-table-column>
        <el-table-column prop="qty" :label="$t('depotDetailList.qty')" sortable></el-table-column>
        <el-table-column prop="isSame" :label="$t('depotDetailList.isSame')" width="150" sortable>
          <template scope="scope">
            <el-tag :type="scope.row.isSame ? 'primary' : 'danger'">{{scope.row.isSame | bool2str}}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import boolSelect from "components/common/bool-select"
  export default {
    components:{
      boolSelect
    },
    data() {
      return {
        page:{},
        searchText:"",
        formData:{
          extra:{}
        },
        pickerDateOption:util.pickerDateOption,
        formProperty:{},
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
        util.setQuery("depotDetailList",submitData);
        axios.get('/api//ws/future/crm/depotDetail',{params:submitData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getSort(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },exportData(){
        util.confirmBeforeExportData(this).then(() => {
          var submitData = util.deleteExtra(this.formData);
          axios.get('/api/ws/future/crm/depotDetail/export?'+qs.stringify(submitData)).then((response)=> {
            console.log(response.data)
            window.location.href="/api/general/sys/folderFile/download?id="+response.data;
          });
        }).catch(()=>{});
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      axios.get('/api/ws/future/crm/depotDetail/getQuery').then((response) =>{
        this.formProperty=response.data;
        this.pageRequest();
      });
    }
  };
</script>

