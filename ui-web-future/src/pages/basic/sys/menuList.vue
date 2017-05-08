<template>
  <div>
    <head-tab active="menuList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus"  v-permit="'sys:menu:edit'">{{$t('menuList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'sys:menu:view'">{{$t('menuList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('menuList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.menuCategoryId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.menuCategoryId" filterable clearable>
                  <el-option v-for="item in formData.menuCategoryList" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.category.label" :label-width="formLabelWidth">
                <el-select v-model="formData.category" filterable clearable :placeholder="$t('menuList.inputKey')">
                  <el-option v-for="category in formData.categoryList"  :key="category" :label="category" :value="category"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('menuList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('menuList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('menuList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('menuList.id')" sortable width="150"></el-table-column>
        <el-table-column prop="name" :label="$t('menuList.name')" ></el-table-column>
        <el-table-column prop="code" label="code" ></el-table-column>
        <el-table-column prop="menuCategoryName" :label="$t('menuList.menuCategoryName')" ></el-table-column>
        <el-table-column prop="sort" :label="$t('menuList.sort')" ></el-table-column>
        <el-table-column prop="locked" :label="$t('menuList.locked')" width="100">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" :label="$t('menuList.enabled')" width="100">
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'primary' : 'danger'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="mobile" :label="$t('menuList.mobile')" width="100">
          <template scope="scope">
            <el-tag :type="scope.row.mobile ? 'primary' : 'danger'">{{scope.row.mobile | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" :label="$t('menuList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('menuList.operation')" width="140">
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
          menuCategoryId:'',
          category:"",
          name:''
        },formLabel:{
          menuCategoryId:{label:this.$t('menuList.menuCategoryId')},
          category:{label:this.$t('menuList.category')},
          name:{label:this.$t('menuList.name')}
        },
        formLabelWidth: '120px',
        formVisible: false,
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("menuList",this.submitData);
        util.copyValue(this.formData,this.submitData);
        axios.get('/api/basic/sys/menu?'+qs.stringify(this.submitData)).then((response) => {
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
        this.$router.push({ name: 'menuForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'menuForm', query: { id: id }})
        } else if(action=="删除") {
          axios.get('/api/basic/sys/menu/delete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
            this.pageRequest();
          })
        }
      }
    },created () {
      var that = this;
      this.pageHeight = window.outerHeight -320;
      axios.get('/api/basic/sys/menu/getQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(that.$route.query,that.formData);
        that.pageRequest();
      });
    }
  };
</script>

