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
        <el-form-item :label="$t('chainForm.shopType')" prop="depotList">
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
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        inputForm:{},
        loading:false,
        submitData:{
            id:"",
            name:"",
            remarks:"",
            depotIdList:[]
        },
        rules: {
          name: [{ required: true, message: this.$t('chainForm.prerequisiteMessage')}]
        }
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            util.copyValue(this.inputForm,this.submitData);
            axios.post('/api/ws/future/basic/chain/save',qs.stringify(this.submitData, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
            this.submitDisabled = false;
              if(this.isCreate){
                form.resetFields();
              } else {
                this.$router.push({name:'chainList',query:util.getQuery("chainList")})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },initPage(){
        axios.get('/api/ws/future/basic/chain/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm = response.data;
        });
      }
      },created(){
        this.initPage();
    },activated () {
      if(!this.$route.query.headClick) {
        this.initPage();
      }
    }
  }
</script>
