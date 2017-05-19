<template>
  <div>
    <head-tab active="shopAdForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('shopAdForm.shopAdTypeId')" prop="shopAdTypeId">
              <el-select v-model="inputForm.shopAdTypeId" filterable clearable :placeholder="$t('shopAdForm.inputWord')">
                <el-option v-for="shopAdType in inputForm.shopAdTypeFormList" :key="shopAdType.id" :label="shopAdType.name+' ('+shopAdType.remarks+')'" :value="shopAdType.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.shopId')" prop="shopId">
              <depot-select v-model="inputForm.shopId" category="adShop"></depot-select>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.length')" prop="length">
              <el-input v-model="inputForm.length"></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.width')" prop="width">
              <el-input v-model="inputForm.width" ></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.qty')" prop="qty">
              <el-input v-model="inputForm.qty" ></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.content')" prop="content">
              <el-input v-model="inputForm.content" ></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.specialArea')" prop="specialArea">
              <bool-radio-group v-model="inputForm.specialArea"></bool-radio-group>
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
  import depotSelect from 'components/future/depot-select';
  import boolRadioGroup from 'components/common/bool-radio-group';
  export default{
    components:{depotSelect,boolRadioGroup},
    data(){
      return{
        isCreate:this.$route.query.id==null,
        action:this.$route.query.action,
        submitDisabled:false,
        remoteLoading:false,
        formProperty:{},
        shops:[],
        fileList:[],
        inputForm:{},
        submitData:{
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
            util.copyValue(this.inputForm,this.submitData);
            axios.post('/api/ws/future/layout/shopAd/save', qs.stringify(this.submitData)).then((response)=> {
              if(response.data.message){
                this.$message(response.data.message);
              }
              if(this.isCreate){
                form.resetFields();
                this.fileList=[];
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'shopAdList',query:util.getQuery("shopAdList")})
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
      }
    },created(){
      axios.get('/api/ws/future/layout/shopAd/getForm',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm = response.data;
        if(response.data.specialArea ==true){
            this.inputForm.specialArea = 1;
        }else{
          this.inputForm.specialArea = 0;
        }
        if(this.inputForm.attachment !=null) {
          axios.get('/api/general/sys/folderFile/findByIds',{params: {ids:this.inputForm.attachment}}).then((response)=>{
            this.fileList= response.data;
          });
        }
      });

    }
  }
</script>
