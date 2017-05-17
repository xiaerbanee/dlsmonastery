<template>
  <div>
    <head-tab active="customerReceive"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">过滤</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog title="过滤" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="7">
            <el-col :span="12">
              <el-form-item :label="formLabel.dateRange.label" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.dateRange"></date-range-picker>
              </el-form-item>
              <el-form-item :label="formLabel.customerGroup.label" :label-width="formLabelWidth">
                <el-select v-model="formData.customerGroup" placeholder="请选择客户分组">
                  <el-option v-for="item in formData.customerGroupList" :key="item.value" :label="item.name" :value="item.value"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.customerIdList.label" :label-width="formLabelWidth">
                <el-select v-model="formData.customerIdList"  multiple filterable remote placeholder="请输入关键词" :remote-method="remoteCustomer" :loading="remoteLoading">
                  <el-option v-for="item in customers" :key="item.fnumber" :label="item.fname" :value="item.fnumber"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">搜索</el-button>
        </div>
      </el-dialog>
      <el-dialog v-model="detailVisible" size="large">
        <el-table :data="detail" :row-class-name="tableRowClassName" v-loading="detailLoading" element-loading-text="拼命加载中....." border>
          <el-table-column prop="billType" label="业务类型"></el-table-column>
          <el-table-column prop="billNo" label="单据编号"></el-table-column>
          <el-table-column prop="date" label="单据日期"></el-table-column>
          <el-table-column prop="materialName" label="商品名称"></el-table-column>
          <el-table-column prop="qty" label="数量"></el-table-column>
          <el-table-column prop="price" label="单价"></el-table-column>
          <el-table-column prop="shouldGet" label="金额"></el-table-column>
          <el-table-column prop="shouldGet" label="应收"></el-table-column>
          <el-table-column prop="realGet" label="实收"></el-table-column>
          <el-table-column prop="endShouldGet" label="期末"></el-table-column>
          <el-table-column prop="note" label="摘要"></el-table-column>
        </el-table>
      </el-dialog>
      <el-table :data="summary" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" element-loading-text="拼命加载中....." @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="customerGroupName" label="客户分组" sortable width="150"></el-table-column>
        <el-table-column prop="customerName" label="客户名称"></el-table-column>
        <el-table-column prop="beginShouldGet" label="期初应收"></el-table-column>
        <el-table-column prop="endShouldGet" label="期末应收"></el-table-column>
        <el-table-column fixed="right" label="操作" width="120">
          <template scope="scope">
            <el-button size="small" @click="detailAction(scope.row.customerId)">详细</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="customerPage" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<style>
  .el-table .detail-item1 {
    background: #c9e5f5;
  }

  .el-table .detail-error {
    background: #FF8888;
  }

  .el-table .detail-title {
    background: #FFEE99;
  }
  .el-table .detail-item2{
    background:#FFFFFF;
  }

</style>
<script>
  export default {
    data() {
      return {
        customerPage:{},
        customers:{},
        summary: [],
        detail: [],
        formData: {
          dateRange: [new Date().toDateString(),new Date().toDateString()],
          customerGroup:'',
          customerIdList:[],
          customerGroupList:[],
        },
        submitData: {
          page:0,
          size:25,
          sort:'t1.FCUSTID',
          dateRange: '',
          customerGroup:'',
          customerIdList:new Array(),
        },
        submitDetail: {
          dateRange: '',
          customerId: '',
        },
        formLabel:{
          dateRange:{label:"日期"},
          customerGroup:{label:"客户分组",value:''},
          customerIdList:{label:"客户名称",value:''}
        },
        formLabelWidth: '120px',
        remoteLoading:false,
        formVisible: false,
        detailVisible:false,
        pageLoading: false,
        detailLoading:false,
        pageHeight:'',
      };
    },
    methods: {
      pageRequest() {
        var that = this;
        that.pageLoading = true;
        util.getQuery("customerReceive");
        util.setQuery("customerReceive",that.formData);
        that.formData.dateRange = util.formatDateRange(that.formData.dateRange);
        util.copyValue(that.formData,that.submitData);
        axios.get('/api/global/cloud/kingdee/bdCustomer?'+qs.stringify(that.submitData)).then((response) => {
            that.customerPage = response.data;
            let customerIdList = new Array();
            let customers = response.data.content;
            for (let item in customers){
              customerIdList.push(customers[item].fcustId);
            }
            that.submitData.customerIdList = customerIdList;
            if(that.submitData.customerIdList.length !== 0){
              console.log(that.submitData.customerIdList);
              axios.get('/api/global/cloud/report/customerReceive/list?'+qs.stringify(that.submitData)).then((response) => {
                this.summary = response.data;
              });
            }
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.sort=util.getSort(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },detailAction:function(customerId){
        this.detailLoading = true;
        if(customerId !== null) {
          util.copyValue(this.formData,this.submitDetail);
          this.submitDetail.customerId = customerId;
          axios.get('/api/global/cloud/report/customerReceive/detail',{params:this.submitDetail}).then((response) =>{
            this.detail = response.data;
            this.detailLoading = false;
            this.detailVisible = true;
          })
        }
      },tableRowClassName(row, index) {
        if (row.index === -2) { //head
          return "detail-item2";
        } else if (row.index === -3) { //error
          return "detail-error";
        } else if (row.index === -1) {
          return "detail-title";
        } else if (row.index / 2 === 0) {
          return "detail-item1";
        } else if (row.index / 2 !== 0) {
          return "detail-item2";
        }
      },remoteCustomer(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/global/cloud/kingdee/bdCustomer/getByNameLike',{params:{name:query}}).then((response)=>{
            this.customers = response.data;
            console.log(this.customers);
            this.remoteLoading = false;
          })
        } else {
          this.customers = {};
        }
      },
    },created () {
      this.pageHeight = window.outerHeight -320;
      axios.get('/api/global/cloud/kingdee/bdCustomer/findCustomerGroupList').then((response) => {
        this.formData.customerGroupList = response.data;
      });
      this.pageRequest();
    }
  };
</script>

