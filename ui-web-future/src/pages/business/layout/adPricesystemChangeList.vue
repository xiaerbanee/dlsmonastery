<template>
  <div>
    <head-tab :active="$t('adPricesystemChangeList.adPricesystemChangeList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus"  v-permit="'crm:adPricesystemChange:edit'">{{$t('adPricesystemChangeList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:adPricesystemChange:view'">{{$t('adPricesystemChangeList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('adPricesystemChangeList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.productName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.productName" auto-complete="off" :placeholder="$t('adPricesystemChangeList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('adPricesystemChangeList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('adPricesystemChangeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="product.code" :label="$t('adPricesystemChangeList.productCode')" sortable width="150"></el-table-column>
        <el-table-column prop="product.name" :label="$t('adPricesystemChangeList.productName')"></el-table-column>
        <el-table-column prop="adPricesystem.name" :label="$t('adPricesystemChangeList.adPricesystem')"></el-table-column>
        <el-table-column prop="oldPrice" :label="$t('adPricesystemChangeList.oldPrice')"></el-table-column>
        <el-table-column prop="newPrice" :label="$t('adPricesystemChangeList.newPrice')"></el-table-column>
        <el-table-column prop="created.loginName" :label="$t('adPricesystemChangeList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('adPricesystemChangeList.createdDate')"></el-table-column>

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
          page:0,
          size:25,
          productName:''
        },formLabel:{
          productName:{label:this.$t('adPricesystemChangeList.productName')}
        },
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("adPricesystemChangeList",this.formData);
        axios.get('/api/crm/adPricesystemChange',{params:this.formData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getOrder(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'adPricesystemChangeForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'adPricesystemChangeForm', query: { id: id }})
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.pageRequest();
    }
  };
</script>

