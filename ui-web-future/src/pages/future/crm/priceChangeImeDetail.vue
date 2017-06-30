<template>
  <div>
    <head-tab active="priceChangeImeDetail"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('priceChangeImeDetail.priceChangeName')">
              {{inputForm.priceChangeName}}
            </el-form-item>
            <el-form-item :label="$t('priceChangeImeDetail.type')">
              {{inputForm.productName}}
            </el-form-item>
            <el-form-item :label="$t('priceChangeImeDetail.ime')">
              {{inputForm.ime}}
            </el-form-item>
            <el-form-item :label="$t('priceChangeImeDetail.remarks')">
              {{inputForm.remarks}}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('priceChangeImeDetail.shopName')">
              {{inputForm.shopName}}
            </el-form-item>
            <el-form-item :label="$t('priceChangeImeDetail.imagefile')" prop="image" v-if="action=='upload'">
              <el-upload action="/api/general/sys/folderFile/upload?uploadPath=/调价串码抽检" :on-change="handleChange" :on-remove="handleRemove" :on-preview="handlePreview" :file-list="fileList" list-type="picture" multiple >
                <el-button size="small" type="primary">{{$t('priceChangeImeDetail.clickUpload')}}</el-button>
                <div slot="tip" class="el-upload__tip">{{$t('priceChangeImeDetail.uploadImageSizeFor5000KB')}}</div>
              </el-upload>
            </el-form-item>
            <el-form-item :label="$t('priceChangeImeDetail.pass')" prop="pass" v-if="action=='audit'">
              <bool-radio-group v-model="inputForm.pass"></bool-radio-group>
            </el-form-item>
            <el-form-item :label="$t('priceChangeImeDetail.auditRemarks')" prop="auditRemarks"  v-if="action=='audit'">
              <el-input v-model="inputForm.auditRemarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()" v-if="action !='detail'">{{$t('priceChangeImeDetail.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<script>
  import boolRadioGroup from 'components/common/bool-radio-group';
    export default{
      components:{boolRadioGroup},
      data(){
          return{
            isCreate:this.$route.query.id==null,
            action:this.$route.query.action,
            submitDisabled:false,
            formProperty:{},
            fileList:[],
            url:'',
            inputForm:{
              pass:'',
              auditRemarks:'',
            },
            submitData:{
                id:'',
              image:'',
              pass:'',
              auditRemarks:'',
            },
            rules: {}
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          let form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
                this.inputForm.image = util.getFolderFileIdStr(this.fileList)
                util.copyValue(this.inputForm,this.submitData);
              this.inputForm.image = util.getFolderFileIdStr(this.fileList);
              if(this.action==='upload'){
                this.url = '/api/ws/future/crm/priceChangeIme/imageUpload';
              }else if(this.action==='audit'){
                this.url = '/api/ws/future/crm/priceChangeIme/audit';
              }
              axios.post(this.url,qs.stringify(this.submitData, {allowDots:true})).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.fileList = [];
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'priceChangeImeList',query:util.getQuery("priceChangeImeList"), params:{_closeFrom:true}})
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
        },initPage(){
          axios.get('/api/ws/future/crm/priceChangeIme/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            this.inputForm = response.data;
            if(this.inputForm.image != null) {
              axios.get('/api/general/sys/folderFile/findByIds',{params: {ids:this.inputForm.image}}).then((res)=>{
                this.fileList= res.data;
              });
            }
          })
          axios.get('/api/ws/future/crm/priceChangeIme/getForm').then((response)=>{
            this.formProperty=response.data;
          });
        }
      },created(){
          this.initPage();
      }
    }
</script>
