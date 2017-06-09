<template>
  <div>
    <head-tab active="depotInventoryReport"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:bank:view'">过滤</el-button>
        <el-dropdown  @command="exportData">
          <el-button type="primary">导出<i class="el-icon-caret-bottom el-icon--right"></i></el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="按数量导出">按数量导出</el-dropdown-item>
            <el-dropdown-item command="按合计导出">按合计导出</el-dropdown-item>
            <el-dropdown-item command="按串码导出">按串码导出</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <el-button type="primary" @click="saleReportGrid()" icon="document">明细</el-button>
        <el-button type="primary" @click="preLevel()" v-show="officeIds.length">返回</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog title="过滤" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item label="查看" :label-width="formLabelWidth">
                <el-select v-model="formData.outType" clearable filterable placeholder="请选择">
                  <el-option v-for="item in formData.extra.outTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="区域" :label-width="formLabelWidth">
                <el-select v-model="formData.areaType" clearable filterable placeholder="请选择">
                  <el-option v-for="item in formData.extra.areaTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="区域" :label-width="formLabelWidth">
                <el-select v-model="formData.areaType" clearable filterable placeholder="请选择">
                  <el-option v-for="item in formData.extra.areaTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="乡镇" :label-width="formLabelWidth">
                <el-select v-model="formData.townType" clearable filterable placeholder="请选择">
                  <el-option v-for="item in formData.extra.townTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-col :span="12">
              </el-col>
              <el-form-item label="日期" :label-width="formLabelWidth">
                <date-picker v-model="formData.date"></date-picker>
              </el-form-item>
              <el-form-item label="打分型号" :label-width="formLabelWidth">
                <el-select v-model="formData.scoreType" clearable filterable placeholder="请选择">
                  <el-option v-for="(key,value) in formData.extra.boolMap" :key="key" :label="value " :value="key"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="货品" :label-width="formLabelWidth">
                <product-select v-model="formData.productIds"></product-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">确定</el-button>
        </div>
      </search-dialog>
      <el-table :data="page"  style="margin-top:5px;" v-loading="pageLoading" element-loading-text="加载中" @sort-change="sortChange" @row-click="nextLevel" stripe border>
        <el-table-column fixed prop="officeName" label="门店" sortable width="300"></el-table-column>
        <el-table-column prop="qty" label="数量"  sortable></el-table-column>
        <el-table-column prop="percent" label="占比(%)"></el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  import productSelect from 'components/future/product-select'

  export default {
    components:{
      productSelect
    },
    data() {
      return {
        page:[],
        searchText:"",
        formData:{
          extra:{},
        },
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
        officeIds:[],
        type:"门店报表",
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
        this.formData.type=this.type;
        var submitData = util.deleteExtra(this.formData);
        util.setQuery("productImeStockReport",this.formData);
        axios.get('/api/ws/future/basic/depotShop/depotReport?'+qs.stringify(submitData)).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },sortChange(column) {
        this.formData.sort=util.getSort(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },nextLevel(	row, event, column){
        this.officeIds.push(row.officeId);
        this.formData.officeId=this.officeIds[this.officeIds.length-1];
        this.pageRequest();
      },preLevel(){
        this.officeIds.pop();
        this.formData.officeId=this.officeIds[this.officeIds.length-1];
        this.pageRequest();
      },saleReportGrid(){

      },exportData(command) {
      }
    },created () {
      axios.get('/api/ws/future/crm/productIme/getReportQuery').then((response) => {
        this.formData = response.data;
        console.log(this.formData.extra)
        util.copyValue(this.$route.query, this.formData);
        this.pageRequest();
      })
    }
  };
</script>

