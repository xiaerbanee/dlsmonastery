<template>
  <div>
    <head-tab active="shopAllotList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopAllot:edit'">{{$t('shopAllotList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'crm:shopAllot:view'">{{$t('shopAllotList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('shopAllotList.filter')" v-model="formVisible" size="tiny" class="search-form" ref="searchDialog"  z-index="1500">
        <el-form :model="formData" :label-width="fromLabelWidth">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('shopAllotList.fromShop')">
                <depot-select  category="directShop" v-model="formData.fromShopId"  @afterInit="setSearchText"></depot-select>
              </el-form-item>
              <el-form-item :label="$t('shopAllotList.toShop')">
                <depot-select  category="directShop" v-model="formData.toShopId"  @afterInit="setSearchText"></depot-select>
              </el-form-item>
              <el-form-item :label="$t('shopAllotList.createdDate')">
                <date-range-picker v-model="formData.createdDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('shopAllotList.auditDateRange')">
                <date-range-picker v-model="formData.auditDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('shopAllotList.status')">
                <el-select v-model="formData.status" filterable clearable :placeholder="$t('shopAllotList.inputKey')">
                  <el-option v-for="item in formData.extra.statusList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('shopAllotList.billCode')" >
                <el-input v-model="formData.businessId" :placeholder="$t('shopAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('shopAllotList.billCode')">
                <el-input type="textarea" v-model="formData.businessIds" :placeholder="$t('shopAllotList.multiEnterOrComma')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('shopAllotList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('shopAllotList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="formatId" column-key="id" :label="$t('shopAllotList.billCode')" sortable></el-table-column>
        <el-table-column prop="createdByName" column-key="createdBy" :label="$t('shopAllotList.createdBy')" sortable></el-table-column>
        <el-table-column prop="createdDate" :label="$t('shopAllotList.createdDate')" sortable></el-table-column>
        <el-table-column prop="fromShopName" column-key="fromShopId" :label="$t('shopAllotList.fromShop')"  sortable></el-table-column>
        <el-table-column prop="toShopName" column-key="toShopId" :label="$t('shopAllotList.toShop')" sortable></el-table-column>
        <el-table-column prop="outReturnCode" :label="$t('shopAllotList.outReturnCode')" sortable>
          <template scope="scope">
            <a href="javascript:void(0);" style="color:blue;text-decoration:underline;" @click="print(scope.row.id, 'returnPrint')">{{ scope.row.outReturnCode}}</a>
          </template>
        </el-table-column>
        <el-table-column prop="outSaleCode" :label="$t('shopAllotList.outSaleCode')" sortable>
          <template scope="scope">
            <a href="javascript:void(0);" style="color:blue;text-decoration:underline;" @click="print(scope.row.id, 'salePrint')">{{ scope.row.outSaleCode}}</a>
          </template>
        </el-table-column>

        <el-table-column prop="status" :label="$t('shopAllotList.status')" width="120" sortable>
          <template scope="scope">
            <el-tag :type="scope.row.status=='已通过' ? 'primary' : 'danger'">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" :label="$t('shopAllotList.remarks')" sortable></el-table-column>
        <el-table-column prop="enabled" :label="$t('shopAllotList.enabled')" width="70">
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'primary' : 'danger'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('shopAllotList.operation')">
          <template scope="scope">
            <div class="action" v-permit="'crm:shopAllot:view'"><el-button size="small" @click.native="itemAction(scope.row.id, 'detail')">{{$t('shopAllotList.detail')}}</el-button></div>
            <div class="action" v-if="scope.row.auditable" v-permit="'crm:shopAllot:audit'" ><el-button  size="small"  @click.native="itemAction(scope.row.id, 'audit')">{{$t('shopAllotList.audit')}}</el-button></div>
            <div class="action" v-if="scope.row.editable" v-permit="'crm:shopAllot:edit'"><el-button size="small" @click.native="itemAction(scope.row.id, 'edit')">{{$t('shopAllotList.edit')}}</el-button></div>
            <div class="action" v-if="scope.row.editable" v-permit="'crm:shopAllot:delete'"><el-button   size="small"  @click.native="itemAction(scope.row.id, 'delete')">{{$t('shopAllotList.delete')}}</el-button></div>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import depotSelect from 'components/future/depot-select'

  export default{
    components:{
      depotSelect
    },
    data() {
      return {
        page:{},
        searchText:"",
        formData:{
          extra:{}
        },
        initPromise:{},
        formVisible: false,
        pageLoading: false,
        pageHeight:600,
        fromLabelWidth:'28%'
      };
    },
    methods: {
      setSearchText(){
        this.$nextTick(function () {
          this.searchText = util.getSearchText(this.$refs.searchDialog);
        })
      },
      pageRequest() {
        this.pageLoading = true;
        this.setSearchText();
        let submitData = util.deleteExtra(this.formData);
        util.setQuery("shopAllotList", submitData);
        axios.get('/api/ws/future/crm/shopAllot',{params:submitData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        });
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
        this.$router.push({ name: 'shopAllotForm'})
      },print(id, action){
        if(action==="returnPrint") {
          var newWindow = window.open('/#/future/layout/shopAllotShipPrint?printType=returnPrint&id=' + id, '', '');
        } else if(action==="salePrint") {
          var newWindow = window.open('/#/future/layout/shopAllotShipPrint?printType=salePrint&id=' + id, '', '');
        }
      },itemAction:function(id, action){
        if(action==="edit") {
          this.$router.push({ name: 'shopAllotForm', query: { id: id, action:'edit'}})
        } else if(action==="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/crm/shopAllot/logicDelete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
              this.pageRequest();
            });
          }).catch(()=>{});
        }else if(action==="detail"){
          this.$router.push({ name: 'shopAllotDetail', query: { id: id, action:'view'}})
        }else if(action==="audit"){
          this.$router.push({ name: 'shopAllotDetail', query: { id: id, action:'audit'}})}
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      this.initPromise = axios.get('/api/ws/future/crm/shopAllot/getQuery').then((response) =>{
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

