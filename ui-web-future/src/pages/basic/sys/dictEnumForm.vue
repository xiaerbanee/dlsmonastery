<template>
  <div>
    <head-tab active="dictEnumForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('dictEnumForm.category')" prop="category">
          <el-select v-model="inputForm.category" filterable :placeholder="$t('dictEnumForm.selectGroup')">
            <el-option v-for="category in inputForm.extra.categoryList" :key="category" :label="category" :value="category"></el-option>
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
      return this.getData()
    },
    methods:{
      getData() {
        return{
          submitDisabled:false,
          inputForm:{
            extra:{}
          },
          rules: {
            category: [{ required: true, message: this.$t('dictEnumForm.prerequisiteMessage')}],
            sort: [{ required: true, message: this.$t('dictEnumForm.prerequisiteMessage')},{ type: 'number', message: this.$t('dictEnumForm.inputLegalValue')}],
            value: [{ required: true, message: this.$t('dictEnumForm.prerequisiteMessage')}]
          }
        }
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/basic/sys/dictEnum/save', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
              this.$message(response.data.message);
              if(this.inputForm.create){
                Object.assign(this.$data, this.getData());
                this.initPage();
              }else{
                this.submitDisabled = false;
                this.$router.push({name:'dictEnumList',query:util.getQuery("dictEnumList")})
              }
            }).catch(function () {
              that.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },initPage(){
        axios.get('/api/basic/sys/dictEnum/getForm').then((response)=>{
          this.inputForm = response.data;
          axios.get('/api/basic/sys/dictEnum/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
          });
        });
      }
    },created () {
        this.initPage();
    }
  }
</script>
