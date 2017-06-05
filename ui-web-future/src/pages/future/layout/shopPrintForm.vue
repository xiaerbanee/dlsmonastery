<template>
  <div>
    <head-tab active="shopPrintForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('shopPrintForm.officeName')" prop="officeName">
              <office-select v-model="inputForm.officeId" :disabled="officeDisabled"></office-select>
            </el-form-item>
            <el-form-item :label="$t('shopPrintForm.printType')" prop="printType">
              <dict-map-select v-model="inputForm.printType" category="门店_广告印刷" @input="typeChange"></dict-map-select>
            </el-form-item>
            <el-form-item :label="$t('shopPrintForm.printTypeContent')" prop="printTypeContent">{{printTypeContent}}
            </el-form-item>
            <el-form-item :label="$t('shopPrintForm.qty')" prop="qty">
              <el-input v-model.number="inputForm.qty" ></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopPrintForm.content')" prop="content">
              <el-input v-model="inputForm.content" type="textarea"></el-input>
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
                  <el-upload action="/api/general/sys/folderFile/upload?uploadPath=/广告印刷" :headers="headers" :on-change="handleChange" :on-remove="handleRemove"  :on-preview="handlePreview" :file-list="fileList" list-type="picture">
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
  import officeSelect from 'components/basic/office-select';
  import dictMapSelect from 'components/basic/dict-map-select';
  export default{
    components:{officeSelect,dictMapSelect},
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        officeDisabled:false,
        formProperty:{},
        printTypeContent:'',
        fileList:[],
        inputForm:{},
        submitData:{
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
        headers:{Authorization: 'Bearer ' + this.$store.state.global.token.access_token},
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
            axios.post('/api/ws/future/layout/shopPrint/save', qs.stringify(this.submitData)).then((response)=> {
              this.$message(response.data.message);
              this.submitDisabled = false;
              if(response.data.success) {
                if (this.isCreate) {
                  form.resetFields();
                  this.fileList = [];
                } else {
                  this.$router.push({name: 'shopPrintList', query: util.getQuery("shopPrintList")})
                }
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }
        })
      },typeChange(){
        axios.get('/api/basic/sys/dictMap/findByName?name=' + this.inputForm.printType).then((response)=>{
          this.printTypeContent=response.data;
        })
      },
      handlePreview(file) {
        window.open(file.url);
      },handleChange(file, fileList) {
        this.fileList = fileList;
      },handleRemove(file, fileList) {
        this.fileList = fileList;
      },initPage(){
        axios.get('/api/ws/future/layout/shopPrint/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm = response.data;
        if(this.inputForm.printType!=null){
          this.typeChange();
        }
        if(this.inputForm.attachment !=null) {
          axios.get('/api/general/sys/folderFile/findByIds',{params: {ids:this.inputForm.attachment}}).then((response)=>{
            this.fileList= response.data;
        });
        }
      });
        if(!this.isCreate){
          this.officeDisabled = true;
        }
      }
    },created(){
      this.initPage();
    },activated () {
      if(!this.$route.query.headClick) {
        this.initPage();
      }
      }
  }
</script>
