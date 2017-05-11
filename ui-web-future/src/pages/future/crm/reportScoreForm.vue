<template>
  <div>
    <head-tab active="reportScoreForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('reportScoreForm.scoreDate')" prop="scoreDate">
              <el-date-picker v-model="inputForm.scoreDate" align="right" :placeholder="$t('reportScoreForm.selectScoreDate')" :picker-options="pickerDateOption"></el-date-picker>
            </el-form-item>
            <el-form-item :label="$t('reportScoreForm.companyScore')" prop="companyScore">
              <el-input v-model="inputForm.companyScore"></el-input>
            </el-form-item>
            <el-form-item :label="$t('reportScoreForm.companyMonthScore')" prop="companyMonthScore">
              <el-input v-model="inputForm.companyMonthScore"></el-input>
            </el-form-item>
            <el-form-item :label="$t('reportScoreForm.cardQty')" prop="cardQty">
              <el-input v-model="inputForm.cardQty"></el-input>
            </el-form-item>
            <el-form-item :label="$t('reportScoreForm.monthCardQty')" prop="monthCardQty">
              <el-input v-model="inputForm.monthCardQty"></el-input>
            </el-form-item>
            <el-form-item :label="$t('reportScoreForm.rank')" prop="rank">
              <el-input v-model="inputForm.rank"></el-input>
            </el-form-item>
            <el-form-item :label="$t('reportScoreForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('reportScoreForm.save')}}</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-card class="box-card">
              {{$t('reportScoreForm.productNames')}}： {{productNames}}
            </el-card>
          </el-col>
        </el-row>
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
            formProperty:{},
            remoteLoading:false,
            productNames:'',
            pickerDateOption:util.pickerDateOption,
            inputForm:{
              id:this.$route.query.id,
              scoreDate:'',
              companyScore:"",
              companyMonthScore:"",
              cardQty:"",
              monthCardQty:"",
              rank:"",
              remarks:''
            },
            rules: {
              scoreDate: [{ required: true, message: this.$t('reportScoreForm.prerequisiteMessage')}],
              companyScore: [{ required: true, message: this.$t('reportScoreForm.prerequisiteMessage')}],
              companyMonthScore: [{ required: true, message: this.$t('reportScoreForm.prerequisiteMessage')}],
              cardQty: [{ required: true, message: this.$t('reportScoreForm.prerequisiteMessage')}],
              monthCardQty: [{ required: true, message: this.$t('reportScoreForm.prerequisiteMessage')}],
              rank: [{ required: true, message: this.$t('reportScoreForm.prerequisiteMessage')}]
            }
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              this.inputForm.scoreDate=util.formatLocalDate(this.inputForm.scoreDate);
              axios.post('/api/crm/reportScore/save',qs.stringify(this.inputForm)).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'reportScoreList',query:util.getQuery("reportScoreList")})
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
        axios.get('/api/crm/reportScore/getFormProperty').then((response)=>{
          this.productNames=response.data.productNames;
        })
      }
    }
</script>
