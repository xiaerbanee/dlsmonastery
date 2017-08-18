<template>
  <div>
    <head-tab active="folderList"></head-tab>
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
              <el-button size="small" @click.native="itemAction(scope.row.id,'edit')" v-permit="'sys:folder:edit'">{{$t('folderList.edit')}}</el-button>
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
        formData:{
            extra:{}
        },
        initPromise:{},
        formLabelWidth: '120px',
        formVisible: false,
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        var submitData = util.deleteExtra(this.formData);
        axios.get('/api/general/sys/folder?'+qs.stringify(submitData)).then((response) => {
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
      },itemAdd(){
        this.$router.push({ name: 'folderForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'folderForm', query: { id: id }})
        }
      }
    },created () {
       this.pageHeight = 0.74*window.innerHeight;
      this.pageRequest();
    }
  };
</script>

