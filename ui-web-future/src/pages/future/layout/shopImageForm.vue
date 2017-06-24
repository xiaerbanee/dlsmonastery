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
                <el-option v-for="item in inputForm.extra.imageTypeList" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('shopImageForm.imageSize')" prop="imageSize">
              <el-input v-model="inputForm.imageSize"></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopImageForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks" type="textarea"></el-input>
            </el-form-item>
            <el-form-item  :label="$t('shopImageForm.image')" prop="image">
              <el-upload action="/api/general/sys/folderFile/upload?uploadPath=/形象更换" :on-change="handleChange" :on-remove="handleRemove" :on-preview="handlePreview" :file-list="fileList" list-type="picture">
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
          fileList:[],
          inputForm:{
            extra:{}
          },
          rules: {
            shopId: [{ required: true, message: this.$t('shopImageForm.prerequisiteMessage')}],
            imageType: [{ required: true, message: this.$t('shopImageForm.prerequisiteMessage')}],
            imageSize: [{ required: true, message: this.$t('shopImageForm.prerequisiteMessage')}]
          },
        }
      },
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          this.inputForm.image = util.getFolderFileIdStr(this.fileList);
          if (valid) {
            axios.post('/api/ws/future/layout/shopImage/save', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
              this.$message(response.data.message);
              if(response.data.success) {
                if (this.isCreate) {
                  Object.assign(this.$data, this.getData());
                  this.initPage();
                }else{
                  this.$router.push({name: 'shopImageList', query: util.getQuery("shopImageList"), params:{_closeFrom:true}});
                }
              }
            }).catch(() => {
              this.submitDisabled = false;
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
      },initPage(){
          axios.get('/api/ws/future/layout/shopImage/getForm').then((response)=>{
            this.inputForm = response.data;
            if(!this.isCreate) {
              axios.get('/api/ws/future/layout/shopImage/findOne', {params: {id: this.$route.query.id}}).then((response) => {
                util.copyValue(response.data, this.inputForm);
                if (this.isCreate) {
                  this.shopDisabled = false;
                } else {
                  this.shopDisabled = true;
                }
                if (this.inputForm.image != null) {
                  axios.get('/api/general/sys/folderFile/findByIds', {params: {ids: this.inputForm.image}}).then((response) => {
                    this.fileList = response.data;
                  });
                } else {
                  this.fileList = [];
                }
              });
            }
          });
      }
    },created(){
      this.initPage();
    }
  }
</script>
