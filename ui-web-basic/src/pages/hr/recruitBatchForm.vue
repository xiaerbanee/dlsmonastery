<template>
  <div>
    <head-tab active="recruitBatchForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
          <el-form-item  :label="$t('recruitBatchForm.name')" prop="applyFrom">
            <el-select v-model="inputForm.name"  clearable >
              <el-option v-for="item in inputForm.extra.nameList"  :key="item" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item  :label="$t('recruitBatchForm.oldValue')" prop="oldValue">
            <el-select v-model="inputForm.name"  clearable >
              <el-option v-for="item in inputForm.extra.nameList"  :key="item" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item  :label="$t('recruitBatchForm.newValue')" prop="newValue">
            <el-select v-model="inputForm.name"  clearable >
              <el-option v-for="item in inputForm.extra.nameList"  :key="item" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('recruitBatchForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return this.getData();
    },
    methods:{
      getData(){
        return {
          isCreate: this.$route.query.ids == null,
          submitDisabled: false,
          inputForm: {
            extra: {}
          },
          rules: {},
          remoteLoading: false
        };
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/basic/hr/recruit/update',qs.stringify(this.inputForm)).then((response)=> {
              this.$message(response.data.message);
              Object.assign(this.$data, this.getData());
              this.$router.push({name:'recruitList',query:util.getQuery("recruitList")})
            }).catch(function () {
              that.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },initPage() {

      }
    },activated () {
        axios.get('/api/basic/hr/recruit/getForm').then((response)=>{
          this.inputForm.extra = response.data;
        });
      }
  }
</script>
