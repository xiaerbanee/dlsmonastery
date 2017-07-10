<template>
  <div>
    <head-tab active="roleList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" >添加</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" >过滤</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" title="过滤" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" :label-width="formLabelWidth">
              <el-form-item :label="$t('roleList.name')">
                <el-input v-model="formData.name" auto-complete="off" ></el-input>
              </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">确定</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" element-loading-text="数据加载中" @sort-change="sortChange" stripe border>
        <el-table-column  prop="name" label="名称" ></el-table-column>
        <el-table-column prop="permission" label="权限"></el-table-column>
        <el-table-column prop="locked" label="锁定" >
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" label="备注"></el-table-column>
        <el-table-column prop="createdByName" label="创建人"></el-table-column>
        <el-table-column prop="createdDate" label="创建时间"></el-table-column>
        <el-table-column fixed="right" label="操作" width="140">
          <template scope="scope">
            <div class="action"> <el-button size="small" @click.native="itemAction(scope.row.id,'edit')" v-permit="'sys:role:edit'">修改模块</el-button></div>
            <div class="action"> <el-button size="small" @click.native="itemAction(scope.row.id,'delete')" v-permit="'sys:role:delete'">删除</el-button></div>
            <div class="action"> <el-button size="small" @click.native="itemAction(scope.row.id,'roleAuthority')" v-permit="'sys:role:edit'">修改</el-button></div>
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
        page:{},
        searchText:"",
        formData:{
          extra:{}
        },
        initPromise:{},
        formLabelWidth: '25%',
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
        util.setQuery("roleList",submitData);
        axios.get('/api/basic/sys/role?'+qs.stringify(submitData)).then((response) => {
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
        this.$router.push({ name: 'roleForm'})
      },itemEdit(){
        this.$router.push({ name: 'roleEdit'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'roleForm', query: { id: id }})
        } else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
          axios.get('/api/basic/sys/role/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          });
        }).catch(()=>{});
        }else if(action=="roleAuthority"){
          this.$router.push({name:"roleAuthorityForm",query:{id:id}})
        }
      }
    },created () {
        var that=this;
      that.pageHeight = window.outerHeight -320;
      this.initPromise = axios.get('/api/basic/sys/role/getQuery').then((response) =>{
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

