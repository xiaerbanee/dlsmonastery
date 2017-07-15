<template>
  <div>
    <head-tab active="carrierProductList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'api:carrierProduct:edit'">{{$t('carrierProductList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'api:carrierProduct:view'">{{$t('carrierProductList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible=false" :title="$t('carrierShopList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" :label-width="formLabelWidth">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('carrierProductList.name')" >
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('carrierShopList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">确定</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('carrierProductList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="name" label="商城名称" sortable width="200"></el-table-column>
        <el-table-column prop="productName" label="系统名称"></el-table-column>
        <el-table-column prop="createdByName" :label="$t('carrierProductList.createdBy')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('carrierProductList.remarks')"></el-table-column>
        <el-table-column prop="locked" :label="$t('carrierProductList.locked')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('carrierProductList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" @click.native="itemAction(scope.row.id,'edit')" v-permit="'api:carrierProduct:edit'">{{$t('carrierProductList.edit')}}</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'delete')" v-permit="'api:carrierProduct:delete'">{{$t('carrierProductList.delete')}}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  export default{
    data() {
      return {
        page:{},
        searchText:"",
        formData:{
          extra:{}
        },
        initPromise:{},
        formVisible: false,
        pageLoading: false,
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
        var submitData = util.deleteExtra(this.formData);
        util.setQuery("carrierProductList",submitData);
        axios.get('/api/ws/future/api/carrierProduct?'+qs.stringify(submitData)).then((response) => {
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
        this.$router.push({ name: 'carrierProductForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'carrierProductForm', query: { id: id }})
        } else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/api/carrierProduct/delete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
              this.pageRequest();
            });
          }).catch(()=>{});
        }
      }
    },created () {
      var that = this;
      that.pageHeight = window.outerHeight -320;
      this.initPromise = axios.get('/api/ws/future/api/carrierProduct/getQuery').then((response) =>{
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

