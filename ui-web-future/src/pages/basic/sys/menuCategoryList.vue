<template>
  <div>
    <head-tab active="menuCategoryList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus"  v-permit="'sys:menuCategory:edit'">{{$t('menuCategoryList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'sys:menuCategory:view'">{{$t('menuCategoryList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('menuCategoryList.filter')"  v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('menuCategoryList.name')" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('menuCategoryList.likeSearch')"></el-input>
              </el-form-item>
              <!--<el-form-item :label="$t('menuCategoryList.sort')" :label-width="formLabelWidth">-->
                <!--<el-input v-model="formData.sort" auto-complete="off" :placeholder="$t('menuCategoryList.likeSearch')"></el-input>-->
              <!--</el-form-item>-->
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('menuCategoryList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('menuCategoryList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="code" :label="$t('menuCategoryList.code')" sortable width="200"></el-table-column>
        <el-table-column prop="name" :label="$t('menuCategoryList.name')"></el-table-column>
        <el-table-column prop="backendModuleCode" :label="$t('menuCategoryList.backendModuleCode')"></el-table-column>
        <el-table-column prop="backendModuleName" :label="$t('menuCategoryList.backendModuleName')"></el-table-column>
        <el-table-column prop="sort" :label="$t('menuCategoryList.sort')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('menuCategoryList.remarks')"></el-table-column>
        <el-table-column prop="locked" :label="$t('menuCategoryList.locked')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('menuCategoryList.operation')" width="140">
          <template scope="scope">
              <el-button size="small" @click.native="itemAction(scope.row.id,'edit')" v-permit="'sys:menuCategory:edit'">修改</el-button>
              <el-button size="small" @click.native="itemAction(scope.row.id,'delete')" v-permit="'sys:menuCategory:delete'">删除</el-button>
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
        pageLoading: false,
        pageHeight:600,
        page:{},
        searchText:'',
        formData:{
          extra:{}
        },
        initPromise:{},
        formLabelWidth: '120px',
        formVisible: false
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
        util.setQuery("menuCategoryList",submitData);
        axios.get('/api/basic/sys/menuCategory?'+qs.stringify(submitData)).then((response) => {
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
      },itemAdd(){
        this.$router.push({ name: 'menuCategoryForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'menuCategoryForm', query: { id: id }})
        } else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
          axios.get('/api/basic/sys/menuCategory/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          });
        }).catch(()=>{});
        }
      }
    },created () {
        var that=this;
      that.pageHeight = window.outerHeight -320;
      this.initPromise=axios.get('/api/basic/sys/menuCategory/getQuery').then((response) =>{
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

