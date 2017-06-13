<template>
  <div>
    <head-tab active="shopImageList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopImage:edit'">{{$t('shopImageList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:shopImage:view'">{{$t('shopImageList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('shopImageList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('shopImageList.officeName')" :label-width="formLabelWidth">
                <office-select v-model="formData.officeId"></office-select>
              </el-form-item>
              <el-form-item :label="$t('shopImageList.shopName')" :label-width="formLabelWidth">
                <depot-select v-model="formData.shopId" category="adShop"></depot-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('shopImageList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('shopImageList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="areaName" :label="$t('shopImageList.areaName')" width="150"></el-table-column>
        <el-table-column prop="officeName" :label="$t('shopImageList.officeName')"></el-table-column>
        <el-table-column column-key="shopId" prop="shopName"  :label="$t('shopImageList.shopName')" sortable></el-table-column>
        <el-table-column prop="imageType"  :label="$t('shopImageList.imageType')" sortable></el-table-column>
        <el-table-column prop="image"  :label="$t('shopImageList.image')" sortable></el-table-column>
        <el-table-column prop="imageSize"  :label="$t('shopImageList.imageSize')" sortable></el-table-column>
        <el-table-column prop="remarks" :label="$t('shopImageList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('shopImageList.operation')" width="160">
          <template scope="scope">
            <div class="action" v-permit="'crm:shopImage:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'edit')">{{$t('shopImageList.edit')}}</el-button></div>
            <div class="action" v-permit="'crm:shopImage:delete'"><el-button size="small" @click.native="itemAction(scope.row.id,'delete')">{{$t('shopImageList.delete')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import officeSelect from 'components/basic/office-select';
  import depotSelect from 'components/future/depot-select'
  export default {
    components:{officeSelect,depotSelect},
    data() {
      return {
        searchText:"",
        page:{},
        formData:{
          extra:{}
        },
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
        util.setQuery("shopImageList",submitData);
        axios.get('/api/ws/future/layout/shopImage',{params:submitData}).then((response) => {
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
        this.$router.push({ name: 'shopImageForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'shopImageForm', query: { id: id }})
        } else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/layout/shopImage/delete', {params: {id: id}}).then((response) => {
              this.$message(response.data.message);
              this.pageRequest();
            })
          })
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/layout/shopImage/getQuery').then((response) =>{
        this.formData=response.data;
        util.copyValue(this.$route.query,this.formData);
        this.pageRequest();
      });
    }
  };
</script>

