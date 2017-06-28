<template>
  <div>
    <head-tab active="dutyWorktimeForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="24">
          <el-col :span="6">
        <el-form-item :label="$t('dutyWorktimeForm.yearMonth')" prop="yearMonth">
          <el-date-picker v-model="inputForm.yearMonth"  type="month" :placeholder="$t('dutyWorktimeForm.selectMonth')"></el-date-picker>
        </el-form-item>
        <el-form-item :label="$t('dutyWorktimeForm.importFile')" prop="folderFileId">
          <el-upload action="/api/general/sys/folderFile/upload?uploadPath=/dutyWorktime" multiple    :on-change="handleChange" :on-remove="handleRemove" :on-preview="handlePreview" :file-list="fileList" list-type="picture">
            <el-button size="small" type="primary">{{$t('dutyWorktimeForm.clickUpload')}}</el-button>
            <div class="el-upload__tip" slot="tip">{{$t('dutyWorktimeForm.uploadImageSizeFor5000KB')}}</div>
          </el-upload>
        </el-form-item>
        <el-form-item :label="$t('dutyWorktimeForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('dutyWorktimeForm.save')}}</el-button>
        </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<script>
    import util from "../../../utils/util";

    export default{
      data(){
        return this.getData();
      },
      methods:{
        getData(){
          return{
            submitDisabled:false,
            fileList:[],
            inputForm:{
              extra:{}
            },
            rules: {
              folderFileId: [{ required: true, message: this.$t('dutyWorktimeForm.prerequisiteMessage')}],
              yearMonth: [{ required: true, message: this.$t('dutyWorktimeForm.prerequisiteMessage')}],
            },
          }
        },
        formSubmit(){
          var that = this;
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          this.inputForm.folderFileId = util.getFolderFileIdStr(this.fileList);
          form.validate((valid) => {
            if (valid) {
              console.log(this.inputForm);
              var submitData=util.deleteExtra(this.inputForm);
              submitData.yearMonth=util.formatLocalMonth(this.inputForm.yearMonth)
              axios.post('/api/basic/hr/dutyWorktime/import', qs.stringify(submitData)).then((response)=> {
                this.$message(response.data.message);
                Object.assign(this.$data, this.getData());
                this.initPage();
              }).catch( ()=> {
                that.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        }, handlePreview(file) {
          window.open(file.url);
        },handleChange(file, fileList) {
          this.fileList = fileList;
        },handleRemove(file, fileList) {
          this.fileList = fileList;
        },initPage(){
          axios.get('/api/basic/hr/dutyWorktime/getForm').then((response)=>{
            this.inputForm = response.data;
          })
        }
      },created(){
          this.initPage();
      }
  }
</script>
