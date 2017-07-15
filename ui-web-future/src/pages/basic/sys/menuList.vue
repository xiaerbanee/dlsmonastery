<template>
  <div>
    <head-tab active="menuList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus"  v-permit="'sys:menu:edit'">{{$t('menuList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'sys:menu:view'">{{$t('menuList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible=false" :title="$t('menuList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" :label-width="formLabelWidth">
              <el-form-item :label="$t('menuList.category')" >
                <el-select v-model="formData.menuCategoryId" filterable clearable :placeholder="$t('menuList.inputKey')">
                  <el-option v-for="item in formData.extra.menuCategoryList"  :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('menuList.name')">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('menuList.likeSearch')"></el-input>
              </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('menuList.sure')}}</el-button>
        </div>
      </search-dialog>
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
              <el-button size="small" @click.native="itemAction(scope.row.id,'edit')" v-permit="'sys:menu:edit'">修改</el-button>
              <el-button size="small" @click.native="itemAction(scope.row.id,'delete')" v-permit="'sys:menu:delete'">删除</el-button>
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
        page:{},
        searchText:"",
        formData:{
          extra:{}
        },
        initPromise:{},
        formLabelWidth: '25%',
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
        var submitData = util.deleteExtra(this.formData);
        util.setQuery("menuList",submitData);
        axios.get('/api/basic/sys/menu?'+qs.stringify(submitData)).then((response) => {
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
        this.$router.push({ name: 'menuForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'menuForm', query: { id: id }})
        } else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
          axios.get('/api/basic/sys/menu/delete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
            this.pageRequest();
          });
        }).catch(()=>{});
        }
      }
    },created () {
      var that = this;
       this.pageHeight = 0.75*window.innerHeight;
      this.initPromise = axios.get('/api/basic/sys/menu/getQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(that.$route.query,that.formData);
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      })
    }
  };
</script>

