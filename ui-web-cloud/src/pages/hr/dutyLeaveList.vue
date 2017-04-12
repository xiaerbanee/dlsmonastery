<template>
  <div>
    <head-tab :active="$t('dutyLeaveList.dutyLeaveList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('dutyLeaveList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel = "formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('dutyLeaveList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
              <el-form-item :label="formLabel.dutyDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.dutyDate" type="daterange" align="right" :placeholder="$t('dutyLeaveList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.leaveType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.leaveType" filterable clearable :placeholder="$t('dutyLeaveList.inputKey')">
                  <el-option v-for="item in formProperty.leaveList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.dateType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.dateType" filterable clearable :placeholder="$t('dutyLeaveList.inputKey')">
                  <el-option v-for="item in formProperty.dateList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('dutyLeaveList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('dutyLeaveList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="dutyDate" :label="$t('dutyLeaveList.dutyDate')"></el-table-column>
        <el-table-column prop="dateType"  :label="$t('dutyLeaveList.dateType')" ></el-table-column>
        <el-table-column prop="leaveType"  :label="$t('dutyLeaveList.leaveType')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('dutyLeaveList.remarks')"></el-table-column>
        <el-table-column prop="status"  :label="$t('dutyLeaveList.status')">
          <template scope="scope">
            <el-tag :type="scope.row.status == '申请中' ? 'primary' : 'danger'">{{scope.row.status}}</el-tag>
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
           dutyDate:'',
          dutyDateBTW:'',
          leaveType:'',
          dateType:''
        },
        formLabel:{
          dutyDateBTW:{label:this.$t('dutyLeaveList.dutyDate')},
          leaveType:{label:this.$t('dutyLeaveList.leaveType')},
          dateType:{label:this.$t('dutyLeaveList.dateType')}
        },
        pickerDateOption:util.pickerDateOption,
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("dutyLeaveList",this.formData);
         this.formData.dutyDateBTW = util.formatDateRange(this.formData.dutyDate);
        axios.get('/api/hr/dutyLeave',{params:this.formData}).then((response) => {
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
      },getListProperty(){
        axios.get('/api/hr/dutyLeave/getListProperty').then((response) =>{
          this.formProperty=response.data;
          this.pageRequest();
        });
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.getListProperty();
    }
  };
</script>

