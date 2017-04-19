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
        <el-form-item :label="$t('permissionForm.position')" prop="positionIdList">
          <el-select v-model="inputForm.positionIdList" multiple filterable remote :placeholder="$t('permissionForm.inputWord')" :remote-method="remotePosition" :loading="remoteLoading">
            <el-option v-for="position in positions" :key="position.id" :label="position.name" :value="position.id"></el-option>
          </el-select>
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
            positions:[],
            inputForm:{},
            submitData:{
              id:'',
              menuId:'',
              name:'',
              permission:'',
              positionIdList:'',
              remarks:''
            },
            rules: {
              menuId: [{ required: true, message: this.$t('permissionForm.prerequisiteMessage')}],
              permission: [{ required: true, message: this.$t('permissionForm.prerequisiteMessage')}],
              name: [{ required: true, message: this.$t('permissionForm.prerequisiteMessage')}]
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
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },remotePosition(query){
         if (query !== '') {
           this.remoteLoading = true;
           axios.get('/api/hr/position/search',{params:{name:query}}).then((response)=>{
             this.positions=response.data;
              this.remoteLoading = false;
           })
         } else {
           this.positions = [];
         }
       }
      },created(){
        var that = this;
        axios.get('/api/basic/sys/permission/findOne',{params: {id:that.$route.query.id}}).then((response)=>{
          that.inputForm = response.data;
          if(response.data.positionList!=null && response.data.positionList.length>0){
            that.positions=response.data.positionList;
            that.inputForm.positionIdList=util.getIdList(that.positions);
          }
        })
      }
    }
</script>
