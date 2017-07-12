<template>
  <div>
    <head-tab active="depotShopList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" >添加基础属性</el-button>
        <el-button type="primary" @click="itemAddDepot" icon="plus" >添加业务属性</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search">过滤或导出</el-button>
        <el-button type="primary" @click="synArea = true" icon="search">机构同步</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('dutyTripList.filter')" v-model="formVisible" size="medium" class="search-form" z-index="1500" ref="searchDialog">
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
                <el-select v-model="formData.chainId" filterable clearable :placeholder="$t('dictEnumList.inputKey')">
                  <el-option v-for="chain in formData.extra.chainList" :key="chain.id" :label="chain.name" :value="chain.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="物料运费">
                <el-select v-model="formData.adPricesystemId" filterable clearable :placeholder="$t('dictEnumList.inputKey')">
                  <el-option v-for="adPricesystem in formData.extra.adPricesystemList" :key="adPricesystem.id" :label="adPricesystem.name" :value="adPricesystem.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="快递公司">
                <express-company-select v-model="formData.expressCompanyId"></express-company-select>
              </el-form-item>
              <el-form-item label="行政区域" >
                <district-select v-model="formData.districtId" ></district-select>
              </el-form-item>
              <el-form-item label="是否广告办事处">
                <bool-select v-model="formData.adShopBsc"></bool-select>
              </el-form-item>
              <el-form-item label="是否广告门店" >
                <bool-select v-model="formData.adShop"></bool-select>
              </el-form-item>
              <el-form-item label="是否隐藏">
                <bool-select v-model="formData.hidden"></bool-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="exportData()">导出</el-button>
          <el-button type="primary" @click="search()">过滤</el-button>
        </div>
      </search-dialog>
      <search-dialog :show="synArea" @hide="synArea=false" title="过滤" v-model="synArea"  size="tiny" class="search-form" z-index="1500">
        <el-form :model="formData">
          <el-form-item label="门店名称" :label-width="formLabelWidth">
            <el-input v-model="synData.depotName" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="办事处" :label-width="formLabelWidth">
            <el-select v-model="synData.areaId" clearable filterable>
              <el-option v-for="item in areaList" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="synArea()">同步</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" element-loading-text="数据加载中" @sort-change="sortChange" stripe border>
        <el-table-column  prop="depotName" label="门店名称" sortable ></el-table-column>
        <el-table-column prop="parentName" label="总店名称"  />
        <el-table-column prop="clientName" label="金蝶名称"  />
        <el-table-column prop="areaName" label="办事处"  />
        <el-table-column prop="officeName" label="机构"  />
        <el-table-column prop="areaType" label="地区属性"  />
        <el-table-column prop="pricesystemName" label="价格体系"  />
        <el-table-column prop="chainName" label="连锁体系"  />
        <el-table-column prop="depositMap.scbzj" label="市场保证金"  />
        <el-table-column prop="depositMap.xxbzj" label="形象押金"  />
        <el-table-column prop="contator" label="联系人"  />
        <el-table-column prop="mobilePhone" label="手机"  />
        <el-table-column prop="salePointType" label="门店类型"  />
        <el-table-column prop="remarks" label="备注"></el-table-column>
        <el-table-column  label="操作" width="140">
          <template scope="scope">
            <div class="action"><el-button size="small"   @click.native="itemAction(scope.row.id,'depotShopEdit')" v-permit="'crm:depotShop:basicEdit'">基础属性修改</el-button></div>
            <div class="action"><el-button size="small"  @click.native="itemAction(scope.row.id,'depotEdit')" v-permit="'crm:depotShop:businessEdit'">业务属性修改</el-button></div>
            <div class="action"><el-button size="small"  @click.native="itemAction(scope.row.id,'delete')" v-permit="'crm:depotShop:delete'">删除</el-button></div>
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
  import districtSelect from 'components/general/district-select';
  import expressCompanySelect from 'components/future/express-company-select';
  export default {
    components:{
      boolSelect,
      officeSelect,
      dictMapSelect,
      districtSelect,
      expressCompanySelect
    },
    data() {
      return {
        searchText:"",
        page:{},
        formData:{
          extra:{}
        },
        synData:{
          depotName:"",
          areaId:""
        },
        initPromise:{},
        formProperty:{},
        areaList:[],
        formLabelWidth: '120px',
        formVisible: false,
        synArea:false,
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
        let submitData = util.deleteExtra(this.formData);
        util.setQuery("depotShopList",submitData);
        axios.get('/api/ws/future/basic/depotShop',{params:submitData}).then((response) => {
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
        this.$router.push({ name: 'depotShopForm'})
      },itemAddDepot(){
        this.$router.push({ name: 'shopForm'})
      },exportData(){
        this.formVisible = false;
        util.confirmBeforeExportData(this).then(() => {
          window.location.href='/api/ws/future/basic/depot/export?'+qs.stringify(util.deleteExtra(this.formData));
          this.pageRequest();
        }).catch(()=>{});
      },synArea(){
        this.synArea = false;
        axios.get('/api/ws/future/basic/depot/synArea',{params:this.synData}).then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        });
      },itemAction:function(id,action){
        if(action=="depotShopEdit") {
          this.$router.push({ name: 'depotShopForm', query: { id: id }})
        }else if(action=="depotEdit") {
          this.$router.push({ name: 'shopForm', query: { id: id }})
        }else if(action=="delete"){
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/basic/depotShop/delete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
              this.pageRequest();
            });
          });
        }
      }
    },created () {
       this.pageHeight = 0.75*window.innerHeight;
      this.initPromise = axios.get('/api/ws/future/basic/depotShop/getQuery').then((response) =>{
        this.formData = response.data;
        util.copyValue(this.$route.query,this.formData);
      });
      this.initPromise =axios.get('/api/basic/sys/office/findByOfficeRuleName').then((response) =>{
        this.areaList=response.data;
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      })
    }


  };
</script>

