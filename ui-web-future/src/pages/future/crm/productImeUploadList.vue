<template>
  <div>
    <head-tab active="productImeUploadList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:productImeUpload:edit'">{{$t('productImeUploadList.upload')}}</el-button>
        <el-button type="primary" @click="itemBack" icon="minus" v-permit="'crm:productImeUpload:edit'">{{$t('productImeUploadList.back')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:productImeUpload:view'">{{$t('productImeUploadList.filter')}}</el-button>
        <el-button type="primary" @click="batchPass" icon="check" v-permit="'crm:productImeUpload:edit'">{{$t('productImeUploadList.batchPass')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('productImeUploadList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.officeId.label" :label-width="formLabelWidth">
                <office-select v-model="formData.officeId"></office-select>
              </el-form-item>
              <el-form-item :label="formLabel.month.label" :label-width="formLabelWidth">
                <month-picker  v-model="formData.month" ></month-picker>
              </el-form-item>
              <el-form-item :label="formLabel.createdDateRange.label" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDateRange"></date-range-picker>
              </el-form-item>
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off"  :placeholder="$t('productImeUploadList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.imeOrMeids.label"  :label-width="formLabelWidth">
                <el-input  type="textarea" v-model="formData.imeOrMeids"  :placeholder="$t('productImeUploadList.imeOrMeidsMultiLine')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="exportData()" v-permit="'crm:productImeUpload:view'">{{$t('productImeUploadList.export')}}</el-button>
          <el-button type="primary" @click="search()">{{$t('productImeUploadList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" @selection-change="selectionChange"   :element-loading-text="$t('productImeUploadList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column type="selection" width="55" :selectable="checkSelectable"></el-table-column>
        <el-table-column  prop="month" :label="$t('productImeUploadList.month')" width="180" ></el-table-column>
        <el-table-column prop="shopName" :label="$t('productImeUploadList.updateShopName')"  ></el-table-column>
        <el-table-column prop="productImeIme" :label="$t('productImeUploadList.ime')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('productImeUploadList.updateDate')"></el-table-column>
        <el-table-column prop="employeeName" :label="$t('productImeUploadList.employeeName')"></el-table-column>
        <el-table-column prop="status"  :label="$t('productImeUploadList.status')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.status=='已通过' ? 'primary' : 'danger'">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>

      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>

  import officeSelect from 'components/basic/office-select'
  import monthPicker from 'components/common/month-picker'

  export default{
    components:{
      officeSelect,
      monthPicker,

    },
    data() {
      return {
        pageLoading: false,
        pageHeight:600,
        page:{},

        formData:{},
        submitData:{
          page:0,
          size:25,
          sort:"id,DESC",
          officeId:'',
          createdDateRange:'',
          shopName:"",
          imeOrMeids:"",
          month:"",
        },formLabel:{
          officeId:{label:this.$t('productImeUploadList.officeId')},
          createdDateRange:{label: this.$t('productImeUploadList.createdDate')},
          shopName:{label:this.$t('productImeUploadList.shopName')},
          imeOrMeids:{label:this.$t('productImeUploadList.imeOrMeids')},
          month:{label:this.$t('productImeUploadList.month')},
        },

        selects:[],
        formLabelWidth: '120px',
        formVisible: false,

      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.copyValue(this.formData,this.submitData);
        util.setQuery("productImeUploadList",this.submitData);
        axios.get('/api/ws/future/crm/productImeUpload',{params:this.submitData}).then((response) => {
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
        this.$router.push({ name: 'productImeUploadForm'});
      },itemBack(){
        this.$router.push({ name: 'productImeUploadBackForm'});
      },exportData(){

      },selectionChange(selection){
        console.log(selection);
        this.selects=[];
        for(let each of selection){
          this.selects.push(each.id)
        }
      },batchPass(){

        if(!this.selects || this.selects.length < 1){
          this.$message(this.$t('productImeUploadList.noSelectionFound'));
          return ;
        }

        util.confirmBeforeBatchPass(this).then(() => {
          this.submitDisabled = true;
          this.pageLoading = true;
          axios.get('/api/ws/future/crm/productImeUpload/batchAudit',{params:{ids:this.selects, pass:true}}).then((response) =>{
            this.$message(response.data.message);
            this.pageLoading = false;
            this.submitDisabled = false;
            this.pageRequest();
          });
        }).catch(()=>{});


      },checkSelectable(row) {
        return row.status === '申请中';
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/crm/productImeUpload/getQuery').then((response) =>{
        this.formData=response.data;
        util.copyValue(this.$route.query,this.formData);
        this.pageRequest();
      });
    }
  };
</script>

