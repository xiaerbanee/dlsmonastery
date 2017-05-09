<template>
  <div>
    <head-tab active="officeRuleForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter = "15">
          <el-col :span="15">
            <el-form-item label="上级" prop="parentId">
              <el-select v-model="inputForm.parentId" filterable>
                <el-option v-for="item in inputForm.officeRuleList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('officeRuleForm.name')" prop="name">
              <el-input v-model="inputForm.name"></el-input>
            </el-form-item>
            <el-form-item :label="$t('officeRuleForm.code')" prop="code">
              <el-input v-model="inputForm.code"></el-input>
            </el-form-item>
            <el-form-item label="是否有点位" prop="hasPoint">
              <el-radio-group v-model="inputForm.hasPoint">
                <el-radio v-for="(value,key) in inputForm.boolMap" :key="key" :label="value">{{key | bool2str}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="备注" prop="remark">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('menuForm.save')}}</el-button>
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
        inputForm:{},
        submitData:{
          id:'',
          type:"",
          parentId:'',
          name:'',
          code:'',
          hasPoint:true,
          remarks:'',
        },
        rules: {
          name: [{ required: true, message: this.$t('officeRuleForm.prerequisiteMessage')}],
          code: [{ required: true, message: this.$t('officeRuleForm.prerequisiteMessage')}],
          hasPoint: [{ required: true, message: this.$t('officeRuleForm.prerequisiteMessage')}],
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
            axios.post('/api/basic/sys/officeRule/save',qs.stringify(this.submitData)).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'officeRuleList',query:util.getQuery("officeRuleList")})
              }
            });
          }else{
            this.submitDisabled = false;
          }
        })
      }
    },created(){
      axios.get('/api/basic/sys/officeRule/findForm',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm = response.data;
        if(response.data.parenId){
          this.inputForm.officeRuleList=new Array({id:response.data.parenId,name:response.data.parentName});
        }
        this.inputForm.hasPoint=this.inputForm.hasPoint?"1":"0"
        console.log(this.inputForm.officeRuleEnumList)
      })
    }
  }
</script>
