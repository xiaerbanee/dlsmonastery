<template>
  <div>
    <head-tab :active="$t('folderForm.folderForm') "></head-tab>
    <div >
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('folderForm.parentName')" prop="parentName">
          <el-select v-model="inputForm.parentName" filterable :placeholder="$t('folderForm.selectGroup')">
            <el-option v-for="folder in formProperty.folders" :key="folder.name":label="folder.name" :value="folder.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('folderForm.name')" prop="name">
          <el-input v-model="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('folderForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('folderForm.save')}}</el-button>
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
            inputForm:{
              id:'',
              name:'',
              parentName:"",
              remarks:''
            },
            rules: {
              parentName: [{ required: true, message: this.$t('folderForm.prerequisiteMessage')}],
              name: [{ required: true, message: this.$t('folderForm.prerequisiteMessage')}]
            }
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/sys/folder/save', qs.stringify(this.inputForm)).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'folderList',query:util.getQuery("folderList")})
                }
              });
            }else{
              this.submitDisabled = false;
            }
          })
        }
      },created(){
        axios.get('/api/sys/folder/getFormProperty').then((response)=>{
          this.formProperty=response.data;
        });
        if(!this.isCreate){
          axios.get('/api/sys/folder/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
          })
        }
      }
    }
</script>
