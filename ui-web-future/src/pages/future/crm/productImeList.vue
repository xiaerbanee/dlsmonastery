<template>
  <div>
    <head-tab active="productImeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" v-permit="'crm:productIme:view'" @click="formVisible = true" icon="search">{{$t('productImeList.filterOrExport')}}</el-button>

        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('productImeList.filter')" v-model="formVisible" size="small" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="formLabel.boxIme.label" :label-width="formLabelWidth">
                <el-input v-model="formData.boxIme" auto-complete="off" :placeholder="$t('productImeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.imeReverse.label" :label-width="formLabelWidth">
                <el-input v-model="formData.imeReverse" ></el-input>
              </el-form-item>

              <el-form-item :label="formLabel.ime2.label" :label-width="formLabelWidth">
                <el-input v-model="formData.ime2" auto-complete="off" :placeholder="$t('productImeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.meid.label" :label-width="formLabelWidth">
                <el-input v-model="formData.meid" auto-complete="off" :placeholder="$t('productImeList.likeSearch')"></el-input>
              </el-form-item>

              <el-form-item :label="formLabel.depotId.label" :label-width="formLabelWidth">
                <depot-select v-model="formData.depotId" category="store" ></depot-select>
              </el-form-item>

              <el-form-item :label="formLabel.inputType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.inputType" clearable filterable :placeholder="$t('productImeList.selectInputType')">
                  <el-option v-for="item in formData.inputTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.imeOrMeids.label" :label-width="formLabelWidth">
                <el-input  type="textarea"   v-model="formData.imeOrMeids" auto-complete="off" :placeholder="$t('productImeList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="formLabel.productId.label" :label-width="formLabelWidth">

                <product-select v-model="formData.productId" ></product-select>
              </el-form-item>
              <el-form-item :label="formLabel.createdDateRange.label" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createdDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="formLabel.createTimeRange.label" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.createTimeRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="formLabel.retailDateRange.label" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.retailDateRange" ></date-range-picker>
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

        <el-table-column type="expand">
          <template scope="props">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item :label="$t('productImeList.remarks')">
                <span>{{ props.row.remarks }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>

        <el-table-column prop="ime" :label="$t('productImeList.ime')"  width="160" sortable></el-table-column>
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
        <el-table-column prop="locked" :label="$t('productImeList.locked')" >
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'danger' : 'primary'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" :label="$t('productImeList.enabled')" >
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'primary' : 'danger'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
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
      productSelect,

    },
    data() {
      return {
        pageLoading: false,
        pageHeight:600,
        page:{},
        formData:{},
        submitData:{
          page:0,
          size:25,
          sort:"id,DESC",
          imeReverse:'',
          ime2:'',
          boxIme:'',
          meid:'',
          depotId:'',
          inputType:'',
          imeOrMeids:'',
          productId:'',
          createdDateRange:"",
          retailDateRange:"",
          saleDateRange:"",
          createTimeRange:"",
        },formLabel:{
          imeReverse:{label:this.$t('productImeList.ime')},
          ime2:{label:this.$t('productImeList.ime2')},
          boxIme:{label:this.$t('productImeList.boxIme')},
          meid:{label:this.$t('productImeList.meid')},
          depotId:{label:this.$t('productImeList.depotName')},
          inputType:{label:this.$t('productImeList.inputType')},
          imeOrMeids:{label:this.$t('productImeList.imeOrMeids')},
          productId:{label:this.$t('productImeList.productType')},
          createdDateRange:{label: this.$t('productImeList.createdDate')},
          retailDateRange:{label:this.$t('productImeList.retailDateBtw')},
          saleDateRange:{label:this.$t('productImeList.saleDate')},
          createTimeRange:{label:this.$t('productImeList.createTimeBtw')},
        },

        formLabelWidth: '120px',
        formVisible: false,

      };
    },
    methods: {
      pageRequest() {

        this.pageLoading = true;
        util.copyValue(this.formData,this.submitData);
        util.setQuery("productImeList",this.submitData);
        axios.get('/api/ws/future/crm/productIme?'+qs.stringify(this.submitData)).then((response) => {
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

      },itemAction:function(id,action){
        if(action==="detail") {
          this.$router.push({ name: 'productImeDetail', query: { id: id }})
        }
      },exportData(){

        util.confirmBeforeExportData(this).then(() => {
          util.copyValue(this.formData,this.submitData);
          axios.get('/api/ws/future/crm/productIme/export?'+qs.stringify(this.submitData)).then((response)=> {
            window.location.href="/api/general/sys/folderFile/download?id="+response.data;
          });
        }).catch(()=>{});
      }
    },created () {

      let that = this;
      that.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/crm/productIme/getQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(that.$route.query,that.formData);
        that.pageRequest();
      });

    }
  };
</script>

