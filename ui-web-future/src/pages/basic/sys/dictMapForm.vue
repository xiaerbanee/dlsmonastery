<template>
  <div>
    <head-tab active="dictMapForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('dictMapForm.category')" prop="category">
          <el-select v-model="inputForm.category" filterable :placeholder="$t('dictMapForm.selectCategory')">
            <el-option v-for="category in inputProperty.categoryList" :key="category" :label="category" :value="category"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('dictMapForm.name')" prop="name">
          <el-input v-model="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('dictMapForm.value')" prop="value">
          <el-input v-model="inputForm.value"></el-input>
        </el-form-item>
        <el-form-item :label="$t('dictMapForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"  :disabled="submitDisabled" @click="formSubmit()">{{$t('dictMapForm.save')}}</el-button>
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
            inputForm:{},
            inputProperty:{},
            submitData:{
              id:'',
              category:'',
              name:'',
              value:'',
              remarks:''
            },
            rules: {
              category: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
              name: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
              value: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}]
            }
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
                util.copyValue(this.inputForm,this.submitData);
              axios.post('/api/basic/sys/dictMap/save', qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'dictMapList',query:util.getQuery("dictMapList")})
                }
              }).catch(function () {
                this.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        }
      },created(){
          axios.get('/api/basic/sys/dictMap/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            this.inputForm = response.data;
          })
          axios.get('/api/basic/sys/dictMap/getFormProperty').then((response)=>{
            this.inputProperty = response.data;
          })
      }
    }
</script>
