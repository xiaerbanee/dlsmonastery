<template>
  <div>
    <head-tab :active="$t('priceChangeImeDetail.priceChangeImeDetail') "></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('priceChangeImeDetail.priceChangeName')">
              {{inputForm.priceChange.name}}
            </el-form-item>
            <el-form-item :label="$t('priceChangeImeDetail.type')">
              {{inputForm.productIme.product.name}}
            </el-form-item>
            <el-form-item :label="$t('priceChangeImeDetail.ime')">
              {{inputForm.productIme.ime}}
            </el-form-item>
            <el-form-item :label="$t('priceChangeImeDetail.remarks')">
              {{inputForm.pass}}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('priceChangeImeDetail.shopName')">
              {{inputForm.shop.name}}
            </el-form-item>
            <el-form-item :label="$t('priceChangeImeDetail.imagefile')" prop="image">
              <el-upload  action="/api/sys/folderFile/upload?uploadPath=/调价串码抽检" :on-preview="handlePreview" :file-list="fileList" list-type="picture" multiple >
              </el-upload>
            </el-form-item>
            <el-form-item :label="$t('priceChangeImeDetail.imagefile')" prop="image" v-if="action=='上传'">
              <el-upload action="/api/sys/folderFile/upload?uploadPath=/调价串码抽检" :on-change="handleChange" :on-remove="handleRemove" :on-preview="handlePreview" :file-list="fileList" list-type="picture" multiple >
                <el-button size="small" type="primary">{{$t('priceChangeImeDetail.clickUpload')}}</el-button>
                <div slot="tip" class="el-upload__tip">{{$t('priceChangeImeDetail.uploadImageSizeFor5000KB')}}</div>
              </el-upload>
            </el-form-item>
            <el-form-item :label="$t('priceChangeImeDetail.pass')" prop="pass" v-if="action=='审核'">
              <el-radio-group v-model="inputForm.pass">
                <el-radio v-for="(value,key) in formProperty.bools" :key="key" :label="value">{{key | bool2str}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="$t('priceChangeImeDetail.auditRemarks')" prop="auditRemarks"  v-if="action=='审核'">
              <el-input v-model="inputForm.auditRemarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('priceChangeImeDetail.save')}}</el-button>
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
            action:this.$route.query.action,
            submitDisabled:false,
            formProperty:{bools:''},
            fileList:[],
            url:'',
            inputForm:{
              id:this.$route.query.id,
              priceChange:{name:''},
              shop:{name:''},
              productIme:{
                ime:'',
                product:{
                  name:""
                }
              },
              remarks:"",
              image:"",
              pass:'',
              auditRemarks:''
            },
            rules: {
            }
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              this.inputForm.image = util.getFolderFileIdStr(this.fileList);
              if(this.action==='上传'){
                this.url = '/api/crm/priceChangeIme/imageUpload';
              }else if(this.action==='审核'){
                this.url = '/api/crm/priceChangeIme/audit';
              }
              axios.post(this.url,qs.stringify(this.inputForm, {allowDots:true})).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.fileList = [];
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'priceChangeImeList',query:util.getQuery("priceChangeImeList")})
                }
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
        }
      },created(){
        if(!this.isCreate){
          axios.get('/api/crm/priceChangeIme/detail',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data.priceChangeIme,this.inputForm);
            util.copyValue(response.data.bools,this.formProperty.bools);
            if(this.inputForm.image != null) {
              axios.get('/api/sys/folderFile/findByIds',{params: {ids:this.inputForm.image}}).then((res)=>{
                this.fileList= res.data;
              });
            }
          })
        }
      }
    }
</script>
