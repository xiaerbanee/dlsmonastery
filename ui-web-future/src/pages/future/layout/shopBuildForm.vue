<template>
  <div>
    <head-tab active="shopBuildForm"></head-tab>
    <div >
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('shopBuildForm.shopId')" prop="shopId">
              <depot-select v-model="inputForm.shopId" category="adShop" :disabled="shopDisabled"></depot-select>
            </el-form-item>
            <el-form-item :label="$t('shopBuildForm.shopType')" prop="shopType">
              <dict-enum-select v-model="inputForm.shopType" category="店面类型"></dict-enum-select>
            </el-form-item>
            <el-form-item :label="$t('shopBuildForm.fixtureType')" prop="fixtureType">
              <dict-enum-select v-model="inputForm.fixtureType" category="装修类别" @input="shopChange"></dict-enum-select>
            </el-form-item>
            <el-form-item :label="$t('shopBuildForm.fixtureContent')" prop="fixtureContent">{{fixtureContent}}
            </el-form-item>
            <el-form-item :label="$t('shopBuildForm.newContents')" prop="newContents">
              <el-input v-model="inputForm.newContents" ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('shopBuildForm.buildType')" prop="buildType">
              <dict-enum-select v-model="inputForm.buildType" category="项目建设方式"></dict-enum-select>
            </el-form-item>
            <el-form-item :label="$t('shopBuildForm.applyAccount')" prop="applyAccountId">
              <account-select v-model="inputForm.applyAccountId"></account-select>
            </el-form-item>
            <el-form-item :label="$t('shopBuildForm.content')" prop="content">
              <el-input v-model="inputForm.content" type="textarea"></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopBuildForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks" type="textarea"></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopBuildForm.scenePhoto')" prop="scenePhoto">
              <el-upload action="/api/general/sys/folderFile/upload?uploadPath=/门店建设" :headers="headers" :on-change="handleChange" :on-remove="handleRemove" :on-preview="handlePreview" :file-list="fileList" list-type="picture">
                <el-button size="small" type="primary">{{$t('shopBuildForm.clickUpload')}}</el-button>
                <div slot="tip" class="el-upload__tip">{{$t('shopBuildForm.uploadImageSizeFor5000KB')}}</div>
              </el-upload>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled"  @click="formSubmit()">{{$t('shopBuildForm.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>

<script>
  import dictEnumSelect from 'components/basic/dict-enum-select';
  import accountSelect from 'components/basic/account-select';
  import depotSelect from 'components/future/depot-select';
  export default{
    components:{
      dictEnumSelect,
      accountSelect,
      depotSelect
    },
    data(){
      return this.getData();
    },
    methods:{
      getData(){
        return {
          isCreate:this.$route.query.id==null,
          submitDisabled: false,
          shopDisabled:false,
          fileList: [],
          fixtureContent:'',
          inputForm: {
            extra:{}
          },
          rules: {
            shopId: [{required: true, message: this.$t('shopBuildForm.prerequisiteMessage')}],
            shopType: [{required: true, message: this.$t('shopBuildForm.prerequisiteMessage')}],
            newContents: [{required: true, message: this.$t('shopBuildForm.prerequisiteMessage')}],
            buildType: [{required: true, message: this.$t('shopBuildForm.prerequisiteMessage')}],
            accountId: [{required: true, message: this.$t('shopBuildForm.prerequisiteMessage')}],
          },
          remoteLoading:false,
          headers:{Authorization: 'Bearer ' + this.$store.state.global.token.access_token}
        }
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          this.inputForm.scenePhoto = util.getFolderFileIdStr(this.fileList);
          if (valid) {
              axios.post('/api/ws/future/layout/shopBuild/save', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
                this.$message(response.data.message);
                if(response.data.success) {
                  if (this.isCreate) {
                    Object.assign(this.$data,this.getData());
                    this.initPage();
                  }
                  else{
                    this.submitDisabled = false;
                    this.$router.push({name: 'shopBuildList', query: util.getQuery("shopBuildList")})
                  }
                }
              }).catch(function () {
                that.submitDisabled = false;
              });
          }
        })
      },shopChange(){
        axios.get('/api/basic/sys/dictEnum/findByValue?value=' + this.inputForm.fixtureType).then((response)=>{
          this.fixtureContent=response.data;
        })
      },
      handlePreview(file) {
        window.open(file.url);
      },handleChange(file, fileList) {
        this.fileList = fileList;
      },handleRemove(file, fileList) {
        this.fileList = fileList;
      },initPage(){
        axios.get('/api/ws/future/layout/shopBuild/getForm').then((response)=>{
          this.inputForm = response.data;
          axios.get('/api/ws/future/layout/shopBuild/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
            if(this.inputForm.id != null){
              this.shopDisabled = true;
            }
            if(this.inputForm.fixtureType!=null){
              this.shopChange();
            }
            if(this.inputForm.scenePhoto !=null) {
              axios.get('/api/general/sys/folderFile/findByIds',{params: {ids:this.inputForm.scenePhoto}}).then((response)=>{
                this.fileList= response.data;
              });
            }
          });
        });
      }
    },created () {
      this.initPage();
    }
  }
</script>
