<template>
  <div>
    <head-tab active="adPricesystemChangeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus"  v-permit="'crm:adPricesystemChange:edit'">{{$t('adPricesystemChangeList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'crm:adPricesystemChange:view'">{{$t('adPricesystemChangeList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('adPricesystemChangeList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('adPricesystemChangeList.productName')" :label-width="formLabelWidth">
                <el-input v-model="formData.productName" auto-complete="off" :placeholder="$t('adPricesystemChangeForm.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('adPricesystemChangeList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('adPricesystemChangeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column column-key="productId" prop="productCode" :label="$t('adPricesystemChangeList.productCode')" sortable width="150"></el-table-column>
        <el-table-column column-key="productId" prop="productName" :label="$t('adPricesystemChangeList.productName')" sortable></el-table-column>
        <el-table-column column-key="adPricesystemId" prop="adPricesystemName" :label="$t('adPricesystemChangeList.adPricesystem')" sortable></el-table-column>
        <el-table-column prop="oldPrice" :label="$t('adPricesystemChangeList.oldPrice')" sortable></el-table-column>
        <el-table-column prop="newPrice" :label="$t('adPricesystemChangeList.newPrice')" sortable></el-table-column>
        <el-table-column column-key="createdBy" prop="createdByName" :label="$t('adPricesystemChangeList.createdBy')" sortable></el-table-column>
        <el-table-column prop="createdDate" :label="$t('adPricesystemChangeList.createdDate')" sortable></el-table-column>
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
        searchText:"",
        pageLoading: false,
        pageHeight:600,
        page:{},
        formData:{
          extra:{}
        },
        initPromise:{},
        formLabelWidth: '120px',
        formVisible: false,
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
        util.setQuery("adPricesystemChangeList",submitData);
        axios.get('/api/ws/future/layout/adPricesystemChange',{params:submitData}).then((response) => {
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
        this.$router.push({ name: 'adPricesystemChangeForm'})
      }
    },created () {
       this.pageHeight = 0.75*window.innerHeight;
      this.initPromise = axios.get('/api/ws/future/layout/adPricesystemChange/getQuery').then((response) => {
        this.formData = response.data;
        util.copyValue(this.$route.query,this.formData);
      })
    },activated() {
      this.initPromise.then(()=>{
        this.pageRequest();
      })
    }
  };
</script>

