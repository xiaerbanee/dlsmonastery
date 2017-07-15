<template>
  <div>
    <head-tab active="productList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="productEdit" icon="edit" v-permit="'crm:product:edit'">{{$t('productList.productEdit')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'crm:product:view'">{{$t('productList.filter')}}</el-button>
        <el-button type="primary" @click="synData"  icon="plus" v-permit="'crm:bank:edit'">{{$t('productList.syn')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible=false" :title="$t('productList.filter')" v-model="formVisible" size="medium" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="8">
            <el-col :span="12">
              <el-form-item :label="$t('productList.name')" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('productList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('productList.hasIme')" :label-width="formLabelWidth">
                <bool-select v-model="formData.hasIme" ></bool-select>
              </el-form-item>
              <el-form-item :label="$t('productList.code')" :label-width="formLabelWidth">
                <el-input v-model="formData.code" auto-complete="off" :placeholder="$t('productList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('productList.visible')" :label-width="formLabelWidth">
                <bool-select v-model="formData.visible" ></bool-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="$t('productList.productType')" :label-width="formLabelWidth">
                <product-type-select v-model="formData.productTypeId" @afterInit="setSearchText"></product-type-select>
              </el-form-item>
              <el-form-item :label="$t('productList.allowOrder')" :label-width="formLabelWidth">
                <bool-select v-model="formData.allowOrder" ></bool-select>
              </el-form-item>
              <el-form-item :label="$t('productList.outGroupName')" :label-width="formLabelWidth">
                <el-select v-model="formData.outGroupName" filterable clearable :placeholder="$t('productList.inputWord')">
                  <el-option v-for="outGroupNames  in formData.extra.outGroupNameList" :key="outGroupNames" :label="outGroupNames" :value="outGroupNames"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('productList.netType')" :label-width="formLabelWidth">
                <el-select v-model="formData.netType" filterable clearable :placeholder="$t('productList.inputKey')">
                  <el-option v-for="netType in formData.extra.netTypeList" :key="netType" :label="netType" :value="netType"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('productList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('productList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="name" :label="$t('productList.name')" sortable width="300"></el-table-column>
        <el-table-column prop="code" :label="$t('productList.code')" sortable></el-table-column>
        <el-table-column prop="netType" :label="$t('productList.netType')" sortable></el-table-column>
        <el-table-column prop="outId" :label="$t('productList.outId')" sortable></el-table-column>
        <el-table-column prop="outGroupId" :label="$t('productList.outGroupId')" sortable></el-table-column>
        <el-table-column prop="outGroupName"  :label="$t('productList.outGroupName')" sortable></el-table-column>
        <el-table-column column-key="productTypeId" prop="productTypeName" :label="$t('productList.productType')" sortable></el-table-column>
        <el-table-column prop="hasIme"  :label="$t('productList.hasIme')" sortable>
          <template scope="scope">
            <el-tag :type="scope.row.hasIme ? 'primary' : 'danger'">{{scope.row.hasIme | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="allowOrder" :label="$t('productList.allowOrder')" sortable>
          <template scope="scope">
            <el-tag :type="scope.row.allowOrder ? 'primary' : 'danger'">{{scope.row.allowOrder | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="visible" :label="$t('productList.visible')" sortable>
          <template scope="scope">
            <el-tag :type="scope.row.visible ? 'primary' : 'danger'">{{scope.row.visible | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="image" :label="$t('productList.image')" sortable></el-table-column>
        <el-table-column prop="remarks" :label="$t('productList.remarks')"></el-table-column>
        <el-table-column prop="locked" :label="$t('productList.locked')" sortable>
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('productList.operation')">
          <template scope="scope">
            <div class="action" v-permit="'crm:shopAdType:edit'"><el-button size="small" @click.native="itemEdit(scope.row.id)">{{$t('shopAdTypeList.edit')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import boolSelect from 'components/common/bool-select'
  import productTypeSelect from 'components/future/product-type-select'
  export default {
    components:{
      boolSelect,
      productTypeSelect
    },
    data() {
      return {
        searchText:"",
        page:{},
        formData:{
            extra:{}
        },
        initPromise:{},
        formLabelWidth: '120px',
        pageHeight:600,
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
        let submitData = util.deleteExtra(this.formData);
        util.setQuery("productList",submitData);
        axios.get('/api/ws/future/basic/product',{params:submitData}).then((response) => {
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
      },productEdit(){
        this.$router.push({ name: 'productAdEdit'})
      },synData(){
        axios.get('/api/ws/future/basic/product/syn').then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        })
      },itemEdit:function(id){
          this.$router.push({ name: 'productForm', query: { id: id }})
      }
    },created () {
        let that = this;
        that.pageHeight = window.outerHeight -320;
        this.initPromise = axios.get('/api/ws/future/basic/product/getQuery').then((response) =>{
          this.formData = response.data;
          util.copyValue(this.$route.query,this.formData);
        });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      })
    }
  };
</script>

