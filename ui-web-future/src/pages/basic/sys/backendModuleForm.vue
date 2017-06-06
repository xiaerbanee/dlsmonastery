<template>
  <div>
    <head-tab active="backendModuleForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('backendModuleForm.code')" prop="code">
          <el-input v-model.number="inputForm.code"></el-input>
        </el-form-item>
        <el-form-item :label="$t('backendModuleForm.name')" prop="name">
          <el-input v-model.number="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item label="所属项目" prop="backendId">
          <el-select v-model="inputForm.backendId" filterable placeholder="所属项目">
            <el-option v-for="item in inputProperty.backendList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('backendModuleForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('backendModuleForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
    export default{
      data:function () {
        return this.getData();
      },
      methods:{
        getData(){
          return{
            isInit:false,
            submitDisabled:false,
            inputForm:{},
            inputProperty:{},
            submitData:{
              id:'',
              code:'',
              backendId:'',
              name:'',
              remarks:''
            },
            rules: {
              backendId: [{ required: true, message: this.$t('backendModuleForm.prerequisiteMessage')}],
              name: [{ required: true, message: this.$t('backendModuleForm.prerequisiteMessage')}],
              code: [{ required: true, message: this.$t('backendModuleForm.prerequisiteMessage')}]
            }
          }
        },
        formSubmit(){
          var that = this;
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              util.copyValue(this.inputForm,this.submitData);
              axios.post('/api/basic/sys/backendModule/save', qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                Object.assign(this.$data,this.getData());
                if(!this.inputForm.create){
                  this.$router.push({name:'backendModuleList',query:util.getQuery("backendModuleList")})
                }
              }).catch(function () {
                that.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        }
      },activated () {
        if(!this.$route.query.headClick || !this.isInit) {
          Object.assign(this.$data,this.getData());
          axios.get('/api/basic/sys/backendModule/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            this.inputForm = response.data;
          })
          axios.get('/api/basic/sys/backendModule/getForm').then((response)=>{
            this.inputProperty = response.data;
          })
        }
        this.isInit = true;
      }
    }
</script>
