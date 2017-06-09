<template>
  <div>
    <head-tab active="productImeSaleList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="sale" icon="plus" v-permit="'crm:productImeSale:edit'">{{$t('productImeSaleList.sale')}}</el-button>
        <el-button type="primary" @click="saleBack" icon="minus"  v-permit="'crm:productImeSale:back'">{{$t('productImeSaleList.saleBack')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:productImeSale:view'">{{$t('productImeSaleList.filter')}}</el-button>
        <el-button type="primary" @click="exportData"  v-permit="'crm:productImeSale:view'">{{$t('productImeSaleList.export')}}</el-button>

        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('productImeSaleList.filter')" v-model="formVisible" size="small" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="8">
              <el-form-item :label="formLabel.employeeId.label" :label-width="formLabelWidth">
                <employee-select v-model="formData.employeeId"></employee-select>
              </el-form-item>
              <el-form-item :label="formLabel.createdDateRange.label" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="formLabel.isBack.label" :label-width="formLabelWidth">
                <bool-select v-model="formData.isBack"></bool-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off"  :placeholder="$t('productImeSaleList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.ime.label" :label-width="formLabelWidth">
                <el-input v-model="formData.ime" auto-complete="off"  :placeholder="$t('productImeSaleList.inputImeTail')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('productImeSaleList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('productImeSaleList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="shopName" column-key="shopId" :label="$t('productImeSaleList.saleShopName')" width="180" sortable></el-table-column>
        <el-table-column prop="productImeIme"  :label="$t('productImeSaleList.ime')" ></el-table-column>
        <el-table-column prop="productImeProductName" :label="$t('productImeSaleList.productName')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('productImeSaleList.saleDate')" sortable></el-table-column>
        <el-table-column prop="employeeName" column-key="employeeId" :label="$t('productImeSaleList.employeeName')" sortable></el-table-column>
        <el-table-column prop="isBack" :label="$t('productImeSaleList.isBack')" width="70">
          <template scope="scope">
            <el-tag :type="scope.row.isBack ? 'primary' : 'danger'">{{scope.row.isBack | bool2str}}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>

  import boolSelect from 'components/common/bool-select'
  import employeeSelect from 'components/basic/employee-select'
  export default{
    components:{

      boolSelect,
      employeeSelect,
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
          employeeId:'',
          createdDateRange:"",
          shopName:"",
          ime:"",
          isBack:'',
        },formLabel:{
          employeeId:{label:this.$t('productImeSaleList.employeeName')},
          createdDateRange:{label: this.$t('productImeSaleList.createdDate')},
          shopName:{label:this.$t('productImeSaleList.shopName')},
          ime:{label:this.$t('productImeSaleList.ime')},
          isBack:{label:this.$t('productImeSaleList.isBack')},
        },
        formLabelWidth: '120px',
        formVisible: false,
        loading:false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.copyValue(this.formData,this.submitData);
        util.setQuery("productImeSaleList",this.submitData);
        axios.get('/api/ws/future/crm/productImeSale?'+qs.stringify(this.submitData)).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        }).catch(()=>{
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
      },exportData(){
        util.confirmBeforeExportData(this).then(() => {
          axios.get('/api/ws/future/crm/productImeSale/export',{params:this.submitData}).then((response)=> {
            window.location.href="/api/general/sys/folderFile/download?id="+response.data;
          });
        }).catch(()=>{});

      },sale(){
        this.$router.push({ name: 'productImeSaleForm'});
      },saleBack(){
        this.$router.push({ name: 'productImeSaleBackForm'});
      },itemAction:function(id,action){
        if(action==="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/crm/productImeSale/delete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
              this.pageRequest();
            });
          }).catch(()=>{});
        }
      }
    },created () {

      let that = this;
      that.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/crm/productImeSale/getQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(that.$route.query,that.formData);
        that.pageRequest();
      });

    }
  };
</script>

