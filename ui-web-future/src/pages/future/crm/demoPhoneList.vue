<template>
  <div>
    <head-tab active="demoPhoneList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:demoPhone:edit'">{{$t('demoPhoneList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'crm:demoPhone:view'">{{$t('demoPhoneList.filter')}}</el-button>
        <el-button type="primary" @click="exportData" icon="upload" v-permit="'crm:demoPhone:view'">{{$t('demoPhoneList.export')}}</el-button>
        <el-button type="primary" @click="itemCollect" icon="document" v-permit="'crm:demoPhone:view'">{{$t('demoPhoneList.collect')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('demoPhoneList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('demoPhoneList.ime')" :label-width="formLabelWidth">
                <el-input v-model="formData.ime" auto-complete="off" :placeholder="$t('demoPhoneList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('demoPhoneList.shopName')" :label-width="formLabelWidth">
                <depot-select v-model="formData.shopId" category="shop" @afterInit="setSearchText"></depot-select>
              </el-form-item>
              <el-form-item :label="$t('demoPhoneList.demoPhoneType')" :label-width="formLabelWidth">
                <demo-phone-type v-model = "formData.demoPhoneTypeId" @afterInit="setSearchText"></demo-phone-type>
              </el-form-item>
              <el-form-item :label="$t('demoPhoneList.createdDate')" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDate"></date-range-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('demoPhoneList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('demoPhoneList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="ime" :label="$t('demoPhoneList.ime')" sortable></el-table-column>
        <el-table-column prop="areaName" :label="$t('demoPhoneList.areaName')"></el-table-column>
        <el-table-column column-key="shopId" prop="shopName" :label="$t('demoPhoneList.shopName')" sortable></el-table-column>
        <el-table-column column-key="demoPhoneTypeId" prop="demoPhoneType" :label="$t('demoPhoneList.demoPhoneType')" sortable></el-table-column>
        <el-table-column column-key="employeeId" prop="employeeName" :label="$t('demoPhoneList.employeeName')" sortable></el-table-column>
        <el-table-column prop="status" :label="$t('demoPhoneList.status')" sortable>
          <template scope="scope">
            <el-tag :type="scope.row.status=='已通过' ? 'primary' : 'danger'">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdDate" :label="$t('demoPhoneList.createdDate')" sortable></el-table-column>
        <el-table-column prop="remarks" :label="$t('demoPhoneList.remarks')"></el-table-column>
        <el-table-column prop="locked" :label="$t('demoPhoneList.locked')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" :label="$t('demoPhoneList.enabled')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'primary' : 'danger'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('demoPhoneList.operation')" width="140">
          <template scope="scope">
            <div class="action" v-permit="'crm:demoPhone:delete'"><el-button size="small" @click.native="itemAction(scope.row.id,'delete')">{{$t('demoPhoneList.delete')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import depotSelect from 'components/future/depot-select'
  import demoPhoneType from 'components/future/demo-phone-type-select'
  import ElTag from "../../../../node_modules/element-ui/packages/tag/src/tag.vue";
  export default {
    components:{
      ElTag,
      depotSelect,demoPhoneType},
    data() {
      return {
        pageLoading: false,
        pageHeight:600,
        page:{},
        searchText:"",
        formData:{
            extra:{}
        },
        initPromise:{},
        formLabelWidth: '120px',
        formVisible: false,

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
        util.setQuery("demoPhoneList",submitData);
        axios.get('/api/ws/future/crm/demoPhone?'+qs.stringify(submitData)).then((response)  => {
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
        this.$router.push({ name: 'demoPhoneForm'})
      },itemCollect(){
        this.$router.push({ name: 'demoPhoneTypeOfficeList'})
      },itemAction:function(id,action){
        if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
          axios.get('/api/ws/future/crm/demoPhone/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          });
        }).catch(()=>{});
        }
      },exportData(){
        util.confirmBeforeExportData(this).then(() => {
          window.location.href='/api/ws/future/crm/demoPhone/export?'+qs.stringify(util.deleteExtra(this.formData));
        }).catch(()=>{});
      }
    },created () {
       this.pageHeight = 0.75*window.innerHeight;
      this.initPromise=axios.get('/api/ws/future/crm/demoPhone/getQuery').then((response) => {
        this.formData = response.data;
        util.copyValue(this.$route.query, this.formData);
      })
    },activated(){
      this.initPromise.then(() => {
        this.pageRequest();
      });
    }
  };
</script>


