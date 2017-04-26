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
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('chainForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  export default{
    updated(){
      if(this.pageUpdated) {
        this.pageUpdated = false;
        if(this.content != null && this.content.length>0) {
          this.$refs['table'].clearSelection();
          this.content.map((v,index)=>{
            if(v.chainId ===this.inputForm.id){
              this.$refs['table'].toggleRowSelection(this.content[index],true);
            }
          })
          this.selectChange = true;
        }
      }
    },
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        inputForm:{},
        submitData:{
            id:"",
            name:"",
            remarks:""
        },
        selects:[],
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
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'chainList',query:util.getQuery("chainList")})
              }
            });
          }else{
            this.submitDisabled = false;
          }
        })
      }
      },created(){
        axios.get('/api/ws/future/basic/chain/findForm',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm = response.data;
        })
    }
  }
</script>
