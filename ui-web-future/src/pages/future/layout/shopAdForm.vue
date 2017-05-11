<template>
  <div>
    <head-tab active="shopAdForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('shopAdForm.shopAdTypeId')" prop="shopAdTypeId">
              <el-select v-model="inputForm.shopAdTypeId" filterable clearable :placeholder="$t('shopAdForm.inputWord')">
                <el-option v-for="shopAdType in formProperty.shopAdTypes" :key="shopAdType.id" :label="shopAdType.name" :value="shopAdType.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.shopId')" prop="shopId">
              <el-select v-model="inputForm.shopId" filterable remote :placeholder="$t('shopAdForm.inputWord')" :remote-method="remoteShop" :loading="remoteLoading" :clearable=true >
                <el-option v-for="shop in shops" :key="shop.id" :label="shop.name" :value="shop.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.length')" prop="length">
              <el-input v-model="inputForm.length"></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.width')" prop="width">
              <el-input v-model.number="inputForm.width" ></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.qty')" prop="qty">
              <el-input v-model="inputForm.qty" ></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.content')" prop="content">
              <el-input v-model="inputForm.content" ></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.specialArea')" prop="specialArea">
              <el-radio-group v-model="inputForm.specialArea">
                <el-radio v-for="(value,key) in formProperty.bools":key="key" :label="value">{{key | bool2str}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.attachment')" prop="attachment">
              <el-upload action="/api/general/sys/folderFile/upload?uploadPath=/广告申请" :headers="headers" :on-change="handleChange" :on-remove="handleRemove" :on-preview="handlePreview" :file-list="fileList" list-type="picture">
                <el-button size="small" type="primary">{{$t('shopAdForm.clickUpload')}}</el-button>
                <div slot="tip" class="el-upload__tip">{{$t('shopAdForm.uploadImageSizeFor5000KB')}}</div>
              </el-upload>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled"  @click="formSubmit()">{{$t('shopAdForm.save')}}</el-button>
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
        shops:[],
        fileList:[],
        inputForm:{
          id:'',
          shopAdTypeId:'',
          shopId:'',
          length:'',
          qty:'',
          width:'',
          content:'',
          specialArea:'',
          attachment:'',
          remarks:''
        },
        rules: {
          shopAdTypeId: [{ required: true, message: this.$t('shopAdForm.prerequisiteMessage')}],
          shopId: [{ required: true, message: this.$t('shopAdForm.prerequisiteMessage')}],
          length: [{ required: true, message: this.$t('shopAdForm.prerequisiteMessage')}],
          width: [{ required: true, message: this.$t('shopAdForm.prerequisiteMessage')}],
          qty: [{ required: true, message: this.$t('shopAdForm.prerequisiteMessage')}],
        },
        headers:{Authorization:'Bearer ' + this.$store.state.global.token.access_token}
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          this.inputForm.attachment = util.getFolderFileIdStr(this.fileList);
        if (valid) {
            axios.post('/api/crm/shopAd/save', qs.stringify(this.inputForm)).then((response)=> {
              if(response.data.message){
                this.$message(response.data.message);
              }
              if(this.isCreate){
                form.resetFields();
                this.fileList=[];
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'shopPrintList',query:util.getQuery("shopPrintList")})
              }
            }).catch(function () {
            this.submitDisabled = false;
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
        axios.get('/api/crm/shopAd/getFormProperty').then((response)=>{
          this.formProperty=response.data;
        });
      },findOne(){
        axios.get('/api/crm/shopAd/detail',{params: {id:this.$route.query.id}}).then((response)=>{
          util.copyValue(response.data,this.inputForm);
        if(this.inputForm.attachment !=null) {
          axios.get('/api/basic/sys/folderFile/findByIds',{params: {ids:this.inputForm.attachment}}).then((response)=>{
            this.fileList= response.data;
        });
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
      this.getFormProperty();
      if(!this.isCreate){
        this.findOne();
      }
    }
  }
</script>
