<template>
  <div>
    <head-tab active="productImeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:productIme:edit'">{{$t('productImeList.add')}}</el-button>
        <el-dropdown @command="handleCommand">
          <el-button type="primary">
            {{$t('productImeList.more')}}<i class="el-icon-caret-bottom el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item v-permit="'crm:productImeSale:edit'" command="sale">{{$t('productImeList.sale')}}</el-dropdown-item>
            <el-dropdown-item v-permit="'crm:productImeSale:back'" command="saleBack">{{$t('productImeList.saleBack')}}</el-dropdown-item>
            <el-dropdown-item v-permit="'crm:imeAllot:edit'" command="allot">{{$t('productImeList.allot')}}</el-dropdown-item>
            <el-dropdown-item v-permit="'crm:productIme:adjust'" command="change">{{$t('productImeList.change')}}</el-dropdown-item>
            <el-dropdown-item v-permit="'crm:imeAllot:batchSave'" command="batchAllot">{{$t('productImeList.batchAllot')}}</el-dropdown-item>
            <el-dropdown-item v-permit="'crm:productImeUpload:edit'" command="upload">{{$t('productImeList.upload')}}</el-dropdown-item>
            <el-dropdown-item v-permit="'crm:productImeUpload:edit'" command="uploadBack">{{$t('productImeList.uploadBack')}}</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <el-button type="primary" @click="exportData" v-permit="'crm:productIme:view'">{{$t('productImeList.export')}}</el-button>
        <el-button type="primary" @click="batchQuery"  >{{$t('productImeList.batchQuery')}}</el-button>
        <el-button type="primary" v-permit="'crm:productIme:view'"@click="formVisible = true" icon="search">{{$t('productImeList.filter')}}</el-button>

        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('productImeList.filter')" v-model="formVisible" size="medium" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="$t('productImeList.boxIme')" :label-width="formLabelWidth">
                <el-input v-model="formData.boxIme" auto-complete="off" :placeholder="$t('productImeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('productImeList.ime')" :label-width="formLabelWidth">
                <el-input v-model="formData.ime" auto-complete="off" :placeholder="$t('productImeList.imeFilter')"></el-input>
              </el-form-item>

              <el-form-item :label="$t('productImeList.ime2')" :label-width="formLabelWidth">
                <el-input v-model="formData.ime2" auto-complete="off" :placeholder="$t('productImeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('productImeList.meid')" :label-width="formLabelWidth">
                <el-input v-model="formData.meid" auto-complete="off" :placeholder="$t('productImeList.likeSearch')"></el-input>
              </el-form-item>

              <el-form-item :label="$t('productImeList.depotName')" :label-width="formLabelWidth">
                <depot-select v-model="formData.depotId" category="store" @afterInit="setSearchText"></depot-select>
              </el-form-item>

              <el-form-item :label="$t('productImeList.inputType')" :label-width="formLabelWidth">
                <el-select v-model="formData.inputType" clearable filterable :placeholder="$t('productImeList.selectInputType')">
                  <el-option v-for="item in formData.extra.inputTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('productImeList.imeOrMeids')" :label-width="formLabelWidth">
                <el-input  type="textarea"   v-model="formData.imeOrMeids" auto-complete="off" :placeholder="$t('productImeList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="$t('productImeList.productType')" :label-width="formLabelWidth">
                <product-select v-model="formData.productId" hasIme=true  @afterInit="setSearchText"></product-select>
              </el-form-item>
              <el-form-item :label="$t('productImeList.createdDate')" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('productImeList.createTimeBtw')" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdTimeRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('productImeList.retailDateBtw')" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.retailDateRange" ></date-range-picker>
              </el-form-item>

            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">

          <el-button type="primary" @click="search()">{{$t('productImeList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('productImeList.loading')" @sort-change="sortChange" stripe border>

        <el-table-column type="expand">
          <template scope="props">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item :label="$t('productImeList.remarks')">
                <span>{{ props.row.remarks }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>

        <el-table-column prop="ime" :label="$t('productImeList.ime')" sortable></el-table-column>
        <el-table-column prop="ime2"  :label="$t('productImeList.ime2')" sortable ></el-table-column>
        <el-table-column prop="meid" :label="$t('productImeList.meid')" sortable></el-table-column>
        <el-table-column prop="retailDate" :label="$t('productImeList.retailDate')"></el-table-column>
        <el-table-column prop="productImeSaleCreateDate" :label="$t('productImeList.saleDate')"></el-table-column>
        <el-table-column prop="productImeUploadCreateDate" :label="$t('productImeList.uploadDate')"></el-table-column>
        <el-table-column prop="depotName" column-key="depotId"   :label="$t('productImeList.depotName')" sortable></el-table-column>
        <el-table-column prop="productNetType"  :label="$t('productImeList.netType')" width="80"></el-table-column>
        <el-table-column prop="productName" column-key="productId" :label="$t('productImeList.productType')" sortable></el-table-column>
        <el-table-column prop="inputType" :label="$t('productImeList.inputType')"></el-table-column>
        <el-table-column prop="createdTime" :label="$t('productImeList.createdTime')" sortable></el-table-column>
        <el-table-column fixed="right" :label="$t('productImeList.operation')" >
          <template scope="scope">
            <div class="action" v-permit="'crm:productIme:view'"><el-button size="small"  @click.native="itemAction(scope.row.id, 'detail')">{{$t('productImeList.detail')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page"   v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import depotSelect from 'components/future/depot-select'
  import productSelect from 'components/future/product-select'

  export default{
    components:{
      depotSelect,
      productSelect
    },
    data() {
      return {
        pageLoading: false,
        pageHeight:600,
        page:{},
        searchText:"",
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
        util.setQuery("productImeList",submitData);
        axios.get('/api/ws/future/crm/productIme?'+qs.stringify(submitData)).then((response) => {
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
        this.$router.push({ name: 'productImeCreate'});
      },batchQuery(){
        this.$router.push({ name: 'productImeBatchQuery'});
      },
      handleCommand(command) {
        if(command==="sale") {
          this.$router.push({ name: 'productImeSaleForm'});
        }else if(command==="saleBack"){
          this.$router.push({ name: 'productImeSaleBackForm'});
        }else if(command==="allot"){
          this.$router.push({ name: 'imeAllotForm'});
        }else if(command==="change"){
          this.$router.push({ name: 'productImeChange'});
        }else if(command==="batchAllot"){
          this.$router.push({ name: 'imeAllotBatchForm'});
        }else if(command==="upload"){
          this.$router.push({ name: 'productImeUploadForm'});
        }else if(command==="uploadBack"){
          this.$router.push({ name: 'productImeUploadBackForm'});
        }

      },
      itemAction:function(id,action){
        if(action==="detail") {
          this.$router.push({ name: 'productImeDetail', query: { id: id }})
        }
      },exportData(){
        util.confirmBeforeExportData(this).then(() => {
          window.location.href="/api/ws/future/crm/productIme/export?"+qs.stringify(util.deleteExtra(this.formData));
        }).catch(()=>{});
      }
    },created () {

      let that = this;
      that.pageHeight = window.outerHeight -320;
      this.initPromise=axios.get('/api/ws/future/crm/productIme/getQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(that.$route.query,that.formData);
      });
    },activated(){
    this.initPromise.then(()=>{
      this.pageRequest();
    });
  }
  };
</script>

