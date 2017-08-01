<template>
  <div>
    <head-tab active="employeeSalaryForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('employeeSalaryForm.employeeName')" prop="employeeId">
          <employee-select v-model="inputForm.employeeId"></employee-select>
        </el-form-item>
        <el-form-item :label="$t('employeeSalaryForm.type')" prop="type">
          <el-select v-model="inputForm.item" filterable :placeholder="$t('employeeSalaryForm.selectGroup')">
            <el-option v-for="item in inputForm.extra.typeList" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('employeeSalaryForm.realGet')" prop="realGet">
          <el-input v-model.number="inputForm.realGet"></el-input>
        </el-form-item>
        <el-form-item :label="$t('employeeSalaryForm.month')" prop="month">
          <el-date-picker v-model="month"  type="month" :placeholder="$t('employeeSalaryForm.selectMonth')"></el-date-picker>
        </el-form-item>
        <el-form-item :label="$t('employeeSalaryForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('employeeSalaryForm.save')}}</el-button>
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
          month:null,
          rules: {
            type: [{ required: true, message: this.$t('employeeSalaryForm.prerequisiteMessage'),trigger:"blur"}],
            realGet: [{required:true, message: this.$t('employeeSalaryForm.prerequisiteMessage'),trigger:"blur"}],
          }
        }
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/basic/salary/employeeSalary/save', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                Object.assign(this.$data, this.getData());
                this.initPage();
              }else{
                this.$router.push({name:'employeeSalaryListList',query:util.getQuery("employeeSalaryList"), params:{_closeFrom:true}})
              }
            }).catch(function () {
              that.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },initPage(){
        axios.get('/api/basic/salary/employeeSalary/getForm').then((response)=>{
          this.inputForm = response.data;
          axios.get('/api/basic/salary/employeeSalary/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
          });
        });
      }
    },created () {
      this.initPage();
    }
  }
</script>
