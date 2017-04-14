<template>
  <div>
    <head-tab :active="$t('shopBuildForm.shopBuildForm') "></head-tab>
    <div >
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('shopBuildForm.shopId')" prop="shopId">
              <el-select v-model="inputForm.shopId" filterable remote :placeholder="$t('shopBuildForm.inputWord')" :remote-method="remoteShop" :loading="remoteLoading" :clearable=true >
                <el-option v-for="shop in shops" :key="shop.id" :label="shop.name" :value="shop.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('shopBuildForm.shopType')" prop="shopType">
              <el-select v-model="inputForm.shopType" filterable clearable :placeholder="$t('shopBuildForm.inputType')">y
                <el-option v-for="shopType in formProperty.shopTypes"  :key="shopType" :label="shopType" :value="shopType"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('shopBuildForm.fixtureType')" prop="fixtureType">
              <el-select v-model="inputForm.fixtureType" filterable clearable :placeholder="$t('shopBuildForm.inputType')">
                <el-option v-for="fixtureType in formProperty.fixtureTypes":key="fixtureType" :label="fixtureType" :value="fixtureType"></el-option>
              </el-select>
            </el-form-item>
            <div v-show="inputForm.fixtureType.indexOf('包柱')>0">
            <el-form-item :label="$t('shopBuildForm.oldContents')" prop="oldContents">
              <el-input v-model="inputForm.oldContents" ></el-input>
            </el-form-item>
              </div>
            <el-form-item :label="$t('shopBuildForm.newContents')" prop="newContents">
              <el-input v-model="inputForm.newContents" ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('shopBuildForm.buildType')" prop="buildType">
              <el-select v-model="inputForm.buildType" filterable clearable :placeholder="$t('shopBuildForm.inputType')">
                <el-option v-for="buildType in formProperty.buildTypes" :key="buildType" :label="buildType" :value="buildType"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('shopBuildForm.applyAccount')" prop="applyAccountId">
              <el-select v-model="inputForm.applyAccountId" filterable remote :placeholder="$t('shopBuildForm.inputWord')" :remote-method="remoteAccount" :loading="remoteLoading" :clearable=true>
                <el-option v-for="account in accounts" :key="account.id" :label="account.loginName" :value="account.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('shopBuildForm.content')" prop="content">
              <el-input v-model="inputForm.content" type="textarea"></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopBuildForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopBuildForm.scenePhoto')" prop="scenePhoto">
              <el-upload action="/api/basic/sys/folderFile/upload?uploadPath=/门店建设" :on-change="handleChange" :on-remove="handleRemove" :on-preview="handlePreview" :file-list="fileList" list-type="picture">
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
  export default{
    data(){
      return {
        isCreate: this.$route.query.id == null,
        submitDisabled: false,
        formProperty: {},
        fileList: [],
        inputForm: {
          id: '',
          shopId: '',
          shopType: '',
          fixtureType: '',
          oldContents:'',
          newContents: '',
          buildType: '',
          applyAccountId: '',
          content: '',
          remarks: '',
          scenePhoto: ''
        },
        rules: {
          shopId: [{required: true, message: this.$t('shopBuildForm.prerequisiteMessage')}],
          shopType: [{required: true, message: this.$t('shopBuildForm.prerequisiteMessage')}],
          newContents: [{required: true, message: this.$t('shopBuildForm.prerequisiteMessage')}],
          buildType: [{required: true, message: this.$t('shopBuildForm.prerequisiteMessage')}],
          accountId: [{required: true, message: this.$t('shopBuildForm.prerequisiteMessage')}],
        },
        shops: [],
        accounts: [],
        remoteLoading:false
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          this.inputForm.scenePhoto = util.getFolderFileIdStr(this.fileList);
          if (valid) {
            axios.post('/api/crm/shopBuild/save', qs.stringify(this.inputForm)).then((response)=> {
              if(response.data.message){
                this.$message(response.data.message);
              }
              if(this.isCreate){
                form.resetFields();
                this.fileList=[];
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'shopBuildList',query:util.getQuery("shopBuildList")})
              }
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },remoteAccount(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/hr/account/search',{params:{key:query}}).then((response)=>{
            this.accounts=response.data;
            this.remoteLoading = false;
          })
        }else{
          this.accounts=[];
        }
      },remoteShop(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/crm/depot/search',{params:{name:query}}).then((response)=>{
            this.shops=response.data;
            this.remoteLoading = false;
          })
        }
      },getFormProperty(){
        axios.get('/api/crm/shopBuild/getFormProperty').then((response)=>{
          this.formProperty=response.data;
        });
      },findOne(){
        axios.get('/api/crm/shopBuild/detail',{params: {id:this.$route.query.id}}).then((response)=>{
          util.copyValue(response.data,this.inputForm);
          if(this.inputForm.scenePhoto !=null) {
            axios.get('/api/basic/sys/folderFile/findByIds',{params: {ids:this.inputForm.scenePhoto}}).then((response)=>{
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
