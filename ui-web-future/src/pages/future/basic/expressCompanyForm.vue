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
            <el-option v-for="item in inputForm.expressTypeList" :key="item" :label="item" :value="item"></el-option>
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
          <el-input v-model="inputForm.shouldGetRule"></el-input>
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
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            inputForm:{},
            submitData:{
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
            }
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              util.copyValue(this.inputForm, this.submitData);
              axios.post('/api/ws/future/basic/expressCompany/save',qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'expressCompanyList',query:util.getQuery("expressCompanyList")})
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
          axios.get('/api/ws/future/basic/expressCompany/getFormProperty',{params: {id:this.$route.query.id}}).then((response)=>{
            this.inputForm = response.data;
          });
      }
    }
</script>
