<template>
  <div>
    <head-tab active="dictEnumForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('dictEnumForm.category')" prop="category">
          <el-select v-model="inputForm.category" filterable :placeholder="$t('dictEnumForm.selectGroup')">
            <el-option v-for="category in inputProperty.categoryList" :key="category" :label="category" :value="category"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('dictEnumForm.sort')" prop="sort">
          <el-input v-model.number="inputForm.sort"></el-input>
        </el-form-item>
        <el-form-item :label="$t('dictEnumForm.value')" prop="value">
          <el-input v-model="inputForm.value"></el-input>
        </el-form-item>
        <el-form-item :label="$t('dictEnumForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('dictEnumForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        submitDisabled:false,
        inputForm:{},
        inputProperty:{},
        submitData:{
          id:'',
          category:'',
          sort:'',
          value:'',
          remarks:''
        },
        rules: {
          category: [{ required: true, message: this.$t('dictEnumForm.prerequisiteMessage')}],
          sort: [{ required: true, message: this.$t('dictEnumForm.prerequisiteMessage')},{ type: 'number', message: this.$t('dictEnumForm.inputLegalValue')}],
          value: [{ required: true, message: this.$t('dictEnumForm.prerequisiteMessage')}]
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
            axios.post('/api/basic/sys/dictEnum/save', qs.stringify(this.submitData)).then((response)=> {
              this.$message(response.data.message);
              this.submitDisabled = false;
              if(this.inputForm.create){
                form.resetFields();
              } else {
                this.$router.push({name:'dictEnumList',query:util.getQuery("dictEnumList")})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },initPage() {
        axios.get('/api/basic/sys/dictEnum/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm = response.data;
        });
        axios.get('/api/basic/sys/dictEnum/getForm').then((response)=>{
          this.inputProperty = response.data;
        });
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
