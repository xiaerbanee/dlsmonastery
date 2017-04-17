<template>
  <div>
    <head-tab :active="$t('productImeUploadList.productImeUploadList')"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:productImeUpload:edit'">{{$t('productImeUploadList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:productImeUpload:view'">{{$t('productImeUploadList.filter')}}</el-button>
        <el-button type="primary" @click="batchPass" icon="check" v-permit="'crm:productImeUpload:edit'">{{$t('productImeUploadList.batchPass')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('productImeUploadList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="8">
              <el-form-item :label="formLabel.officeId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.officeId" filterable remote :placeholder="$t('productImeUploadList.inputWord')" :remote-method="remoteOffice" :loading="remoteLoading" :clearable=true>
                  <el-option v-for="office in offices" :key="office.id" :label="office.name" :value="office.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.createdDate" type="daterange" align="right"  :placeholder="$t('productImeUploadList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off"  :placeholder="$t('productImeUploadList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.ime.label" :label-width="formLabelWidth">
                <el-input v-model="formData.ime" auto-complete="off"  :placeholder="$t('productImeUploadList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('productImeUploadList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" @selection-change="selectionChange"   :element-loading-text="$t('productImeUploadList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column type="selection" width="55" :selectable="checkSelectable"></el-table-column>
        <el-table-column  prop="month" :label="$t('productImeUploadList.month')" width="180" ></el-table-column>
        <el-table-column prop="shop.name" :label="$t('productImeUploadList.updateShopName')"  ></el-table-column>
        <el-table-column prop="productIme.ime" :label="$t('productImeUploadList.ime')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('productImeUploadList.updateDate')"></el-table-column>
        <el-table-column prop="employee.name" :label="$t('productImeUploadList.employeeName')"></el-table-column>
        <el-table-column prop="status"  :label="$t('productImeUploadList.status')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.status=='已通过' ? 'primary' : 'danger'">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('productImeUploadList.operation')" width="140">
          <template scope="scope">
            <div v-for="action in scope.row.actionList" :key="action" class="action">
              <el-button size="small" @click.native="itemAction(scope.row.id,action)">{{action}}</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        pageLoading: false,
        pageHeight:600,
        page:{},
        offices:[],
        formData:{
          page:0,
          size:25,
          officeId:'',
          createdDate:"",
          createdDateBTW:'',
          shopName:"",
          ime:""
        },formLabel:{
          officeId:{label:this.$t('productImeUploadList.officeId')},
          createdDateBTW:{label: this.$t('productImeUploadList.createdDate')},
          shopName:{label:this.$t('productImeUploadList.shopName')},
          ime:{label:this.$t('productImeUploadList.ime')},
        },
        formProperty:{},
        pickerDateOption:util.pickerDateOption,
        selects:new Array(),
        formLabelWidth: '120px',
        formVisible: false,
        isPageChange:false,
        remoteLoading:false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formData.createdDateBTW=util.formatDateRange(this.formData.createdDate);
        util.setQuery("productImeUploadList",this.formData);
        axios.get('/api/crm/productImeUpload',{params:this.formData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        if(this.isPageChange){
          this.formData.page = pageNumber;
          this.formData.size = pageSize;
          this.pageRequest();
        }
        this.isPageChange = true;
      },sortChange(column) {
        this.formData.order=util.getOrder(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'productImeUploadForm'})
      },itemAction:function(id,action){
        if(action=="删除") {
          axios.get('/api/crm/productImeSale/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      },selectionChange(selection){
        console.log(selection);
        this.selects=new Array();
        for(var key in selection){
          this.selects.push(selection[key].id)
        }
      },batchPass(){
        axios.get('/api/crm/productImeUpload/audit',{params:{ids:this.selects,pass:true}}).then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        });
      },checkSelectable(row) {
        return row.status === '申请中'
      },remoteOffice(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/hr/office/search',{params:{name:query}}).then((response)=>{
            this.offices=response.data;
            this.remoteLoading = false;
          })
        }
      },
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      axios.get('/api/crm/productImeSale/getQuery').then((response) =>{
        this.formProperty=response.data;
        this.pageRequest();
      });
    }
  };
</script>

