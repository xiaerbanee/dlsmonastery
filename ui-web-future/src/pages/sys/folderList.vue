<template>
  <div>
    <head-tab :active="$t('folderList.folderList') "></head-tab>
    <div>
      <el-table :data="page" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('folderList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="name" :label="$t('folderList.name')" sortable ></el-table-column>
        <el-table-column prop="locked" :label="$t('folderList.locked')">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" :label="$t('folderList.enabled')">
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'primary' : 'danger'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" :label="$t('folderList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('folderList.operation')" width="140">
          <template scope="scope">
              <el-button size="small" @click.native="itemAction(scope.row.id,'修改')">修改</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        pageLoading: false,
        page:[],
        submitData:{
          page:0,
          size:25,
          name:''
        },formLabel:{
          name:{label:this.$t('folderList.name')}
        },
        formData:{},
        formLabelWidth: '120px',
        formVisible: false,
        loading:false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("folderList",this.formData);
        util.copyValue(this.formData,this.submitData);
        axios.get('/api/basic/sys/folder',{params:this.submitData}).then((response) => {
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
      },itemAdd(){
        this.$router.push({ name: 'folderForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'folderForm', query: { id: id }})
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.pageRequest();
    }
  };
</script>

