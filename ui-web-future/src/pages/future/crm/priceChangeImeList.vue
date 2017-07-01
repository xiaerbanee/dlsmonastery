<template>
  <div>
    <head-tab active="priceChangeImeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:priceChangeIme:edit'">{{$t('priceChangeImeList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:priceChangeIme:view'">{{$t('priceChangeImeList.filter')}}</el-button>
        <el-button type="primary" icon="picture" @click="pictureAdd" v-permit="'crm:priceChangeIme:view'">{{$t('priceChangeImeList.uploadPicture')}}</el-button>
        <el-button type="primary" @click="exportData" icon="upload" v-permit="'crm:priceChangeIme:view'">{{$t('priceChangeImeList.export')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('priceChangeImeList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('priceChangeImeList.status')" :label-width="formLabelWidth">
                <el-select v-model="formData.status" filterable clearable :placeholder="$t('priceChangeImeList.inputKey')">
                  <el-option v-for="item in formData.extra.statusList" :key="item":label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('priceChangeImeList.officeName')" :label-width="formLabelWidth">
                <office-select v-model="formData.officeId" @afterInit="setSearchText"></office-select>
              </el-form-item>
              <el-form-item :label="$t('priceChangeImeList.type')" :label-width="formLabelWidth">
                <product-select v-model="formData.productId" @afterInit="setSearchText"></product-select>
              </el-form-item>
              <el-form-item :label="$t('priceChangeImeList.shopName')" :label-width="formLabelWidth">
                <depot-select v-model="formData.shopId" category="shop" @afterInit="setSearchText"></depot-select>
              </el-form-item>
              <el-form-item :label="$t('priceChangeImeList.isCheck')" :label-width="formLabelWidth">
                <bool-select v-model="formData.isCheck"></bool-select>
              </el-form-item>
              <el-form-item :label="$t('priceChangeImeList.image')" :label-width="formLabelWidth">
                <bool-select v-model="formData.hasImage"></bool-select>
              </el-form-item>
              <el-form-item :label="$t('priceChangeImeList.ime')" :label-width="formLabelWidth">
                <el-input v-model="formData.ime" auto-complete="off" :placeholder="$t('priceChangeImeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('priceChangeImeList.priceChangeName')" :label-width="formLabelWidth">
                <el-input v-model="formData.priceChangeName" auto-complete="off" :placeholder="$t('priceChangeImeList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('priceChangeImeList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('priceChangeImeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column  prop="ime" :label="$t('priceChangeImeList.ime')" sortable width="150"></el-table-column>
        <el-table-column prop="saleDate" :label="$t('priceChangeImeList.saleDate')" sortable></el-table-column>
        <el-table-column column-key="productId" prop="productName" :label="$t('priceChangeImeList.type')" sortable></el-table-column>
        <el-table-column column-key="areaId" prop="areaName" :label="$t('priceChangeImeList.areaName')" sortable></el-table-column>
        <el-table-column column-key="officeId" prop="officeName" :label="$t('priceChangeImeList.officeName')" sortable></el-table-column>
        <el-table-column column-key="shopId" prop="shopName" :label="$t('priceChangeImeList.shopName')" sortable></el-table-column>
        <el-table-column column-key="priceChangeId" prop="priceChangeName"  :label="$t('priceChangeImeList.priceChangeName')" sortable></el-table-column>
        <el-table-column prop="auditDate" :label="$t('priceChangeImeList.auditDate')" sortable></el-table-column>
        <el-table-column prop="isCheck"  :label="$t('priceChangeImeList.isCheck')"width="120" sortable>
          <template scope="scope">
            <el-tag :type="scope.row.isCheck ? 'primary' : 'danger'">{{scope.row.isCheck | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="image" :label="$t('priceChangeImeList.image')" sortable></el-table-column>
        <el-table-column prop="status"  :label="$t('priceChangeImeList.status')"width="120" sortable>
          <template scope="scope">
            <el-tag :type="scope.row.status=='已通过' ? 'primary' : 'danger'">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" :label="$t('priceChangeImeList.remarks')"></el-table-column>
        <el-table-column column-key="createdBy" prop="createdByName" :label="$t('priceChangeImeList.createdBy')" sortable></el-table-column>
        <el-table-column  :label="$t('priceChangeImeList.operation')" width="140">
          <template scope="scope">
            <div class="action" v-if="scope.row.status =='申请中'" v-permit="'crm:priceChangeIme:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'audit')">{{$t('priceChangeImeList.audit')}}</el-button></div>
            <div class="action" v-if="scope.row.status !='已通过'" v-permit="'crm:priceChangeIme:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'upload')">{{$t('priceChangeImeList.upload')}}</el-button></div>
            <div class="action" v-permit="'crm:priceChangeIme:view'"><el-button size="small" @click.native="itemAction(scope.row.id,'detail')">{{$t('priceChangeImeList.detail')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import officeSelect from 'components/basic/office-select';
  import productSelect from 'components/future/product-select';
  import depotSelect from 'components/future/depot-select';
  import boolSelect from 'components/common/bool-select';
  export default {
    components:{
      officeSelect,productSelect,depotSelect,boolSelect
    },
    data() {
      return {
        pageLoading: false,
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
        let submitData = util.deleteExtra(this.formData);
        util.setQuery("priceChangeImeList",submitData);
        axios.get('/api/ws/future/crm/priceChangeIme',{params:submitData}).then((response) => {
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
        this.$router.push({ name: 'priceChangeImeForm'})
      },itemAction:function(id,action){
          this.$router.push({ name: 'priceChangeImeDetail', query: { id: id ,action:action }})
      },exportData(){
        util.confirmBeforeExportData(this).then(() => {
          window.location.href='/api/ws/future/crm/priceChangeIme/export?'+qs.stringify(util.deleteExtra(this.formData));
        }).catch(()=>{});
      },pictureAdd(){
        this.$router.push({ name: 'priceChangeImeImageUpload'})
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      this.initPromise=axios.get('/api/ws/future/crm/priceChangeIme/getQuery').then((response) =>{
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

