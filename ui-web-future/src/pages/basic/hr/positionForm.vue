<template>
  <div>
    <head-tab active="positionForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter = "20">
          <el-col :span = "7">
            <el-form-item :label="$t('positionForm.name')" prop="name">
              <el-input v-model="inputForm.name"></el-input>
            </el-form-item>
            <el-form-item label="绑定角色" prop="roleId">
              <el-select v-model="inputForm.roleId" filterable  :placeholder="$t('accountForm.inputWord')" >
                <el-option v-for="item in inputProperty.roleList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('positionForm.permission')" prop="permission">
              <el-input v-model="inputForm.permission"></el-input>
            </el-form-item>
            <el-form-item :label="$t('positionForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('positionForm.save')}}</el-button>
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
        remoteLoading:false,
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        inputForm:{},
        inputProperty:{},
        submitData:{
          id:this.$route.query.id,
          name:'',
          permission:'',
          remarks:'',
          roleId:'',
        },
        roleList:[],
        rules: {
          name: [{ required: true, message: this.$t('positionForm.prerequisiteMessage')}],
          permission: [{ required: true, message: this.$t('positionForm.prerequisiteMessage')}],
        },
      };
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            util.copyValue(this.inputForm,this.submitData);
            axios.post('/api/basic/hr/position/save',qs.stringify(this.submitData)).then((response)=> {
              this.$message(response.data.message);
              form.resetFields();
              this.submitDisabled = false;
              if(!this.isCreate){
                this.$router.push({name:'positionList',query:util.getQuery("positionList")})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },initPage() {
        axios.get('/api/basic/hr/position/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm=response.data;
        })
        axios.get('/api/basic/hr/position/getForm').then((response)=>{
          this.inputProperty=response.data;
        })
      }
    },activated () {
      if(!this.$route.query.headClick) {
        this.initPage();
      }
    }
  }
</script>
