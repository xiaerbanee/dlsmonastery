<template>
  <div>
    <head-tab active="shopPrintForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('shopPrintForm.officeName')" prop="officeId">
              <el-select v-model="inputForm.officeId" filterable clearable >
                <el-option v-for="item in inputForm.extra.areaList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
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
              <el-input v-model="inputForm.remarks" type="textarea" :placeholder="$t('shopPrintForm.remarksPlaceholder')"></el-input>
            </el-form-item>
            <el-form-item :label="$t('shopPrintForm.attachment')" prop="attachment">
              <el-upload action="/api/general/sys/folderFile/upload?uploadPath=/广告印刷" :on-change="handleChange" :on-remove="handleRemove"  :on-preview="handlePreview" :file-list="fileList" list-type="picture">
                <el-button size="small" type="primary">{{$t('shopPrintForm.clickUpload')}}</el-button>
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
      return this.getData();
    },
    methods:{
      getData(){
        return{
          isCreate:this.$route.query.id==null,
          submitDisabled:false,
          officeDisabled:false,
          printTypeContent:'',
          fileList:[],
          inputForm:{
            extra:{}
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
      formSubmit(){
        this.submitDisabled = true;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          this.inputForm.attachment = util.getFolderFileIdStr(this.fileList);
          if (valid) {
            axios.post('/api/ws/future/layout/shopPrint/save', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
              this.$message(response.data.message);

              if(response.data.success) {
                if (this.isCreate) {
                  Object.assign(this.$data,this.getData());
                  this.initPage();
                }else{
                  this.$router.push({name: 'shopPrintList', query: util.getQuery("shopPrintList"), params:{_closeFrom:true}})
                }
              }
            }).catch(() => {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },typeChange(){
        axios.get('/api/basic/sys/dictMap/findByName',{params: {name: this.inputForm.printType}}).then((response)=>{
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
        axios.get('/api/ws/future/layout/shopPrint/getForm').then((response)=>{
          this.inputForm = response.data;
          if(!this.isCreate) {
            this.officeDisabled = true;
            axios.get('/api/ws/future/layout/shopPrint/findOne', {params: {id: this.$route.query.id}}).then((response) => {
              util.copyValue(response.data, this.inputForm);
              if (this.inputForm.printType != null) {
                this.typeChange();
              }
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
