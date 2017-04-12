<template>
  <div>
    <head-tab :active="$t('productImeSaleList.productImeSaleList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:productImeSale:edit'">{{$t('productImeSaleList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:productImeSale:view'">{{$t('productImeSaleList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('productImeSaleList.filter')" v-model="formVisible" size="small" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="8">
              <el-form-item :label="formLabel.name" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('productImeSaleList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.createdDate" type="daterange" align="right"  :placeholder="$t('productImeSaleList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.isBack.label" :label-width="formLabelWidth">
                <el-radio-group v-model="formData.isBack">
                  <el-radio v-for="(value,key) in formProperty.bools" :key="key" :label="value">{{key | bool2str}}</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off"  :placeholder="$t('productImeSaleList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.ime.label" :label-width="formLabelWidth">
                <el-input v-model="formData.ime" auto-complete="off"  :placeholder="$t('productImeSaleList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('productImeSaleList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('productImeSaleList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column  prop="shop.name" :label="$t('productImeSaleList.saleShopName')" width="180" ></el-table-column>
        <el-table-column prop="productIme.ime"  :label="$t('productImeSaleList.ime')" ></el-table-column>
        <el-table-column prop="productIme.product.name" :label="$t('productImeSaleList.productName')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('productImeSaleList.saleDate')"></el-table-column>
        <el-table-column prop="employee.name" :label="$t('productImeSaleList.employeeName')"></el-table-column>
        <el-table-column prop="isBack" :label="$t('productImeSaleList.isBack')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.isBack ? 'primary' : 'danger'">{{scope.row.isBack | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('productImeSaleList.operation')" width="140">
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
        formData:{
          pageNumber:0,
          pageSize:25,
          name:'',
          createdDate:"",
          createdDateBTW:'',
          shopName:"",
          ime:"",
          isBack:"FALSE"
        },formLabel:{
          name:{label:this.$t('productImeSaleList.name')},
          createdDateBTW:{label: this.$t('productImeSaleList.createdDate')},
          shopName:{label:this.$t('productImeSaleList.shopName')},
          ime:{label:this.$t('productImeSaleList.ime')},
          isBack:{label:this.$t('productImeSaleList.isBack'),value:""},
        },
        formProperty:{},
        pickerDateOption:util.pickerDateOption,
        selects:new Array(),
        formLabelWidth: '120px',
        formVisible: false,
        isPageChange:false,
        loading:false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formData.createdDateBTW=util.formatDateRange(this.formData.createdDate);
        this.formLabel.isBack.value = util.bool2str(this.formData.isBack);
        util.setQuery("productImeSaleList",this.formData);
        axios.get('/api/crm/productImeSale',{params:this.formData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        if(this.isPageChange){
          this.formData.pageNumber = pageNumber;
          this.formData.pageSize = pageSize;
          this.pageRequest();
        }
        this.isPageChange = true;
      },sortChange(column) {
        this.formData.order=util.getOrder(column);
        this.formData.pageNumber=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'productImeSaleForm'})
      },itemAction:function(id,action){
        if(action=="删除") {
          axios.get('/api/crm/productImeSale/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      axios.get('/api/crm/productImeSale/getListProperty').then((response) =>{
        this.formProperty=response.data;
        this.pageRequest();
      });
    }
  };
</script>

