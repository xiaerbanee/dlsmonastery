<template>
  <div>
    <head-tab active="expressList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:express:edit'">{{$t('expressList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'crm:express:view'">{{$t('expressList.filter')}}</el-button>
        <el-button type="primary" @click="exportData"  v-permit="'crm:express:view'">{{$t('expressList.export')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('expressList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" :label-width="formLabelWidth">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('expressList.code')" >
                <el-input v-model="formData.code" auto-complete="off" :placeholder="$t('expressList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('expressList.toDepotName')" >
                <depot-select v-model="formData.expressOrderToDepotId" category="shop" @afterInit="setSearchText"></depot-select>
              </el-form-item>
              <el-form-item :label="$t('expressOrderList.createdDate')" >
                <date-range-picker v-model="formData.createdDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('expressList.extendBusinessId')">
                <el-input v-model="formData.expressOrderExtendBusinessId" auto-complete="off" :placeholder="$t('expressList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('expressList.fromDepotName')">
                <depot-select v-model="formData.expressOrderFromDepotId" category="store" @afterInit="setSearchText"></depot-select>
              </el-form-item>
              <el-form-item :label="$t('expressList.expressCompanyName')">
                <express-company-select v-model="formData.expressOrderExpressCompanyId" @afterInit="setSearchText"></express-company-select>
              </el-form-item>
              <el-form-item :label="$t('expressList.extendType')" >
                <el-select v-model="formData.expressOrderExtendType" filterable clearable :placeholder="$t('expressList.inputKey')">
                  <el-option v-for="item in formData.extra.expressOrderExtendTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('expressList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('expressList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="code" :label="$t('expressList.code')" sortable></el-table-column>
        <el-table-column prop="expressOrderExpressCompanyName"  :label="$t('expressList.expressCompanyName')" ></el-table-column>
        <el-table-column prop="expressOrderExtendType" :label="$t('expressList.extendType')" ></el-table-column>
        <el-table-column prop="expressOrderExtendBusinessId" :label="$t('expressList.extendBusinessId')" ></el-table-column>
        <el-table-column prop="expressOrderFromDepotName" :label="$t('expressList.fromDepotName')" ></el-table-column>
        <el-table-column prop="expressOrderToDepotName"  :label="$t('expressList.toDepotName')" ></el-table-column>
        <el-table-column prop="expressOrderContator" :label="$t('expressList.contact')" ></el-table-column>
        <el-table-column prop="expressOrderMobilePhone" :label="$t('expressList.mobilePhone')" ></el-table-column>
        <el-table-column prop="expressOrderAddress" :label="$t('expressList.address')" ></el-table-column>
        <el-table-column prop="weight" :label="$t('expressList.weight')" sortable></el-table-column>
        <el-table-column prop="qty" :label="$t('expressList.qty')" sortable></el-table-column>
        <el-table-column prop="shouldPay" :label="$t('expressList.shouldPay')" sortable></el-table-column>
        <el-table-column prop="realPay" :label="$t('expressList.realPay')" sortable></el-table-column>
        <el-table-column prop="createdByName" column-key="createdBy" :label="$t('expressList.createdBy')" sortable></el-table-column>
        <el-table-column prop="createdDate" :label="$t('expressList.createdDate')" sortable></el-table-column>
        <el-table-column fixed="right" :label="$t('expressList.operation')">
          <template scope="scope">
            <div class="action" v-permit="'crm:express:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'edit')">{{$t('expressList.edit')}}</el-button></div>
            <div class="action" v-permit="'crm:express:delete'"><el-button size="small"  @click.native="itemAction(scope.row.id,'delete')">{{$t('expressList.delete')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import depotSelect from 'components/future/depot-select'
  import expressCompanySelect from 'components/future/express-company-select'
  export default{
    components:{
      depotSelect,
      expressCompanySelect,
    }, data() {
      return {
        page:{},
        searchText:"",
        formData:{
            extra:{}
        },
        initPromise:{},
        formLabelWidth: '28%',
        formVisible: false,
        pageLoading: false,
        pageHeight: 600,
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
        util.setQuery("expressList",submitData);
        axios.get('/api/ws/future/crm/express?'+qs.stringify(submitData)).then((response) => {
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
      },itemAdd(){
        this.$router.push({ name: 'expressForm'})
      },itemAction:function(id,action){
        if(action==="edit") {
          this.$router.push({ name: 'expressForm', query: { id: id }})
        } else if(action==="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/crm/express/delete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
              this.pageRequest();
            });
          }).catch(()=>{});
        }
      },exportData(){
        util.confirmBeforeExportData(this).then(() => {
          window.location.href="/api/ws/future/crm/express/export?"+qs.stringify(util.deleteExtra(this.formData));
        }).catch(()=>{});
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      this.initPromise=axios.get('/api/ws/future/crm/express/getQuery').then((response) =>{
        this.formData=response.data;
        util.copyValue(this.$route.query,this.formData);
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
  };
</script>

