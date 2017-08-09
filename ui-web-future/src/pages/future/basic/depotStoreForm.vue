<template>
  <div>
    <head-tab active="depotStoreForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
            <el-form-item label="门店" prop="depotId">
              <depot-select v-model="inputForm.depotId" category="shop" :disabled="!isCreate"></depot-select>
            </el-form-item>
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
            <el-form-item label="是否是广告仓库" prop="popShop">
              <bool-radio-group v-model="inputForm.popShop"></bool-radio-group>
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
        inputForm:{
          extra:{}
        },
        rules: {
          depotId: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          type: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          jointLevel:[{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          popShop: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          storeGroup: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
        }
      }
    },
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
            if (valid) {
              let submitData=util.deleteExtra(this.inputForm);
              axios.post('/api/ws/future/basic/depotStore/save', qs.stringify(submitData, {allowDots:true})).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  Object.assign(this.$data,this.getData());
                  this.initPage();
                }else {
                  this.$router.push({name:'depotStoreList',query:util.getQuery("depotStoreList"),params:{_closeFrom:true}})
                }
              }).catch(()=> {
                this.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
        })
      },initPage() {
        axios.get('/api/ws/future/basic/depotStore/getForm').then((response)=>{
          this.inputForm = response.data;
          axios.get('/api/ws/future/basic/depotStore/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
          });
        });
      }
    },created () {
      this.initPage();
    }
  }
</script>
