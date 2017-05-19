<template>
  <div>
    <head-tab active="depotStoreForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form-item label="名称" prop="depotForm.name">
              <el-input v-model="inputForm.depotForm.name" />
            </el-form-item>
            <el-form-item label="Code" prop="depotForm.code">
              <el-input v-model="inputForm.depotForm.code" />
            </el-form-item>
            <el-form-item label="机构" prop="depotForm.officeId">
              <office-select v-model="inputForm.depotForm.officeId"></office-select>
            </el-form-item>
            <el-form-item label="负责人" prop="depotForm.contator">
              <account-select  v-model="inputForm.depotForm.contator"/>
            </el-form-item>
            <el-form-item label="手机号" prop="depotForm.mobilePhone">
              <el-input v-model="inputForm.depotForm.mobilePhone" />
            </el-form-item>
            <el-form-item label="地址" prop="depotForm.address">
              <el-input v-model="inputForm.depotForm.address" />
            </el-form-item>
            <el-form-item label="税务门店名称" prop="depotForm.taxName">
              <el-input v-model="inputForm.depotForm.taxName" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="寄售门店" prop="depotForm.delegateDepotId">
              <depot-select v-model="inputForm.depotForm.delegateDepotId" category="directShop"></depot-select>
            </el-form-item>
            <el-form-item label="仓库类型" prop="type">
              <el-select v-model="inputForm.type" filterable>
                <el-option v-for="item in inputForm.depotStoreTypeList" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="是否是广告仓库" prop="depotForm.popShop">
              <bool-radio-group v-model="inputForm.depotForm.popShop"></bool-radio-group>
            </el-form-item>
            <el-form-item label="地区" prop="depotForm.districtId">
              <district-select v-model="inputForm.depotForm.districtId"></district-select>
            </el-form-item>
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
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        delegateDepotList:"",
        inputForm:{depotForm:{}},
        submitData:{
          id:'',
          depotForm:{
            name:"",
            popShop:"",
            code:"",
            officeId:"",
            contator:"",
            delegateDepotI:"",
            mobilePhone:"",
            address:"",
            taxName:"",
            delegateDepotId:"",
            districtId:"",
          },
          type:"",
          storeGroup:"",
          remarks:"",
        },
        rules: {
          "depotForm.name": [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          "depotForm.code": [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          "depotForm.officeId": [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          "depotForm.contator": [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          "depotForm.mobilePhone": [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          "depotForm.address": [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          "depotForm.districtId": [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          "depotForm.taxName": [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          type: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          storeGroup:[{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
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
            axios.post('/api/ws/future/basic/depotStore/save', qs.stringify(this.submitData, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'depotStoreList',query:util.getQuery("depotStoreList")})
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
      axios.get('/api/ws/future/basic/depotStore/getFormProperty',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm = response.data;
        if(!response.data.depotForm){
            this.inputForm.depotForm={};
            this,inputForm.depotForm.popShop=0;
        }else {
          this.inputForm.depotForm.popShop=this.inputForm.depotForm.popShop?1:0;
        }
      })
    }
  }
</script>
