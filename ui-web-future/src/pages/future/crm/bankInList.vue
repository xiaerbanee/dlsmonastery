<template>
  <div>
    <head-tab active="bankInList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:bankIn:edit'">{{$t('bankInList.add')}}</el-button>
        <el-button type="primary" :disabled="submitDisabled"  @click="batchPass" icon="check" v-permit="'crm:bankIn:audit'">{{$t('bankInList.batchPass')}}</el-button>
        <el-button   type="primary" @click="exportData" v-permit="'crm:bankIn:view'">{{$t('bankInList.export')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:bankIn:view'">{{$t('bankInList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('bankInList.filter')" v-model="formVisible" size="large" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="10">
              <el-form-item :label="$t('bankInList.id')" :label-width="formLabelWidth">
                <el-input v-model="formData.id" auto-complete="off" :placeholder="$t('bankInList.preciseSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('bankInList.shopName')" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off" :placeholder="$t('bankInList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('bankInList.billDate')" :label-width="formLabelWidth">
                <date-range-picker  v-model="formData.billDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('bankInList.amount')" :label-width="formLabelWidth">
                <el-input v-model="formData.amount" auto-complete="off" :placeholder="$t('bankInList.preciseSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('bankInList.inputDate')" :label-width="formLabelWidth">
                <date-range-picker  v-model="formData.inputDateRange"></date-range-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="$t('bankInList.outCode')" :label-width="formLabelWidth">
                <el-input v-model="formData.outCode" auto-complete="off" :placeholder="$t('bankInList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('bankInList.bankName')" :label-width="formLabelWidth">
                <el-input v-model="formData.bankName" auto-complete="off" :placeholder="$t('bankInList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('bankInList.processStatus')" :label-width="formLabelWidth">
                <el-select v-model="formData.processStatus" clearable filterable :placeholder="$t('bankInList.selectProcessStatus')">
                  <el-option v-for="status in formData.extra.processStatusList" :key="status" :label="status" :value="status"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('bankInList.createdBy')" :label-width="formLabelWidth">
                <account-select  v-model="formData.createdBy" ></account-select>
              </el-form-item>
              <el-form-item :label="$t('bankInList.createdDate')" :label-width="formLabelWidth">
                <date-range-picker  v-model="formData.createdDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('bankInList.serialNumber')" :label-width="formLabelWidth">
                <el-input v-model="formData.serialNumber" auto-complete="off" :placeholder="$t('bankInList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('bankInList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" @selection-change="selectionChange"  :element-loading-text="$t('bankInList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column type="selection" width="55" :selectable="checkSelectable"></el-table-column>
        <el-table-column fixed prop="formatId" column-key="id"  :label="$t('bankInList.id')" sortable width="160"></el-table-column>
        <el-table-column prop="shopName" column-key="shopId"  :label="$t('bankInList.shopName')" sortable></el-table-column>
        <el-table-column prop="shopClientName"  :label="$t('bankInList.shopClientName')" width="140" ></el-table-column>
        <el-table-column prop="bankName" column-key="bankId"  :label="$t('bankInList.bankName')" sortable></el-table-column>
        <el-table-column prop="amount" :label="$t('bankInList.amount')" ></el-table-column>
        <el-table-column prop="serialNumber" :label="$t('bankInList.serialNumber')" sortable></el-table-column>
        <el-table-column prop="billDate" :label="$t('bankInList.billDate')" width="140" sortable></el-table-column>
        <el-table-column prop="inputDate" :label="$t('bankInList.inputDate')" width="140" sortable></el-table-column>
        <el-table-column prop="createdByName" :label="$t('bankInList.createdBy')" ></el-table-column>
        <el-table-column prop="createdDate" :label="$t('bankInList.createdDate')" ></el-table-column>
        <el-table-column prop="outCode" :label="$t('bankInList.outCode')" sortable></el-table-column>
        <el-table-column prop="processStatus" :label="$t('bankInList.processStatus')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('bankInList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('bankInList.operation')">
          <template scope="scope">
            <div class="action"><el-button size="small"  v-permit="'crm:bankIn:view'" @click.native="itemAction(scope.row.id, 'detail')">{{$t('bankInList.detail')}}</el-button></div>
            <div class="action"><el-button size="small"  v-if="scope.row.auditable"   v-permit="'crm:bankIn:audit'" @click.native="itemAction(scope.row.id, 'audit')">{{$t('bankInList.audit')}}</el-button></div>
            <div class="action"><el-button size="small"  v-if="scope.row.editable"   v-permit="'crm:bankIn:edit'" @click.native="itemAction(scope.row.id, 'edit')">{{$t('bankInList.edit')}}</el-button></div>
            <div class="action"><el-button size="small"  v-if="scope.row.editable" v-permit="'crm:bankIn:delete'" @click.native="itemAction(scope.row.id, 'delete')">{{$t('bankInList.delete')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import accountSelect from 'components/basic/account-select'

  export default{
    components:{
      accountSelect,
    },
    data() {
      return {
        pageLoading: false,
        searchText:"",
        pageHeight:600,
        page:{},
        formData:{
            extra:{}
        },
        formLabelWidth: '120px',
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
        var submitData = util.deleteExtra(this.formData);
        util.setQuery("bankInList",submitData);
        axios.get('/api/ws/future/crm/bankIn?'+qs.stringify(submitData)).then((response) => {
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
        this.$router.push({ name: 'bankInForm'})
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
          });
        }).catch(()=>{});
      },exportData(){
        util.confirmBeforeExportData(this).then(() => {
          axios.get('/api/ws/future/crm/bankIn/export?'+qs.stringify(this.submitData)).then((response)=> {
            window.location.href="/api/general/sys/folderFile/download?id="+response.data;
          });
        }).catch(()=>{});
      }
    },created () {
      let that = this;
      that.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/crm/bankIn/getQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(that.$route.query,that.formData);
        that.pageRequest();
      });

    }
  };
</script>

