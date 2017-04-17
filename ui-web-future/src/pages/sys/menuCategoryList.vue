<template>
  <div>
    <head-tab :active="$t('menuCategoryList.menuCategoryList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus"  v-permit="'sys:menuCategory:edit'">{{$t('menuCategoryList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'sys:menuCategory:view'">{{$t('menuCategoryList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('menuCategoryList.filter')"  v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('menuCategoryList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.sort.label" :label-width="formLabelWidth">
                <el-input v-model="formData.sort" auto-complete="off" :placeholder="$t('menuCategoryList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('menuCategoryList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('menuCategoryList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="name" :label="$t('menuCategoryList.name')" sortable width="150"></el-table-column>
        <el-table-column prop="sort" :label="$t('menuCategoryList.sort')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('menuCategoryList.remarks')"></el-table-column>
        <el-table-column prop="locked" :label="$t('menuCategoryList.locked')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('menuCategoryList.operation')" width="140">
          <template scope="scope">
              <el-button size="small" @click.native="itemAction(scope.row.id,'修改')">修改</el-button>
              <el-button size="small" @click.native="itemAction(scope.row.id,'删除')">删除</el-button>
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
        formData:{},
        submitData:{
          page:0,
          size:25,
          name:"",
          sort:''
        },formLabel:{
          name:{label:this.$t('menuCategoryList.name')},
          sort:{label:this.$t('menuCategoryList.sort')}
        },
        formLabelWidth: '120px',
        formVisible: false,
        loading:false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.getQuery("menuCategoryList");
        util.setQuery("menuCategoryList",this.formData);
        util.copyValue(this.formData,this.submitData);
        axios.get('/api/basic/sys/menuCategory',{params:this.submitData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getOrder(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'menuCategoryForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'menuCategoryForm', query: { id: id }})
        } else if(action=="删除") {
          axios.get('/api/basic/sys/menuCategory/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.pageRequest();
    }
  };
</script>

