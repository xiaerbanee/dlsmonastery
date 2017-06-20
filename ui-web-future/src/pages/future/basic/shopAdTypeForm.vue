<template>
  <div>
    <head-tab active="shopAdTypeForm"></head-tab>
    <div >
      <el-form :model="formData" ref="formData" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('shopAdTypeForm.name')" prop="name">
          <el-input v-model="formData.name"></el-input>
        </el-form-item>

        <el-form-item :label="$t('shopAdTypeForm.totalPriceType')" prop="totalPriceType" >
            <el-select v-model="formData.totalPriceType" filterable clearable :placeholder="$t('shopAdTypeList.inputKey')">
              <el-option v-for="item in formData.extra.totalPriceTypeList" :key="item" :label="item" :value="item"></el-option>
            </el-select>
        </el-form-item>

        <el-form-item :label="$t('shopAdTypeForm.price')" prop="price">
          <el-input v-model.number="formData.price"></el-input>
        </el-form-item>
        <el-form-item :label="$t('shopAdTypeForm.remarks')" prop="remarks">
          <el-input v-model="formData.remarks" type="textarea" :rows="5"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('shopAdTypeForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
    export default{
      data(){
        return this.getData()
      },
      methods:{
        getData() {
          return{
            submitDisabled:false,
            formData:{
              extra:{}
            },
            rules: {
              name: [{ required: true, message: this.$t('shopAdTypeForm.prerequisiteMessage')}],
              totalPriceType: [{ required: true, message: this.$t('shopAdTypeForm.prerequisiteMessage')}],
              price: [{ required: true, message: this.$t('shopAdTypeForm.prerequisiteMessage')},{ type: 'number', message: this.$t('shopAdTypeForm.inputLegalValue')}]
            }
          }
      },
        formSubmit(){
          var that = this;
          this.submitDisabled = true;
          var form = this.$refs["formData"];
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/ws/future/basic/shopAdType/save', qs.stringify(util.deleteExtra(this.formData))).then((response)=> {
                this.$message(response.data.message);
                if(this.formData.isCreate){
                  Object.assign(this.$data,this.getData());
                  this.initPage();
                }
                else{
                  this.submitDisabled = false;
                  this.$router.push({name:'shopAdTypeList',query:util.getQuery("shopAdTypeList")})
                }
              }).catch(function () {
                that.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },
        initPage () {
          axios.get('/api/ws/future/basic/shopAdType/getForm').then((response)=>{
            this.formData = response.data;
            if(!this.isCreate) {
              axios.get('/api/ws/future/basic/shopAdType/findOne', {params: {id: this.$route.query.id}}).then((response) => {
                util.copyValue(response.data, this.formData);
              });
            }
          });
        }
      },created(){
        this.initPage();
      }
    }
</script>
