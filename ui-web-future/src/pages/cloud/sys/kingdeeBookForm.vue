<template>
  <div>
    <head-tab active="kingdeeBookForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item label="公司ID" prop="companyId">
          <el-input v-model="inputForm.companyId"></el-input>
        </el-form-item>
        <el-form-item label="账套" prop="name">
          <el-select v-model="inputForm.name" filterable placeholder="请选择" :clearable=true>
            <el-option v-for="name in inputForm.nameList" :key="name" :label="name" :value="name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="账套类型" prop="type">
          <el-select v-model="inputForm.type" filterable placeholder="请选择" :clearable=true>
            <el-option v-for="type in inputForm.typeList" :key="type" :label="type" :value="type"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="URL" prop="kingdeeUrl">
          <el-input v-model="inputForm.kingdeeUrl"></el-input>
        </el-form-item>
        <el-form-item label="POST-URL" prop="kingdeePostUrl">
          <el-input v-model="inputForm.kingdeePostUrl"></el-input>
        </el-form-item>
        <el-form-item label="用户名" prop="kingdeeUsername">
          <el-input v-model="inputForm.kingdeeUsername"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="kingdeePassword">
          <el-input v-model="inputForm.kingdeePassword"></el-input>
        </el-form-item>
        <el-form-item label="DBID" prop="kingdeeDbid">
          <el-input v-model="inputForm.kingdeeDbid"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">保存</el-button>
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
            submitData:{
              id:'',
              companyId:'',
              name:'',
              type:'',
              kingdeeUrl:'',
              kingdeePostUrl:'',
              kingdeeUsername:'',
              kingdeePassword:'',
              kingdeeDbid:'',
              remarks:'',
            },
            rules: {
              companyId: [{ required: true, message: "必填信息"}],
              name: [{ required: true, message: "必填信息"}],
              type: [{ required: true, message: "必填信息"}],
              kingdeeUrl: [{ required: true, message: "必填信息"}],
              kingdeePostUrl: [{ required: true, message: "必填信息"}],
              kingdeeUsername: [{ required: true, message: "必填信息"}],
              kingdeePassword: [{ required: true, message: "必填信息"}],
              kingdeeDbid: [{ required: true, message: "必填信息"}],
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
              axios.post('/api/global/cloud/sys/kingdeeBook/save', qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                if(this.inputForm.id){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'kingdeeBookList',query:util.getQuery("kingdeeBookList")})
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
          axios.get('/api/global/cloud/sys/kingdeeBook/findForm',{params: {id:this.$route.query.id}}).then((response)=>{
            this.inputForm = response.data;
          })
      }
    }
</script>
