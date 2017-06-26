<template>
  <div>
    <head-tab active="folderForm"></head-tab>
    <div >
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('folderForm.parentId')" prop="parentId">
          <el-select v-model="inputForm.parentId" filterable :placeholder="$t('folderForm.selectGroup')">
            <el-option v-for="folder in inputForm.extra.folderList" :key="folder.id" :label="folder.name" :value="folder.id"></el-option>
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
    data:function () {
      return this.getData();
    },
    methods:{
      getData(){
        return{
          isInit:false,
          isCreate:this.$route.query.id==null,
          submitDisabled:false,
          inputForm:{
            extra:{}
          },
          rules: {
            name: [{ required: true, message: this.$t('folderForm.prerequisiteMessage')}]
          }
        }
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/general/sys/folder/save', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
              this.$message(response.data.message);
              Object.assign(this.$data,this.getData());
              if(!that.isCreate){
                this.$router.push({name:'folderList',query:util.getQuery("folderList"),params:{_closeFrom:true}});
              }
            }).catch(function () {
              that.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },initPage(){
        axios.get('/api/general/sys/folder/getForm').then((response)=>{
          this.inputForm = response.data;
          axios.get('/api/general/sys/folder/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
          });
        });
      }
    },created(){
        this.initPage();
    }
  }
</script>
