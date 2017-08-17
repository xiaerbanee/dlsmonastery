<template>
  <div>
    <head-tab active="productMonthPriceSum"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:productMonthPrice:sum'">过滤</el-button>
        <el-button type="primary" @click="exportData()" icon="upload" v-permit="'crm:productMonthPrice:sum'">导出</el-button>
        <el-button type="primary" @click="uploadAudit()" icon="check">上报审核</el-button>
        <el-button type="primary" @click="search()">刷新</el-button>
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
        formData: {
          extra: {}
        },

        tableHeaders: [],
        tableDatas: [],
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
        util.setQuery("productMonthPriceSum", submitData);
        axios.get('/api/ws/future/crm/productMonthPriceSum', {params: submitData}).then((response) => {
          this.tableHeaders = response.data.header;
          this.tableDatas = response.data.data;
          if(response.data.data) {
            this.tableDatas = this.composeDatas(this.tableHeaders, this.tableDatas);
          }
          this.pageLoading = false;
        }).catch(()=>{
          this.pageLoading = false;
        });
      },
      search() {
        this.formVisible = false;
        this.pageRequest();
      },
      exportData(){
        if(!this.formData.areaId) {
          this.$message({message:"请选择办事处",type:"warning"});
          return;
        }
        util.confirmBeforeAction(this, "最多导出50000条记录，确认导出?").then(() => {
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
      composeDatas(headerList, datasList) {
        let mapDatas = [];
        for (let i=0;i<datasList.length;i++) {
          let obj = {};
          for(let j=0;j<headerList.length;j++) {
            obj[headerList[j]] = datasList[i][j];
          }
          mapDatas.push(obj);
        }
        return mapDatas;
      }
    },
    created() {
      this.pageHeight = window.outerHeight - 325;
      axios.get('/api/ws/future/crm/productMonthPriceSum/getQuery').then((response) => {
        this.formData = response.data;
        util.copyValue(this.$route.query, this.formData);
        this.pageRequest();
      });
    }
  };
</script>
