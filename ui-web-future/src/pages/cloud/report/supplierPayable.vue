<template>
  <div>
    <head-tab active="supplierPayable"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true">过滤&导出</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" title="过滤" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" :label-width="formLabelWidth">
          <el-row :gutter="7">
            <el-col :span="24">
              <el-form-item label="开始日期" >
                <date-picker placeholder="选择开始日期" v-model="formData.dateStart"></date-picker>
              </el-form-item>
              <el-form-item label="截止日期" >
                <date-picker placeholder="选择截止日期" v-model="formData.dateEnd"></date-picker>
              </el-form-item>
              <el-form-item label="供应商名称" >
                <el-select v-model="formData.supplierIdList"  multiple filterable remote placeholder="请输入关键词" :remote-method="remoteSupplier" :loading="remoteLoading">
                  <el-option v-for="item in suppliers" :key="item.fsupplierId" :label="item.fname" :value="item.fsupplierId"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()" icon="search">搜索</el-button>
          <el-button type="primary" @click="exportData()" icon="upload">导出</el-button>
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
          <el-table-column prop="amount" label="金额"></el-table-column>
          <el-table-column prop="payableAmount" label="应付"></el-table-column>
          <el-table-column prop="actualPayAmount" label="实付"></el-table-column>
          <el-table-column prop="endAmount" label="期末"></el-table-column>
          <el-table-column prop="note" label="摘要"></el-table-column>
        </el-table>
      </el-dialog>
      <el-table :data="summary" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" element-loading-text="拼命加载中....." stripe border>
        <el-table-column fixed prop="supplierName" label="供应商名称" sortable width="200"></el-table-column>
        <el-table-column prop="beginAmount" label="期初应付"></el-table-column>
        <el-table-column prop="payableAmount" label="应付金额"></el-table-column>
        <el-table-column prop="actualPayAmount" label="实付金额"></el-table-column>
        <el-table-column prop="endAmount" label="期末应付"></el-table-column>
        <el-table-column fixed="right" label="操作" width="120">
          <template scope="scope">
            <el-button size="small" @click="detailAction(scope.row.supplierId)">详细</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<style>
  .el-table .info-row {
    background: #c9e5f5;
  }

  .el-table .danger-row {
    background: #FF8888;
  }

  .el-table .warning-row {
    background: #FFEE99;
  }
  .el-table .default-row{
    background:#FFFFFF;
  }
</style>
<script>
  export default {
    data() {
      return {
        page:{},
        summary:[],
        suppliers:[],
        detail:[],
        formData: {
          extra:{}
        },
        searchText:"",
        initPromise:{},
        formLabelWidth: '28%',
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
        var submitData = util.deleteExtra(this.formData);
        util.setQuery("supplierPayable",submitData);
        axios.get('/api/global/cloud/kingdee/bdSupplier?'+ qs.stringify(submitData)).then((response) => {
          let supplierIdList = new Array();
          let suppliers = response.data.content;
          for (let item in suppliers) {
            supplierIdList.push(suppliers[item].fsupplierId);
          }
          submitData.supplierIdList = supplierIdList;
          if (submitData.supplierIdList.length !== 0) {
            axios.get('/api/global/cloud/report/supplierPayable/list?' + qs.stringify(submitData)).then((response) => {
              this.summary = response.data;
            });
          }
          this.page = response.data;
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
      },exportData(){
        util.confirmBeforeExportData(this).then(() => {
          window.location.href='/api/global/cloud/report/supplierPayable/export?'+qs.stringify(util.deleteExtra(this.formData));
        }).catch(()=>{});
      },detailAction:function(supplierId){
        this.detailLoading = true;
        if(supplierId !== null){
          let submitDetail = Object();
          submitDetail.supplierIdList = supplierId;
          submitDetail.dateStart = this.formData.dateStart;
          submitDetail.dateEnd = this.formData.dateEnd;
          axios.get('/api/global/cloud/report/supplierPayable/detail',{params:submitDetail}).then((response) =>{
            this.detail = response.data;
            this.detailLoading = false;
            this.detailVisible = true;
          })
        }
      },tableRowClassName(row, index) {
        if (row.index === 0) {
          return "default-row";
        } else if (row.documentStatus !== "C" && row.billType !== "期初应付" && row.billType !== "期末应付") {
          return "danger-row ";
        } else if (row.billType === "期初应付" || row.billType === "期末应付") {
          return "warning-row";
        } else if (row.index % 2 === 0) {
          return "default-row";
        } else if (row.index % 2 !== 0) {
          return "info-row";
        }
      },remoteSupplier(query) {
          if (query !== '') {
            this.remoteLoading = true;
            axios.get('/api/global/cloud/kingdee/bdSupplier/findByNameLike',{params:{name:query}}).then((response)=>{
              this.suppliers = response.data;
              this.remoteLoading = false;
            })
          } else {
            this.suppliers = {};
          }
        },
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    },created () {
      let that = this;
      that.pageHeight = window.outerHeight -320;
      that.initPromise = axios.get('/api/global/cloud/kingdee/bdSupplier/getQueryForSupplierPayable').then((response) =>{
        that.formData = response.data;
        util.copyValue(that.$route.query,that.formData);
      });
    }
  };
</script>

