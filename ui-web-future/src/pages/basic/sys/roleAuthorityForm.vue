<template>
  <div>
    <head-tab active="roleAuthorityForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter = "20">
          <el-col :span = "7">
            <el-form-item label="角色" prop="id">
              <el-select v-model="inputForm.id" filterable :clearable=true remote placeholder="请输入关键字" :remote-method="remoteRole" @change="getTreeNode(inputForm.id)" :loading="remoteLoading">
                <el-option v-for="item in roleList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">保存</el-button>
            </el-form-item>
          </el-col>
          <el-col :span = "10">
            <el-form-item  label="权限" prop="permissionIdList">
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
        isCreate:this.$route.query.id!=null,
        submitDisabled:false,
        inputForm:{},
        submitData:{
          id:"",
          permissionIdList:""
        },
        rules: {
          id: [{ required: true, message: "必填属性"}],
        },
        roleList:[],
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
            axios.post('/api/basic/sys/role/saveAuthorityList',qs.stringify(this.submitData)).then((response)=> {
                console.log(response.data)
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
      }, handleCheckChange(data, checked, indeterminate) {
        var permissions=new Array()
        var check=this.$refs.tree.getCheckedKeys();
        for(var index in check){
          if(check[index].match("\^(0|[1-9][0-9]*)$")&& check[index]!=0){
            permissions.push(check[index])
          }
        }
        this.inputForm.permissionIdList=permissions;
      },remoteRole(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/basic/sys/role/search',{params:{name:query}}).then((response)=>{
            this.roleList=response.data;
            this.remoteLoading = false;
          })
        } else {
          this.roleList = [];
        }
      },getTreeNode(id){
          axios.get('/api/basic/sys/role/getTreeNode',{params:{id:id}}).then((response)=>{
            this.treeData =response.data.treeNode.children;
            this.checked = response.data.permissionIdList;
          })
      }
    }
  }
</script>
