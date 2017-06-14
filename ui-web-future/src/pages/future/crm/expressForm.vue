<template>
  <div>
    <head-tab active="expressForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('expressForm.expressOrderToDepotId')" prop="expressOrderToDepotId">
          <depot-select :disabled="!isCreate" v-model="inputForm.expressOrderToDepotId" category="shop" ></depot-select>
        </el-form-item>
        <el-form-item :label="$t('expressForm.code')" prop="code">
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
          <el-input type="textarea"  v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('expressForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import depotSelect from 'components/future/depot-select'
  export default{
    components:{
      depotSelect,
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
              code: [{ required: true, message: this.$t('expressForm.prerequisiteMessage')}],
              expressOrderToDepotId: [{ required: true, message: this.$t('expressForm.prerequisiteMessage')}],
            }
          }
      },
        formSubmit(){
          var that = this;
          let form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              this.submitDisabled = true;
              util.copyValue(this.express,this.submitData);
              axios.post('/api/ws/future/crm/express/save', qs.stringify(this.inputForm)).then((response)=> {
                this.$message(response.data.message);
                if(response.data.success) {
                  if (!this.inputForm.create) {
                    this.submitDisabled = false;
                    this.$router.push({name: 'expressList', query: util.getQuery("expressList")});
                  }else{
                    Object.assign(this.$data, this.getData());
                    this.initPage();
                  }
                }
              }).catch( () => {
                that.submitDisabled = false;
              });
            }
          })
        },initPage(){
        axios.get('/api/ws/future/crm/express/getForm').then((response)=> {
          this.inputForm = response.data;
          axios.get('/api/ws/future/crm/express/findDto', {params: {id: this.$route.query.id}}).then((response) => {
            util.copyValue(response.data,this.inputForm);``
          });
        });
        }
      },created () {
      this.initPage();
    }
    }
</script>
