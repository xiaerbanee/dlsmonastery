<template>
  <div>
    <head-tab :active="$t('positionList.positionList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'hr:position:edit'">{{$t('positionList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'hr:position:view'">{{$t('positionList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel = "formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('positionList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.positionName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.positionName" auto-complete="off" :placeholder="$t('positionList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.jobName.label" :label-width="formLabelWidth">
                <el-select v-model="formData.jobName" filterable clearable :placeholder="$t('positionList.inputKey')">
                  <el-option v-for="job in formProperty.jobList" :key="job.name" :label="job.name" :value="job.name"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('positionList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('positionList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="name" :label="$t('positionList.positionName')" sortable width="150"></el-table-column>
        <el-table-column prop="job.name" :label="$t('positionList.jobName')"></el-table-column>
        <el-table-column prop="dataScope" :label="$t('positionList.dataScope')" width="120">
          <template scope="scope">{{scope.row.dataScope | getEnumLabel('dataScope')}}</template>
        </el-table-column>
        <el-table-column prop="permission" :label="$t('positionList.permission')"></el-table-column>
        <el-table-column prop="locked" :label="$t('positionList.locked')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" :label="$t('positionList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('positionList.operation')" width="140">
          <template scope="scope">
            <div v-for="action in scope.row.actionList" :key="action" class="action">
              <el-button size="small" @click.native="itemAction(scope.row.id,action)">{{action}}</el-button>
            </div>
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
          pageNumber:0,
          pageSize:25,
          positionName:'',
          jobName:''
        },formLabel:{
          positionName:{label:this.$t('positionList.positionName')},
          jobName:{label:this.$t('positionList.jobName')}
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
        util.setQuery("positionList",this.formData);
        axios.get('/api/hr/position',{params:this.formData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.pageNumber = pageNumber;
        this.formData.pageSize = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getOrder(column);
        this.formData.pageNumber=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'positionForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'positionForm', query: { id: id }})
        } else if(action=="删除") {
          axios.get('/api/hr/position/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      axios.get('/api/hr/position/getListProperty').then((response) =>{
        this.formProperty=response.data;
      });
      this.pageRequest();
    }
  };
</script>

