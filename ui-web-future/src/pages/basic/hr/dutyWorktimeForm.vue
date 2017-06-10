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
        <el-form-item :label="$t('dutyWorktimeForm.importFile')" prop="importFile">
          <el-upload action = "/api/basic/hr/dutyWorktime/import" multiple  :on-change="handleChange" :on-remove="handleRemove" :on-preview="handlePreview" :file-list="fileList" list-type="picture">
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
    export default{
      data(){
        return this.getData();
      },
      methods:{
        getData(){
          return{
            isInit:false,
            submitDisabled:false,
            fileList:[],
            inputForm:{
              importFile:'',
              yearMonth:'',
              remarks:''
            },
            rules: {
              importFile: [{ required: true, message: this.$t('dutyWorktimeForm.prerequisiteMessage')}],
              yearMonth: [{ required: true, message: this.$t('dutyWorktimeForm.prerequisiteMessage')}],
            },
          }
        },
        formSubmit(){
          var that = this;
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.get('/api/basic/hr/dutyWorktime/import', {params: this.inputForm}).then((response)=> {
                this.$message(response.data.message);
                Object.assign(this.$data, this.getData());
              }).catch(function () {
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
        }
      },
      activated () {
        if(!this.$route.query.headClick || !this.isInit) {
          Object.assign(this.$data, this.getData());
          
        }
        this.isInit = true;
      }
  }
</script>
