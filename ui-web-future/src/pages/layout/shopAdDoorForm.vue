<template>
  <div>
    <head-tab active="shopAdDoorForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('shopAdForm.shopAdTypeId')" prop="shopAdTypeId">
              <el-select v-model="inputForm.shopAdTypeId" filterable :placeholder="$t('shopAdForm.inputWord')" @input="shopAdTypeChange">
                <el-option v-for="shopAdType in inputForm.extra.shopAdTypeFormDoorList" :key="shopAdType.id" :label="shopAdType.name+' ('+shopAdType.remarks+')'" :value="shopAdType.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.shopId')" prop="shopId">
              <depot-select v-model="inputForm.shopId" category="shop" :disabled="!isCreate"></depot-select>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.length')" prop="length">
              <el-input v-model.number="inputForm.length" auto-complete="off" :placeholder="$t('shopAdForm.transverseDimensions')"></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.width')" prop="width">
              <el-input v-model.number="inputForm.width" auto-complete="off" :placeholder="$t('shopAdForm.longitudinalDimension')"></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.thickness')" prop="thickness">
              <el-input v-model.number="inputForm.thickness" auto-complete="off" :placeholder="$t('shopAdForm.thicknessDimension')"></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.qty')" prop="qty">
              <el-input v-model.number="inputForm.qty" ></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.area')" prop="area">
              <el-input v-model.number="inputForm.area" ></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.content')" prop="content">
              <el-input v-model="inputForm.content" type="textarea"></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.specialArea')" prop="specialArea" v-if="isDoorType">
              <bool-radio-group v-model="inputForm.specialArea"></bool-radio-group>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks" :placeholder="$t('shopAdForm.placeRemarksForDoor')" type="textarea"  :rows="4"></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopAdForm.attachment')" prop="attachment">
              <el-upload action="/api/general/sys/folderFile/upload?uploadPath=/广告申请" :on-change="handleChange" :on-remove="handleRemove" :on-preview="handlePreview" :file-list="fileList" list-type="picture">
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
      return this.getData();
    },
    methods:{
      getData(){
        return{
          isCreate:this.$route.query.id==null,
          action:this.$route.query.action,
          isDoorType:true,
          submitDisabled:false,
          remoteLoading:false,
          fileList:[],
          inputForm:{
            extra:{}
          },
          rules: {
            shopAdTypeId: [{ required: true, message: this.$t('shopAdForm.prerequisiteMessage')}],
            shopId: [{ required: true, message: this.$t('shopAdForm.prerequisiteMessage')}],
            length: [{ required: true, message: this.$t('shopAdForm.prerequisiteMessage')},{type:"number",message:this.$t('shopPrintForm.inputLegalValue')}],
            width: [{ required: true, message: this.$t('shopAdForm.prerequisiteMessage')},{type:"number",message:this.$t('shopPrintForm.inputLegalValue')}],
            thickness: [{ required: true, message: this.$t('shopAdForm.prerequisiteMessage')},{type:"number",message:this.$t('shopPrintForm.inputLegalValue')}],
            qty: [{ required: true, message: this.$t('shopAdForm.prerequisiteMessage')},{type:"number",message:this.$t('shopPrintForm.inputLegalValue')}],
            area: [{ required: true, message: this.$t('shopAdForm.prerequisiteMessage')},{type:"number",message:this.$t('shopPrintForm.inputLegalValue')}],
            content:[{ required: true, message: this.$t('shopAdForm.prerequisiteMessage')}],
            specialArea:[{ required: true, message: this.$t('shopAdForm.prerequisiteMessage')}],
            remarks:[{ required: true, message: this.$t('shopAdForm.prerequisiteMessage')}],
          },
        }
      },
      formSubmit(){
        this.submitDisabled = true;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          this.inputForm.attachment = util.getFolderFileIdStr(this.fileList);
          if (valid) {
            axios.post('/api/ws/future/layout/shopAd/doorSave', qs.stringify(util.deleteExtra(this.inputForm))).then((response) => {
              this.$message(response.data.message);
              if (response.data.success) {
                if (this.isCreate) {
                  Object.assign(this.$data, this.getData());
                  this.initPage();
                }else{
                  util.closeAndBackToPage(this.$router,'shopAdDoorList')
                }
              }
            }).catch(()=> {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        });
      },shopAdTypeChange(){
          if(util.isBlank(this.inputForm.shopAdTypeId)){
            this.isDoorType = true;
            return;
          }
        axios.get('/api/ws/future/basic/shopAdType/findOne', {params: {id: this.inputForm.shopAdTypeId}}).then((response) => {
          if(response.data.name.indexOf('门头')>0){
            this.isDoorType = false;
            this.inputForm.specialArea = true;
          }else{
            this.isDoorType = true;
          }
        });
      },
      handlePreview(file) {
        window.open(file.viewUrl);
      },handleChange(file, fileList) {
        this.fileList = fileList;
      },handleRemove(file, fileList) {
        this.fileList = fileList;
      },initPage(){
        axios.get('/api/ws/future/layout/shopAd/getForm').then((response)=>{
          this.inputForm = response.data;
          if(!this.isCreate) {
            axios.get('/api/ws/future/layout/shopAd/findOne', {params: {id: this.$route.query.id}}).then((response) => {
              util.copyValue(response.data, this.inputForm);
              if (this.inputForm.attachment != null) {
                axios.get('/api/general/sys/folderFile/findByIds', {params: {ids: this.inputForm.attachment}}).then((response) => {
                  this.fileList = response.data;
                });
              }
            });
          }
        });
      }
    },created () {
      this.initPage();
    }
  }
</script>
