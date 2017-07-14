<template>
  <div>
    <head-tab active="shopForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="10">
        <el-form-item label="选择客户" prop="clientId">
          <el-select v-model="inputForm.clientId"  filterable remote  :remote-method="remoteClient" :loading="remoteLoading"  :clearable=true>
            <el-option v-for="item in clientList"  :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="inputForm.name" />
        </el-form-item>
            <el-form-item label="总店" prop="parentId" >
              <depot-select v-model="inputForm.parentId" category="shop"></depot-select>
            </el-form-item>
        <el-form-item label="机构" prop="officeId">
          <office-select v-model="inputForm.officeId"></office-select>
        </el-form-item>
        <el-form-item label="负责人" prop="contator">
          <el-input v-model="inputForm.contator" />
        </el-form-item>
        <el-form-item label="手机号" prop="mobilePhone">
          <el-input v-model="inputForm.mobilePhone" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="inputForm.address" />
        </el-form-item>
        <el-form-item label="地区" prop="districtId">
          <district-select v-model="inputForm.districtId"></district-select>
        </el-form-item>
        <el-form-item label="价格体系" prop="pricesystemId">
          <el-select v-model="inputForm.pricesystemId" filterable>
            <el-option v-for="item in inputForm.extra.pricesystemList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="连锁体系" prop="chainId">
          <el-select v-model="inputForm.chainId" filterable>
            <el-option v-for="item in inputForm.extra.chainList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="额度" prop="credit">
          <el-input v-model="inputForm.credit" />
        </el-form-item>
          </el-col>
          <el-col :span="8">
        <el-form-item label="物料价格体系" prop="adPricesystemId">
          <el-select v-model="inputForm.adPricesystemId" filterable>
            <el-option v-for="item in inputForm.extra.adPricesystemList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="快递公司" prop="expressCompanyId">
          <express-company-select v-model="inputForm.expressCompanyId"></express-company-select>
        </el-form-item>
        <el-form-item label="是否打印价格" prop="printPrice">
          <bool-radio-group v-model="inputForm.printPrice"></bool-radio-group>
        </el-form-item>
        <el-form-item label="打印类型" prop="printType">
          <el-input v-model="inputForm.printType" />
        </el-form-item>
        <el-form-item label="是否让利" prop="rebate">
          <bool-radio-group v-model="inputForm.rebate"></bool-radio-group>
        </el-form-item>
        <el-form-item label="税务名称" prop="taxName">
          <el-input v-model="inputForm.taxName" />
        </el-form-item>
        <el-form-item label="是否是广告门店" prop="adShop">
          <bool-radio-group v-model="inputForm.adShop"></bool-radio-group>
        </el-form-item>
        <el-form-item label="是否隐藏" prop="isHidden">
          <bool-radio-group v-model="inputForm.isHidden"></bool-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"  :disabled="submitDisabled" @click="formSubmit()">{{$t('dictMapForm.save')}}</el-button>
        </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<script>
  import officeSelect from 'components/basic/office-select'
  import districtSelect from 'components/general/district-select'
  import boolRadioGroup from 'components/common/bool-radio-group'
  import expressCompanySelect from 'components/future/express-company-select'
  import depotSelect from 'components/future/depot-select'
  export default {
    components:{depotSelect,officeSelect,districtSelect,boolRadioGroup,expressCompanySelect},
    data(){
      return this.getData()
    },
    methods:{
      getData() {
      return{
        clientList:{},
        isCreate:this.$route.query.id == null,
        submitDisabled:false,
        inputForm:{
          extra:{}
        },
        remoteLoading:false,
        rules: {
          depotShopId: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          name: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          officeId: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          contator: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          mobilePhone: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          address: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
        }
      }
    },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/basic/depotShop/saveDepot', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
            this.$message(response.data.message);
            if(this.isCreate){
              Object.assign(this.$data, this.getData());
              this.initPage();
            }else {
              this.$router.push({name:'depotShopList',query:util.getQuery("depotShopList"),params:{_closeFrom:true}})
            }
            }).catch(function () {
              that.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },remoteClient(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/ws/future/basic/client/search',{params:{name:query}}).then((response)=>{
            this.clientList = response.data;
            this.remoteLoading = false;
          });
        }
      },initPage(){
        axios.get('/api/ws/future/basic/depotShop/findDepotForm').then((response)=>{
          this.inputForm = response.data;
          axios.get('/api/ws/future/basic/depot/findByDepotShopId',{params: {depotShopId:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
            console.log(response.data)
            if(util.isNotBlank(this.inputForm.clientId)){
              this.clientList =new Array({id:response.data.clientId,name:response.data.clientName})
            }
          });
        })
      }
    },created () {
      this.initPage();
    }
  }
</script>
