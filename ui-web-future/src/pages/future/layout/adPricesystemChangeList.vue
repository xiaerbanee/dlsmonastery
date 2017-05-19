<template>
  <div>
    <head-tab active="adPricesystemChangeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus"  v-permit="'crm:adPricesystemChange:edit'">{{$t('adPricesystemChangeList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:adPricesystemChange:view'">{{$t('adPricesystemChangeList.filter')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('adPricesystemChangeList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.productName.label" :label-width="formLabelWidth">
                <product-select v-model="formData.productId"></product-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('adPricesystemChangeList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('adPricesystemChangeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="productCode" :label="$t('adPricesystemChangeList.productCode')" sortable width="150"></el-table-column>
        <el-table-column prop="productName" :label="$t('adPricesystemChangeList.productName')"></el-table-column>
        <el-table-column prop="adPricesystemName" :label="$t('adPricesystemChangeList.adPricesystem')"></el-table-column>
        <el-table-column prop="oldPrice" :label="$t('adPricesystemChangeList.oldPrice')"></el-table-column>
        <el-table-column prop="newPrice" :label="$t('adPricesystemChangeList.newPrice')"></el-table-column>
        <el-table-column prop="createdByName" :label="$t('adPricesystemChangeList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('adPricesystemChangeList.createdDate')"></el-table-column>

      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import productSelect from 'components/future/product-select';
  export default {
    components:{productSelect},
    data() {
      return {
        pageLoading: false,
        pageHeight:600,
        page:{},
        formData:{},
        submitData:{
          page:0,
          size:25,
          productId:''
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
        util.copyValue(this.formData,this.submitData);
        util.setQuery("adPricesystemChangeList",this.submitData);
        axios.get('/api/ws/future/layout/adPricesystemChange',{params:this.submitData}).then((response) => {
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
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/layout/adPricesystemChange/getQuery').then((response) => {
        this.formData = response.data;
        util.copyValue(this.$route.query,this.formData);
        this.pageRequest();
      })
    }
  };
</script>

