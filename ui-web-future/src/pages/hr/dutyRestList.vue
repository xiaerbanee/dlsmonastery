<template>
  <div>
    <head-tab :active="$t('dutyRestList.dutyRestList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('dutyRestList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel = "formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('dutyRestList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
              <el-form-item :label="formLabel.dutyDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.dutyDate" type="daterange" align="right" :placeholder="$t('dutyRestList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.type.label" :label-width="formLabelWidth">
                <el-select v-model="formData.type" filterable clearable :placeholder="$t('dutyRestList.inputKey')">
                  <el-option v-for="item in formProperty.restList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.dateType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.dateType" filterable clearable :placeholder="$t('dutyRestList.inputKey')">
                  <el-option v-for="item in formProperty.dateList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('dutyRestList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('dutyRestList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="dutyDate" :label="$t('dutyRestList.dutyDate')"></el-table-column>
        <el-table-column prop="type":label="$t('dutyRestList.type')" ></el-table-column>
        <el-table-column prop="dateType" :label="$t('dutyRestList.dateType')"></el-table-column>
        <el-table-column prop="timeStart" :label="$t('dutyRestList.timeStart')"></el-table-column>
        <el-table-column prop="timeEnd" :label="$t('dutyRestList.timeEnd')" ></el-table-column>
        <el-table-column prop="hour" :label="$t('dutyRestList.hour')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('dutyRestList.dutyRestRemarks')"></el-table-column>
        <el-table-column prop="status" :label="$t('dutyRestList.status')">
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
          page:0,
          size:25,
           dutyDate:'',
          dutyDateBTW:'',
          type:'',
          dateType:''
        },
        formLabel:{
          dutyDateBTW:{label:this.$t('dutyRestList.dutyDate')},
          type:{label:this.$t('dutyRestList.type')},
          dateType:{label:this.$t('dutyRestList.dateType')}
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
        util.setQuery("dutyRestList",this.formData);
         this.formData.dutyDateBTW = util.formatDateRange(this.formData.dutyDate);
        axios.get('/api/basic/hr/dutyRest',{params:this.formData}).then((response) => {
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
      },getListProperty(){
      axios.get('/api/basic/hr/dutyRest/getListProperty').then((response) =>{
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

