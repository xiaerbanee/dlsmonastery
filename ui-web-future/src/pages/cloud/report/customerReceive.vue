<template>
  <div>
    <head-tab active="customerReceive"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">过滤</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog title="过滤" v-model="formVisible" size="small" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="8">
            <el-form-item label="日期范围" :label-width="formLabelWidth">
              <el-col :span="7">
                <date-picker placeholder="选择开始日期" v-model="formData.dateStart" style="width: 50px"></date-picker>
              </el-col>
              <el-col class="line" :span="1">-</el-col>
              <el-col :span="7">
                <date-picker placeholder="选择截止日期" v-model="formData.dateEnd" style="width: 50px"></date-picker>
              </el-col>
            </el-form-item>
          </el-row>
          <el-row :gutter="4">
              <el-form-item label="客户分组" :label-width="formLabelWidth">
                <el-select v-model="formData.customerGroup" placeholder="请选择客户分组">
                  <el-option v-for="item in formData.extra.customerGroupList" :key="item.value" :label="item.name" :value="item.value"></el-option>
                </el-select>
              </el-form-item>
          </el-row>
          <el-row :gutter="4">
              <el-form-item label="客户名称" :label-width="formLabelWidth">
                <el-select v-model="formData.customerIdList"  multiple filterable remote placeholder="请输入关键词" :remote-method="remoteCustomer" :loading="remoteLoading">
                  <el-option v-for="item in customers" :key="item.fnumber" :label="item.fname" :value="item.fnumber"></el-option>
                </el-select>
              </el-form-item>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">搜索</el-button>
        </div>
      </search-dialog>
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
          extra:{},
          customerIdList:[],
        },
        searchText:"",
        initPromise:{},
        submitDetail: {
          customerId: '',
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
      setSearchText() {
        this.$nextTick(function () {
          this.searchText = util.getSearchText(this.$refs.searchDialog);
        })
      },
      pageRequest() {
        this.pageLoading = true;
        this.setSearchText();
        let submitData = util.deleteExtra(this.formData);
        util.setQuery("customerReceive", submitData);
          axios.get('/api/global/cloud/kingdee/bdCustomer?'+ qs.stringify(submitData)).then((response) => {
            let customerIdList = new Array();
            let customers = response.data.content;
            for (let item in customers) {
              customerIdList.push(customers[item].fcustId);
            }
            this.formData.customerIdList = customerIdList;
            if (this.formData.customerIdList.length !== 0) {
              let submitData = util.deleteExtra(this.formData);
              axios.post('/api/global/cloud/report/customerReceive/list', qs.stringify(submitData, {allowDots:true})).then((response) => {
                this.summary = response.data;
                this.summary = response.data;
              });
            }
            this.customerPage = response.data;
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
          axios.get('/api/global/cloud/kingdee/bdCustomer/findByNameLike',{params:{name:query}}).then((response)=>{
            this.customers = response.data;
            console.log(this.customers);
            this.remoteLoading = false;
          })
        } else {
          this.customers = {};
        }
      },
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    },created () {
      let that = this;
      that.pageHeight = window.outerHeight -320;
      that.initPromise = axios.get('/api/global/cloud/report/customerReceive/getQuery').then((response) =>{
        this.formData = response.data;
        util.copyValue(that.$route.query,that.formData);
      });
    }
  };
</script>

