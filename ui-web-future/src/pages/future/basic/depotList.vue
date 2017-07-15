<template>
  <div>
    <head-tab active="depotList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:depot:edit'">{{$t('depotList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'crm:depot:view'">{{$t('depotList.filter')}}</el-button>
        <el-button type="primary" @click="synData"  icon="plus" v-permit="'crm:bank:edit'">{{$t('depotList.syn')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible = false" :title="$t('bankList.filter')" v-model="formVisible" size="large" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="8">
              <el-form-item :label="$t('depotList.name')" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('depotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('depotList.typeLabel')" :label-width="formLabelWidth">
                <el-select v-model="formData.type" filterable clearable :placeholder="$t('depotList.inputKey')">
                  <el-option v-for="(value,key) in formData.extra.typeList" :key="key" :label="value" :value="key"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('depotList.areaType')" :label-width="formLabelWidth">
                <el-select v-model="formData.areaType" filterable clearable :placeholder="$t('depotList.inputKey')">
                  <el-option v-for="areaType in formData.extra.areaTypeList"  :key="areaType.name" :label="areaType.name" :value="areaType.name"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('depotList.pricesystemName')" :label-width="formLabelWidth">
                <el-select v-model="formData.pricesystemId" filterable clearable :placeholder="$t('depotList.inputKey')">
                  <el-option v-for="pricesystem in formData.pricesystemList"  :key="pricesystem.name" :label="pricesystem.name" :value="pricesystem.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('depotList.contact')" :label-width="formLabelWidth">
                <el-input v-model="formData.contator" auto-complete="off" :placeholder="$t('depotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('depotList.mobilePhone')" :label-width="formLabelWidth">
                <el-input v-model="formData.mobilePhone" auto-complete="off" :placeholder="$t('depotList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item :label="$t('depotList.specialityStore')" :label-width="formLabelWidth">
                <bool-select v-model="formData.specialityStore" ></bool-select>
              </el-form-item>
              <el-form-item :label="$t('depotList.specialityStoreType')" :label-width="formLabelWidth">
                <dict-map-select v-model="formData.specialityStoreType" category="门店_体验店类型"/>
              </el-form-item>
              <el-form-item :label="$t('depotList.officeName')" :label-width="formLabelWidth">
                <el-select v-model="formData.officeId" filterable remote :placeholder="$t('depotList.inputWord')" :clearable=true>
                  <el-option v-for="office in formData.offices" :key="office.id" :label="office.name" :value="office.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('depotList.chainName')" :label-width="formLabelWidth">
                <el-select v-model="formData.chainId" filterable clearable :placeholder="$t('depotList.inputKey')">
                  <el-option v-for="chain in formData.chainList" :key="chain.id" :label="chain.name" :value="chain.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('depotList.adPricesystemId')" :label-width="formLabelWidth">
                <el-select v-model="formData.adPricesystemId" filterable clearable :placeholder="$t('depotList.inputKey')">
                  <el-option v-for="adPricesystem in formData.adPricesystemList" :key="adPricesystem.id" :label="adPricesystem.name" :value="adPricesystem.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('depotList.expressCompany')" :label-width="formLabelWidth">
                <express-company-select v-model="formData.expressCompanyId" ></express-company-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item :label="$t('depotList.districtName')" :label-width="formLabelWidth">
                <el-input v-model="formData.districtName" auto-complete="off" :placeholder="$t('depotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('depotList.isAdShopBsc')" :label-width="formLabelWidth">
                <bool-select v-model="formData.adShopBsc"></bool-select>
              </el-form-item>
              <el-form-item :label="$t('depotList.isAdShop')" :label-width="formLabelWidth">
                <bool-select v-model="formData.adShop"></bool-select>
              </el-form-item>
              <el-form-item :label="$t('depotList.isHidden')" :label-width="formLabelWidth">
                <bool-select v-model="formData.isHidden"></bool-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('depotList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('depotList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="name" :label="$t('depotList.name')" ></el-table-column>
        <el-table-column prop="officeName" :label="$t('depotList.officeName')" ></el-table-column>
        <el-table-column prop="areaName" :label="$t('depotList.areaName')" ></el-table-column>
        <el-table-column prop="typeLabel" :label="$t('depotList.typeLabel')"  ></el-table-column>
        <el-table-column prop="depositMap.xxbzj" :label="$t('depotList.xxbzj')"></el-table-column>
        <el-table-column prop="depositMap.scbzj" :label="$t('depotList.scbzj')"></el-table-column>
        <el-table-column prop="contator" :label="$t('depotList.contact')"></el-table-column>
        <el-table-column prop="mobilePhone" :label="$t('depotList.mobilePhone')"></el-table-column>
        <el-table-column prop="outGroupName" :label="$t('depotList.outGroupName')"></el-table-column>
        <el-table-column prop="chainName" :label="$t('depotList.chainName')"></el-table-column>
        <el-table-column prop="pricesystemName" :label="$t('depotList.pricesystemName')"></el-table-column>
        <el-table-column prop="adShop" :label="$t('depotList.adShop')" >
          <template scope="scope">
            <el-tag :type="scope.row.adShop ? 'primary' : 'danger'">{{scope.row.adShop | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isHidden" :label="$t('depotList.isHidden')" >
          <template scope="scope">
            <el-tag :type="scope.row.isHidden ? 'primary' : 'danger'">{{scope.row.isHidden | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="areaType" :label="$t('depotList.areaType')"></el-table-column>
        <el-table-column prop="delegateDepotName" :label="$t('depotList.delegateDepotName')"></el-table-column>
        <el-table-column prop="townType" :label="$t('depotList.townType')"></el-table-column>
        <el-table-column prop="credit" :label="$t('depotList.credit')"></el-table-column>
        <el-table-column prop="code" :label="$t('depotList.id')"></el-table-column>
        <el-table-column prop="id" :label="$t('depotList.unicode')"></el-table-column>
        <el-table-column prop="outId" :label="$t('depotList.outCode')"></el-table-column>
        <el-table-column prop="expressCompanyName" :label="$t('depotList.expressCompany')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('depotList.remarks')"></el-table-column>
        <el-table-column prop="rebate" :label="$t('depotList.rebate')" >
          <template scope="scope">
            <el-tag :type="scope.row.rebate ? 'primary' : 'danger'">{{scope.row.rebate | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="locked" :label="$t('depotList.locked')" >
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'primary' : 'danger'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdByName" :label="$t('depotList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('depotList.createdDate')" ></el-table-column>
        <el-table-column prop="lastModifiedByName" :label="$t('depotList.lastModifiedBy')"></el-table-column>
        <el-table-column fixed="right" :label="$t('depotList.operation')" >
          <template scope="scope">
            <el-button size="small"  @click.native="itemAction(scope.row.id,'edit')" class="action">修改</el-button>
            <el-button size="small"  @click.native="itemAction(scope.row.id,'syn')" class="action">同步</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import dictMapSelect from 'components/basic/dict-map-select'
  import boolSelect from 'components/common/bool-select'
  import expressCompanySelect from 'components/future/express-company-select'
  export default{
    components:{
      dictMapSelect,
      boolSelect,
      expressCompanySelect
    },
    data() {
      return {
        page:{},
        searchText:"",
        formData:{
          extra:{}
        },
        offices:[],
        formLabelWidth: '80px',
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
        util.setQuery("depotList",submitData);
        axios.get('/api/ws/future/basic/depotShop',{params:submitData}).then((response) => {
          console.log(response.data)
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
        this.$router.push({ name: 'depotForm'})
      },synData(){
        axios.get('/api/ws/future/basic/depot/syn').then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        })
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'depotForm', query: { id: id }})
        }else if(action=="syn"){

        }
      }
    },created () {
       this.pageHeight = 0.75*window.innerHeight;
      util.copyValue(this.$route.query,this.formData);
      axios.get('/api/ws/future/basic/depotShop/getReportQuery').then((response) =>{
        this.formData = response.data;
        this.pageRequest();
      });
    }
  };
</script>

