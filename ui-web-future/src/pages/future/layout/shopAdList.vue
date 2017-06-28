<template>
  <div>
    <head-tab active="shopAdList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="batchPass" icon="check" v-permit="'crm:shopAd:edit'">{{$t('shopAdList.batchPass')}}</el-button>
        <el-button type="primary" @click="batchBack" icon="check" v-permit="'crm:shopAd:edit'">{{$t('shopAdList.batchBlack')}}</el-button>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopAd:edit'">{{$t('shopAdList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:shopAd:view'">{{$t('shopAdList.filter')}}</el-button>
        <el-button type="primary" @click="exportData" icon="upload" v-permit="'crm:shopAd:view'">{{$t('shopAdList.export')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('shopAdList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('shopAdList.areaName')" :label-width="formLabelWidth">
                <office-select v-model="formData.officeId"></office-select>
              </el-form-item>
              <el-form-item :label="$t('shopAdList.adCode')" :label-width="formLabelWidth">
                <el-input v-model="formData.id" auto-complete="off" :placeholder="$t('shopAdList.inputNotZeroPart')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('shopAdList.shopName')" :label-width="formLabelWidth">
                <depot-select v-model="formData.shopId" category="adShop"></depot-select>
              </el-form-item>
              <el-form-item :label="$t('shopAdList.specialArea')" :label-width="formLabelWidth">
                <bool-select v-model="formData.specialArea"></bool-select>
              </el-form-item>
              <el-form-item :label="$t('shopAdList.shopAdType')" :label-width="formLabelWidth">
                <el-select v-model="formData.shopAdTypeId" filterable clearable :placeholder="$t('expressOrderList.inputKey')">
                  <el-option v-for="shopAdType in formData.extra.shopAdTypes" :key="shopAdType.id" :label="shopAdType.name" :value="shopAdType.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('shopAdList.processStatus')" :label-width="formLabelWidth">
                <process-status-select v-model="formData.processStatus" type="ShopAd" @afterInit="setSearchText"></process-status-select>
              </el-form-item>
              <el-form-item :label="$t('shopAdList.createdDate')" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDate"></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('shopAdList.createdBy')" :label-width="formLabelWidth">
                <account-select  v-model="formData.createdBy"></account-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('expressOrderList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('expressOrderList.loading')"  @selection-change="handleSelectionChange" @sort-change="sortChange" stripe border>
        <el-table-column type="selection" width="50" :selectable="checkSelectable"></el-table-column>
        <el-table-column column-key="id" fixed prop="formatId" :label="$t('shopAdList.code')" sortable width=120></el-table-column>
        <el-table-column column-key="officeId" prop="officeName"  :label="$t('shopAdList.areaName')" sortable></el-table-column>
        <el-table-column column-key="shopId" prop="shopName"  :label="$t('shopAdList.shopName')" sortable></el-table-column>
        <el-table-column prop="specialArea" :label="$t('shopAdList.specialArea')">
          <template scope="scope">
            <el-tag :type="scope.row.specialArea ? 'primary' : 'danger'">{{scope.row.specialArea | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column column-key="shopAdTypeId" prop="shopAdTypeName" :label="$t('shopAdList.shopAdType')" sortable></el-table-column>
        <el-table-column prop="lengthWidthQty" :label="$t('shopAdList.lengthAndWidthAndQty')" width="120"></el-table-column>
        <el-table-column prop="area" :label="$t('shopAdList.totalArea')"></el-table-column>
        <el-table-column prop="price" :label="$t('shopAdList.price')" sortable></el-table-column>
        <el-table-column prop="content" :label="$t('shopAdList.content')" width="120"></el-table-column>
        <el-table-column prop="processStatus" :label="$t('shopAdList.processStatus')" width="160" sortable>
          <template scope="scope">
            <el-tag :type="scope.row.processStatus === '已通过' ? 'primary' : 'danger'"  close-transition>{{scope.row.processStatus}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column column-key="createdBy" prop="createdByName" :label="$t('expressOrderList.createdBy')" sortable></el-table-column>
        <el-table-column prop="createdDate" :label="$t('expressOrderList.createdDate')" width="100" sortable></el-table-column>
        <el-table-column column-key="lastModifiedBy" prop="lastModifiedByName" :label="$t('expressOrderList.lastModifiedBy' )" sortable></el-table-column>
        <el-table-column prop="lastModifiedDate" :label="$t('expressOrderList.lastModifiedDate')" sortable width=100></el-table-column>
        <el-table-column prop="remarks" :label="$t('expressOrderList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('expressOrderList.operation')" width="140">
          <template scope="scope">
            <div class="action" v-permit="'crm:shopAd:view'"><el-button size="small" @click.native="itemAction(scope.row.id,'detail')">{{$t('shopPrintList.detail')}}</el-button></div>
            <div class="action" v-if="scope.row.isAuditable&&scope.row.processStatus !== '已通过'&&scope.row.processStatus !== '未通过'" v-permit="'crm:shopAd:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'audit')">{{$t('shopBuildList.audit')}}</el-button></div>
            <div class="action" v-if="scope.row.isEditable&&!scope.row.locked" v-permit="'crm:shopAd:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'edit')">{{$t('shopBuildList.edit')}}</el-button></div>
            <div class="action" v-if="scope.row.isEditable&&!scope.row.locked" v-permit="'crm:shopAd:delete'"><el-button size="small" @click.native="itemAction(scope.row.id,'delete')">{{$t('shopBuildList.delete')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import officeSelect from 'components/basic/office-select';
  import accountSelect from 'components/basic/account-select';
  import depotSelect from 'components/future/depot-select';
  import boolSelect from 'components/common/bool-select';
  import processStatusSelect from 'components/general/process-status-select';
  export default {
    components:{
      officeSelect,
      accountSelect,
      depotSelect,
      boolSelect,
      processStatusSelect
    },
    data() {
      return {
        searchText:"",
        multiple:true,
        page:{},
        formData:{
          extra:{}
        },
        initPromise:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
        multipleSelection:[],
        pickerDateOption:util.pickerDateOption
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
        util.setQuery("shopAdList",submitData);
        axios.get('/api/ws/future/layout/shopAd',{params:submitData}).then((response) => {
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
          window.location.href='/api/ws/future/layout/shopAd/export?'+qs.stringify(util.deleteExtra(this.formData));
        }).catch(()=>{});
      },batchPass(){
        if(!this.multipleSelection || this.multipleSelection.length < 1){
          this.$message(this.$t('shopAdList.noSelectionFound'));
          return ;
        }
        util.confirmBeforeBatchPass(this).then(() => {
          axios.get('/api/ws/future/layout/shopAd/batchAudit', {
            params: {
              pass: true,
              ids: this.multipleSelection
            }
          }).then((response) => {
            this.$message(response.data.message);
            this.pageRequest();
          });
        }).catch(()=>{});
    },batchBack(){
        if(!this.multipleSelection || this.multipleSelection.length < 1){
          this.$message(this.$t('shopAdList.noSelectionFound'));
          return ;
        }
        util.confirmBeforeBatchNotPass(this).then(() => {
          axios.get('/api/ws/future/layout/shopAd/batchAudit', {
            params: {
              pass: false,
              ids: this.multipleSelection
            }
          }).then((response) => {
            this.$message(response.data.message);
            this.pageRequest();
          });
        }).catch(()=>{});
      },itemAdd(){
        this.$router.push({ name: 'shopAdForm'});
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'shopAdForm', query: { id: id }});
        }else if(action=="detail"){
          this.$router.push({ name: 'shopAdDetail', query: { id: id,action:action}});
        }else if(action =="audit"){
            this.$router.push({name:'shopAdDetail',query:{id:id,action:action}});
        }else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/layout/shopAd/delete', {params: {id: id}}).then((response) => {
              this.$message(response.data.message);
              this.pageRequest();
            });
          })
        }
      },handleSelectionChange(val) {
        var arrs=[];
        for(var i in val){
          arrs.push(val[i].id);
        }
        this.multipleSelection=arrs;
      },checkSelectable(row) {
        return row.processStatus !== '已通过';
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      this.initPromise = axios.get('/api/ws/future/layout/shopAd/getQuery').then((response)=>{
        this.formData=response.data;
        util.copyValue(this.$route.query,this.formData);
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      })
    }
  };
</script>

