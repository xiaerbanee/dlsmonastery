<template>
  <div>
    <head-tab active="priceChangeImeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="batchPass" icon="check" v-permit="'crm:priceChangeIme:audit'">{{$t('priceChangeImeList.batchPass')}}</el-button>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:priceChangeIme:edit'">{{$t('priceChangeImeList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'crm:priceChangeIme:view'">{{$t('priceChangeImeList.filter')}}</el-button>
        <!--<el-button type="primary" icon="picture" @click="pictureAdd" v-permit="'crm:priceChangeIme:view'">{{$t('priceChangeImeList.uploadPicture')}}</el-button>-->
        <el-button type="primary" @click="exportData" icon="upload" v-permit="'crm:priceChangeIme:view'">{{$t('priceChangeImeList.export')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible=false" :title="$t('priceChangeImeList.filter')" v-model="formVisible" size="medium" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" :label-width="formLabelWidth">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="$t('priceChangeImeList.status')" >
                <el-select v-model="formData.status" filterable clearable :placeholder="$t('priceChangeImeList.inputKey')">
                  <el-option v-for="item in formData.extra.statusList" :key="item":label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('priceChangeImeList.officeName')" >
                <office-select v-model="formData.officeId" @afterInit="setSearchText"></office-select>
              </el-form-item>
              <el-form-item :label="$t('priceChangeImeList.type')" >
                <product-select v-model="formData.productId" @afterInit="setSearchText"></product-select>
              </el-form-item>
              <el-form-item :label="$t('priceChangeImeList.isCheck')" >
                <bool-select v-model="formData.isCheck"></bool-select>
              </el-form-item>
              <el-form-item :label="$t('priceChangeImeList.image')" >
                <bool-select v-model="formData.hasImage"></bool-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="$t('priceChangeImeList.shopName')" >
                <depot-select v-model="formData.shopId" category="shop" @afterInit="setSearchText"></depot-select>
              </el-form-item>
              <el-form-item :label="$t('priceChangeImeList.ime')" >
                <el-input v-model="formData.ime" auto-complete="off" :placeholder="$t('priceChangeImeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('priceChangeImeList.priceChangeName')" >
                <el-input v-model="formData.priceChangeName" auto-complete="off" :placeholder="$t('priceChangeImeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('priceChangeImeList.createdBy')">
                <account-select v-model="formData.createdBy" @afterInit="setSearchText"></account-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('priceChangeImeList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('priceChangeImeList.loading')" @sort-change="sortChange" @selection-change="handleSelectionChange" stripe border>
        <el-table-column type="selection" width="55" :selectable="checkSelectable"></el-table-column>
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
            <div class="action" v-if="scope.row.status !== '已通过'&&scope.row.image !== null" v-permit="'crm:priceChangeIme:audit'"><el-button size="small" @click.native="itemAction(scope.row.id,'audit')">{{$t('priceChangeImeList.audit')}}</el-button></div>
            <div class="action" v-if="scope.row.status ==='申请中'" v-permit="'crm:priceChangeIme:upload'"><el-button size="small" @click.native="itemAction(scope.row.id,'upload')">{{$t('priceChangeImeList.upload')}}</el-button></div>
            <div class="action" v-permit="'crm:priceChangeIme:view'"><el-button size="small" @click.native="itemAction(scope.row.id,'detail')">{{$t('priceChangeImeList.detail')}}</el-button></div>
            <div class="action" v-if="scope.row.status ==='申请中'" v-permit="'crm:priceChangeIme:edit'"><el-button size="small" @click.native="itemAction(scope.row.id,'delete')">{{$t('priceChangeImeList.delete')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import officeSelect from 'components/basic/office-select';
  import accountSelect from 'components/basic/account-select';
  import productSelect from 'components/future/product-select';
  import depotSelect from 'components/future/depot-select';
  import boolSelect from 'components/common/bool-select';
  export default {
    components:{
      officeSelect,accountSelect,productSelect,depotSelect,boolSelect
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
        multipleSelection:[],
        formLabelWidth: '28%',
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
          if(action =="delete"){
            util.confirmBeforeDelRecord(this).then(() => {
              axios.get('/api/ws/future/crm/priceChangeIme/delete',{params:{id:id}}).then((response) =>{
                this.$message(response.data.message);
                this.pageRequest();
              })
            }).catch(()=>{

            });
          }else{
            this.$router.push({ name: 'priceChangeImeDetail', query: { id: id ,action:action }})
          }
      },handleSelectionChange(val) {
          this.multipleSelection = new Array();
          for(var key in val){
            this.multipleSelection.push(val[key].id);
          }
      },batchPass(){
        if(!this.multipleSelection || this.multipleSelection.length < 1){
          this.$message(this.$t('shopBuildList.noSelectionFound'));
          return ;
        }
        util.confirmBeforeBatchPass(this).then(() => {
          axios.get('/api/ws/future/crm/priceChangeIme/batchAudit',{params:{ids:this.multipleSelection}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }).catch(()=>{});
      },exportData(){
        util.confirmBeforeExportData(this).then(() => {
          window.location.href='/api/ws/future/crm/priceChangeIme/export?'+qs.stringify(util.deleteExtra(this.formData));
        }).catch(()=>{});
      },pictureAdd(){
        this.$router.push({ name: 'priceChangeImeImageUpload'})
      },
      checkSelectable(row) {
        return row.status !== '已通过'&&row.image !== null;
      }
    },created () {
       this.pageHeight = 0.74*window.innerHeight;
      this.initPromise=axios.get('/api/ws/future/crm/priceChangeIme/getQuery').then((response) =>{
        this.formData=response.data;
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
  };
</script>

