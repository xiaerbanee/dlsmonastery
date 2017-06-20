<template>
  <div>
    <head-tab active="depotStoreForm"></head-tab>
    <div>
      <el-form :model="depotDto" ref="depotForm" :rules="depotRules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form-item label="名称" prop="name">
              <el-input v-model="depotDto.name" />
            </el-form-item>
            <el-form-item label="Code" prop="code">
              <el-input v-model="depotDto.code" />
            </el-form-item>
            <el-form-item label="负责人" prop="contator">
              <account-select  v-model="depotDto.contator"/>
            </el-form-item>
            <el-form-item label="手机号" prop="mobilePhone">
              <el-input v-model="depotDto.mobilePhone" />
            </el-form-item>
            <el-form-item label="地址" prop="address">
              <el-input v-model="depotDto.address" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="机构" prop="officeId">
              <office-select v-model="depotDto.officeId"></office-select>
            </el-form-item>
            <el-form-item label="税务门店名称" prop="taxName">
              <el-input v-model="depotDto.taxName" />
            </el-form-item>
            <el-form-item label="寄售门店" prop="delegateDepotId">
              <depot-select v-model="depotDto.delegateDepotId" category="directShop"></depot-select>
            </el-form-item>
            <el-form-item label="是否是广告仓库" prop="popShop">
              <bool-radio-group v-model="depotDto.popShop"></bool-radio-group>
            </el-form-item>
            <el-form-item label="地区" prop="districtId">
              <district-select v-model="depotDto.districtId"></district-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form-item label="仓库级别" prop="jointLevel">
              <el-select v-model="inputForm.jointLevel" filterable>
                <el-option v-for="item in inputForm.extra.jointLevelList" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="仓库类型" prop="type">
              <el-select v-model="inputForm.type" filterable>
                <el-option v-for="item in inputForm.extra.depotStoreTypeList" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="分组" prop="storeGroup">
              <el-input v-model="inputForm.storeGroup"></el-input>
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
  import accountSelect from 'components/basic/account-select'
  import boolRadioGroup from 'components/common/bool-radio-group'
  import districtSelect from 'components/general/district-select'
  import depotSelect from 'components/future/depot-select'
  export default {
    components:{officeSelect,districtSelect,boolRadioGroup,accountSelect,depotSelect},
    data(){
      return this.getData()
    },
    methods:{
      getData() {
      return{
        isCreate:this.$route.query.id == null,
        submitDisabled:false,
        depotDto:{},
        inputForm:{
          extra:{}
        },
        depotRules: {
              name: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
            code: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
            officeId: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
            contator: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
            mobilePhone: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
            address: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
            districtId: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
            taxName: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
        },
        rules: {
          type: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          storeGroup:[{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
        }
      }
    },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        var depotForm = this.$refs["depotForm"];
        form.validate((valid) => {
          depotForm.validate((depotValid) => {
            if (valid&&depotValid) {
              var submitData=util.deleteExtra(this.inputForm);
              submitData.depotForm=this.depotDto;
              axios.post('/api/ws/future/basic/depotStore/save', qs.stringify(submitData, {allowDots:true})).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  Object.assign(this.$data,this.getData());
                  this.initPage();
                }else {
                  this.submitDisabled = false;
                  this.$router.push({name:'depotStoreList',query:util.getQuery("depotStoreList")})
                }
              }).catch(function () {
                that.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        })
      },initPage() {
        axios.get('/api/ws/future/basic/depotStore/getForm').then((response)=>{
          this.inputForm = response.data;
          axios.get('/api/ws/future/basic/depotStore/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm)
            axios.get('/api/ws/future/basic/depot/findOne',{params: {id:this.inputForm.depotId}}).then((response)=>{
              this.depotDto=response.data
            });
          });
        });
      }
    },created () {
      this.initPage();
    }
  }
</script>
