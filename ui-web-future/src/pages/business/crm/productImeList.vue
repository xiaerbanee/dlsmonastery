<template>
  <div>
    <head-tab active="productImeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('productImeList.filterOrExport')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('productImeList.filter')" v-model="formVisible" size="small" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="formLabel.ime.label" :label-width="formLabelWidth">
                <el-input v-model="formData.ime" ></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.boxIme.label" :label-width="formLabelWidth">
                <el-input v-model="formData.boxIme" auto-complete="off" :placeholder="$t('productImeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.ime2.label" :label-width="formLabelWidth">
                <el-input v-model="formData.ime2" auto-complete="off" :placeholder="$t('productImeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.meid.label" :label-width="formLabelWidth">
                <el-input v-model="formData.meid" auto-complete="off" :placeholder="$t('productImeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.depotName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.depotName" auto-complete="off" :placeholder="$t('productImeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.inputType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.inputType" clearable filterable :placeholder="$t('productImeList.selectInputType')">
                  <el-option v-for="item in formProperty.inputTypes" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.imes.label" :label-width="formLabelWidth">
                <el-input  type="textarea" :rows="2"  v-model="formData.imes" auto-complete="off" :placeholder="$t('productImeList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="formLabel.productName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.productName" auto-complete="off" :placeholder="$t('productImeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.createdDate" type="daterange" align="right"  :placeholder="$t('productImeList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.retailDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.retailDate" type="daterange" align="right"  :placeholder="$t('productImeList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.saleDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.saleDate" type="daterange" align="right"  :placeholder="$t('productImeList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.createTimeBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.createTime" type="daterange" align="right"  :placeholder="$t('productImeList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.meids.label" :label-width="formLabelWidth">
                <el-input type="textarea" :rows="2" v-model="formData.meids" auto-complete="off" :placeholder="$t('productImeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.month.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.month" type="month" align="right"  :placeholder="$t('productImeList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="exportData" v-permit="'crm:productIme:view'">{{$t('productImeList.export')}}</el-button>
          <el-button type="primary" @click="search()">{{$t('productImeList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('productImeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="ime" :label="$t('productImeList.ime')" sortable width="160"></el-table-column>
        <el-table-column prop="ime2"  :label="$t('productImeList.ime2')" sortable ></el-table-column>
        <el-table-column prop="meid" :label="$t('productImeList.meid')"></el-table-column>
        <el-table-column prop="retailDate" :label="$t('productImeList.retailDate')"></el-table-column>
        <el-table-column prop="productImeSale.createdDate" :label="$t('productImeList.saleDate')"></el-table-column>
        <el-table-column prop="productImeUpload.createdDate" :label="$t('productImeList.uploadDate')"></el-table-column>
        <el-table-column prop="depot.name"  :label="$t('productImeList.depotName')"></el-table-column>
        <el-table-column prop="product.netType"  :label="$t('productImeList.netType')" width="80"></el-table-column>
        <el-table-column prop="product.name"  :label="$t('productImeList.productType')"></el-table-column>
        <el-table-column prop="inputType" :label="$t('productImeList.inputType')"></el-table-column>
        <el-table-column prop="createdTime" :label="$t('productImeList.createdTime')"></el-table-column>
        <el-table-column prop="locked" :label="$t('productImeList.locked')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'danger' : 'primary'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" :label="$t('productImeList.enabled')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'primary' : 'danger'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" :label="$t('productImeList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('productImeList.operation')" width="140">
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
          page:0,
          size:25,
          ime:'',
          ime2:'',
          boxIme:'',
          meid:'',
          depotName:'',
          inputType:'',
          imes:'',
          productName:'',
          createdDate:"",
          createdDateBTW:'',
          retailDate:"",
          retailDateBTW:'',
          saleDate:"",
          saleDateBTW:'',
          createTime:"",
          createTimeBTW:'',
          meids:'',
          month:''
        },formLabel:{
          ime:{label:this.$t('productImeList.ime')},
          ime2:{label:this.$t('productImeList.ime2')},
          boxIme:{label:this.$t('productImeList.boxIme')},
          meid:{label:this.$t('productImeList.meid')},
          depotName:{label:this.$t('productImeList.depotName')},
          inputType:{label:this.$t('productImeList.inputType')},
          imes:{label:this.$t('productImeList.mutilIme')},
          productName:{label:this.$t('productImeList.productType')},
          createdDateBTW:{label: this.$t('productImeList.createdDate')},
          retailDateBTW:{label:this.$t('productImeList.retailDateBtw')},
          saleDateBTW:{label:this.$t('productImeList.saleDate')},
          createTimeBTW:{label:this.$t('productImeList.createTimeBtw')},
          meids:{label:this.$t('productImeList.meids')},
          month:{label:this.$t('productImeList.month')}
        },
        formProperty:{},
        pickerDateOption:util.pickerDateOption,
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
        this.formData.retailDateBTW=util.formatDateRange(this.formData.retailDate);
        this.formData.saleDateBTW=util.formatDateRange(this.formData.saleDate);
        this.formData.createTimeBTW=util.formatDateRange(this.formData.createTime);

        util.setQuery("productImeList",this.formData);
        axios.get('/api/crm/productIme',{params:this.formData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        if(this.isPageChange){
          this.formData.page = pageNumber;
          this.formData.size = pageSize;
          this.pageRequest();
        }
        this.isPageChange = true;
      },sortChange(column) {
        this.formData.order=util.getOrder(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){

      },itemAction:function(id,action){
        if(action=="详情") {
          this.$router.push({ name: '串码列表详情', query: { id: id }})
        }
      },exportData(){
        window.location.href= "/api/crm/productIme/export?"+qs.stringify(this.formData);
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      axios.get('/api/crm/productIme/getQuery').then((response) => {
        this.formProperty = response.data;
      })
      this.pageRequest();
    }
  };
</script>

