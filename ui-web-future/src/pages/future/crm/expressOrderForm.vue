<template>
  <div>
    <head-tab active="expressOrderForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('expressOrderForm.fromDepotId')" prop="fromDepotId">
          <depot-select v-model="inputForm.fromDepotId" category="store"></depot-select>
        </el-form-item>
        <el-form-item :label="$t('expressOrderForm.toDepotId')" prop="toDepotId">
          <depot-select v-model="inputForm.toDepotId"  category="shop"></depot-select>
        </el-form-item>
        <el-form-item :label="$t('expressOrderForm.expressCompanyId')" prop="expressCompanyId">
          <express-company-select v-model="inputForm.expressCompanyId"  ></express-company-select>
        </el-form-item>
        <el-form-item :label="$t('expressOrderForm.contact')" prop="contator">
          <el-input v-model="inputForm.contator"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressOrderForm.address')" prop="address">
          <el-input type="textarea" v-model="inputForm.address"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressOrderForm.mobilePhone')" prop="mobilePhone">
          <el-input v-model="inputForm.mobilePhone"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('expressOrderForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import depotSelect from 'components/future/depot-select'
  import expressCompanySelect from 'components/future/express-company-select'
  export default{
    components: {
      depotSelect,
      expressCompanySelect,
    },
      data(){
        return this.getData()
      },
    methods:{
      getData() {
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            inputForm:{},
            rules: {
            }
          }
      },
        formSubmit(){
          var that = this;
          let form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              this.submitDisabled = true;
              axios.post('/api/ws/future/crm/expressOrder/save', qs.stringify(this.inputForm)).then((response)=> {
                this.$message(response.data.message);
                if(!that.isCreate){
                  this.$router.push({name:'expressOrderList',query:util.getQuery("expressOrderList"), params:{_closeFrom:true}})
                }else{
                  Object.assign(this.$data, this.getData());
                  this.initPage();
                }
              }).catch( () => {
                that.submitDisabled = false;
              });
            }
          })
        },initPage(){
        axios.get('/api/ws/future/crm/expressOrder/findDto', {params: {id: this.$route.query.id}}).then((response) => {
          this.inputForm = response.data;
      })
      }
      },created () {
      this.initPage();
    }
    }
</script>
