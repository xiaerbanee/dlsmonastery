<template>
  <div>
    <head-tab active="imeAllotList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:imeAllot:edit'">{{$t('imeAllotList.imeAllotList')}}</el-button>
        <el-button type="primary" @click="batchAudit(true)" icon="check" v-permit="'crm:imeAllot:batchSave'">{{$t('imeAllotList.batchPass')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:imeAllot:view'">{{$t('imeAllotList.filter')}}</el-button>
        <el-button type="primary" @click="exportData"  v-permit="'crm:imeAllot:view'">{{$t('imeAllotList.export')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible=false" :title="$t('imeAllotList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" :label-width="formLabelWidth">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('imeAllotList.fromDepot')">
                <el-input v-model="formData.fromDepotName" :placeholder="$t('imeAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('imeAllotList.toDepot')">
                <el-input v-model="formData.toDepotName" :placeholder="$t('imeAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('imeAllotList.crossArea')" >
                <bool-select v-model="formData.crossArea"  @afterInit="setSearchText" ></bool-select>
              </el-form-item>
              <el-form-item :label="$t('imeAllotList.ime')">
                <el-input v-model="formData.ime" :placeholder="$t('imeAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('imeAllotList.status')">
                <el-select v-model="formData.status" filterable clearable :placeholder="$t('imeAllotList.inputKey')">
                  <el-option v-for="item in formData.extra.statusList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('imeAllotList.createdDate')">
                <date-range-picker v-model="formData.createdDateRange" ></date-range-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('imeAllotList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" @selection-change="selectionChange"  :element-loading-text="$t('imeAllotList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column type="selection" width="55" :selectable="checkSelectable"></el-table-column>
        <el-table-column  prop="fromDepotName" column-key="fromDepotId"  :label="$t('imeAllotList.fromDepot')" width="150" sortable></el-table-column>
        <el-table-column prop="toDepotName" column-key="toDepotId" :label="$t('imeAllotList.toDepot')" sortable></el-table-column>
        <el-table-column prop="productImeIme" :label="$t('imeAllotList.ime')"  ></el-table-column>
        <el-table-column prop="productImeProductName" :label="$t('imeAllotList.productName')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('imeAllotList.allotDate')" sortable></el-table-column>
        <el-table-column prop="createdByName" column-key="createdBy" :label="$t('imeAllotList.allotEmployee')" sortable></el-table-column>
        <el-table-column prop="status" :label="$t('imeAllotList.status')" width="120" >
          <template scope="scope">
            <el-tag :type="scope.row.status==='已通过' ? 'primary' : 'danger'">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastModifiedByName" column-key="lastModifiedBy" :label="$t('imeAllotList.lastModifiedBy')" sortable></el-table-column>
        <el-table-column prop="enabled" :label="$t('imeAllotList.enabled')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'primary' : 'danger'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column  :label="$t('imeAllotList.operation')" >
          <template scope="scope">
            <div class="action" v-if="scope.row.status === '申请中'" v-permit="'crm:imeAllot:edit'"><el-button size="small" @click="itemAction(scope.row.id, 'auditPass')">{{$t('imeAllotList.auditPass')}}</el-button></div>
            <div class="action" v-if="scope.row.status === '申请中'" v-permit="'crm:imeAllot:edit'"><el-button size="small" @click="itemAction(scope.row.id, 'auditNotPass')">{{$t('imeAllotList.auditNotPass')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>

  import boolSelect from 'components/common/bool-select'

  export default{
    components:{
      boolSelect,
    },
    data() {
      return {
        pageLoading: false,
        page:{},
        initPromise:{},
        searchText:"",
        formData:{
            extra:{}
        },
        selects:[],
        formVisible: false,
        pageHeight: 600,
        formLabelWidth:"28%"
      };
    },
    methods: {
      setSearchText() {
        this.$nextTick(function () {
          this.searchText = util.getSearchText(this.$refs.searchDialog);
        });
      },
      pageRequest() {
        this.pageLoading = true;
        this.setSearchText();
        let submitData = util.deleteExtra(this.formData);
        util.setQuery("imeAllotList",submitData);
        axios.get('/api/ws/future/crm/imeAllot?'+qs.stringify(submitData)).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        });
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
        this.$router.push({ name: 'imeAllotForm'})
      },itemAction:function(id,action){

        if(action==="auditPass") {
          util.confirmBeforeAction(this,  this.$t('imeAllotList.confirmBeforeAuditPass')).then(() => {
            let ids = [id];
            axios.get('/api/ws/future/crm/imeAllot/audit',{params:{ids:ids,pass:true}}).then((response) =>{
              this.$message(response.data.message);
              this.pageRequest();
            });
          }).catch(()=>{});
        }else if(action==="auditNotPass"){

          util.confirmBeforeAction(this,  this.$t('imeAllotList.confirmBeforeAuditNotPass')).then(() => {
            let ids = [id];
            axios.get('/api/ws/future/crm/imeAllot/audit',{params:{ids:ids,pass:false}}).then((response) =>{
              this.$message(response.data.message);
              this.pageRequest();
            });
          }).catch(()=>{});
        }
      },exportData(){
        util.confirmBeforeExportData(this).then(() => {
          window.location.href='/api/ws/future/crm/imeAllot/export?'+qs.stringify(util.deleteExtra(this.formData));
        }).catch(()=>{});

      },selectionChange(selection){
        console.log(selection);
        this.selects=[];
        for(let each of selection){
          this.selects.push(each.id);
        }
      },batchAudit(pass){

        if(!this.selects || this.selects.length < 1){
          this.$message(this.$t('imeAllotList.noSelectionFound'));
          return ;
        }

        util.confirmBeforeBatchPass(this).then(() => {
          axios.get('/api/ws/future/crm/imeAllot/audit',{params:{ids:this.selects,pass:pass}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          });
        }).catch(()=>{});
      },checkSelectable(row) {
        return row.status.indexOf('通过')<0;
      }

    },created () {
       this.pageHeight = 0.75*window.innerHeight;
      this.initPromise = axios.get('/api/ws/future/crm/imeAllot/getQuery').then((response) =>{
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

