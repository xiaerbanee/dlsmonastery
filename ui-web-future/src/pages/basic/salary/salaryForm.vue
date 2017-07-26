<template>
  <div>
    <head-tab active="salaryForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('salaryForm.exportTemplate')" prop="category">
          <el-radio-group v-model="inputForm.salaryTemplateId">
            <el-radio :label="3">备选项</el-radio><a>下载</a>
            <el-radio :label="6">备选项</el-radio>
            <el-radio :label="9">备选项</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('salaryForm.exportData')" prop="sort">
          <el-upload action="/api/general/sys/folderFile/upload?uploadPath=/salary" multiple    :on-change="handleChange" :on-remove="handleRemove" :on-preview="handlePreview" :file-list="fileList" list-type="picture">
            <el-button size="small" type="primary">{{$t('salaryForm.clickUpload')}}</el-button>
            <div class="el-upload__tip" slot="tip">{{$t('salaryForm.uploadImageSizeFor5000KB')}}</div>
          </el-upload>
        </el-form-item>
          <el-form-item :label="$t('salaryForm.yearMonth')" prop="yearMonth">
            <el-date-picker v-model="month"  type="month" :placeholder="$t('salaryForm.selectMonth')"></el-date-picker>
          </el-form-item>
        <el-form-item :label="$t('salaryForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('dictEnumForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return this.getData()
    },
    methods:{
      getData() {
        return{
          isCreate:this.$route.query.id==null,
          submitDisabled:false,
          fileList:[],
          month:null,
          inputForm:{
            salaryTemplateId:null,
            extra:{}
          },
          rules: {

          }
        }
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/basic/salary/salary/save', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                Object.assign(this.$data, this.getData());
                this.initPage();
              }else{
                this.$router.push({name:'salaryList',query:util.getQuery("salaryList"), params:{_closeFrom:true}})
              }
            }).catch(function () {
              that.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },initPage(){

      },handlePreview(file) {
        window.open(file.url);
      },handleChange(file, fileList) {
        this.fileList = fileList;
      },handleRemove(file, fileList) {
        this.fileList = fileList;
      }
    },created () {
      this.initPage();
    }
  }
</script>
