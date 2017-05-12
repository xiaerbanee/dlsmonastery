<template>
  <div>
    <head-tab active="shopImageForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('shopImageForm.shopName')" prop="shopId">
              <depot-select v-model="inputForm.shopId" category="shop"></depot-select>
            </el-form-item>
            <el-form-item :label="$t('shopImageForm.imageType')" prop="imageType">
              <el-select v-model="inputForm.imageType" filterable clearable :placeholder="$t('shopImageForm.inputType')">
                <el-option v-for="item in formProperty" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('shopImageForm.imageSize')" prop="imageSize">
              <el-input v-model="inputForm.imageSize"></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopImageForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item  :label="$t('shopImageForm.image')" prop="image">
              <el-upload action="/api/general/sys/folderFile/upload?uploadPath=/形象更换" :headers="headers" :on-change="handleChange" :on-remove="handleRemove" :on-preview="handlePreview" :file-list="fileList" list-type="picture">
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
  import depotSelect from 'components/future/depot-select';
  export default{
    components:{depotSelect},
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        shopDisabled:false,
        remoteLoading:false,
        formProperty:{},
        fileList:[],
        shops:[],
        inputForm:{
          id:'',
          shopId:'',
          shopName:'',
          imageType:'',
          imageSize:'',
          image:'',
          remarks:''
        },
        rules: {
          shopName: [{ required: true, message: this.$t('shopImageForm.prerequisiteMessage')}],
          imageType: [{ required: true, message: this.$t('shopImageForm.prerequisiteMessage')}],
          imageSize: [{ required: true, message: this.$t('shopImageForm.prerequisiteMessage')}]
        },
        headers:{Authorization: 'Bearer ' + this.$store.state.global.token.access_token}
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          this.inputForm.image = util.getFolderFileIdStr(this.fileList);
          if (valid) {
              console.log(this.inputForm);
            axios.post('/api/ws/future/layout/shopImage/save', qs.stringify(this.inputForm)).then((response)=> {
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
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },getFormProperty(){
        axios.get('/api/basic/sys/companyConfig/getValueByCode',{params:{code:"SHOP_IMAGE_TYPE"}}).then((response)=>{
          this.formProperty=response.data.split(',');
        });
      },handlePreview(file) {
        window.open(file.url);
      },handleChange(file, fileList) {
        this.fileList = fileList;
      },handleRemove(file, fileList) {
        this.fileList = fileList;
      }
    },created(){
      axios.get('/api/ws/future/layout/shopImage/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm = response.data;
        this.shops=[];
        if(this.inputForm.id != null){
            this.shopDisabled = true;
        }
        if(this.inputForm.image != null) {
          axios.get('/api/general/sys/folderFile/findByIds',{params: {ids:this.inputForm.image}}).then((response)=>{
            this.fileList= response.data;
          });
        }
      })
      this.getFormProperty();
    }
  }
</script>
