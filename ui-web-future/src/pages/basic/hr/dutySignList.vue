<template>
  <div>
    <head-tab active="dutySignList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('dutySignList.filterOrExport')}}</el-button>
        <search-tag  :formData="formData" :formLabel = "formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('dutySignList.filter')" v-model="formVisible" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="7">
            <el-col :span="12">
              <el-form-item :label="formLabel.dutyDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.dutyDate" type="daterange" align="right" :placeholder="$t('dutySignList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.employeeName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.employeeName" auto-complete="off" :placeholder="$t('dutySignList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.address.label" :label-width="formLabelWidth">
                <el-input v-model="formData.address" auto-complete="off" :placeholder="$t('dutySignList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="formLabel.officeName.label" :label-width="formLabelWidth">
                <el-select v-model="formData.officeName" filterable clearable :placeholder="$t('dutySignList.inputKey')">
                  <el-option v-for="office in formData.officeList" :key="office.name" :label="office.name" :value="office.name"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.positionName.label" :label-width="formLabelWidth">
                <el-select v-model="formData.positionName" filterable clearable :placeholder="$t('dutySignList.inputKey')">
                  <el-option v-for="position in formData.positionList" :key="position.name" :label="position.name" :value="position.name"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="exportData" icon="upload">{{$t('dutySignList.export')}}</el-button>
          <el-button type="primary" @click="search()">{{$t('dutySignList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('dutySignList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="employee.name" :label="$t('dutySignList.employeeName')" sortable></el-table-column>
        <el-table-column prop="dutyDate" :label="$t('dutySignList.dutyDate')"></el-table-column>
        <el-table-column prop="extendMap.week" :label="$t('dutySignList.week')" ></el-table-column>
        <el-table-column prop="dutyTime" :label="$t('dutySignList.dutyTime')" ></el-table-column>
        <el-table-column prop="address" :label="$t('dutySignList.address')" ></el-table-column>
        <el-table-column prop="uuid" :label="$t('dutySignList.uuid')" ></el-table-column>
        <el-table-column prop="netType" :label="$t('dutySignList.netType')" ></el-table-column>
        <el-table-column prop="attachment" :label="$t('dutySignList.attachment')" >
          <template scope="scope">
            <img :src="scope.row.attachment" width="100px" height="100px"/>
          </template>
        </el-table-column>
        <el-table-column prop="status" :label="$t('dutySignList.status')">
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
          dutyDate:'',
          dutyDateBTW:'',
        },
        submitData:{
          page:0,
          size:25,
          employeeName:'',
          address:'',
          officeName:'',
          positionName:'',
          dutyDate:'',
          dutyDateBTW:'',
        },
        formLabel:{
          dutyDateBTW:{label:this.$t('dutySignList.dutyDate')},
          employeeName:{label:this.$t('dutySignList.employeeName')},
          address:{label:this.$t('dutySignList.address')},
          officeName:{label:this.$t('dutySignList.officeName')},
          positionName:{label:this.$t('dutySignList.positionName')}
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
        util.getQuery("dutySignList");
        util.setQuery("dutySignList",this.formData);
        util.copyValue(this.formData,this.submitData);
        this.formData.dutyDateBTW = util.formatDateRange(this.formData.dutyDate);
        axios.get('/api/basic/hr/dutySign',{params:this.submitData}).then((response) => {
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
      },exportData(){
        this.formData.dutyDateBTW = util.formatDateRange(this.formData.dutyDate);
        window.location.href= "/api/basic/hr/dutySign/export?"+qs.stringify(this.formData);
			},getQuery(){
        axios.get('/api/basic/hr/dutySign/getQuery').then((response) =>{
          this.formData=response.data;
          this.pageRequest();
        });
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      this.getQuery();
      util.copyValue(this.$route.query,this.formData);
    }
  };
</script>

