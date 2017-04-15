<template>
  <div>
    <head-tab :active="$t('dutyAnnualForm.dutyAnnualForm') "></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item :label="$t('dutyAnnualForm.exportTemplate')">
              <a style="text-decoration:underline;color:blue" href="/api/basic/hr/dutyAnnual/import/template">{{$t('dutyAnnualForm.downLoad')}}</a>
            </el-form-item>
            <el-form-item :label="$t('dutyAnnualForm.exportData')" prop="folderFileId">
              <el-upload action="/api/basic/sys/folderFile/upload?uploadPath=/年假管理" :on-change="handleChange" :on-remove="handleRemove" :on-preview="handlePreview" :file-list="fileList" list-type="picture" >
                <el-button size="small" type="primary">{{$t('dutyAnnualForm.clickUpload')}}</el-button>
              </el-upload>
            </el-form-item>
            <el-form-item :label="$t('dutyAnnualForm.annualYear')" prop="annualYear">
              <el-date-picker v-model="inputForm.annualYear" align="right" type="year" :placeholder="$t('dutyAnnualForm.selectAnnualYear')"></el-date-picker>
            </el-form-item>
            <el-form-item :label="$t('dutyAnnualForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled"  @click="formSubmit()">{{$t('dutyAnnualForm.save')}}</el-button>
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
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        fileList:[],
        inputForm:{
          id:'',
          folderFileId:'',
          annualYear:'',
          remarks:''
        },
        rules: {
          folderFileId: [{ required: true, message: this.$t('dutyAnnualForm.prerequisiteMessage')}],
          annualYear: [{ required: true, message: this.$t('dutyAnnualForm.prerequisiteMessage')}],
        },
      }
    },
    methods:{
      formSubmit(){
       this.inputForm.folderFileId = util.getFolderFileIdStr(this.fileList);
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.inputForm.annualYear=util.formatLocalDate(this.inputForm.annualYear)
            axios.post('/api/basic/hr/dutyAnnual/import', qs.stringify(this.inputForm)).then((response)=> {
              if(response.data.message){
                this.$message(response.data.message);
              }
              if(this.isCreate){
                form.resetFields();
                this.fileList = [];
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'dutyAnnualList',query:util.getQuery("dutyAnnualList")})
              }
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
      }
    },created(){

    }
  }
</script>
