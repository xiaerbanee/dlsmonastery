<template>
  <div>
    <head-tab active="accountChangeBatchForm"></head-tab>
    <div>
      <el-alert  :title="errorMesssage" type="error" style="margin-bottom: 20px" closable  v-if="errorMesssage!==''"></el-alert>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item :label="$t('accountChangeBatchForm.exportTemplate')">
              <a style="text-decoration:underline;color:blue" href="javascript:void(0);" @click="downLoad">{{$t('accountChangeBatchForm.downLoad')}}</a>
            </el-form-item>
            <el-form-item :label="$t('accountChangeBatchForm.exportData')" prop="folderFileId">
              <el-upload action="/api/general/sys/folderFile/upload?uploadPath=/员工调整" :on-change="handleChange" :on-remove="handleRemove" :on-preview="handlePreview" :file-list="fileList" list-type="picture" >
                <el-button size="small" type="primary">{{$t('accountChangeBatchForm.clickUpload')}}</el-button>
              </el-upload>
            </el-form-item>
            <el-form-item :label="$t('accountChangeBatchForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled"  @click="formSubmit()">{{$t('accountChangeBatchForm.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>

<script>
  export default{
    data(){
      return this.getData();
    },
    methods:{
      getData(){
        return{
          isCreate:this.$route.query.id==null,
          submitDisabled:false,
          fileList:[],
          errorMesssage:"",
          inputForm:{
          },
          rules: {
          },
        }
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.inputForm.folderFileId = util.getFolderFileIdStr(this.fileList);
            axios.post('/api/basic/hr/accountChange/import', qs.stringify(this.inputForm)).then((response)=> {
              if(response.data.success){
                this.$message(response.data.message);
              }else{
                this.submitDisabled = false;
                this.errorMesssage=response.data.message;
                return;
              }
              if(!this.isCreate) {
                this.submitDisabled = false;
                  util.closeAndBackToPage(this.$router,"accountChangeList")
              } else{
                Object.assign(this.$data, this.getData())
                this.initPage();
              }
            }).catch( ()=> {
              that.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },handlePreview(file) {
        window.open(file.url);
      },handleChange(file, fileList) {
        this.fileList = fileList;
      },handleRemove(file, fileList) {
        this.fileList = fileList;
      },downLoad(){
        window.location.href="/api/basic/hr/accountChange/import/template";
      },
      initPage() {
        //初始化数据
      }
    },created() {
      //加载initPage()方法
    }
  }
</script>
