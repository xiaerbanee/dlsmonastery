<template>
  <div>
    <head-tab active="depotChangeForm"></head-tab>
   <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('depotChangeForm.depotName')" prop="depotId">
              <depot-select v-model="inputForm.depotId" category="shop" @input="getOldValue"></depot-select>
            </el-form-item>
            <el-form-item :label="$t('depotChangeForm.type')" prop="type">
              <el-select v-model="inputForm.type" filterable clearable :placeholder="$t('depotChangeForm.selectGroup')" @change="getOldValue">
                <el-option v-for="item in inputForm.extra.types" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('depotChangeForm.expiryDate')" prop="expiryDate">
              <date-picker v-model="inputForm.expiryDate"></date-picker>
            </el-form-item>
            <el-form-item :label="$t('depotChangeForm.oldValue')" prop="oldValue">
              <el-input v-model="inputForm.oldValue" readonly></el-input>
            </el-form-item>
            <el-form-item v-show="inputForm.type=='名称' ||inputForm.type=='信用额度'" :label="$t('depotChangeForm.newValue')" prop="newValue">
              <el-input v-model="inputForm.newValue" ></el-input>
            </el-form-item>
            <el-form-item v-show="inputForm.type=='有无导购' || inputForm.type=='是否让利'" :label="$t('depotChangeForm.newValue')" prop="newValue">
              <el-select  v-model="inputForm.newValue">
                <el-option value="是">是</el-option>
                <el-option value="否">否</el-option>
              </el-select>
            </el-form-item>
            <el-form-item v-show="inputForm.type=='价格体系'" :label="$t('depotChangeForm.newValue')" prop="newValue" >
              <el-select v-model="inputForm.newValue" filterable clearable :placeholder="$t('pricesystemChangeList.inputKey')">
                <el-option v-for="pricesystem in inputForm.extra.pricesystems" :key="pricesystem.name" :label="pricesystem.name" :value="pricesystem.name"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('depotChangeForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary"  :disabled="submitDisabled" @click="formSubmit()">{{$t('depotChangeForm.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>

<script>
  import depotSelect from 'components/future/depot-select'
  import boolSelect from 'components/common/bool-select'
  import ElOption from "../../../../node_modules/element-ui/packages/select/src/option.vue";
  export default{
    components:{
      ElOption,
      depotSelect,boolSelect},
    data(){
      return this.getData()
    },
    methods:{
      getData() {
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        inputForm:{
            extra:{}
        },
        shop:{},
        rules: {
          depotId: [{ required: true, message: this.$t('depotChangeForm.prerequisiteMessage')}],
          type: [{ required: true, message: this.$t('depotChangeForm.prerequisiteMessage')}],
          newValue: [{ required: true, message: this.$t('depotChangeForm.prerequisiteMessage')}],
        },
        remoteLoading:false
      }
    },
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/crm/depotChange/save', qs.stringify(util.deleteExtra(this.inputForm), {allowDots:true})).then((response)=> {
            this.$message(response.data.message);
              if(this.isCreate){
                Object.assign(this.$data, this.getData());
                this.initPage();
              }else{
                this.submitDisabled = false;
                this.$router.push({name:'depotChangeList',query:util.getQuery("depotChangeList")})
              }
            }).catch(()=> {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },getOldValue(){
          var that = this;
          if(this.inputForm.depotId == null||this.inputForm.type == null){
              return;
          }
          axios.get('/api/ws/future/basic/depot/findOne',{params: {id:this.inputForm.depotId}}).then((response)=>{
            that.shop = response.data;
            if(that.shop == null){
              return;
            }
            if(this.inputForm.type == "价格体系"){
              this.inputForm.oldValue = that.shop.pricesystemName;
            }else if(this.inputForm.type == "名称"){
              this.inputForm.oldValue = that.shop.name;
            }else if(this.inputForm.type == "有无导购"){
              this.inputForm.oldValue = that.shop.hasGuide?"是":"否";
            }else if(this.inputForm.type == "是否让利"){
              this.inputForm.oldValue = that.shop.rebate?"是":"否";
            }else if(this.inputForm.type == "信用额度"){
              this.inputForm.oldValue = that.shop.credit;
            }
          });
      }, initPage(){
        axios.get('/api/ws/future/crm/depotChange/getForm').then((response)=>{
          this.inputForm = response.data;
          if(!this.isCreate){
            axios.get('/api/ws/future/crm/depotChange/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
              util.copyValue(response.data,this.inputForm);
            })
          }
        });

      }
    },created () {
      this.initPage();
    }
  }
</script>
