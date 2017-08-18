<template>
  <div>
    <head-tab active="adApplyList"></head-tab>
    <div>
      <el-row>
        <su-alert type="success" :text="productCode"> </su-alert>
      </el-row>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:adApply:edit'">{{$t('adApplyList.adApplyForm')}}</el-button>
        <el-button type="primary" @click="itemBillAdd" icon="plus" v-permit="'crm:adApply:bill'">{{$t('adApplyList.adApplyBillForm')}}</el-button>
        <el-button type="primary" @click="grain" icon="plus" v-permit="'crm:adApply:goods'">{{$t('adApplyList.adApplyGoods')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'crm:adApply:view'">{{$t('adApplyList.filter')}}</el-button>
        <el-button type="primary" @click="exportData" icon="upload" v-permit="'crm:adApply:view'">{{$t('adApplyList.export')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible=false" :title="$t('adApplyList.filter')" v-model="formVisible" size="tiny" class="search-form" ref="searchDialog"  z-index="1500">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('adApplyList.shopName')" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off" :placeholder="$t('adApplyList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('adApplyList.productCode')" :label-width="formLabelWidth">
                <el-input v-model="formData.productCode" auto-complete="off" :placeholder="$t('adApplyList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('adApplyList.createdBy')" :label-width="formLabelWidth">
                <account-select v-model="formData.createdBy" @afterInit="setSearchText"></account-select>
              </el-form-item>
              <el-form-item :label="$t('adApplyList.createdDate')" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDate"></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('adApplyList.productName')" :label-width="formLabelWidth">
                <el-input v-model="formData.productName" auto-complete="off" :placeholder="$t('adApplyList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('adApplyList.isBilled')" :label-width="formLabelWidth">
                <bool-select v-model="formData.isBilled"></bool-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('adApplyList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-dialog :title = "$t('adApplyList.editLeftQty')" v-model="leftQtyVisible" size="tiny" class="search-form"  z-index="1500">
        <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('adApplyList.shopName')">{{inputForm.shopName}}
              </el-form-item>
              <el-form-item :label="$t('adApplyList.productName')">{{inputForm.productName}}
              </el-form-item>
              <el-form-item :label="$t('adApplyList.applyQty')">{{inputForm.applyQty}}
              </el-form-item>
              <el-form-item :label="$t('adApplyList.confirmQty')" prop="confirmQty">
                <el-input v-model.number="inputForm.confirmQty" ></el-input>
              </el-form-item>
              <el-form-item :label="$t('adApplyList.billedQty')">{{inputForm.billedQty}}
              </el-form-item>
              <el-form-item :label="$t('adApplyList.leftQty')">{{inputForm.leftQty}}
              </el-form-item>
              <el-form-item :label="$t('adApplyList.remarks')" prop="remarks">
                <el-input v-model="inputForm.remarks" type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="leftQtyVisible = false">{{$t('adApplyList.cancel')}}</el-button>
          <el-button type="primary"  @click="formSubmit()">{{$t('adApplyList.save')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :row-class-name="tableRowClassName" :element-loading-text="$t('adApplyList.loading')" @sort-change="sortChange" border>
        <el-table-column column-key="shopId" prop="shopName" :label="$t('adApplyList.shopName')" sortable></el-table-column>
        <el-table-column prop="createdDate" :label="$t('adApplyList.createdDate')" sortable></el-table-column>
        <el-table-column column-key="createdBy" prop="createdByName" :label="$t('adApplyList.createdBy')" sortable></el-table-column>
        <el-table-column column-key="productId" prop="productCode" :label="$t('adApplyList.productCode')" sortable></el-table-column>
        <el-table-column prop="expiryDateRemarks" :label="$t('adApplyList.expiryDateRemarks')"></el-table-column>
        <el-table-column column-key="productId" prop="productName" :label="$t('adApplyList.product')" sortable></el-table-column>
        <el-table-column prop="applyQty" :label="$t('adApplyList.applyQty')+'('+totalQty.totalApplyQty+')'" sortable></el-table-column>
        <el-table-column prop="confirmQty" :label="$t('adApplyList.confirmQty')+'('+totalQty.totalConfirmQty+')'" sortable></el-table-column>
        <el-table-column prop="billedQty" :label="$t('adApplyList.billedQty')+'('+totalQty.totalBilledQty+')'" sortable></el-table-column>
        <el-table-column prop="leftQty" :label="$t('adApplyList.leftQty')+'('+totalQty.totalLeftQty+')'" sortable></el-table-column>
        <el-table-column prop="orderId" :label="$t('adApplyList.orderId')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('adApplyList.remarks')"></el-table-column>
        <el-table-column :label="$t('adApplyList.operation')" width="140">
          <template scope="scope">
            <div class="action" v-permit="'crm:adApply:bill'"><el-button size="small" @click.native="itemAction(scope.row.id,'edit')">{{$t('adGoodsOrderList.edit')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<style>
  .el-table .reminder_row {
    background: #ebcccc !important;
  }
  .el-table .reminder_row:hover>td{
    background: #ebcccc !important;
  }
</style>
<script>
  import accountSelect from 'components/basic/account-select';
  import boolSelect from 'components/common/bool-select';
  import suAlert from 'components/common/su-alert';
  export default {
    components:{
      accountSelect,boolSelect,suAlert
    },
    data() {
      return {
        page:{},
        searchText:"",
        formData:{
            extra:{}
        },
        inputForm:{

        },
        initPromise:{},
        pageHeight:600,
        formLabelWidth: '120px',
        formVisible: false,
        leftQtyVisible:false,
        pageLoading: false,
        remoteLoading: false,
        totalQty:{
          totalApplyQty:'',
          totalConfirmQty:'',
          totalBilledQty:'',
          totalLeftQty:'',
        },
        productCode:'',
        rules: {
          confirmQty: [{ required: true, message: this.$t('adApplyEditForm.prerequisiteMessage')},{type:"number",message:this.$t('adApplyEditForm.inputLegalValue')}],
        },
      };
    },
    methods: {
      setSearchText(){
        this.$nextTick(function () {
          this.searchText = util.getSearchText(this.$refs.searchDialog);
        })
      },
      tableRowClassName(row, index) {
        if (row.leftQty > 0) {
          return 'reminder_row';
        }else{
          return '';
        }
      },
      pageRequest() {
        this.pageLoading = true;
        this.setSearchText();
        let submitData = util.deleteExtra(this.formData);
        //this.getTotalQty(submitData);
        axios.get('/api/ws/future/layout/adApply/getCountQty',{params:submitData}).then((response) => {
          this.totalQty=response.data;
          axios.get('/api/ws/future/layout/adApply',{params:submitData}).then((response) => {
            this.page = response.data;
            this.pageLoading = false;
          })
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
        this.$router.push({ name: 'adApplyForm'});
      },itemBillAdd(){
        this.$router.push({ name: 'adApplyBillForm'});
      },grain(){
        this.$router.push({name: 'adApplyGoods'});
      },exportData(){
        util.confirmBeforeExportData(this).then(() => {
          window.location.href='/api/ws/future/layout/adApply/export?'+qs.stringify(util.deleteExtra(this.formData));
        }).catch(()=>{});
      },getAllowOrderProductCode(){
          this.productCode = "当前开放的物料编码为:";
          axios.get('api/ws/future/basic/product/findAdProductCodeAndAllowOrder').then((response) =>{
            this.productCode += response.data;
          });
      },itemAction:function(id,action){
        if(action=="edit") {
          this.leftQtyVisible=true;
          let page=this.page.content;
          for(let item in page){
            if(id==page[item].id){
              this.inputForm=page[item];
            }
          }
        }
      },formSubmit(){
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/layout/adApply/saveConfirmQty', qs.stringify(this.inputForm)).then((response) => {
              this.$message(response.data.message);
              this.leftQtyVisible = false;
              this.pageRequest();
            })
          }
        })
      },getTotalQty(query){
          axios.get('/api/ws/future/layout/adApply/getCountQty',{params:query}).then((response) => {
            this.totalQty=response.data;
          })
      }
    },created () {
      let that = this;
      that.pageHeight = window.outerHeight -380;
      this.initPromise = axios.get('/api/ws/future/layout/adApply/getQuery').then((response)=>{
        that.formData = response.data;
      });
    },activated(){
      this.getAllowOrderProductCode();
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
  };
</script>

