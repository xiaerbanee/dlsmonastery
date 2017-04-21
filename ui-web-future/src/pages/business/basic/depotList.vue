<template>
  <div>
    <head-tab active="bankList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:depot:edit'">{{$t('depotList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:depot:view'">{{$t('depotList.filter')}}</el-button>
        <el-button type="primary" @click="synData"  icon="plus" v-permit="'crm:bank:edit'">{{$t('depotList.syn')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('bankList.filter')" v-model="formVisible" size="large" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="8">
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('depotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.type.label" :label-width="formLabelWidth">
                <el-select v-model="formData.type" filterable clearable :placeholder="$t('depotList.inputKey')">
                  <el-option v-for="(value,key) in formData.TypeList" :key="key" :label="value" :value="key"></el-option>
                </el-select>
              </el-form-item>
              <!--<el-form-item :label="formLabel.areaType.label" :label-width="formLabelWidth">-->
                <!--<el-select v-model="formData.areaType" filterable clearable :placeholder="$t('depotList.inputKey')">-->
                  <!--<el-option v-for="areaType in formData.areaList"  :key="areaType.name" :label="areaType.name" :value="areaType.name"></el-option>-->
                <!--</el-select>-->
              <!--</el-form-item>-->
              <el-form-item :label="formLabel.pricesystemId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.pricesystemId" filterable clearable :placeholder="$t('depotList.inputKey')">
                  <el-option v-for="pricesystem in formData.pricesystemList"  :key="pricesystem.name" :label="pricesystem.name" :value="pricesystem.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.contator.label" :label-width="formLabelWidth">
                <el-input v-model="formData.contator" auto-complete="off" :placeholder="$t('depotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.mobilePhone.label" :label-width="formLabelWidth">
                <el-input v-model="formData.mobilePhone" auto-complete="off" :placeholder="$t('depotList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item :label="formLabel.specialityStore.label" :label-width="formLabelWidth">
                <el-select v-model="formData.specialityStore" filterable clearable :placeholder="$t('depotList.inputKey')">
                  <el-option v-for="(value,key) in formData.bools" :key="key"  :label="key | bool2str" :value="value"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.specialityStoreType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.specialityStoreType" filterable clearable :placeholder="$t('depotList.inputKey')">
                  <el-option v-for="specialityStore in formData.specialityStoreTypeList" :key="specialityStore.name" :label="specialityStore.name" :value="specialityStore.name"></el-option>
                </el-select>
              </el-form-item>
              <!--<el-form-item :label="formLabel.officeId.label" :label-width="formLabelWidth">-->
                <!--<el-select v-model="formData.officeId" filterable remote :placeholder="$t('depotList.inputWord')" :clearable=true>-->
                  <!--<el-option v-for="office in formData.offices" :key="office.id" :label="office.name" :value="office.id"></el-option>-->
                <!--</el-select>-->
              <!--</el-form-item>-->
              <el-form-item :label="formLabel.chainId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.chainId" filterable clearable :placeholder="$t('depotList.inputKey')">
                  <el-option v-for="chain in formData.chainList" :key="chain.id" :label="chain.name" :value="chain.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.adPricesystemId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.adPricesystemId" filterable clearable :placeholder="$t('depotList.inputKey')">
                  <el-option v-for="adPricesystem in formData.adPricesystemList" :key="adPricesystem.id" :label="adPricesystem.name" :value="adPricesystem.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.expressCompanyId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.expressCompanyId" filterable clearable :placeholder="$t('depotList.inputKey')">
                  <el-option v-for="expressCompany in formData.expressCompanyList" :key="expressCompany.id" :label="expressCompany.name" :value="expressCompany.id"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item :label="formLabel.districtName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.districtName" auto-complete="off" :placeholder="$t('depotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.adShopBsc.label" :label-width="formLabelWidth">
                <el-select v-model="formData.adShopBsc" filterable clearable :placeholder="$t('depotList.inputKey')">
                  <el-option v-for="(value,key) in formData.bools" :key="key" :label="key | bool2str" :value="value"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.adShop.label" :label-width="formLabelWidth">
                <el-select v-model="formData.adShop" filterable clearable :placeholder="$t('depotList.inputKey')">
                  <el-option v-for="(value,key) in formData.bools" :key="key" :label="key | bool2str" :value="value"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.isHidden.label" :label-width="formLabelWidth">
                <el-select v-model="formData.isHidden" filterable clearable :placeholder="$t('depotList.inputKey')">
                  <el-option v-for="(value,key) in formData.bools" :key="key" :label="key | bool2str" :value="value"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('depotList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('depotList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="name" :label="$t('depotList.name')" width="130px"></el-table-column>
        <el-table-column prop="officeName" :label="$t('depotList.officeName')" width="130px"></el-table-column>
        <el-table-column prop="areaName" :label="$t('depotList.areaName')" ></el-table-column>
        <el-table-column prop="typeLabel" :label="$t('depotList.typeLabel')" sortable ></el-table-column>
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
        <el-table-column prop="createdDate" :label="$t('depotList.createdDate')" sortable></el-table-column>
        <el-table-column prop="lastModifiedByName" :label="$t('depotList.lastModifiedBy')"></el-table-column>
        <el-table-column fixed="right" :label="$t('depotList.operation')" width="140">
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
        page:{},
        labelData:{
          pricesystem:{label:''}
        },
        formData:{},
        submitData:{
          page:0,
          size:25,
          name:'',
          type:'',
          areaType:'',
          pricesystemId:'',
          contator:'',
          mobilePhone:'',
          specialityStore:'',
          specialityStoreType:'',
          officeId:'',
          chainId:'',
          adPricesystemId:'',
          expressCompanyId:'',
          districtName:'',
          adShopBsc:'',
          adShop:'',
          isHidden:''
        },
        formLabel:{
          name:{label:this.$t('depotList.name')},
          type:{label:this.$t('depotList.typeLabel'),value:""},
          areaType:{label:this.$t('depotList.areaType')},
          pricesystemId:{label:this.$t('depotList.pricesystemName'),value:""},
          contator:{label:this.$t('depotList.contact')},
          mobilePhone:{label:this.$t('depotList.mobilePhone')},
          specialityStore:{label:this.$t('depotList.specialityStore'),value:""},
          specialityStoreType:{label:this.$t('depotList.specialityStoreType'),value:""},
          officeId:{label:this.$t('depotList.officeName'),value:""},
          chainId:{label:this.$t('depotList.chainName'),value:""},
          adPricesystemId:{label:this.$t('depotList.adPricesystemId'),value:""},
          expressCompanyId:{label:this.$t('depotList.expressCompany'),value:""},
          districtName:{label:this.$t('depotList.districtName')},
          adShopBsc:{label:this.$t('depotList.isAdShopBsc'),value:""},
          adShop:{label:this.$t('depotList.isAdShop'),value:""},
          rebate:{label:this.$t('depotList.rebate'),value:""},
          isHidden:{label:this.$t('depotList.isHidden'),value:""}
        },
        offices:[],
        pickerDateOption:util.pickerDateOption,
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
        remoteLoading:false
      };
    },
    methods: {
      pageRequest() {
//        this.pageLoading = true;
//        this.formLabel.type.value = this.formData.types[this.formData.type];
//        this.formLabel.specialityStore.value = util.bool2str(this.formData.specialityStore);
//        this.formLabel.adShopBsc.value = util.bool2str(this.formData.adShopBsc);
//        this.formLabel.adShop.value =  util.bool2str(this.formData.adShop);
//        this.formLabel.isHidden.value = util.bool2str(this.formData.isHidden);
//
//        this.formLabel.pricesystemId.value = util.getLabel(this.formData.pricesystems, this.formData.pricesystemId);
//        this.formLabel.expressCompanyId.value = util.getLabel(this.formData.expressCompanys, this.formData.expressCompanyId);
//        this.formLabel.specialityStoreType.value = util.getLabel(this.formData.specialityStoreTypes, this.formData.specialityStoreType);
//        this.formLabel.chainId.value = util.getLabel(this.formData.chains, this.formData.chainId);
//        this.formLabel.adPricesystemId.value = util.getLabel(this.formData.adPricesystems, this.formData.adPricesystemId);
//        this.formLabel.officeId.value = util.getLabel(this.offices, this.formData.officeId);

        util.setQuery("depotList",this.formData);
        util.copyValue(this.formData,this.submitData);
        axios.get('/api/ws/future/basic/depot',{params:this.submitData}).then((response) => {
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
      },itemAdd(){
        this.$router.push({ name: 'depotForm'})
      },synData(){
        axios.get('/api/ws/future/basic/depot/syn').then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        })
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'depotForm', query: { id: id }})
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      axios.get('/api/ws/future/basic/depot/getQuery').then((response) =>{
        this.formData = response.data;
        this.pageRequest();
      });
    }
  };
</script>

