<template>
  <div>
    <head-tab active="expressCompanyForm"></head-tab>
    <div >
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('expressCompanyForm.name')" prop="name">
          <el-input v-model="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressCompanyForm.expressType')" prop="expressType">
          <el-select v-model="inputForm.expressType" filterable :placeholder="$t('expressCompanyForm.selectType')">
            <el-option v-for="item in inputForm.extra.expressTypeList" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('expressCompanyForm.district')" prop="districtId">
          <district-select v-model="inputForm.districtId">
          </district-select>
        </el-form-item>
        <el-form-item :label="$t('expressCompanyForm.reachPlace')" prop="reachPlace">
          <el-input v-model="inputForm.reachPlace" type="textarea"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressCompanyForm.shouldGetRule')" prop="shouldGetRule">
          <el-input v-model="inputForm.shouldGetRule" type="textarea"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressCompanyForm.address')" prop="address">
          <el-input v-model="inputForm.address" type="textarea"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressCompanyForm.phone')" prop="phone">
          <el-input v-model="inputForm.phone"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressCompanyForm.mobilePhone')" prop="mobilePhone">
          <el-input v-model="inputForm.mobilePhone"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressCompanyForm.contator')" prop="contator">
          <el-input v-model="inputForm.contator" ></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressCompanyForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks" type="textarea"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('expressCompanyForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import districtSelect from 'components/general/district-select'
  export default{
    components:{
      districtSelect,
    },
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
          rules: {
            name: [{ required: true, message: this.$t('expressCompanyForm.prerequisiteMessage')}],
            expressType: [{ required: true, message: this.$t('expressCompanyForm.prerequisiteMessage')}],
            districtId: [{ required: true, message: this.$t('expressCompanyForm.prerequisiteMessage')}],
            address: [{ required: true, message: this.$t('expressCompanyForm.prerequisiteMessage')}],
            contator: [{ required: true, message: this.$t('expressCompanyForm.prerequisiteMessage')}]
          }
        }
      },formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/basic/expressCompany/save',qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
              this.$message(response.data.message);
              Object.assign(this.$data, this.getData());
              if(this.isCreate){
                Object.assign(this.$data,this.getData());
                this.initPage;
              }else{
                util.closeAndBackToPage(this.$router,'expressCompanyList')
              }
            }).catch(function () {
              that.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },initPage () {
        axios.get('/api/ws/future/basic/expressCompany/getForm').then((response)=>{
          this.inputForm = response.data;
          if(!this.isCreate) {
            axios.get('/api/ws/future/basic/expressCompany/findOne', {params: {id: this.$route.query.id}}).then((response) => {
              util.copyValue(response.data, this.inputForm);
            });
          }
        });
      }
    },created(){
      this.initPage();
    }
  }
</script>
