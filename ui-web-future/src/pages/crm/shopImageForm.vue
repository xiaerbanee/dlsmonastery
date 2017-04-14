<template>
  <div>
    <head-tab :active="$t('shopImageForm.shopImageForm') "></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('shopImageForm.shopName')" prop="shopId">
              <el-select v-model="inputForm.shopId" filterable remote :placeholder="$t('shopImageForm.inputWord')" :remote-method="remoteShop" :loading="remoteLoading" :clearable=true >
                <el-option v-for="shop in shops" :key="shop.id" :label="shop.name" :value="shop.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('shopImageForm.imageType')" prop="imageType">
              <el-select v-model="inputForm.imageType" filterable clearable :placeholder="$t('shopImageForm.inputType')">
                <el-option v-for="item in formProperty.shopImageTypes" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('shopImageForm.imageSize')" prop="imageSize">
              <el-input v-model="inputForm.imageSize"></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopImageForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item  :label="$t('shopImageForm.image')" prop="image">
              <el-upload action="/api/basic/sys/folderFile/upload?uploadPath=/形象更换" :on-change="handleChange" :on-remove="handleRemove" :on-preview="handlePreview" :file-list="fileList" list-type="picture">
                <el-button size="small" type="primary">{{$t('shopImageForm.clickUpload')}}</el-button>
                <div slot="tip" class="el-upload__tip">{{$t('shopImageForm.uploadImageSizeFor5000KB')}}</div>
              </el-upload>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled"  @click="formSubmit()">{{$t('shopImageForm.save')}}</el-button>
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
        remoteLoading:false,
        formProperty:{},
        fileList:[],
        shops:[],
        inputForm:{
          id:'',
          shopId:'',
          imageType:'',
          imageSize:'',
          image:'',
          remarks:''
        },
        rules: {
          shopId: [{ required: true, message: this.$t('shopImageForm.prerequisiteMessage')}],
          imageType: [{ required: true, message: this.$t('shopImageForm.prerequisiteMessage')}],
          imageSize: [{ required: true, message: this.$t('shopImageForm.prerequisiteMessage')}]
        },
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          this.inputForm.image = util.getFolderFileIdStr(this.fileList);
          if (valid) {
            axios.post('/api/crm/shopImage/save', qs.stringify(this.inputForm)).then((response)=> {
              console.log(response.data)
              if(response.data.message){
                this.$message(response.data.message);
              }
              if(this.isCreate){
                form.resetFields();
                this.fileList=[];
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'shopImageList',query:util.getQuery("shopImageList")})
              }
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },remoteShop(query) {
      if (query !== '') {
        this.remoteLoading = true;
        axios.get('/api/crm/depot/shop',{params:{name:query}}).then((response)=>{
          this.shops=response.data;
          this.remoteLoading = false;
        })
      } else {
        this.shops = [];
      }
    },getFormProperty(){
        axios.get('/api/crm/shopImage/getFormProperty').then((response)=>{
          this.formProperty=response.data;
        });
      },findOne(){
        axios.get('/api/crm/shopImage/detail',{params: {id:this.$route.query.id}}).then((response)=>{
          util.copyValue(response.data,this.inputForm);
          this.shops=new Array(response.data.shop);
          if(this.inputForm.image != null) {
            axios.get('/api/basic/sys/folderFile/findByIds',{params: {ids:this.inputForm.image}}).then((response)=>{
              this.fileList= response.data;
            });
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
      this.getFormProperty();
      if(!this.isCreate){
        this.findOne();
      }
    }
  }
</script>
