<template>
  <div>
    <head-tab active="recruitBatchForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
            <el-form-item :label="$t('recruitBatchForm.name')">
              {{nameList}}
            </el-form-item>
            <el-form-item  :label="$t('recruitBatchForm.applyPositionName')" prop="applyPositionName">
              <el-input v-model="inputForm.applyPositionName"></el-input>
            </el-form-item>
          <el-form-item  :label="$t('recruitBatchForm.applyFrom')" prop="applyFrom">
            <el-input v-model="inputForm.applyFrom"></el-input>
          </el-form-item>
          <el-form-item  :label="$t('recruitBatchForm.workArea')" prop="workArea">
            <el-input v-model="inputForm.workArea"></el-input>
          </el-form-item>
          <el-form-item  :label="$t('recruitBatchForm.workCategory')" prop="workCategory">
            <el-input v-model="inputForm.workCategory"></el-input>
          </el-form-item>
          <el-form-item  :label="$t('recruitBatchForm.education')" prop="education">
            <el-select v-model="inputForm.education" >
              <el-option v-for="item in formProperty.educationsList"  :key="item" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item  :label="$t('recruitBatchForm.entryRealDate')" prop="entryRealDate">
            <el-date-picker  v-model="inputForm.entryRealDate" type="datetime" align="left" ></el-date-picker>
          </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('expressOrderList.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        isCreate:this.$route.query.ids==null,
        submitDisabled:false,
        formProperty:{},
        nameList:"",
        inputForm:{
          ids:this.$route.query.ids,
          applyPositionName:'',
          applyFrom:'',
          workArea:"",
          workCategory:"",
          education:'',
          entryRealDate:""
        },
        rules: {
        },
        remoteLoading:false
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/basic/hr/recruit/update',qs.stringify(this.inputForm)).then((response)=> {
              this.$message(response.data.message);
              this.$router.push({name:'recruitList',query:util.getQuery("recruitList")})
            });
          }else{
            this.submitDisabled = false;
          }
        })
      }
    },created(){
      axios.get('/api/basic/hr/recruit/getFormProperty').then((response)=>{
        this.formProperty=response.data;
      });
      console.log(this.inputForm.ids)
      axios.get('/api/basic/hr/recruit/findNameByIds',{params:{ids:this.inputForm.ids}}).then((response)=>{
        this.nameList=response.data;
      });
    }
  }
</script>
