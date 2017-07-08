<template>
  <div>
    <head-tab active="shopAdTypeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopAdType:edit'">{{$t('shopAdTypeList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'crm:shopAdType:view'">{{$t('shopAdTypeList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('shopAdTypeList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" :label-width="formLabelWidth">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('shopAdTypeList.name')" >
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('shopAdTypeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('shopAdTypeList.totalPriceType')" >
                <el-select v-model="formData.totalPriceType" filterable clearable :placeholder="$t('shopAdTypeList.inputKey')">
                  <el-option v-for="item in formData.extra.totalPriceTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('shopAdTypeList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('shopAdTypeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="name" :label="$t('shopAdTypeList.name')" sortable ></el-table-column>
        <el-table-column prop="totalPriceType" :label="$t('shopAdTypeList.totalPriceType')" sortable></el-table-column>
        <el-table-column prop="price" :label="$t('shopAdTypeList.price')" sortable></el-table-column>
        <el-table-column prop="remarks" :label="$t('shopAdTypeList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('shopAdTypeList.operation')" width="140">
          <template scope="scope">
            <div class="action" v-permit="'crm:shopAdType:edit'"><el-button size="small" @click.native="itemEdit(scope.row.id)">{{$t('shopAdTypeList.edit')}}</el-button></div>
            <div class="action" v-permit="'crm:shopAdType:delete'"><el-button size="small" @click.native="itemDelete(scope.row.id)">{{$t('shopAdTypeList.delete')}}</el-button></div>
          </template>
        </el-table-column>
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
        pickerDateOption:util.pickerDateOption,
        formLabelWidth: '28%',
        formVisible: false,
        pageLoading: false
      };
    }, methods: {
      setSearchText() {
        this.$nextTick(function () {
          this.searchText = util.getSearchText(this.$refs.searchDialog);
        })
      },
      pageRequest() {
        this.pageLoading = true;
        this.setSearchText();
        var submitData = util.deleteExtra(this.formData);
        util.setQuery("shopAdTypeList",submitData);
        axios.get('/api/ws/future/basic/shopAdType?'+qs.stringify(submitData)).then((response) => {
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
      },
      search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'shopAdTypeForm'})
      },itemEdit:function(id){
        this.$router.push({ name: 'shopAdTypeForm', query: { id: id }})
      },itemDelete:function(id){
       util.confirmBeforeDelRecord(this).then(() => {
          axios.get('/api/ws/future/basic/shopAdType/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        });
      }
    },created () {
      var that = this;
      that.pageHeight = window.outerHeight -320;
      that.initPromise = axios.get('/api/ws/future/basic/shopAdType/getQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(this.$route.query,that.formData);
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
  };
</script>

