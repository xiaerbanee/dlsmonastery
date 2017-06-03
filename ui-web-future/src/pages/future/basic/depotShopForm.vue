<template>
  <div>
    <head-tab active="depotShopForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form-item label="选择仓库" prop="depotId" v-if="!isCreate">
              <depot-select v-model="inputForm.depotId" category="shop"></depot-select>
            </el-form-item>
            <el-form-item label="门店名称" prop="depotName" v-if="isCreate">
              <el-input v-model="inputForm.depotName" />
            </el-form-item>
            <el-form-item label="开业时间" prop="enableDate">
              <date-picker v-model="inputForm.enableDate"></date-picker>
            </el-form-item>
            <el-form-item label="地区属性" prop="areaType">
              <dict-map-select v-model="inputForm.areaType"  category="门店_地区属性" ></dict-map-select>
            </el-form-item>
            <el-form-item label="运营商类型" prop="carrierType">
              <dict-map-select v-model="inputForm.carrierType"  category="门店_运营商属性" ></dict-map-select>
            </el-form-item>
            <el-form-item label="营业额分类" prop="turnoverType">
              <dict-map-select v-model="inputForm.turnoverType"  category="门店_营业额分类" ></dict-map-select>
            </el-form-item>
            <el-form-item label="运营商社会渠道" prop="channelType">
              <dict-map-select v-model="inputForm.channelType"  category="门店_渠道类型" ></dict-map-select>
            </el-form-item>
            <el-form-item label="门店类型" prop="salePointType">
              <dict-map-select v-model="inputForm.salePointType"  category="门店_售点类型" ></dict-map-select>
            </el-form-item>
            <el-form-item label="乡镇类型" prop="townType">
              <el-select v-model="inputForm.townType"  filterable   :clearable=true >
                <el-option v-for="item in inputForm.townTypeList"  :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="乡镇名称" prop="townId" >
              <town-select v-model="inputForm.townId"></town-select>
            </el-form-item>
            <el-form-item label="体验店类型" prop="specialityStoreType" v-show="inputForm.specialityStoreType">
              <dict-map-select v-model="inputForm.specialityStoreType"  category="门店_体验店类型" ></dict-map-select>
            </el-form-item>
            <el-form-item label="是否核心商圈" prop="bussinessCenter">
              <bool-radio-group v-model="inputForm.bussinessCenter"></bool-radio-group>
            </el-form-item>
            <el-form-item label="核心商圈名称" prop="bussinessCenterName" v-show="inputForm.bussinessCenter">
              <dict-map-select v-model="inputForm.bussinessCenterName"  category="门店_核心商圈" ></dict-map-select>
            </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="有无导购" prop="hasGuide">
            <bool-radio-group v-model="inputForm.hasGuide"></bool-radio-group>
          </el-form-item>
          <el-form-item label="有无门头" prop="doorHead">
            <bool-radio-group v-model="inputForm.doorHead"></bool-radio-group>
          </el-form-item>
          <el-form-item label="是否体验店" prop="specialityStore">
            <bool-radio-group v-model="inputForm.specialityStore"></bool-radio-group>
          </el-form-item>
            <el-form-item label="面积大小" prop="shopArea" placeholder="单位为平方">
              <el-input v-model="inputForm.shopArea" />
            </el-form-item>
            <el-form-item label="2.0悬吊画框数量" prop="frameNum">
              <el-input v-model="inputForm.frameNum" />
            </el-form-item>
            <el-form-item label="2.0 双面体验台数量" prop="deskDoubleNum">
              <el-input v-model="inputForm.deskDoubleNum" />
            </el-form-item>
            <el-form-item label="2.0 单面体验台数量" prop="deskSingleNum">
              <el-input v-model="inputForm.deskSingleNum" />
            </el-form-item>
            <el-form-item label="展柜数量" prop="cabinetNum">
              <el-input v-model="inputForm.cabinetNum" />
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
  import boolRadioGroup from 'components/common/bool-radio-group'
  import dictMapSelect from 'components/basic/dict-map-select'
  import depotSelect from 'components/future/depot-select'
  import townSelect from 'components/general/town-select'
  export default {
    components:{dictMapSelect,boolRadioGroup,depotSelect,townSelect},
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        inputForm:{},
        submitData:{
          id:'',
          townId:"",
          hasGuide:false,
          depotId:'',
          areaType:'',
          carrierType:'',
          turnoverType:'',
          businessType:'',
          channelType:'',
          salePointType:'',
          townType:'',
         bussinessCenter:false,
         bussinessCenterName:"",
         doorHead:"",
         enableDate:"",
         specialityStore:false,
         specialityStoreType:"",
         shopArea:"",
         frameNum:0,
         deskDoubleNum:0,
         deskSingleNum:0,
         cabinetNum:0,
        },
        rules: {
          depotId: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          areaType: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          carrierType: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          turnoverType: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          salePointType: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          salePointType: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          bussinessCenter: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          doorHead: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          enableDate: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          specialityStore: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          shopArea: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
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
            axios.post('/api/ws/future/basic/depotShop/save', qs.stringify(this.submitData)).then((response)=> {
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
      },initPage(){
        axios.get('/api/ws/future/basic/depotShop/getForm',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm = response.data;
        })
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
