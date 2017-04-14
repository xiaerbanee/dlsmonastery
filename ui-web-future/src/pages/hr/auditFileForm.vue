<template>
  <div>
    <head-tab :active="$t('auditFileForm.auditFileForm') "></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item :label="$t('auditFileForm.processTypeName')" prop="processTypeId">
              <el-select v-model="inputForm.processTypeId" filterable clearable :placeholder="$t('auditFileForm.inputWord')">
                <el-option v-for="type in formProperty.processTypes" :key="type.id" :label="type.name" :value="type.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('auditFileForm.title')" prop="title">
              <el-input v-model="inputForm.title"></el-input>
            </el-form-item>
            <el-form-item :label="$t('auditFileForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item :label="$t('auditFileForm.attachment')" prop="attachment">
              <el-upload action="/api/basic/sys/folderFile/upload?uploadPath=/文件审批" :on-change="handleChange" :on-remove="handleRemove" :on-preview="handlePreview" :file-list="fileList" list-type="picture" multiple >
                <el-button size="small" type="primary">{{$t('auditFileForm.clickUpload')}}</el-button>
                <div slot="tip" class="el-upload__tip">{{$t('auditFileForm.uploadImageSizeFor5000KB')}}</div>
              </el-upload>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled"  @click="formSubmit()">{{$t('auditFileForm.save')}}</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="16" :offset="2">
            <el-form-item  :label="$t('auditFileForm.content')"  prop="content">
                <quill-editor :content="inputForm.content"></quill-editor>
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
        formProperty:{},
        fileList:[],
        inputForm:{
          id:'',
          processTypeId:'',
          title:'',
          content:'',
          remarks:'',
          attachment:''
        },
        rules: {
          processTypeId: [{ required: true, message: this.$t('auditFileForm.prerequisiteMessage')}],
          title: [{ required: true, message: this.$t('auditFileForm.prerequisiteMessage')}],
        },
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
        this.inputForm.attachment = util.getFolderFileIdStr(this.fileList);
          console.log( this.inputForm.attachment)
          if (valid) {
            this.inputForm.attachment = util.getFolderFileIdStr(this.fileList);
            axios.post('/api/basic/hr/auditFile/save', qs.stringify(this.inputForm)).then((response)=> {
              if(response.data.message){
                this.$message(response.data.message);
              }
              if(this.isCreate){
                form.resetFields();
                this.fileList = [];
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'auditFileList',query:util.getQuery("auditFileList")})
              }
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },getFormProperty(){
        axios.get('/api/basic/hr/auditFile/getFormProperty').then((response)=>{
          this.formProperty=response.data;
        });
      },
      handlePreview(file) {
        window.open(file.url);
      },handleChange(file, fileList) {
        this.fileList = fileList;
      },handleRemove(file, fileList) {
        this.fileList = fileList;
      }
    },created(){
      this.getFormProperty();
     axios.get('/api/basic/hr/auditFile/detail',{params: {id:this.$route.query.id}}).then((response)=>{
        util.copyValue(response.data,this.inputForm);
      })
    }
  }
</script>
