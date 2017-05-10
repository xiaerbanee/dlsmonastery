<template>
  <div>
    <head-tab active="dutyLeaveList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('dutyLeaveList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel = "formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('dutyLeaveList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
              <el-form-item :label="formLabel.dutyDate.label" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.dutyDate"></date-range-picker>
              </el-form-item>
              <el-form-item :label="formLabel.leaveType.label" :label-width="formLabelWidth">
                <dict-enum-select v-model="formData.dateType" category="请假类型" />
              </el-form-item>
              <el-form-item :label="formLabel.dateType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.leaveType" filterable clearable :placeholder="$t('dutyLeaveList.inputKey')">
                  <el-option v-for="item in formData.leaveList" :key="item" :label="item" :value="item"></el-option>
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
        <el-table-column prop="leaveType" :label="$t('dutyLeaveList.leaveType')"></el-table-column>
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
  import dictEnumSelect from 'components/basic/dict-enum-select'
  export default {
    components:{
      dictEnumSelect
    },
    data() {
      return {
        page:{},
        formData:{
          dutyDate:'',
        },
        submitData:{
          page:0,
          size:25,
          dutyDate:'',
          leaveType:'',
          dateType:''
        },
        formLabel:{
          dutyDate:{label:this.$t('dutyLeaveList.dutyDate')},
          leaveType:{label:this.$t('dutyLeaveList.leaveType')},
          dateType:{label:this.$t('dutyLeaveList.dateType')}
        },
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.copyValue(this.formData,this.submitData);
        util.setQuery("dutyLeaveList",this.submitData);
        axios.get('/api/basic/hr/dutyLeave?'+qs.stringify(this.submitData)).then((response) => {
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
      },getQuery(){
        axios.get('/api/basic/hr/dutyLeave/getQuery').then((response) =>{
          this.formData=response.data;
          util.copyValue(this.$route.query,this.formData);
          this.pageRequest();
        });
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      this.getQuery();
    }
  };
</script>

