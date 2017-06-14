<template>
  <div>
    <head-tab active="permissionForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('permissionForm.menu')" prop="menuId">
          <el-select v-model="inputForm.menuId" filterable :placeholder="$t('permissionForm.selectCategory')">
            <el-option v-for="menu in inputForm.extra.menuList" :key="menu.id" :label="menu.name" :value="menu.id"></el-option>
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
            <el-option v-for="item in inputForm.extra.roleList" :key="item.id" :label="item.name" :value="item.id"></el-option>
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
      data:function () {
        return this.getData();
      },
      methods:{
        getData(){
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            roleList:[],
            inputForm:{
                extra:{}
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
        formSubmit(){
          var that = this;
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/basic/sys/permission/save' ,qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
                this.$message(response.data.message);

                if(this.inputForm.create){
                  Object.assign(this.$data,this.getData());
                  this.initPage();
                }else{
                  this.submitDisabled = false;
                  this.$router.push({name:'permissionList',query:util.getQuery("permissionList")})
                }
              }).catch(function () {
                that.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },initPage(){
          axios.get('/api/basic/sys/permission/getForm').then((response)=>{
            this.inputForm = response.data;
            axios.get('/api/basic/sys/permission/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
              util.copyValue(response.data,this.inputForm)
            })
          });
          this.isInit = true;
        }
      },created(){
        this.initPage();
      }
    }
</script>
