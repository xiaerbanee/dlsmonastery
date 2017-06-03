<template>
  <div>
    <head-tab active="auditFileForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item :label="$t('auditFileForm.processTypeName')" prop="processTypeName">
              <el-select v-model="inputForm.processTypeName" filterable clearable :placeholder="$t('auditFileForm.inputWord')">
                <el-option v-for="type in processTypeList" :key="type.name" :label="type.name" :value="type.name"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('auditFileForm.title')" prop="title">
              <el-input v-model="inputForm.title"></el-input>
            </el-form-item>
            <el-form-item :label="$t('auditFileForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item :label="$t('auditFileForm.attachment')" prop="attachment">
              <el-upload action="/api/general/sys/folderFile/upload?uploadPath=/文件审批" :headers="headers"  :on-change="handleChange" :on-remove="handleRemove" :on-preview="handlePreview" :file-list="fileList" list-type="picture" multiple >
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
        processTypeList:[],
        inputForm:{},
        submitData:{
          id:'',
          processTypeName:'',
          title:'',
          content:'',
          remarks:'',
          attachment:''
        },
        rules: {
          processTypeName: [{ required: true, message: this.$t('auditFileForm.prerequisiteMessage')}],
          title: [{ required: true, message: this.$t('auditFileForm.prerequisiteMessage')}],
        },
        headers:{Authorization: 'Bearer ' + this.$store.state.global.token.access_token}
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
        this.inputForm.attachment = util.getFolderFileIdStr(this.fileList);
          console.log( this.fileList)
          if (valid) {
            this.inputForm.attachment = util.getFolderFileIdStr(this.fileList);
            util.copyValue(this.inputForm, this.submitData);
            axios.post('/api/basic/hr/auditFile/save', qs.stringify(this.submitData)).then((response)=> {
              if(response.data.message){
                this.$message(response.data.message);
              }
              this.submitDisabled = false;
              if(this.isCreate){
                form.resetFields();
                this.fileList = [];
              } else {
                this.$router.push({name:'auditFileList',query:util.getQuery("auditFileList")})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
      handlePreview(file) {
        window.open(file.url);
      },handleChange(file, fileList) {
        this.fileList = fileList;
      },handleRemove(file, fileList) {
        this.fileList = fileList;
      },initPage() {
        axios.get('/api/basic/hr/auditFile/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm = response.data;
        })
        axios.get('/api/general/sys/processType/findAll',{params: {id:this.$route.query.id}}).then((response)=>{
          this.processTypeList = response.data;
        })
      }
    },created(){
      this.initPage();
    },activated () {
      if(!this.$route.query.headClick) {
        this.initPage();
      }
    }
  }
</script>
