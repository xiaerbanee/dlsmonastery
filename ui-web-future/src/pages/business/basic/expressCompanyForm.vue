<template>
  <div>
    <head-tab :active="$t('expressCompanyForm.expressCompanyForm') "></head-tab>
    <div >
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('expressCompanyForm.name')" prop="name">
          <el-input v-model="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressCompanyForm.expressType')" prop="expressType">
          <el-select v-model="inputForm.expressType" filterable :placeholder="$t('expressCompanyForm.selectType')">
            <el-option v-for="item in formProperty.expressType" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('expressCompanyForm.district')" prop="districtId">
          <el-select v-model="inputForm.districtId"  filterable remote :placeholder="$t('expressCompanyForm.inputWord')" :remote-method="remoteDistrict" :loading="remoteLoading"  :clearable=true >
            <el-option v-for="item in districts"  :key="item.id" :label="item.fullName" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('expressCompanyForm.reachPlace')" prop="reachPlace">
          <el-input v-model="inputForm.reachPlace"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressCompanyForm.shouldGetRule')" prop="shouldGetRule">
          <el-input v-model="inputForm.shouldGetRule"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressCompanyForm.address')" prop="address">
          <el-input v-model="inputForm.address" ></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressCompanyForm.phone')" prop="phone">
          <el-input v-model="inputForm.phone"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressCompanyForm.mobilePhone')" prop="mobilePhone">
          <el-input v-model="inputForm.mobilePhone"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressCompanyForm.contact')" prop="contator">
          <el-input v-model="inputForm.contator" ></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressCompanyForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('expressCompanyForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
    export default{
      data(){
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            formProperty:{},
            inputForm:{
              id:'',
              name:'',
              expressType:"",
              districtId:'',
              reachPlace:'',
              shouldGetRule:'',
              address:'',
              phone:'',
              mobilePhone:'',
              contator:"",
              remarks:''
            },
            rules: {
              name: [{ required: true, message: this.$t('expressCompanyForm.prerequisiteMessage')}]
            },
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
              axios.post('/api/crm/expressCompany/save',qs.stringify(this.inputForm)).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'expressCompanyList',query:util.getQuery("expressCompanyList")})
                }
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },remoteDistrict(query) {
          if (query !== '') {
            this.remoteLoading = true;
            axios.get('/api/basic/sys/district/search',{params:{key:query}}).then((response)=>{
              this.districts=response.data;
              this.remoteLoading = false;
            })
          } else {
            this.districts = [];
          }
        }
      },created(){
        axios.get('/api/crm/expressCompany/getFormProperty').then((response)=>{
          this.formProperty=response.data;
        });
        if(!this.isCreate){
          axios.get('/api/crm/expressCompany/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
            if(response.data.district){
              this.districts=new Array(response.data.district);
              this.inputForm.districtId=response.data.district.id;
           }
          })
        }
      }
    }
</script>
