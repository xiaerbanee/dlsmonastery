<template>
  <div>
    <head-tab active="bankList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus">{{$t('dutyAnnualList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('dutyAnnualList.filter')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel = "formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('bankList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('dutyAnnualList.likeSearch')"></el-input>
              </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('dutyAnnualList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('dutyAnnualList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="officeName" :label="$t('dutyAnnualList.officeName')" ></el-table-column>
        <el-table-column prop="positionName" :label="$t('dutyAnnualList.positionName')" ></el-table-column>
        <el-table-column prop="employeeName" :label="$t('dutyAnnualList.employeeName')"></el-table-column>
        <el-table-column prop="annualYear" :label="$t('dutyAnnualList.annualYear')"></el-table-column>
        <el-table-column prop="hour" :label="$t('dutyAnnualList.hour')" ></el-table-column>
        <el-table-column prop="leftHour" :label="$t('dutyAnnualList.leftHour')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('dutyAnnualList.createdDate')"></el-table-column>
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
        },
        submitData:{
          page:0,
          size:25,
          name:''
        },
        formLabel:{
          name:{label:this.$t('dutyAnnualList.employeeName')}
        },
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("dutyAnnualList",this.submitData);
        util.copyValue(this.formData,this.submitData);
        axios.get('/api/basic/hr/dutyAnnual?'+qs.stringify(this.submitData)).then((response) => {
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
        this.$router.push({ name: 'dutyAnnualForm'})
      },
    },created () {
      this.pageHeight = window.outerHeight -320;
      this.pageRequest();
    }
  };
</script>

