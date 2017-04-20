<template>
  <div>
    <head-tab active="employeeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'hr:employee:edit'">{{$t('employeeList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'hr:employee:view'">{{$t('employeeList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('employeeList.filter')" v-model="formVisible" size="large" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('employeeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.leaderName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.leaderName" auto-complete="off" :placeholder="$t('employeeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.mobilePhone.label" :label-width="formLabelWidth">
                <el-input v-model="formData.mobilePhone" auto-complete="off" :placeholder="$t('employeeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.positionId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.positionId" clearable filterable :placeholder="$t('employeeList.selectGroup')">
                  <el-option v-for="item in formData.positionList" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="formLabel.status.label" :label-width="formLabelWidth">
                <el-select v-model="formData.status" clearable filterable :placeholder="$t('employeeList.selectGroup')">
                  <el-option v-for="item in formData.statusList"  :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.officeId.label"  :label-width="formLabelWidth">
                <el-select v-model="formData.officeId" filterable remote :placeholder="$t('employeeList.inputWord')" :remote-method="remoteOffice" :loading="remoteLoading" :clearable=true>
                  <el-option v-for="office in offices" :key="office.id" :label="office.name" :value="office.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.entryDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.entryDate" type="daterange" align="right" :placeholder="$t('employeeList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.regularDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.regularDate" type="daterange" align="right" :placeholder="$t('employeeList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.leaveDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.leaveDate" type="daterange" align="right" :placeholder="$t('employeeList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('employeeList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('employeeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="name" :label="$t('employeeList.employeeName')" sortable></el-table-column>
        <el-table-column prop="sex" :label="$t('employeeList.sex')"></el-table-column>
        <el-table-column prop="officeName" :label="$t('employeeList.officeName')"></el-table-column>
        <el-table-column prop="positionName" :label="$t('employeeList.positionName')"></el-table-column>
        <el-table-column prop="leaderName" :label="$t('employeeList.leader')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('employeeList.createdDate')"></el-table-column>
        <el-table-column prop="entryDate" :label="$t('employeeList.entryDate')"></el-table-column>
        <el-table-column prop="leaveDate" :label="$t('employeeList.leaveDate')"></el-table-column>
        <el-table-column prop="status" :label="$t('employeeList.status')"></el-table-column>
        <el-table-column prop="mobilePhone" :label="$t('employeeList.mobilePhone')"></el-table-column>
        <el-table-column :label="$t('employeeList.operation')" width="140">
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
        formData:{},
        submitData:{
          page:0,
          size:25,
          name:'',
          leaderName:'',
          mobilePhone:'',
          officeId:'',
          positionId:'',
          status:'',
          entryDate:'',
          entryDateBTW:'',
          regularDate:'',
          regularDateBTW:'',
          leaveDate:'',
          leaveDateBTW:''
        },formLabel:{
          name:{label:this.$t('employeeList.employeeName')},
          leaderName:{label:this.$t('employeeList.leader')},
          mobilePhone:{label:this.$t('employeeList.mobilePhone')},
          officeId:{label:this.$t('employeeList.officeName'),value:''},
          positionId:{label:this.$t('employeeList.positionName'),value:''},
          status:{label:this.$t('employeeList.status')},
          entryDateBTW:{label:this.$t('employeeList.entryDate')},
          regularDateBTW:{label:this.$t('employeeList.regularDate')},
          leaveDateBTW:{label:this.$t('employeeList.leaveDate')}
        },
        pickerDateOption:util.pickerDateOption,
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
        remoteLoading:false,
        offices:[]
    };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formData.entryDateBTW=util.formatDateRange(this.formData.entryDate);
        this.formData.regularDateBTW=util.formatDateRange(this.formData.regularDate);
        this.formData.leaveDateBTW=util.formatDateRange(this.formData.leaveDate);
        this.formLabel.officeId.value=util.getLabel(this.offices,this.formData.officeId);
        this.formLabel.positionId.value=util.getLabel(this.formData.positionList,this.formData.positionId);
        util.getQuery("employeeList");
        util.setQuery("employeeList",this.formData);
        util.copyValue(this.formData,this.submitData);
        axios.get('/api/basic/hr/employee',{params:this.submitData}).then((response) => {
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
        this.$router.push({ name: 'employeeForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'employeeForm', query: { id: id }})
        } else if(action=="删除") {
          axios.get('/api/basic/hr/employee/delete',{params:{id:id}}).then((response) =>{
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
        }
      }
    },created () {
      var that = this;
      that.formData = that.submitData;
      that.pageHeight = window.outerHeight -320;
      axios.get('/api/basic/hr/employee/getQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(that.$route.query,that.formData);
        that.pageRequest();
    });
      axios.get('/api/basic/sys/menu/getMenuMap').then((response) =>{

    });
    }
  };
</script>

