<template>
  <div>
    <head-tab :active="$t('accountList.accountList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'hr:account:edit'">{{$t('accountList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'hr:account:view'">{{$t('accountList.filterOrExport')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('accountList.filter')" v-model="formVisible" size="small" class="search-form">
        <el-form :model="formData" method="get" >
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.loginName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.loginName" auto-complete="off" :placeholder="$t('accountList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.employeeName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.employeeName" auto-complete="off" :placeholder="$t('accountList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.officeName.label"  :label-width="formLabelWidth">
                <el-select v-model="formData.officeName"  clearable filterable remote :placeholder="$t('accountList.inputWord')" :remote-method="remoteOffice" :loading="remoteLoading">
                  <el-option v-for="office in offices" :key="office.id" :label="office.name" :value="office.name"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.leaderName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.leaderName" auto-complete="off" :placeholder="$t('accountList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.positionName.label" :label-width="formLabelWidth">
                <el-select v-model="formData.positionName" clearable filterable :placeholder="$t('accountList.selectGroup')">
                  <el-option v-for="position in formProperty.position" :key="position.name" :label="position.name" :value="position.name"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="exportData">{{$t('accountList.export')}}</el-button>
          <el-button type="primary" @click="search()">{{$t('accountList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('accountList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('accountList.id')" sortable width="150"></el-table-column>
        <el-table-column prop="type" :label="$t('accountList.type')"></el-table-column>
        <el-table-column prop="loginName" :label="$t('accountList.loginName')"></el-table-column>
        <el-table-column prop="employeeName" :label="$t('accountList.employeeName')"></el-table-column>
        <el-table-column prop="leaderName" sortable :label="$t('accountList.leader')" width="120"></el-table-column>
        <el-table-column prop="officeName" :label="$t('accountList.officeName')"></el-table-column>
        <el-table-column prop="dataOfficeName" :label="$t('accountList.dataScopeOffice')"></el-table-column>
        <el-table-column prop="positionName" :label="$t('accountList.positionName')"></el-table-column>
        <el-table-column prop="dataScopeLabel" :label="$t('accountList.positionDataScope')" width="120"></el-table-column>
        <el-table-column prop="viewReport" :label="$t('accountList.viewReport')">
          <template scope="scope">
            <el-tag :type="scope.row.viewReport ? 'primary' : 'danger'">{{scope.row.viewReport | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="employeeStatus" :label="$t('accountList.employeeStatus')"></el-table-column>
        <el-table-column fixed="right" :label="$t('accountList.operation')" width="140">
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
        page:{},
        formData:{
          page:0,
          size:25,
          loginName:'',
          employeeName:'',
          officeName:'',
          leaderName:'',
          positionName:''
        },formLabel:{
          loginName:{label:this.$t('accountList.loginName')},
          employeeName:{label:this.$t('accountList.employeeName')},
          officeName:{label:this.$t('accountList.officeName')},
          leaderName:{label:this.$t('accountList.leader')},
          positionName:{label:this.$t('accountList.positionName')}
        },
        offices:[],
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
        remoteLoading:false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("accountList",this.formData);
        axios.get('/api/basic/hr/account',{params:this.formData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        if(column.prop=="leader.loginName") {
          column.sort = "leaderId";
        }
        this.formData.order=util.getOrder(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'accountForm'})
      },exportData(){
				window.location.href= "/api/basic/hr/account/export?"+qs.stringify(this.formData);
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'accountForm', query: { id: id }})
        } else if(action=="删除") {
          axios.get('/api/basic/hr/account/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      },remoteOffice(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/basic/hr/office/search',{params:{name:query}}).then((response)=>{
            this.offices=response.data;
            this.remoteLoading = false;
          })
        } else {
          this.offices = [];
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      axios.get('/api/basic/hr/account/getQuery').then((response) =>{
        this.formProperty=response.data;
      });
      this.pageRequest();
    }
  };
</script>

