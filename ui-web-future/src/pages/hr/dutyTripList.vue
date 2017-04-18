<template>
  <div>
    <head-tab :active="$t('dutyTripList.dutyTripList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('dutyTripList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel = "formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('dutyTripList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
              <el-form-item :label="formLabel.dutyDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.dutyDate" type="daterange" align="right" :placeholder="$t('dutyTripList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('dutyTripList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('dutyTripList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="dateStart" :label="$t('dutyTripList.dateStart')"></el-table-column>
        <el-table-column prop="dateEnd"  :label="$t('dutyTripList.dateEnd')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('dutyTripList.reason')"></el-table-column>
        <el-table-column prop="status" :label="$t('dutyTripList.status')">
          <template scope="scope">
            <el-tag :type="scope.row.status == '已通过' ? 'primary' : 'danger'">{{scope.row.status}}</el-tag>
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
          dutyDate:'',
          dutyDateBTW:'',
        },
        formLabel:{
          dutyDateBTW:{label:this.$t('dutyTripList.date')},
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
        util.getQuery("dutyTripList");
        util.setQuery("dutyTripList",this.formData);
        util.copyValue(this.formData,this.submitData);
        this.formData.dutyDateBTW = util.formatDateRange(this.formData.dutyDate);
        axios.get('/api/basic/hr/dutyTrip',{params:this.submitData}).then((response) => {
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
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.pageRequest();
    }
  };
</script>

