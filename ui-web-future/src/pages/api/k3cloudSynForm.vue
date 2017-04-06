<template>
  <div>
    <head-tab active="金蝶核对详情"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row >
          <el-col :span="8">
            <el-form-item label="单据类型" prop="formId">
              {{inputForm.formId}}
            </el-form-item>
            <el-form-item label="nextFormId" prop="nextFormId" v-if="inputForm.nextFormId !==''">
              {{inputForm.nextFormId}}
            </el-form-item>
            <el-form-item label="单据编号" prop="extendFormatId">
              {{inputForm.extendFormatId}}
            </el-form-item>
            <el-form-item label="OA单据类型" prop="extendType">
              {{inputForm.extendType}}
            </el-form-item>
            <el-form-item label="金蝶单号" prop="billNo">
              {{inputForm.billNo}}
            </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="syn" v-if="inputForm.billNo ==''">重新同步</el-button>
          </el-form-item>
          </el-col>
          <el-col :span="12">

            <el-form-item label="内容" prop="content">
              {{inputForm.content}}
            </el-form-item>
            <el-form-item label="结果" prop="result">
              {{inputForm.result}}
            </el-form-item>

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
            accounts:[],
            remoteLoading:false,
            inputForm:{
              id:this.$route.query.id,
              formId:'',
              content:"",
              extendFormatId:"",
              extendType:"",
              billNo:"",
              nextFormId:"",
              extendId:"",
              nextBillNo:"",
              result:''
            },
            rules: {
            }
          }
      },
      methods:{
        syn(){
          alert("sasdfsd");
          axios.get('/api/api/K3cloudSyn/syn',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.$router.push({name:'k3cloudSynList',query:util.getQuery("k3cloudSynList")})
          });
        }

      },created(){
        if(!this.isCreate){
          axios.get('/api/api/K3cloudSyn/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
          })
        }
      }
    }
</script>
