<template>
  <div>
    <head-tab active="processTypeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'sys:processType:edit'">{{$t('processTypeList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'sys:processType:view'">{{$t('processTypeList.filter')}}</el-button>
        <span  v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('processTypeList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('processTypeList.name')" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('processTypeList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('processTypeList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('processTypeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('processTypeList.id')" sortable ></el-table-column>
        <el-table-column prop="name" :label="$t('processTypeList.name')"sortable ></el-table-column>
        <el-table-column prop="locked" :label="$t('processTypeList.locked')" >
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" :label="$t('processTypeList.enabled')" width="100">
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'primary' : 'danger'">{{!scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('processTypeList.operation')" width="140">
          <template scope="scope">
              <el-button size="small" @click.native="itemAction(scope.row.id,'详细')" v-permit="'sys:processType:view'">详细</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'删除')" v-permit="'sys:processType:delete'">删除</el-button>
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
        searchText:"",
        formData:{
          extra:{}
        },
        detailFormData:{},
        formLabelWidth: '120px',
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
        util.setQuery("processTypeList",submitData);
        axios.get('/api/general/sys/processType?'+qs.stringify(submitData)).then((response) => {
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
        this.$router.push({ name: 'processTypeForm'})
      },itemAction:function(id,action){
        if(action=="详细") {
          this.$router.push({ name: 'processTypeForm', query: { id: id }})
        } else if(action=="删除") {
          axios.get('/api/basic/sys/processType/delete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
            this.pageRequest();
          })
        }
      }
    },created () {
      var that = this;
      that.pageHeight = window.outerHeight - 320;
      axios.get('/api/basic/sys/processType/getQuery').then((response) => {
        that.formData = response.data;
        util.copyValue(that.$route.query, that.formData);
        that.pageRequest();
      });
    }
  };
</script>

