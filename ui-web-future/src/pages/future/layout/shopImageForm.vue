<template>
  <div>
    <head-tab active="shopImageForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('shopImageForm.shopName')" prop="shopId">
              <depot-select v-model="inputForm.shopId" category="adShop" :disabled="shopDisabled"></depot-select>
            </el-form-item>
            <el-form-item :label="$t('shopImageForm.imageType')" prop="imageType">
              <el-select v-model="inputForm.imageType" filterable clearable :placeholder="$t('shopImageForm.inputType')">
                <el-option v-for="item in formProperty.imageTypeList" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('shopImageForm.imageSize')" prop="imageSize">
              <el-input v-model="inputForm.imageSize"></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopImageForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks" type="textarea"></el-input>
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
      return this.getData();
    },
    methods:{
      getData(){
        return{
          isInit:false,
          isCreate:this.$route.query.id==null,
          submitDisabled:false,
          shopDisabled:false,
          remoteLoading:false,
          formProperty:{},
          fileList:[],
          inputForm:{},
          submitData:{
            id:'',
            shopId:'',
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
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          this.inputForm.image = util.getFolderFileIdStr(this.fileList);
          if (valid) {
            util.copyValue(this.inputForm,this.submitData)
            axios.post('/api/ws/future/layout/shopImage/save', qs.stringify(this.submitData)).then((response)=> {
              this.$message(response.data.message);
              this.submitDisabled = false;
              if(response.data.success) {
                if (this.isCreate) {
                  Object.assign(this.$data, this.getData());
                  this.fileList = [];
                } else {
                  this.$router.push({name: 'shopImageList', query: util.getQuery("shopImageList")})
                }
              }
            }).catch(function () {
              that.submitDisabled = false;
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
    },activated () {
      if(!this.$route.query.headClick || !this.isInit) {
        axios.get('/api/ws/future/layout/shopImage/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm = response.data;
          if(this.inputForm.id != null){
            this.shopDisabled = true;
          }else{
            this.shopDisabled = false;
          }
          if(this.inputForm.image != null) {
            axios.get('/api/general/sys/folderFile/findByIds',{params: {ids:this.inputForm.image}}).then((response)=>{
              this.fileList= response.data;
            });
          }else{
            this.fileList = [];
          }
        });
        axios.get('/api/ws/future/layout/shopImage/getForm').then((response)=>{
          this.formProperty = response.data;
        });
      }
      this.isInit = true;
    }
  }
</script>
