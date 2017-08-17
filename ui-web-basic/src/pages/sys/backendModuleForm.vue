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
            <el-option v-for="item in inputForm.extra.backendList" :key="item.id" :label="item.name" :value="item.id"></el-option>
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
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            inputForm:{
                extra:{}
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
              axios.post('/api/basic/sys/backendModule/save', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  Object.assign(this.$data,this.getData());
                  this.initPage();
                }else{
                  this.submitDisabled = false;
                  this.$router.push({name:'backendModuleList',query:util.getQuery("backendModuleList"),params:{_closeFrom:true}});
                }
              }).catch(function () {
                that.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },initPage(){
          axios.get('/api/basic/sys/backendModule/getForm').then((response)=>{
            this.inputForm = response.data;
            axios.get('/api/basic/sys/backendModule/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
              util.copyValue(response.data,this.inputForm);
            });
          });
        }
      },created(){
      this.initPage();
      }
    }
</script>
