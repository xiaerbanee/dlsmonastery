<template>
  <div>
    <head-tab active="shopPrintList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopPrint:edit'">{{$t('shopPrintList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:shopPrint:view'">{{$t('shopPrintList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('shopPrintList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('shopPrintList.officeId')" :label-width="formLabelWidth">
                <office-select v-model="formData.officeId"></office-select>
              </el-form-item>
              <el-form-item :label="$t('shopPrintList.printType')" :label-width="formLabelWidth">
                <dict-map-select v-model="formData.printType" category="门店_广告印刷"></dict-map-select>
              </el-form-item>
              <el-form-item :label="$t('shopPrintList.processStatus')" :label-width="formLabelWidth">
                <process-status-select v-model="formData.processStatus" type="广告物料及制作申请"></process-status-select>
              </el-form-item>
              <el-form-item :label="$t('shopPrintList.createdBy')" :label-width="formLabelWidth">
                <account-select  v-model="formData.createdBy"></account-select>
            </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('shopPrintList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('shopPrintList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('shopPrintList.code')" sortable width="150"></el-table-column>
        <el-table-column column-key="officeId" prop="officeName" :label="$t('shopPrintList.officeId')" sortable></el-table-column>
        <el-table-column prop="printType" :label="$t('shopPrintList.printType')" sortable></el-table-column>
        <el-table-column prop="qty" :label="$t('shopPrintList.qty')" sortable></el-table-column>
        <el-table-column prop="content":label="$t('shopPrintList.content')" width="450" sortable></el-table-column>
        <el-table-column prop="address" :label="$t('shopPrintList.address')" width="150" sortable></el-table-column>
        <el-table-column prop="contator" :label="$t('shopPrintList.contact')" sortable></el-table-column>
        <el-table-column prop="mobilePhone" :label="$t('shopPrintList.mobilePhone')" sortable></el-table-column>
        <el-table-column column-key="createdBy" prop="createdByName" :label="$t('shopPrintList.createdBy')" sortable></el-table-column>
        <el-table-column prop="createdDate" :label="$t('shopPrintList.createdDate')" sortable></el-table-column>
        <el-table-column prop="processStatus" :label="$t('shopPrintList.processStatus')" width="150" sortable>
          <template scope="scope">
            <el-tag :type="scope.row.processStatus=='已通过' ? 'primary' : 'danger'">{{scope.row.processStatus}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('shopPrintList.operation')" width="140">
          <template scope="scope">
            <div class="action" v-permit="'crm:shopPrint:view'"><el-button size="small" @click.native="itemAction(scope.row.id,'detail')">{{$t('shopPrintList.detail')}}</el-button></div>
            <div class="action" v-if="scope.row.isAuditable&&scope.row.processStatus!='已通过'&&scope.row.processStatus!='未通过'" v-permit="'crm:shopPrint:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'audit')">{{$t('shopPrintList.audit')}}</el-button></div>
            <div class="action" v-if="scope.row.isEditable" v-permit="'crm:shopPrint:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'edit')">{{$t('shopPrintList.edit')}}</el-button></div>
            <div class="action" v-if="scope.row.isEditable" v-permit="'crm:shopPrint:delete'"><el-button size="small" @click.native="itemAction(scope.row.id,'delete')">{{$t('shopPrintList.delete')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import officeSelect from 'components/basic/office-select';
  import dictMapSelect from 'components/basic/dict-map-select';
  import accountSelect from 'components/basic/account-select'
  import processStatusSelect from 'components/general/process-status-select'
  export default {
    components:{officeSelect,dictMapSelect,accountSelect,processStatusSelect},
    data() {
      return {
        searchText:"",
        page:{},
        formData:{
          extra:{}
        },
        initPromise:{},
        formLabelWidth: '120px',
        formVisible: false,
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
        var submitData = util.deleteExtra(this.formData);
        util.setQuery("shopPrintList",submitData);
        axios.get('/api/ws/future/layout/shopPrint',{params:submitData}).then((response) => {
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
        this.$router.push({ name: 'shopPrintForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'shopPrintForm', query: { id: id }})
        } else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/layout/shopPrint/delete', {params: {id: id}}).then((response) => {
              this.$message(response.data.message);
              this.pageRequest();
            })
          })
        }else if(action == "detail"||action == "audit"){
           this.$router.push({ name: 'shopPrintDetail', query: { id: id,action:action}});
        }
      }
    },created () {
      var that = this;
      that.pageHeight = window.outerHeight -320;
      this.initPromise = axios.get('/api/ws/future/layout/shopPrint/getQuery').then((response) =>{
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

