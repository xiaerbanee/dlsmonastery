<template>
  <div>
    <head-tab active="apiCarrierOrder"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" >{{$t('productTypeList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" >{{$t('productTypeList.filter')}}</el-button>
        <el-button type="primary" @click="exportData" >{{$t('productTypeList.export')}}</el-button>
        <el-button type="primary" @click="carrierShip" >商城发货</el-button>
        <el-dropdown split-button type="primary"  @command="handleCommand">
          {{command?command:'没有选中任何选项'}}
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="空值">空值</el-dropdown-item>
            <el-dropdown-item command="已导入">已导入</el-dropdown-item>
            <el-dropdown-item command="待付款">待付款</el-dropdown-item>
            <el-dropdown-item command="验证中">验证中</el-dropdown-item>
            <el-dropdown-item command="验证失败">验证失败</el-dropdown-item>
            <el-dropdown-item command="问题单号">问题单号</el-dropdown-item>
            <el-dropdown-item command="坏单">坏单</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('productTypeList.filter')" v-model="formVisible" size="small" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" label-width="120px">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item label="订货单号">
                <el-input v-model="formData.businessId" :placeholder="$t('productTypeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item label="状态">
                <el-input v-model="formData.CarrierOrderStatus" :placeholder="$t('productTypeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item label="门店">
                <el-input v-model="formData.shopName" :placeholder="$t('productTypeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item label="发货时间" >
                <date-range-picker v-model="formData.shipDate"></date-range-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="订单备注">
                <el-input v-model="formData.remarks" :placeholder="$t('productTypeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item label="订单状态">
                <el-input v-model="formData.status" :placeholder="$t('productTypeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item label="商城单号">
                <el-input v-model="formData.code" :placeholder="$t('productTypeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item label="创建时间">
                <date-range-picker v-model="formData.createdDate"></date-range-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('productTypeList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('productTypeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="goodsOrderStatus" label="状态"  width="150" sortable></el-table-column>
        <el-table-column prop="goodsOrderFormatId" label="货品订货单号" sortable></el-table-column>
        <el-table-column prop="areaName" label="办事处" sortable></el-table-column>
        <el-table-column prop="shopName" label="门店" width="300"></el-table-column>
        <el-table-column prop="carrierShopName" label="商城门店" sortable></el-table-column>
        <el-table-column prop="shipDate" label="发货时间" sortable></el-table-column>
        <el-table-column prop="lastModifiedDate" label="更新时间" sortable></el-table-column>
        <el-table-column prop="code" label="商城单号" sortable></el-table-column>
        <el-table-column prop="status" label="状态" sortable></el-table-column>
        <el-table-column prop="remarks" label="订单备注"></el-table-column>
        <el-table-column fixed="right" :label="$t('productTypeList.operation')" >
          <template scope="scope">
            <div class="action" v-permit="'crm:productType:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'edit')">{{$t('demoPhoneTypeList.edit')}}</el-button></div>
            <div class="action" v-permit="'crm:productType:delete'"><el-button size="small" @click.native="itemAction(scope.row.id,'delete')">{{$t('demoPhoneTypeList.delete')}}</el-button></div>
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
        page:{},
        formData:{
          extra:{}
        },
        initPromise:{},
        searchText:'',
        command:"",
        formVisible: false,
        pageHeight: 600,
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
        let submitData = util.deleteExtra(this.formData);
        util.setQuery("productTypeList",submitData);
        axios.get('/api/ws/future/basic/productType?'+qs.stringify(submitData)).then((response) => {
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
        this.$router.push({ name: 'productTypeForm'})
      },itemAction:function(id,action){
        if(action==="edit") {
          this.$router.push({ name: 'productTypeForm', query: { id: id }})
        } else if(action==="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/basic/productType/delete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
              this.pageRequest();
            });
          }).catch(()=>{});
        }
      },exportData(){
        util.confirmBeforeExportData(this).then(() => {
          axios.get('/api/ws/future/basic/productType/export',{params:util.deleteExtra(this.formData)}).then((response)=> {
            window.location.href="/api/general/sys/folderFile/download?id="+response.data;
          });
        }).catch(()=>{});
      },carrierShip(){

      },
      handleCommand(command) {
        this.command=command;
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      this.initPromise = axios.get('/api/ws/future/basic/productType/getQuery').then((response) =>{
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
