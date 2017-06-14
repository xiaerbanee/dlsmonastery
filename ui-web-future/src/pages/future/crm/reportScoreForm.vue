<template>
  <div>
    <head-tab active="reportScoreForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('reportScoreForm.scoreDate')" prop="scoreDate">
              <date-picker v-model="inputForm.scoreDate" ></date-picker>
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
              {{$t('reportScoreForm.productNames')}}： {{inputProperty.productTypeNameStr}}
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
        return this.getData()
      },
      methods:{
        getData() {
          return{
            isInit:false,
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            inputProperty:{},
            inputForm:{},
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
        formSubmit(){
          var that = this;
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/ws/future/crm/reportScore/save',qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
                this.$message(response.data.message);
                if(this.inputForm.create){
                  Object.assign(this.$data, this.getData());
                  this.initPage();
                }else{
                  this.submitDisabled = false;
                  this.$router.push({name:'reportScoreList',query:util.getQuery("reportScoreList")})
                }
              }).catch(function () {
                that.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },initPage(){
          axios.get('/api/ws/future/crm/reportScore/getForm').then((response)=>{
            this.inputForm = response.data;
            axios.get('/api/ws/future/crm/reportScore/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
              util.copyValue(response.data,this.inputForm);
              this.inputProperty=response.data;
              console.log(this.inputProperty)
            });
          });
        }
      },created () {
        this.initPage();
      }
    }
</script>
