<template>
  <div>
    <head-tab active="roleForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter = "20">
          <el-col :span = "7">
            <el-form-item label="名称" prop="name">
              <el-input v-model="inputForm.name"></el-input>
            </el-form-item>
            <el-form-item label="权限" prop="permission">
              <el-input v-model="inputForm.permission"></el-input>
            </el-form-item>
            <el-form-item label="备注" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">保存</el-button>
            </el-form-item>
          </el-col>
          <el-col :span = "10">
            <el-form-item  label="授权" prop="moduleIdStr">
              <el-tree
                :data="treeData"
                show-checkbox
                node-key="id"
                ref="tree"
                :default-checked-keys="checked"
                :default-expanded-keys="checked"
                @check-change="handleCheckChange"
                :props="defaultProps">
              </el-tree>
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
        submitData:{
          id:this.$route.query.id,
          name:'',
          permission:'',
          remarks:'',
          moduleIdStr:""
        },
        rules: {
          name: [{ required: true, message: this.$t('roleForm.prerequisiteMessage')}],
          permission: [{ required: true, message: this.$t('roleForm.prerequisiteMessage')}],
        },
        backendList:[],
        treeData:[],
        checked:[],
        defaultProps: {
          label: 'label',
          children: 'children'
        }
      };
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            util.copyValue(this.inputForm,this.submitData);
            axios.post('/api/basic/sys/role/save',qs.stringify(this.submitData)).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'roleList',query:util.getQuery("roleList")})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
      handleCheckChange(data, checked, indeterminate) {
        var modules=new Array()
        var check=this.$refs.tree.getCheckedKeys();
        for(var index in check){
          if(check[index].indexOf("p")!=0&& check[index]!=0){
            modules.push(check[index])
          }
        }
        this.inputForm.moduleIdStr=modules.join();
      }
    },created(){
      axios.get('/api/basic/sys/role/getFormProperty',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm=response.data;
        this.treeData =new Array(response.data.treeNode);
        this.checked = response.data.treeNode.checked;
        this.inputForm.moduleIdStr = response.data.treeNode.checked.join();
      })
    }
  }
</script>
