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
              <el-option v-for="item in formData.totalPriceTypeList" :key="item" :label="item" :value="item"></el-option>
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
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            formData:{},
            submitData:{
              id:'',
              name:'',
              totalPriceType:'按数量',
              price:'',
              remarks:''
            },
            rules: {
              name: [{ required: true, message: this.$t('shopAdTypeForm.prerequisiteMessage')}],
              price: [{ required: true, message: this.$t('shopAdTypeForm.prerequisiteMessage')},{ type: 'number', message: this.$t('shopAdTypeForm.inputLegalValue')}]
            }
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["formData"];
          util.copyValue(this.formData,this.submitData);
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/ws/future/basic/shopAdType/save', qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'shopAdTypeList',query:util.getQuery("shopAdTypeList")})
                }
              });
            }else{
              this.submitDisabled = false;
            }
          })
        }
      },created(){


          axios.get('/api/ws/future/basic/shopAdType/findForm',{params: {id:this.$route.query.id}}).then((response)=>{
            this.formData = response.data;
          })

      }
    }
</script>
