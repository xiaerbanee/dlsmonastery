<template>
  <div>
    <head-tab active="shopPromotionForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="130px"  class="form input-form">
        <el-row>
          <el-col :span="12">
           <el-form-item :label="$t('shopPromotionForm.shopId')" prop="shopId">
             <depot-select v-model="inputForm.shopId" category="SHOP" :disabled="shopDisabled"></depot-select>
            </el-form-item>
            <el-form-item :label="$t('shopPromotionForm.activityDate')" prop="activityDate">
              <el-date-picker v-model="inputForm.activityDate" type="date" :placeholder="$t('shopPromotionForm.inputDate')"></el-date-picker>
            </el-form-item>
            <el-form-item :label="$t('shopPromotionForm.activityType')" prop="activityType">
              <el-select v-model="inputForm.activityType" filterable clearable :placeholder="$t('shopPromotionForm.inputType')">
                <el-option v-for="(key,value) in inputForm.activityTypeList" :key="key" :label="key" :value="key"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('shopPromotionForm.amount')" prop="amount">
              <el-input v-model.number="inputForm.amount" ></el-input>
            </el-form-item>
            <el-form-item  :label="$t('shopPromotionForm.dayAmount')" prop="dayAmount">
              <el-input v-model.number="inputForm.dayAmount" ></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopPromotionForm.salerComment')"prop="salerComment">
              <el-input v-model="inputForm.salerComment" ></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopPromotionForm.materialComment')" prop="materialComment">
              <el-input v-model="inputForm.materialComment" type="textarea"></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopPromotionForm.comment')" prop="comment">
              <el-input v-model="inputForm.comment" type="textarea"></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopPromotionForm.phone')" prop="phone">
              <el-input v-model="inputForm.phone" ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('shopPromotionForm.activityImage1')" prop="activityImage1">
                  <el-upload action="/api/general/sys/folderFile/upload?uploadPath=/活动拉销"  :headers="headers" :on-change="handleChange1" :on-remove="handleRemove1"  :on-preview="handlePreview1" :file-list="fileList1" list-type="picture">
                    <el-button size="small" type="primary">{{$t('shopPromotionForm.clickUpload')}}</el-button>
                    <div slot="tip" class="el-upload__tip">{{$t('shopPromotionForm.uploadImageSizeFor5000KB')}}</div>
                  </el-upload>
            </el-form-item>
            <el-form-item :label="$t('shopPromotionForm.activityImage2')" prop="activityImage2">
                  <el-upload action="/api/general/sys/folderFile/upload?uploadPath=/活动拉销"  :headers="headers" :on-change="handleChange2" :on-remove="handleRemove2"  :on-preview="handlePreview2" :file-list="fileList2" list-type="picture">
                    <el-button size="small" type="primary">{{$t('shopPromotionForm.clickUpload')}}</el-button>
                    <div slot="tip" class="el-upload__tip">{{$t('shopPromotionForm.uploadImageSizeFor5000KB')}}</div>
                  </el-upload>
            </el-form-item>
            <el-form-item :label="$t('shopPromotionForm.activityImage3')" prop="activityImage3">
                  <el-upload action="/api/general/sys/folderFile/upload?uploadPath=/活动拉销"  :headers="headers" :on-change="handleChange3" :on-remove="handleRemove3"  :on-preview="handlePreview3" :file-list="fileList3" list-type="picture">
                    <el-button size="small" type="primary">{{$t('shopPromotionForm.clickUpload')}}</el-button>
                    <div slot="tip" class="el-upload__tip">{{$t('shopPromotionForm.uploadImageSizeFor5000KB')}}</div>
                  </el-upload>
            </el-form-item>
            <el-form-item :label="$t('shopPromotionForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled"  @click="formSubmit()">{{$t('shopPromotionForm.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>

<script>
  import depotSelect from 'components/future/depot-select'
  export default{
    components:{depotSelect},
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        shopDisabled:false,
        formProperty:{},
        fileList1:[],
        fileList2:[],
        fileList3:[],
        shops:[],
        inputForm:{},
        submitData:{
          id:'',
          shopId:'',
          activityDate:'',
          activityType:'',
          amount:'',
          dayAmount:'',
          salerComment:'',
          materialComment:'',
          comment:'',
          phone:'',
          activityImage1:'',
          activityImage2:'',
          activityImage3:'',
          remarks:''
        },
        rules: {
          shopId: [{ required: true, message: this.$t('shopPromotionForm.prerequisiteMessage')}],
          activityDate: [{ required: true, message: this.$t('shopPromotionForm.prerequisiteMessage')}],
          activityType: [{ required: true, message: this.$t('shopPromotionForm.prerequisiteMessage')}],
          amount: [{ required: true, message: this.$t('shopPromotionForm.prerequisiteMessage')},{type:"number",message:this.$t('shopPromotionForm.inputLegalValue')}],
          dayAmount: [{ required: true, message: this.$t('shopPromotionForm.prerequisiteMessage')},{type:"number",message:this.$t('shopPromotionForm.inputLegalValue')}],
          phone: [{ required: true, message: this.$t('shopPromotionForm.prerequisiteMessage')}],
        },
        remoteLoading:false,
        headers:{Authorization: 'Bearer ' + this.$store.state.global.token.access_token}
      }
    },
    methods: {
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        this.inputForm.activityDate = util.formatLocalDate(this.inputForm.activityDate)
        form.validate((valid) => {
          this.inputForm.activityImage1 = util.getFolderFileIdStr(this.fileList1);
          this.inputForm.activityImage2 = util.getFolderFileIdStr(this.fileList2);
          this.inputForm.activityImage3 = util.getFolderFileIdStr(this.fileList3);

          if (valid) {
              util.copyValue(this.formData,this.submitData);
            axios.post('/api/ws/future/layout/shopPromotion/save', qs.stringify(this.submitData)).then((response) => {
              if (response.data.message) {
                this.$message(response.data.message);
              }
              if (this.isCreate) {
                form.resetFields();
                this.fileList1 = [];
                this.fileList2 = [];
                this.fileList3 = [];
                this.submitDisabled = false;
              } else {
                this.$router.push({name: 'shopPromotionList', query: util.getQuery("shopPromotionList")})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          } else {
            this.submitDisabled = false;
          }
        })
      }, handlePreview1(file) {
        window.open(file.url);
      }, handleChange1(file, fileList) {
        this.fileList1 = fileList;
      }, handleRemove1(file, fileList) {
        this.fileList1 = fileList;
      }, handlePreview2(file) {
        window.open(file.url);
      }, handleChange2(file, fileList) {
        this.fileList2 = fileList;
      }, handleRemove2(file, fileList) {
        this.fileList2 = fileList;
      }, handlePreview3(file) {
        window.open(file.url);
      }, handleChange3(file, fileList) {
        this.fileList3 = fileList;
      }, handleRemove3(file, fileList) {
        this.fileList3 = fileList;
      }, findOne(){

      }
    },created(){
      axios.get('/api/ws/future/layout/shopPromotion/findForm',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm = response.data;
        if(response.data.shopId!=null){
          this.shops=new Array(response.data.shopId);
          this.shopDisabled = true;
        }
        if(this.inputForm.activityImage1 !=null) {
          axios.get('/api/general/sys/folderFile/findByIds',{params: {ids:this.inputForm.activityImage1}}).then((response)=>{
            this.fileList1= response.data;
          });
        }
        if(this.inputForm.activityImage2 !=null) {
          axios.get('/api/general/sys/folderFile/findByIds',{params: {ids:this.inputForm.activityImage2}}).then((response)=>{
            this.fileList2= response.data;
          });
        }
        if(this.inputForm.activityImage3 !=null) {
          axios.get('/api/general/sys/folderFile/findByIds',{params: {ids:this.inputForm.activityImage3}}).then((response)=>{
            this.fileList3= response.data;
          });
        }
      })
    }
  }
</script>
