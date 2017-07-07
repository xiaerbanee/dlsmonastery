<template>
  <div>
    <head-tab active="bankInList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:bankIn:edit'">{{$t('bankInList.add')}}</el-button>
        <el-button type="primary" @click="itemBatchAdd" icon="plus" v-permit="'crm:bankIn:edit'">批量添加</el-button>
        <el-button type="primary" :disabled="submitDisabled"  @click="batchPass" icon="check" v-permit="'crm:bankIn:audit'">{{$t('bankInList.batchPass')}}</el-button>
        <el-button   type="primary" @click="exportData" v-permit="'crm:bankIn:view'">{{$t('bankInList.export')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'crm:bankIn:view'">{{$t('bankInList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('bankInList.filter')" v-model="formVisible" size="medium" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" label-width="150px">
          <el-row :gutter="4">
            <el-col :span="10">
              <el-form-item :label="$t('bankInList.id')">
                <el-input v-model="formData.id" :placeholder="$t('bankInList.preciseSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('bankInList.shopName')">
                <el-input v-model="formData.shopName" :placeholder="$t('bankInList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('bankInList.billDate')">
                <date-range-picker  v-model="formData.billDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('bankInList.amount')">
                <el-input v-model="formData.amount" :placeholder="$t('bankInList.preciseSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('bankInList.inputDate')">
                <date-range-picker  v-model="formData.inputDateRange"></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('bankInList.transferType')">
                <el-select v-model="formData.transferType"  clearable>
                  <el-option v-for="item in formData.extra.transferTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="$t('bankInList.outCode')">
                <el-input v-model="formData.outCode" :placeholder="$t('bankInList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('bankInList.bankName')">
                <el-input v-model="formData.bankName" :placeholder="$t('bankInList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('bankInList.processStatus')">
                <process-status-select v-model="formData.processStatus"  type="BankIn" @afterInit="setSearchText"></process-status-select>
              </el-form-item>
              <el-form-item :label="$t('bankInList.createdBy')">
                <account-select  v-model="formData.createdBy" @afterInit="setSearchText"></account-select>
              </el-form-item>
              <el-form-item :label="$t('bankInList.createdDate')">
                <date-range-picker  v-model="formData.createdDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('bankInList.serialNumber')">
                <el-input v-model="formData.serialNumber" :placeholder="$t('bankInList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('bankInList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" style="margin-top:5px;" v-loading="pageLoading" @selection-change="selectionChange"  :element-loading-text="$t('bankInList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column type="selection" :selectable="checkSelectable"></el-table-column>
        <el-table-column prop="shopName" column-key="shopId"  :label="$t('bankInList.shopName')" sortable></el-table-column>
        <el-table-column prop="shopClientName"  :label="$t('bankInList.shopClientName')"></el-table-column>
        <el-table-column prop="bankName" column-key="bankId"  :label="$t('bankInList.bankName')" sortable></el-table-column>
        <el-table-column prop="transferType" :label="$t('bankInList.transferType')" sortable></el-table-column>
        <el-table-column prop="amount" :label="$t('bankInList.amount')" ></el-table-column>
        <el-table-column prop="serialNumber" :label="$t('bankInList.serialNumber')" sortable></el-table-column>
        <el-table-column prop="billDate" :label="$t('bankInList.billDate')" sortable></el-table-column>
        <el-table-column prop="createdByName" :label="$t('bankInList.createdBy')" ></el-table-column>
        <el-table-column prop="createdDate" :label="$t('bankInList.createdDate')" ></el-table-column>
        <el-table-column prop="outCode" :label="$t('bankInList.outCode')" sortable></el-table-column>
        <el-table-column prop="processStatus" :label="$t('bankInList.processStatus')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('bankInList.remarks')" width="200"></el-table-column>
        <el-table-column :label="$t('bankInList.operation')">
          <template scope="scope" >
            <div class="action" v-permit="'crm:bankIn:view'"><el-button size="small" @click.native="itemAction(scope.row.id, 'detail')">{{$t('bankInList.detail')}}</el-button></div>
            <div class="action" v-if="scope.row.auditable" v-permit="'crm:bankIn:audit'"><el-button size="small" @click.native="itemAction(scope.row.id, 'audit')">{{$t('bankInList.audit')}}</el-button></div>
            <div class="action" v-if="canEdit(scope.row)" v-permit="'crm:bankIn:edit'"><el-button size="small" @click.native="itemAction(scope.row.id, 'edit')">{{$t('bankInList.edit')}}</el-button></div>
            <div class="action" v-if="canEdit(scope.row)" v-permit="'crm:bankIn:delete'"><el-button size="small"  @click.native="itemAction(scope.row.id, 'delete')">{{$t('bankInList.delete')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import accountSelect from 'components/basic/account-select';
  import processStatusSelect from 'components/general/process-status-select';

  export default{
    components:{
      accountSelect,
      processStatusSelect,
    },
    data() {
      return {
        pageLoading: false,
        searchText:"",
        initPromise:{},
        page:{},
        formData:{
            extra:{}
        },
        formVisible: false,
        selects:[],
        submitDisabled:false,
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
        util.setQuery("bankInList",submitData);
        axios.get('/api/ws/future/crm/bankIn?'+qs.stringify(submitData)).then((response) => {
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
        this.$router.push({ name: 'bankInForm'})
      },itemBatchAdd(){
        this.$router.push({ name: 'bankInBatchForm'})
      },itemAction:function(id, action){
        if(action==="edit") {
          this.$router.push({ name: 'bankInForm', query: { id: id }})
        }else if(action==="detail"){
          this.$router.push({ name: 'bankInDetail', query: { id: id, action:action}})
        }else if(action==="audit"){
          this.$router.push({ name: 'bankInDetail', query: { id: id, action:action}})
        }else if(action==="delete"){
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get("/api/ws/future/crm/bankIn/delete",{params:{id:id}}).then((response)=>{
              this.$message(response.data.message);
              this.pageRequest();
            })
          }).catch(()=>{});
        }
      },checkSelectable(row) {
        return row.processStatus !== '已通过' && row.processStatus !== '未通过'
      },selectionChange(selection){
        this.selects=[];
        for(let each of selection){
          this.selects.push(each.id);
        }
      },batchPass(){
          if(!this.selects || this.selects.length < 1){
            this.$message(this.$t('bankInList.noSelectionFound'));
            return ;
          }
        util.confirmBeforeBatchPass(this).then(() => {
          this.submitDisabled = true;
          this.pageLoading = true;
          axios.get('/api/ws/future/crm/bankIn/batchAudit',{params:{ids:this.selects, pass:true}}).then((response) =>{
            this.$message(response.data.message);
            this.pageLoading = false;
            this.submitDisabled = false;
            this.pageRequest();
          }).catch(()=>{
            this.pageLoading = false;
            this.submitDisabled = false;
            this.pageRequest();
          });
        }).catch(()=>{});
      },exportData(){
        util.confirmBeforeExportData(this).then(() => {
          window.location.href='/api/ws/future/crm/bankIn/export?'+qs.stringify(util.deleteExtra(this.formData));
        }).catch(()=>{});
      }, canEdit(row){
          if(row.status !== '省公司审核'){
              return false;
          }
        if(row.editable || util.isPermit('crm:bankIn:audit')) {
          return true;
        } else {
          return false;
        }
      }
    },created () {
      this.initPromise = axios.get('/api/ws/future/crm/bankIn/getQuery').then((response) =>{
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

