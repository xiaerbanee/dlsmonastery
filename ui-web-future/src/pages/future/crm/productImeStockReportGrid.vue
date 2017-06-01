<template>
  <div>
    <head-tab active="productImeStockReportGrid"></head-tab>
    <div>
      <el-form :model="reportScore" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('reportScoreForm.scoreDate')" prop="scoreDate">
              <date-picker v-model="reportScore.scoreDate" ></date-picker>
            </el-form-item>
            <el-form-item :label="$t('reportScoreForm.companyScore')" prop="companyScore">
              <el-input v-model="reportScore.companyScore"></el-input>
            </el-form-item>
            <el-form-item :label="$t('reportScoreForm.companyMonthScore')" prop="companyMonthScore">
              <el-input v-model="reportScore.companyMonthScore"></el-input>
            </el-form-item>
            <el-form-item :label="$t('reportScoreForm.cardQty')" prop="cardQty">
              <el-input v-model="reportScore.cardQty"></el-input>
            </el-form-item>
            <el-form-item :label="$t('reportScoreForm.monthCardQty')" prop="monthCardQty">
              <el-input v-model="reportScore.monthCardQty"></el-input>
            </el-form-item>
            <el-form-item :label="$t('reportScoreForm.rank')" prop="rank">
              <el-input v-model="reportScore.rank"></el-input>
            </el-form-item>
            <el-form-item :label="$t('reportScoreForm.remarks')" prop="remarks">
              <el-input v-model="reportScore.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('reportScoreForm.save')}}</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-card class="box-card">
              {{$t('reportScoreForm.productNames')}}： {{productTypeNames}}
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
            productTypeNames:'',
            notScores:'',
            reportScore:{},
            submitData:{
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
              util.copyValue(this.reportScore, this.submitData);
              axios.post('/api/ws/future/crm/reportScore/save',qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                this.submitDisabled = false;
                if(response.data.success){
                  if(this.isCreate){
                    form.resetFields();
                  } else {
                    this.$router.push({name:'reportScoreList',query:util.getQuery("reportScoreList")})
                  }
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

        axios.get('/api/ws/future/crm/reportScore/getProductTypeNamesAndNotScores').then((response)=>{
          this.productTypeNames = response.data.productTypeNames;
          this.notScores = response.data.notScores;
        });

        //每日打分，只能新增和删除，不能修改
        axios.get('/api/ws/future/crm/reportScore/findDto').then((response)=>{
          this.reportScore=response.data;
        })
      }
    }
</script>
