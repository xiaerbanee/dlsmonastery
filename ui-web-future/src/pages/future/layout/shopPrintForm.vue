<template>
  <div>
    <head-tab active="shopPrintForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('shopPrintForm.officeId')" prop="officeId">
              <el-select v-model="inputForm.officeId" filterable clearable :placeholder="$t('shopPrintForm.inputWord')">
                <el-option v-for="area in formProperty.areas" :key="area.id" :label="area.name" :value="area.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('shopPrintForm.printType')" prop="printType">
              <el-select v-model="inputForm.printType" filterable clearable :placeholder="$t('shopPrintForm.inputType')">
                <el-option v-for="type in formProperty.printTypes" :key="type.name" :label="type.name" :value="type.name"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('shopPrintForm.content')" prop="content">
              <el-input v-model="inputForm.content"></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopPrintForm.qty')" prop="qty">
              <el-input v-model.number="inputForm.qty" ></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopPrintForm.address')" prop="address">
              <el-input v-model="inputForm.address" ></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopPrintForm.contact')" prop="contator">
              <el-input v-model="inputForm.contator" ></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopPrintForm.mobilePhone')" prop="mobilePhone">
            <el-input v-model="inputForm.mobilePhone" ></el-input>
          </el-form-item>
            <el-form-item :label="$t('shopPrintForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopPrintForm.attachment')" prop="attachment">
                  <el-upload action="/api/basic/sys/folderFile/upload?uploadPath=/广告印刷" :on-change="handleChange" :on-remove="handleRemove"  :on-preview="handlePreview" :file-list="fileList" list-type="picture">
                    <el-button size="small" type="primary">{{$t('shopPrintForm.clickUpload')}}</el-button>
                    <div slot="tip" class="el-upload__tip">{{$t('shopPrintForm.uploadImageSizeFor5000KB')}}</div>
                  </el-upload>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled"  @click="formSubmit()">{{$t('shopPrintForm.save')}}</el-button>
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
        formProperty:{},
        fileList:[],
        inputForm:{
          id:'',
          officeId:'',
          printType:'',
          content:'',
          qty:'',
          address:'',
          contator:'',
          mobilePhone:'',
          attachment:'',
          remarks:''
        },
        rules: {
          officeId: [{ required: true, message: this.$t('shopPrintForm.prerequisiteMessage')}],
          printType: [{ required: true, message: this.$t('shopPrintForm.prerequisiteMessage')}],
          qty: [{ required: true, message: this.$t('shopPrintForm.prerequisiteMessage')},{type:"number",message:this.$t('shopPrintForm.inputLegalValue')}],
          address: [{ required: true, message: this.$t('shopPrintForm.prerequisiteMessage')}],
          contator: [{ required: true, message: this.$t('shopPrintForm.prerequisiteMessage')}],
          mobilePhone: [{ required: true, message: this.$t('shopPrintForm.prerequisiteMessage')}],
        },
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        this.inputForm.expiryDate=util.formatLocalDate( this.inputForm.expiryDate)
        form.validate((valid) => {
          this.inputForm.attachment = util.getFolderFileIdStr(this.fileList);
          if (valid) {
            this.inputForm.attachment = util.getFolderFileIdStr(this.fileList);
            axios.post('/api/crm/shopPrint/save', qs.stringify(this.inputForm)).then((response)=> {
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
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },getFormProperty(){
        axios.get('/api/crm/shopPrint/getFormProperty').then((response)=>{
          this.formProperty=response.data;
        });
      },findOne(){
        axios.get('/api/crm/shopPrint/detail',{params: {id:this.$route.query.id}}).then((response)=>{
          util.copyValue(response.data,this.inputForm);
          this.depots=new Array(response.data.depot);
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
      }
    }
  }
</script>
