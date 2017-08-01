<template>
  <div>
    <head-tab active="employeeSalaryBasicForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('employeeSalaryBasicForm.salaryType')">
        </el-form-item>
        <el-form-item :label="$t('employeeSalaryBasicForm.employeeName')" prop="employeeId">
          <employee-select v-model="inputForm.employeeId"></employee-select>
        </el-form-item>
        <el-form-item :label="$t('employeeSalaryBasicForm.shouldGet')" prop="shouldGet">
          <el-input v-model.number="inputForm.shouldGet"></el-input>
        </el-form-item>
        <el-form-item :label="$t('employeeSalaryBasicForm.effectiveDate')" prop="value">
          <date-picker v-model="inputForm.effectiveDate"></date-picker>
        </el-form-item>
        <el-form-item :label="$t('employeeSalaryBasicForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('employeeSalaryBasicForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import employeeSelect from "components/basic/employee-select"
  export default{
    components:{employeeSelect},
    data(){
      return this.getData()
    },
    methods:{
      getData() {
        return{
          isCreate:this.$route.query.id==null,
          submitDisabled:false,
          inputForm:{
            extra:{}
          },
          rules: {
            shouldGet: [{ required: true, message: this.$t('employeeSalaryBasicForm.prerequisiteMessage'),trigger:"blur"}]
          }
        }
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/basic/salary/employeeSalaryBasic/save', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                Object.assign(this.$data, this.getData());
                this.initPage();
              }else{
                this.$router.push({name:'employeeSalaryBasicList',query:util.getQuery("employeeSalaryBasicList"), params:{_closeFrom:true}})
              }
            }).catch(function () {
              that.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },initPage(){
        axios.get('/api/basic/salary/employeeSalaryBasic/getForm').then((response)=>{
          this.inputForm = response.data;
          axios.get('/api/basic/salary/employeeSalaryBasic/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
          });
        });
      }
    },created () {
      this.initPage();
    }
  }
</script>
