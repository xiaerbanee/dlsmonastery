<template>
  <div>
    <head-tab active="depotStoreList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus"  >添加</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">过滤</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog title="过滤" v-model="formVisible"  size="" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" :label-width="formLabelWidth">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item label="门店名称" >
                <el-input v-model="formData.name" auto-complete="off"></el-input>
              </el-form-item>
              <el-form-item label="区域类型"  >
                <dict-map-select  v-model="formData.areaType" category="门店_地区属性"></dict-map-select>
              </el-form-item>
              <el-form-item label="价格体系" >
                <el-select v-model="formData.pricesystemId" filterable clearable :placeholder="$t('dictEnumList.inputKey')">
                  <el-option v-for="pricesystem in formData.extra.pricesystemList" :key="pricesystem.id" :label="pricesystem.name" :value="pricesystem.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="负责人">
                <el-input v-model="formData.contator" auto-complete="off"></el-input>
              </el-form-item>
              <el-form-item label="手机" >
                <el-input v-model="formData.mobilePhone" auto-complete="off"></el-input>
              </el-form-item>
              <el-form-item label="是否体验店">
                <bool-select v-model="formData.specialityStore"></bool-select>
              </el-form-item>
              <el-form-item label="专卖店类型">
                <dict-map-select  v-model="formData.specialityStoreType" category="门店_体验店类型"></dict-map-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="机构" >
                <office-select v-model="formData.officeId"></office-select>
              </el-form-item>
              <el-form-item label="连锁体系">
                <dict-map-select  v-model="formData.chainName" category="门店_连锁属性"></dict-map-select>
              </el-form-item>
              <el-form-item label="物料运费">
                <el-select v-model="formData.adPricesystemId" filterable clearable :placeholder="$t('dictEnumList.inputKey')">
                  <el-option v-for="adPricesystem in formData.extra.adPricesystemList" :key="adPricesystem.id" :label="adPricesystem.name" :value="adPricesystem.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="快递公司">
                <express-company-select v-model="formData.officeId"></express-company-select>
              </el-form-item>
              <el-form-item label="行政区域" >
                <el-input v-model="formData.districtName" auto-complete="off"></el-input>
              </el-form-item>
              <el-form-item label="是否广告办事处">
                <bool-select v-model="formData.adShopBsc"></bool-select>
              </el-form-item>
              <el-form-item label="是否广告门店" >
                <bool-select v-model="formData.adShop"></bool-select>
              </el-form-item>
              <el-form-item label="是否隐藏">
                <bool-select v-model="formData.isHidden"></bool-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">过滤</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" element-loading-text="数据加载中" @sort-change="sortChange" stripe border>
        <el-table-column prop="areaName" label="办事处"  />
        <el-table-column prop="officeName" label="机构"  />
        <el-table-column  prop="depotName" label="名称" ></el-table-column>
        <el-table-column prop="contator" label="联系人"  />
        <el-table-column prop="mobilePhone" label="手机"  />
        <el-table-column prop="storeGroup" label="分组"  />
        <el-table-column prop="type" label="仓库类型"  />
        <el-table-column prop="taxName" label="税务门店名称"  />
        <el-table-column prop="delegateDepotName" label="寄售门店" />
        <el-table-column prop="remarks" label="备注" />
        <el-table-column prop="popShop" label="是否广告仓库" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.popShop ? 'primary' : 'danger'">{{scope.row.popShop | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column  label="操作" width="140">
          <template scope="scope">
            <el-button size="small"  v-permit="'crm:expressCompany:edit'" @click.native="itemAction(scope.row.id,'edit')">修改</el-button>
            <el-button size="small"  v-permit="'crm:expressCompany:edit'" @click.native="itemAction(scope.row.id,'delete')">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import boolSelect from 'components/common/bool-select';
  import officeSelect from 'components/basic/office-select';
  import dictMapSelect from 'components/basic/dict-map-select';
  import expressCompanySelect from 'components/future/express-company-select';
  export default {
    components:{
      boolSelect,
      officeSelect,
      dictMapSelect,
      expressCompanySelect
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
        formVisible: false,
        pageLoading: false,
        remoteLoading:false
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
        util.setQuery("depotStoreList",submitData);
        axios.get('/api/ws/future/basic/depotStore',{params:submitData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getSort(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'depotStoreForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'depotStoreForm', query: { id: id }})
        }else if(action=="delete"){
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/basic/depotStore/delete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
              this.pageRequest();
            });
          });
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      this.initPromise = axios.get('/api/ws/future/basic/depotStore/getQuery').then((response) =>{
        this.formData = response.data;
        console.log(response.data)
        util.copyValue(this.$route.query,this.formData);
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      });
    }
  };
</script>

