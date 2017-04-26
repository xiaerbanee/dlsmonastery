<template>
  <div>
    <head-tab active="expressOrderForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('expressOrderForm.fromDepotId')" prop="fromDepotId">
          <el-select v-model="inputForm.fromDepotId" filterable  >
            <el-option v-for="item in formProperty.fromDepots" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('expressOrderForm.toDepotId')" prop="toDepotId">
          <el-select v-model="inputForm.toDepotId"  filterable remote  clearable :placeholder="$t('expressOrderForm.inputWord')" :remote-method="remoteDepot" :loading="remoteLoading">
            <el-option v-for="depot in toDepots" :key="depot.id" :label="depot.name" :value="depot.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('expressOrderForm.expressCompanyId')" prop="expressCompanyId">
          <el-select v-model="inputForm.expressCompanyId" filterable  :placeholder="$t('expressOrderForm.inputWord')" :loading="remoteLoading" >
            <el-option v-for="item in formProperty.expressCompanys"  :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('expressOrderForm.contact')" prop="contator">
          <el-input v-model="inputForm.contator"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressOrderForm.address')" prop="address">
          <el-input v-model="inputForm.address"></el-input>
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
    export default{
      data(){
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            formProperty:{},
            remoteLoading:false,
            toDepots:[],
            inputForm:{
              id:this.$route.query.id,
              fromDepotId:'',
              toDepotId:'',
              expressCompanyId:'',
              contator:'',
              address:'',
              mobilePhone:''
            },
            rules: {
            }
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/crm/expressOrder/update',qs.stringify(this.inputForm)).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'expressOrderList',query:util.getQuery("expressOrderList")})
                }
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },remoteDepot(query){
          if (query !== '') {
            this.remoteLoading = true;
            axios.get('/api/crm/depot/search',{params:{name:query}}).then((response)=>{
              this.toDepots=response.data;
              this.remoteLoading = false;
            })
          }
        }
      },created(){
        axios.get('/api/crm/expressOrder/getFormProperty').then((response)=>{
          this.formProperty=response.data;
        });
        if(!this.isCreate){
          axios.get('/api/crm/expressOrder/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
            if(response.data.toDepot !=null){
              this.toDepots = new Array(response.data.toDepot);
            }
          })
        }
      }
    }
</script>
