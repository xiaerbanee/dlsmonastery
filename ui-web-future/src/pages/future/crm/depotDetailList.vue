<template>
  <div>
    <head-tab active="depotDetailList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:depotDetail:view'">{{$t('depotDetailList.filterOrExport')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="upload2" v-permit="'crm:depotDetail:edit'">{{$t('depotDetailList.syn')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('depotDetailList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData" >
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.depotName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.depotName" auto-complete="off" :placeholder="$t('depotDetailList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.productName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.productName" auto-complete="off" :placeholder="$t('depotDetailList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.hasIme.label" :label-width="formLabelWidth">
                <el-select v-model="formData.hasIme" filterable clearable :placeholder="$t('depotDetailList.inputKey')">
                  <el-option v-for="(value,key) in formProperty.bools" :key="key" :label="key | bool2str" :value="value"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.isSame.label" :label-width="formLabelWidth">
                <el-select v-model="formData.isSame" filterable clearable :placeholder="$t('depotDetailList.inputKey')">
                  <el-option v-for="(value,key) in formProperty.bools" :key="key" :label="key | bool2str" :value="value"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="exportData" v-permit="'crm:depotDetail:edit'">{{$t('depotDetailList.export')}}</el-button>
          <el-button type="primary" @click="search()">{{$t('depotDetailList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('depotDetailList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="depot.name" :label="$t('depotDetailList.shopName')" ></el-table-column>
        <el-table-column prop="product.name" :label="$t('depotDetailList.productName')" ></el-table-column>
        <el-table-column prop="hasIme" :label="$t('depotDetailList.hasIme')" width="150" sortable>
          <template scope="scope">
            <el-tag :type="scope.row.hasIme ? 'primary' : 'danger'">{{scope.row.hasIme | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="outQty" :label="$t('depotDetailList.outQty')" sortable></el-table-column>
        <el-table-column prop="qty" :label="$t('depotDetailList.qty')" sortable></el-table-column>
        <el-table-column prop="isSame" :label="$t('depotDetailList.isSame')" width="150" sortable>
          <template scope="scope">
            <el-tag :type="scope.row.isSame ? 'primary' : 'danger'">{{scope.row.isSame | bool2str}}</el-tag>
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
          depotName:'',
          productName:'',
          hasIme:'',
          isSame:''
        },formLabel:{
          depotName:{label:this.$t('depotDetailList.shopName')},
          productName:{label:this.$t('depotDetailList.productName')},
          hasIme:{label:this.$t('depotDetailList.hasIme'),value:""},
          isSame:{label:this.$t('depotDetailList.isSame'),value:""}
        },
        pickerDateOption:util.pickerDateOption,
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formLabel.hasIme.value = util.bool2str(this.formData.hasIme);
        this.formLabel.isSame.value= util.bool2str(this.formData.isSame);
        util.setQuery("depotDetailList",this.formData);
        axios.get('/api/crm/depotDetail',{params:this.formData}).then((response) => {
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
      },exportData(){
       	window.location.href="/api/crm/depotDetail/export?"+qs.stringify(this.formData);
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      axios.get('/api/crm/depotDetail/getQuery').then((response) =>{
        this.formProperty=response.data;
        this.pageRequest();
      });
    }
  };
</script>

