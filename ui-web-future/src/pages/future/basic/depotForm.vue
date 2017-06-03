<template>
  <div>
    <head-tab active="depotForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('depotForm.shopType')" prop="type">
              <el-select v-model="inputForm.type" filterable :placeholder="$t('depotForm.selectType')">
                <el-option v-for="(type,key) in formProperty.types" :key="key" :label="type" :value="key | toInteger"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('depotForm.officeName')" prop="officeId">
              <el-select v-model="inputForm.officeId"  filterable remote :placeholder="$t('depotForm.inputWord')" :remote-method="remoteOffice" :loading="remoteLoading">
                <el-option v-for="office in offices" :key="office.id" :label="office.name" :value="office.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('depotForm.name')" prop="name">
              <el-input v-model="inputForm.name"></el-input>
            </el-form-item>
            <el-form-item :label="$t('depotForm.taxName')" prop="taxName">
              <el-input v-model="inputForm.taxName"></el-input>
            </el-form-item>
            <el-form-item :label="$t('depotForm.reportName')" prop="reportName">
              <el-input v-model="inputForm.reportName"></el-input>
            </el-form-item>
            <div v-show="inputForm.type <=200">
              <el-form-item :label="$t('depotForm.storeName')" prop="parentId">
                <el-select v-model="inputForm.parentId" clearable filterable remote :placeholder="$t('depotForm.inputKeyOf20Item')" :remote-method="remoteStore" :loading="remoteLoading">
                  <el-option v-for="store in stores" :key="store.id" :label="store.name" :value="store.id"></el-option>
                </el-select>
              </el-form-item>
            </div>
            <div v-show="inputForm.type >200">
              <el-form-item :label="$t('depotForm.shopName')" prop="parentId">
                <el-select v-model="inputForm.parentId" clearable filterable remote :placeholder="$t('depotForm.inputKeyOf20Item')" :remote-method="remoteDepot" :loading="remoteLoading">
                  <el-option v-for="shop in shops" :key="shop.id" :label="shop.name" :value="shop.id"></el-option>
                </el-select>
              </el-form-item>
            </div>
            <el-form-item :label="$t('depotForm.contact')" prop="contator">
              <el-input v-model="inputForm.contator"></el-input>
            </el-form-item>
            <el-form-item :label="$t('depotForm.mobilePhone')" prop="mobilePhone">
              <el-input v-model="inputForm.mobilePhone"></el-input>
            </el-form-item>
            <el-form-item :label="$t('depotForm.address')" prop="address">
              <el-input v-model="inputForm.address"></el-input>
            </el-form-item>
            <el-form-item :label="$t('depotForm.forSaleMan')" prop="accountIdList">
              <account-select  v-model="inputForm.accountIdList" multiple="multiple" ></account-select>
            </el-form-item>
            <div v-show="inputForm.type >200">
              <el-form-item :label="$t('depotForm.areaType')" prop="areaType">
                <el-select v-model="inputForm.areaType" filterable clearable :placeholder="$t('depotForm.inputKey')">
                  <el-option v-for="areaType in formProperty.areaTypes" :key="areaType.value" :label="areaType.name" :value="areaType.value"></el-option>
                </el-select>
              </el-form-item>
              <div v-show="inputForm.areaType=='市区'">
                <el-form-item :label="$t('depotForm.businessCenter')" prop="bussinessCenter">
                  <el-radio-group v-model="inputForm.bussinessCenter">
                    <el-radio :label=1>{{$t('depotForm.true')}}</el-radio>
                    <el-radio :label=0>{{$t('depotForm.false')}}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </div>
              <div v-show="inputForm.areaType=='市区' && inputForm.bussinessCenter=='1'">
                <el-form-item :label="$t('depotForm.businessCenterName')" prop="bussinessCenterName">
                  <el-select v-model="inputForm.bussinessCenterName" filterable clearable :placeholder="$t('depotForm.inputKey')">
                    <el-option v-for="bussinessCenterType in formProperty.businessCenterTypes" :key="bussinessCenterType.value" :label="bussinessCenterType.name" :value="bussinessCenterType.value"></el-option>
                  </el-select>
                </el-form-item>
              </div>
                <el-form-item :label="$t('depotForm.channelType')" prop="channelType" label-width="125px">
                  <el-select v-model="inputForm.channelType" filterable clearable :placeholder="$t('depotForm.inputKey')">
                    <el-option v-for="channelType in formProperty.channelTypes"  :key="channelType.value" :label="channelType.name" :value="channelType.value"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item :label="$t('depotForm.salePointType')" prop="salePointType">
                  <el-select v-model="inputForm.salePointType" filterable clearable :placeholder="$t('depotForm.inputKey')">
                    <el-option v-for="salePointType in formProperty.salePointTypes"  :key="salePointType.value" :label="salePointType.name" :value="salePointType.value"></el-option>
                  </el-select>
                </el-form-item>
                <div v-show="inputForm.salePointType=='体验店'">
                  <el-form-item :label="$t('depotForm.specialityStoreType')" prop="specialityStoreType">
                    <el-select v-model="inputForm.specialityStoreType" filterable clearable :placeholder="$t('depotForm.inputKey')">
                      <el-option v-for="specialityStoreType in formProperty.specialityStoreTypes"  :key="specialityStoreType.value" :label="specialityStoreType.name" :value="specialityStoreType.value"></el-option>
                    </el-select>
                  </el-form-item>
                </div>
              <div v-show="inputForm.salePointType=='全国连锁'">
                <el-form-item :label="$t('depotForm.chainType')" prop="chainType">
                  <el-select v-model="inputForm.chainType" filterable clearable :placeholder="$t('depotForm.inputKey')">
                    <el-option v-for="chainType in formProperty.chainTypes" :key="chainType.value" :label="chainType.name" :value="chainType.value"></el-option>
                  </el-select>
                </el-form-item>
              </div>
              <div v-show="inputForm.salePointType=='运营商营业厅'">
                <el-form-item :label="$t('depotForm.carrierType')" prop="carrierType">
                  <el-select v-model="inputForm.carrierType" filterable clearable :placeholder="$t('depotForm.inputKey')">
                    <el-option v-for="carrierType in formProperty.carrierTypes"  :key="carrierType.value" :label="carrierType.name" :value="carrierType.value"></el-option>
                  </el-select>
                </el-form-item>
              </div>
              <el-form-item :label="$t('depotForm.turnoverType')" prop="turnoverType">
                <el-select v-model="inputForm.turnoverType" filterable clearable :placeholder="$t('depotForm.inputKey')">
                  <el-option v-for="turnoverType in formProperty.turnoverTypes" :key="turnoverType.value" :label="turnoverType.name" :value="turnoverType.value"></el-option>
                </el-select>
              </el-form-item>
              <div v-show="inputForm.areaType=='市区' && inputForm.bussinessCenter=='1'">
                <el-form-item :label="$t('depotForm.specialityStore')" prop="specialityStore">
                  <el-radio-group v-model="inputForm.specialityStore">
                    <el-radio :label=1>{{$t('depotForm.true')}}</el-radio>
                    <el-radio :label=0>{{$t('depotForm.false')}}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('depotForm.district')" prop="districtId">
              <el-select v-model="inputForm.districtId"  clearable filterable remote :placeholder="$t('depotForm.inputWord')" :remote-method="remoteDistrict" :loading="remoteLoading">
                <el-option v-for="item in districts" :key="item.id"  :label="item.fullName" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('depotForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks" type="textarea"></el-input>
            </el-form-item>
            <div v-show="inputForm.type >200">
              <el-form-item :label="$t('depotForm.doorHead')" prop="doorHead">
                <el-radio-group v-model="inputForm.doorHead">
                  <el-radio :label=1>{{$t('depotForm.true')}}</el-radio>
                  <el-radio :label=0>{{$t('depotForm.false')}}</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item :label="$t('depotForm.enableDate')" prop="enableDate">
                <el-date-picker v-model="inputForm.enableDate" type="date" :placeholder="$t('depotForm.selectDate')" ></el-date-picker>
              </el-form-item>
              <el-form-item :label="$t('depotForm.clientName')" prop="clientId">
                <el-select v-model="inputForm.clientId" filterable clearable :placeholder="$t('depotForm.inputKey')">
                  <el-option v-for="client in formProperty.clients" :key="client.id" :label="client.name" :value="client.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('depotForm.pricesystem')" prop="pricesystemId">
                <el-select v-model="inputForm.pricesystemId" filterable clearable :placeholder="$t('depotForm.inputKey')">
                  <el-option v-for="pricesystem in formProperty.pricesystems" :key="pricesystem.id" :label="pricesystem.name" :value="pricesystem.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('depotForm.chain')" prop="chainId">
                <el-select v-model="inputForm.chainId" filterable clearable :placeholder="$t('depotForm.inputKey')">
                  <el-option v-for="chain in formProperty.chains" :key="chain.id" :label="chain.name" :value="chain.id"></el-option>
                </el-select>
              </el-form-item>
              <div v-show="inputForm.specialityStore=='true' && inputForm.bussinessCenter=='1'">
                <el-form-item :label="$t('depotForm.shopArea')" prop="shopArea">
                  <el-select v-model="inputForm.shopArea" filterable clearable :placeholder="$t('depotForm.inputKey')">
                    <el-option v-for="shopArea in formProperty.shopAreaTypes"  :key="shopArea.name" :label="shopArea.name" :value="shopArea.name"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item :label="$t('depotForm.frameNum')" prop="frameNum ">
                  <el-input v-model="inputForm.remarks"></el-input>
                </el-form-item>
                <el-form-item :label="$t('depotForm.cabinetNum')" prop="cabinetNum ">
                  <el-input v-model="inputForm.remarks"></el-input>
                </el-form-item>
                <el-form-item :label="$t('depotForm.deskDoubleNum')" prop="deskDoubleNum ">
                  <el-input v-model="inputForm.remarks"></el-input>
                </el-form-item>
                <el-form-item :label="$t('depotForm.deskSingleNum')" prop="deskSingleNum ">
                  <el-input v-model="inputForm.remarks"></el-input>
                </el-form-item>
              </div>
              <div v-show="inputForm.areaType=='乡镇'">
                <el-form-item :label="$t('depotForm.townName')" prop="townId">
                  <el-select v-model="inputForm.townId"  filterable remote :placeholder="$t('depotForm.inputKeyOf20Item')" :remote-method="remoteTown" :loading="remoteLoading">
                    <el-option v-for="town in towns" :key="town.id" :label="town.fullName" :value="town.id"></el-option>
                  </el-select>
                </el-form-item>
              </div>
              <el-form-item :label="$t('depotForm.cmccCarrierShop')" prop="cmccCarrierShopId" label-width="125px">
                <el-select v-model="inputForm.cmccCarrierShopId" filterable remote clearable  :placeholder="$t('depotForm.inputKey')" :remote-method="remoteCmcc" :loading="remoteLoading">
                  <el-option v-for="cmccCarrierShop in cmccCarrierShops" :key="cmccCarrierShop.id" :label="cmccCarrierShop.name" :value="cmccCarrierShop.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('depotForm.ctccCarrierShop')" prop="ctccCarrierShopId" label-width="125px">
                <el-select v-model="inputForm.ctccCarrierShopId" filterable remote clearable :placeholder="$t('depotForm.inputKey')" :remote-method="remoteCtcc" :loading="remoteLoading">
                  <el-option v-for="ctccCarrierShop in ctccCarrierShops"  :key="ctccCarrierShop.id" :label="ctccCarrierShop.name" :value="ctccCarrierShop.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('depotForm.hasGuide')" prop="hasGuide">
                <el-radio-group v-model="inputForm.hasGuide">
                  <el-radio :label=1>{{$t('depotForm.true')}}</el-radio>
                  <el-radio :label=0>{{$t('depotForm.false')}}</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item :label="$t('depotForm.rebate')" prop="rebate">
                <el-radio-group v-model="inputForm.rebate">
                  <el-radio :label=1>{{$t('depotForm.true')}}</el-radio>
                  <el-radio :label=0>{{$t('depotForm.false')}}</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item :label="$t('depotForm.printPrice')" prop="printPrice">
                <el-radio-group v-model="inputForm.printPrice">
                  <el-radio :label=1>{{$t('depotForm.true')}}</el-radio>
                  <el-radio :label=0>{{$t('depotForm.false')}}</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item :label="$t('depotForm.isHidden')" prop="isHidden">
                <el-radio-group v-model="inputForm.isHidden">
                  <el-radio :label=1>{{$t('depotForm.true')}}</el-radio>
                  <el-radio :label=0>{{$t('depotForm.false')}}</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item :label="$t('depotForm.adShop')" prop="adShop">
                <el-radio-group v-model="inputForm.adShop">
                  <el-radio :label=1>{{$t('depotForm.true')}}</el-radio>
                  <el-radio :label=0>{{$t('depotForm.false')}}</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item :label="$t('depotForm.adShopBsc')" prop="adShopBsc">
                <el-radio-group v-model="inputForm.adShopBsc">
                  <el-radio :label=1>{{$t('depotForm.true')}}</el-radio>
                  <el-radio :label=0>{{$t('depotForm.false')}}</el-radio>
                </el-radio-group>
              </el-form-item>
            </div >
            <el-form-item>
              <el-button type="primary"  :disabled="submitDisabled" @click="formSubmit()">{{$t('depotForm.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<script>
  import accountSelect from 'components/basic/account-select'
  export default{
      components:{
          accountSelect
      },
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        multiple:true,
        formProperty:{},
        inputForm:{},
        submitData:{
          id:'',
          type:'',
          officeId:null,
          name:'',
          taxName:'',
          reportName:'',
          parentId:null,
          contator:'',
          mobilePhone:'',
          address:'',
          accountIdList:null,
          areaType:'',
          bussinessCenter:'',
          bussinessCenterName:'',
          channelType:'',
          salePointType:'',
          specialityStoreType:'',
          chainType:'',
          carrierType:'',
          turnoverType:'',
          doorHead:'',
          enableDate:'',
          clientId:null,
          pricesystemId:null,
          chainId:null,
          specialityStore:'',
          shopArea:'',
          frameNum:'',
          cabinetNum:'',
          deskDoubleNum:'',
          deskSingleNum:'',
          townId:null,
          districtId:'',
          remarks:'',
          cmccCarrierShopId:null,
          ctccCarrierShopId:null,
          hasGuide:1,
          rebate:0,
          printPrice:0,
          isHidden:0,
          adShop:0,
          adShopBsc:0
        },
        rules: {
          type:[{ required: true, message: this.$t('depotForm.prerequisiteMessage')},{type: 'number', message: this.$t('depotForm.inputLegalValue')}],
          officeId:{ required: true, message: this.$t('depotForm.prerequisiteMessage'), trigger: 'blur' },
          name:{ required: true, message: this.$t('depotForm.prerequisiteMessage'), trigger: 'blur' },
          contator:{ required: true, message: this.$t('depotForm.prerequisiteMessage'), trigger: 'blur' },
          address:{ required: true, message: this.$t('depotForm.prerequisiteMessage'), trigger: 'blur' }
        },
        accounts:[],
        offices:[],
        towns:[],
        cmccCarrierShops:[],
        ctccCarrierShops:[],
        shops:[],
        stores:[],
        districts:[],
        remoteLoading:false
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            util.copyValue(this.inputForm,this.submitData)
            axios.post('/api/ws/future/basic/depot/save', qs.stringify(this.submitData)).then((response)=> {
              if(response.data.message){
                this.$message(response.data.message);
              }
            this.submitDisabled = false;
              if(this.isCreate){
                form.resetFields();
              } else {
                this.$router.push({name:'depotList',query:util.getQuery("depotList")})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },remoteOffice(query){
      if (query !== '') {
        this.remoteLoading = true;
        axios.get('/api/hr/office/search',{params:{name:query}}).then((response)=>{
          this.offices=response.data;
          this.remoteLoading = false;
        })
      } else {
        this.offices = [];
      }
    },remoteTown(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/basic/sys/town/search',{params:{name:query}}).then((response)=>{
            this.towns=response.data;
            this.remoteLoading = false;
          })
        } else {
          this.towns = [];
        }
      },remoteCmcc(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/api/carrierShop/search',{params:{name:query,type:"移动"}}).then((response)=>{
            this.cmccCarrierShops=response.data;
            this.remoteLoading = false;
          })
        } else {
          this.cmccCarrierShops = [];
        }
      },remoteCtcc(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/api/carrierShop/search',{params:{name:query,type:"联信"}}).then((response)=>{
            this.ctccCarrierShops=response.data;
            this.remoteLoading = false;
          })
        } else {
          this.ctccCarrierShops = [];
        }
      },remoteDepot(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/ws/future/basic/depot/search', {params: {name: query, category: "SHOP"}}).then((response)=> {
            this.shops = response.data;
            this.remoteLoading = false;
          })
        } else {
          this.shops = [];
        }
      },remoteStore(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/ws/future/basic/depot/search',{params:{name:query,category:"STORE"}}).then((response)=>{
            this.stores=response.data;
            this.remoteLoading = false;
          })
        } else {
          this.stores = [];
        }
      },remoteDistrict(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/basic/sys/district/search',{params:{key:query}}).then((response)=>{
            this.districts=response.data;
            this.remoteLoading = false;
          })
        } else {
          this.districts = [];
        }
      },initPage(){
        axios.get('/api/ws/future/basic/depot/getForm',{params: {id:this.$route.query.id}}).then((response)=>{
          util.copyValue(response.data,this.inputForm);
        this.inputForm.doorHead = response.data.doorHead?1:0;
        this.inputForm.rebate = response.data.rebate?1:0;
        this.inputForm.hasGuide = response.data.hasGuide?1:0;
        this.inputForm.printPrice = response.data.printPrice?1:0;
        this.inputForm.isHidden = response.data.isHidden?1:0;
        this.inputForm.adShop = response.data.adShop?1:0;
        this.inputForm.adShopBsc = response.data.adShopBsc?1:0;
        this.inputForm.bussinessCenter = response.data.bussinessCenter?1:0;
        this.inputForm.specialityStore = response.data.specialityStore?1:0;
        if(response.data.office){
          this.offices=new Array(response.data.office);
          this.inputForm.officeId=response.data.office.id;
        }
        if(response.data.district){
          this.districts=new Array(response.data.district);
          this.inputForm.districtId=response.data.district.id;
        }
        if(response.data.chain){
          this.formProperty.chains=new Array(response.data.chain);
          this.inputForm.chainId=response.data.chain.id;
        }
        if(response.data.pricesystem){
          this.formProperty.pricesystems=new Array(response.data.pricesystem);
          this.inputForm.pricesystemId=response.data.pricesystem.id;
        }
        if(response.data.town){
          this.towns=new Array(response.data.town);
          this.inputForm.townId=response.data.town.id;
        }
        if(response.data.cmccCarrierShop){
          this.cmccCarrierShops=new Array(response.data.cmccCarrierShop);
          this.inputForm.cmccCarrierShopId=response.data.cmccCarrierShop.id;
        }
        if(response.data.ctccCarrierShop){
          this.ctccCarrierShops=new Array(response.data.ctccCarrierShop);
          this.inputForm.ctccCarrierShopId=response.data.ctccCarrierShop.id;
        }
        if(response.data.accountList){
          this.accounts=response.data.accountList;
          this.inputForm.accountIdList=util.getIdList(this.accounts);
        }
        if(response.data.parent){
          this.shops=new Array(response.data.parent);
          this.inputForm.parentId=response.data.parent.id;
        }
      });
      }
    },created(){
      this.initPage();
    },activated () {
      if(!this.$route.query.headClick) {
        this.initPage();
      }
    }
  }
</script>
