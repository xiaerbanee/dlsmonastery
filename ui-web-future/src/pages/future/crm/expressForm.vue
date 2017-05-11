<template>
  <div>
    <head-tab active="expressForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('expressForm.expressOrderType')" prop="code" v-if="!isCreate">
          {{inputForm.expressOrder.extendType}}
        </el-form-item>
        <el-form-item :label="$t('expressForm.billCode')" prop="weight"  v-if="!isCreate">
          {{inputForm.expressOrder.id}}
        </el-form-item>
        <el-form-item :label="$t('expressForm.toDepotName')" prop="toDepotId">
          <el-select v-model="inputForm.expressOrder.toDepotId" filterable remote :placeholder="$t('expressForm.inputWord')" :remote-method="remoteDepot" :loading="remoteLoading" >
            <el-option v-for="item in depots" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('expressForm.expressOrderCode')" prop="code">
          <el-input v-model="inputForm.code"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressForm.weight')" prop="weight">
          <el-input v-model="inputForm.weight"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressForm.qty')" prop="qty">
          <el-input v-model="inputForm.qty"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressForm.tracking')" prop="tracking">
          <el-input v-model="inputForm.tracking"></el-input>
        </el-form-item>
        <el-form-item :label="$t('expressForm.remarks')" prop="remarks">
          <el-input type="textarea" :rows="2" v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('expressForm.save')}}</el-button>
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
            depots:[],
            remoteLoading:false,
            inputForm:{
              id:this.$route.query.id,
              code:'',
              weight:'',
              qty:'',
              tracking:'',
              remarks:'',
              expressOrder:{
                id:'',
                toDepotId:'',
                extendType:''
              }
            },
            rules: {
              name: [{ required: true, message: this.$t('expressForm.prerequisiteMessage')}],
              code: [{ required: true, message: this.$t('expressForm.prerequisiteMessage')}]
            }
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/crm/express/save',qs.stringify(this.inputForm, {allowDots:true})).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'expressList',query:util.getQuery("expressList")})
                }
              }).catch(function () {
                this.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },remoteDepot(query){
          if (query !== '') {
            this.remoteLoading = true;
            axios.get('/api/crm/depot/search',{params:{name:query}}).then((response)=>{
              this.depots=response.data;
              this.remoteLoading = false;
            })
          }
        }
      },created(){
        if(!this.isCreate){
          axios.get('/api/crm/express/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
            if(response.data.expressOrder.toDepot!=null){
              this.depots=new Array(response.data.expressOrder.toDepot)
            }
          })
        }
      }
    }
</script>
