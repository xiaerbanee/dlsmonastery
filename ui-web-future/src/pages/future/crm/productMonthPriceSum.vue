<template>
  <div>
    <head-tab active="productMonthPriceSum"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:productMonthPrice:sum'">过滤</el-button>
        <el-button type="primary" @click="exportDatas()" icon="upload"  v-permit="'crm:productMonthPrice:sum'">导出</el-button>
        <el-button type="primary" @click="uploadAudit()" icon="check">上报审核</el-button>
        <span v-html="searchText"></span>
      </el-row>

     <search-dialog @enter="search()" :show="formVisible" @hide="formVisible = false" title="过滤"  v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" method="get" >
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item label="月份" :label-width="formLabelWidth">
                <month-picker  v-model="formData.month" ></month-picker>
              </el-form-item>
              <el-form-item label="保卡状态" :label-width="formLabelWidth">
                <el-select v-model="formData.status" clearable filterable >
                  <el-option v-for="item in formData.extra.statusList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="办事处" :label-width="formLabelWidth">
                <el-select v-model="formData.areaId" clearable filterable >
                  <el-option v-for="item in formData.extra.areaList" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">搜索</el-button>
        </div>
      </search-dialog>
      <el-table :data="tableDatas" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('expressOrderList.loading')" stripe border>
        <el-table-column v-for="header in tableHeaders" :prop="header" :label="header" :key="header" width="240"></el-table-column>
      </el-table>
      <div style="margin-top: 5px;">
        显示第{{index}}至{{sum}}项结果，共{{sum}}项
      </div>
    </div>
  </div>
</template>
<script>

  import monthPicker from 'components/common/month-picker'
  export default {
    components:{
      monthPicker,
    },
    data() {
      return {
        searchText: "",
        page: {},
        formData: {
          extra: {}
        },

        tableHeaders: [],
        tableDatas: [],
        index:0,
        sum:0,
        initPromise: {},
        formVisible: false,
        formLabelWidth: '120px',
        pageLoading: false
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
        util.setQuery("dictEnumList", submitData);
        axios.get('/api/ws/future/crm/productMonthPriceSum', {params: submitData}).then((response) => {
          this.page = response.data;
          this.tableHeaders = response.data.header;
          this.tableDatas = response.data.datas;
          this.tableDatas = this.composeDatas(this.tableHeaders, this.tableDatas);
          this.sum = this.tableDatas.length;
          this.index= 1;
          if (this.tableDatas.length === 0) {
            this.index = 0;
          }
          this.pageLoading = false;
        })
      },
      search() {
        this.formVisible = false;
        this.pageRequest();
      },
      exportDatas(){
        if(!this.formData.areaId) {
          this.$message({message:"请选择办事处",type:"warning"});
          return;
        }
        util.confirmBeforeExportData(this).then(() => {
          window.location.href='/api/ws/future/crm/productMonthPriceSum/export?'+qs.stringify(util.deleteExtra(this.formData));
        }).catch(()=>{});
      },
      uploadAudit(){
        if(!this.formData.areaId) {
          this.$message({message:"请选择办事处",type:"warning"});
          return;
        }
        util.confirmBeforeAction(this,"确认审核通过吗?").then(()=>{
          axios.get('/api/ws/future/crm/productMonthPriceSum/uploadAudit',{params: util.deleteExtra(this.formData)}).then((response)=>{
            this.$message(response.data.message);
          })
        })
      },
      composeDatas(titles, datas) {
        let mapDatas = new Array();
        for (let i=0;i<datas.length;i++) {
          let obj = new Object();
          for(let j=0;j<titles.length;j++) {
            obj[titles[j]] = datas[i][j];
          }
          mapDatas.push(obj);
        }
        return mapDatas;
      }
    },
    created() {
      let that = this;
      this.pageHeight = window.outerHeight - 325;
      this.initPromise = axios.get('/api/ws/future/crm/productMonthPriceSum/getQuery').then((response) => {
        that.formData = response.data;
        util.copyValue(that.$route.query, that.formData);
        this.pageRequest();
      });
    }
  };
</script>
