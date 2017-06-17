<template>
  <div>
    <head-tab active="chainForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('chainForm.name')" prop="name">
          <el-input v-model="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('chainForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item :label="$t('chainForm.shop')" prop="depotList">
          <depot-select v-model="inputForm.depotIdList" category="shop" multiple="multiple"></depot-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('chainForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import depotSelect from 'components/future/depot-select'
  export default{
      components:{
          depotSelect
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
        loading:false,
        rules: {
          name: [{ required: true, message: this.$t('chainForm.prerequisiteMessage')}]
        }
      }
    },
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/basic/chain/save',qs.stringify(util.deleteExtra(this.inputForm), {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(this.inputForm.isCreate){
                Object.assign(this.$data,this.inputForm);
                this.initPage();
              }else {
                this.submitDisabled = false;
                this.$router.push({name:'chainList',query:util.getQuery("chainList")})
              }
            }).catch( ()=> {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },initPage(){
        axios.get('/api/ws/future/basic/chain/getForm').then((response)=>{
          this.inputForm = response.data;
          if(!this.isCreate){
            axios.get('/api/ws/future/basic/chain/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
              util.copyValue(response.data,this.inputForm);
            });
          }
        });
      }
    },created () {
      this.initPage();
    }
  }
</script>
