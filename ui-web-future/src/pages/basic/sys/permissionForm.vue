<template>
  <div>
    <head-tab active="permissionForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('permissionForm.menu')" prop="menuId">
          <el-select v-model="inputForm.menuId" filterable :placeholder="$t('permissionForm.selectCategory')">
            <el-option v-for="menu in inputForm.menuList" :key="menu.id" :label="menu.name" :value="menu.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('permissionForm.name')" prop="name">
          <el-input v-model="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('permissionForm.permission')" prop="permission">
          <el-input v-model="inputForm.permission"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="roleIdList">
          <el-select v-model="inputForm.roleIdList" multiple filterable  :placeholder="$t('accountForm.inputWord')" >
            <el-option v-for="item in inputForm.roleList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="请求Url" prop="url">
          <el-input v-model="inputForm.url"></el-input>
        </el-form-item>
        <el-form-item label="请求方法类型" prop="method">
          <el-input v-model="inputForm.method"></el-input>
        </el-form-item>
        <el-form-item :label="$t('permissionForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('permissionForm.save')}}</el-button>
        </el-form-item>
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
            roleList:[],
            inputForm:{},
            submitData:{
              id:'',
              menuId:'',
              name:'',
              permission:'',
              roleIdList:'',
              remarks:'',
              url:"",
              method:"",
            },
            rules: {
              menuId: [{ required: true, message: this.$t('permissionForm.prerequisiteMessage')}],
              permission: [{ required: true, message: this.$t('permissionForm.prerequisiteMessage')}],
              name: [{ required: true, message: this.$t('permissionForm.prerequisiteMessage')}],
              url: [{ required: true, message: this.$t('permissionForm.prerequisiteMessage')}],
              method: [{ required: true, message: this.$t('permissionForm.prerequisiteMessage')}],
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
              util.copyValue(this.inputForm,this.submitData);
              axios.post('/api/basic/sys/permission/save' ,qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'permissionList',query:util.getQuery("permissionList")})
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
        axios.get('/api/basic/sys/permission/getFormProperty',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm = response.data;
          if(response.data.roleIdList !=null){
            let roleList=new Array();
            for(var i=response.data.roleIdList.length-1;i>=0;i--){
              roleList.push({id:response.data.roleIdList[i],name:response.data.roleNameList[i]})
            }
            this.roleList=roleList;
            this.inputForm.roleIdList=response.data.roleIdList
          }
        })
      }
    }
</script>
