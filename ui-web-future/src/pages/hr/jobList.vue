<template>
  <div>
    <head-tab :active="$t('jobList.jobList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'hr:job:edit'">{{$t('jobList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'hr:job:view'">{{$t('jobList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel = "formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('jobList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.jobName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.jobName" auto-complete="off" :placeholder="$t('jobList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('jobList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('jobList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="name" :label="$t('jobList.jobName')" sortable></el-table-column>
        <el-table-column prop="permission" :label="$t('jobList.permission')" ></el-table-column>
        <el-table-column prop="locked" :label="$t('jobList.locked')">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" :label="$t('jobList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('jobList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" @click.native="itemAction(scope.row.id,'修改')">修改</el-button>             <el-button size="small" @click.native="itemAction(scope.row.id,'删除')">删除</el-button>
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
        formData:{
          page:0,
          size:25,
          jobName:''
        },formLabel:{
          jobName:{label:this.$t('jobList.jobName')}
        },
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("jobList",this.formData);
        axios.get('/api/hr/job',{params:this.formData}).then((response) => {
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
        this.$router.push({ name: 'jobForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'jobForm', query: { id: id }})
        } else if(action=="删除") {
          axios.get('/api/hr/job/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      this.pageRequest();
    }
  };
</script>

