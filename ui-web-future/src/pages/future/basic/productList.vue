<template>
  <div>
    <head-tab active="productList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="productEdit" icon="edit" v-permit="'crm:product:edit'">{{$t('productList.productEdit')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:product:view'">{{$t('productList.filter')}}</el-button>
        <el-button type="primary" @click="synData"  icon="plus" v-permit="'crm:bank:edit'">{{$t('productList.syn')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('productList.filter')" v-model="formVisible"  size="small" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="8">
            <el-col :span="12">
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('productList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.hasIme.label" :label-width="formLabelWidth">
                <el-select v-model="formData.hasIme" filterable clearable :placeholder="$t('productList.inputKey')">
                  <el-option v-for="(item,key) in formProperty.boolMap" :key="key" :label="item | bool2str" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.code.label" :label-width="formLabelWidth">
                <el-input v-model="formData.code" auto-complete="off" :placeholder="$t('productList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.allowBill.label" :label-width="formLabelWidth">
                <el-select v-model="formData.allowBill" filterable clearable :placeholder="$t('productList.inputKey')">
                  <el-option v-for="(item,key) in formProperty.boolMap" :key="key" :label="item | bool2str " :value="item"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="formLabel.productType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.productType" filterable clearable :placeholder="$t('productList.inputKey')">
                  <el-option v-for="productType in formProperty.productTypeList" :key="productType.name" :label="productType.name" :value="productType.name"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.allowOrder.label" :label-width="formLabelWidth">
                <el-select v-model="formData.allowOrder" filterable clearable :placeholder="$t('productList.inputKey')">
                  <el-option v-for="(item,key) in formProperty.boolMap" :key="key" :label="item | bool2str " :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.outGroupName.label" :label-width="formLabelWidth">
                <el-select v-model="formData.outGroupName" filterable clearable :placeholder="$t('productList.inputWord')">
                  <el-option v-for="product  in formProperty.outGroupNameList" :key="product.outGroupName" :label="product.outGroupName" :value="product.outGroupName"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.netType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.netType" filterable clearable :placeholder="$t('productList.inputKey')">
                  <el-option v-for="netType in formProperty.netTypeList" :key="netType" :label="netType" :value="netType"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('productList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('productList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="name" :label="$t('productList.name')" sortable width="300"></el-table-column>
        <el-table-column prop="code" :label="$t('productList.code')"></el-table-column>
        <el-table-column prop="netType" :label="$t('productList.netType')"></el-table-column>
        <el-table-column prop="outId" label="outId"></el-table-column>
        <el-table-column prop="outGroupId" :label="$t('productList.outGroupId')"></el-table-column>
        <el-table-column prop="outGroupName"  :label="$t('productList.outGroupName')"></el-table-column>
        <el-table-column prop="productTypeName" :label="$t('productList.productType')"></el-table-column>
        <el-table-column prop="hasIme"  :label="$t('productList.hasIme')">
          <template scope="scope">
            <el-tag :type="scope.row.hasIme ? 'primary' : 'danger'">{{scope.row.hasIme | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="allowOrder" :label="$t('productList.allowOrder')">
          <template scope="scope">
            <el-tag :type="scope.row.allowOrder ? 'primary' : 'danger'">{{scope.row.allowOrder | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="allowBill" :label="$t('productList.allowBill')">
          <template scope="scope">
            <el-tag :type="scope.row.allowBill ? 'primary' : 'danger'">{{scope.row.allowBill | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="image" :label="$t('productList.image')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('productList.remarks')"></el-table-column>
        <el-table-column prop="locked" :label="$t('productList.locked')" >
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('productList.operation')" width="140">
          <template scope="scope">
              <el-button size="small"  v-permit="'crm:shopAdType:edit'" @click.native="itemEdit(scope.row.id)">{{$t('shopAdTypeList.edit')}}</el-button>
              <el-button size="small"  v-permit="'crm:shopAdType:delete'" @click.native="itemDelete(scope.row.id)">{{$t('shopAdTypeList.delete')}}</el-button>
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
          page:0,
          size:25,
          name:'',
          type:'',
          hasIme:'',
          allowBill:'',
          productType:'',
          allowOrder:'',
          outGroupName:'',
          netType:'',
        },formLabel:{
          name:{label:this.$t('productList.name')},
          hasIme:{label:this.$t('productList.hasIme'),value:""},
          code:{label:this.$t('productList.code')},
          allowBill:{label:this.$t('productList.allowBill'),value:""},
          productType:{label:this.$t('productList.productType'),value:""},
          allowOrder:{label:this.$t('productList.allowOrder'),value:""},
          outGroupName:{label:this.$t('productList.outGroupName')},
          netType:{label:this.$t('productList.netType')}
        },
        formProperty:{},
        formLabelWidth: '120px',
        pageHeight:'',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formLabel.hasIme.value = util.bool2str(this.formData.hasIme);
        this.formLabel.allowBill.value = util.bool2str(this.formData.allowBill);
        this.formLabel.allowOrder.value =  util.bool2str(this.formData.allowOrder);
        this.formLabel.productType.value = util.getLabel(this.formProperty.productTypes, this.formData.productType);
        util.setQuery("productList",this.formData);
        axios.get('/api/ws/future/basic/product',{params:this.formData}).then((response) => {
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
      },productEdit(){
        this.$router.push({ name: 'productAdEdit'})
      },synData(){
        axios.get('/api/ws/future/basic/product/syn').then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        })
      },itemEdit:function(id){
          this.$router.push({ name: 'productForm', query: { id: id }})
      },itemDelete:function(id){
            util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/basic/product/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        });
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/basic/product/getQuery').then((response) =>{
        this.formProperty = response.data;
        this.pageRequest();
      });
    }
  };
</script>

