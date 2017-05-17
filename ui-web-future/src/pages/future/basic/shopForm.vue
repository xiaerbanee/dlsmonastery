<template>
  <div>
    <head-tab active="shopForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item label="选择客户" prop="clientId">
          <el-select v-model="inputForm.clientId" filterable>
            <el-option v-for="item in clientList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="inputForm.name" />
        </el-form-item>
        <el-form-item label="Code" prop="code">
          <el-input v-model="inputForm.code" />
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
          <district-select v-model="inputForm.address" />
        </el-form-item>
        <el-form-item label="价格体系" prop="pricesystemId">
          <el-select v-model="inputForm.pricesystemId" filterable>
            <el-option v-for="item in pricesystemList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="价格体系" prop="chainId">
          <el-select v-model="inputForm.chainId" filterable>
            <el-option v-for="item in chainList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="额度" prop="credit">
          <el-input v-model="inputForm.credit" />
        </el-form-item>
        <el-form-item label="物料价格体系" prop="adPricesystemId">
          <el-select v-model="inputForm.adPricesystemId" filterable>
            <el-option v-for="item in adPricesystemList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="快递公司" prop="expressCompanyId">
          <el-select v-model="inputForm.expressCompanyId" filterable>
            <el-option v-for="item in expressCompanyList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
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
        <el-form-item label="是否是广告仓库" prop="popShop">
          <bool-radio-group v-model="inputForm.popShop"></bool-radio-group>
        </el-form-item>
        <el-form-item label="公司分组" prop="companyGroup">
          <el-input v-model="inputForm.companyGroup" />
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"  :disabled="submitDisabled" @click="formSubmit()">{{$t('dictMapForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import officeSelect from 'components/basic/office-select'
  import districtSelect from 'components/general/district-select'
  export default {
    components:{officeSelect,districtSelect},
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        inputForm:{},
        submitData:{
          id:'',
          clientId:"",
          code:"",
          depotShopId:"",
          name:"",
          officeId:"",
          contator:"",
          mobilePhone:"",
          address:"",
          districtId:"",
          pricesystemId:"",
          credit:"",
          chainId:"",
          adPricesystemId:"",
          expressCompanyId:"",
          printPrice:"",
          printType:"",
          rebate:"",
          taxName:"",
          adShop:false,
          isHidden:false,
          popShop : false,
          companyGroup:"",
        },
        rules: {
          clientId: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          code: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          depotShopId: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          name: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          officeId: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          contator: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          mobilePhone: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          address: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          districtId: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          pricesystemId: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          credit:[{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          adPricesystemId: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          expressCompanyId: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          printPrice: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          rebate: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          taxName: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          adShop: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          isHidden:[{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          popShop:[{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          companyGroup:[{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
        }
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            util.copyValue(this.inputForm,this.submitData);
            axios.post('/api/ws/future/basic/depotShop/saveDepot', qs.stringify(this.submitData)).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'depotShopList',query:util.getQuery("depotShopList")})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      }
    },created(){
      axios.get('/api/ws/future/basic/depotShop/findDepotForm',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm = response.data;
      })
    }
  }
</script>
