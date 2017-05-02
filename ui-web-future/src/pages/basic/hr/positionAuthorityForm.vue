<template>
  <div>
    <head-tab active="岗位权限管理"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter = "20">
          <el-col :span = "7">
            <el-form-item label="岗位" prop="id">
              <el-select v-model="inputForm.id" filterable :clearable=true remote placeholder="请输入关键字" :remote-method="remotePosition" @change="getTreeNode(inputForm.id)" :loading="remoteLoading">
                <el-option v-for="item in positions" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">保存</el-button>
            </el-form-item>
          </el-col>
          <el-col :span = "10">
            <el-form-item  label="权限" prop="permissionIdStr">
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
          permissionIdStr:""
        },
        rules: {
          id: [{ required: true, message: "岗位"}],
        },
        positions:[],
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
            axios.post('/api/basic/hr/position/saveAuthorityList',qs.stringify(this.submitData)).then((response)=> {
                console.log(response.data)
              this.$message(response.data.message);
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'positionList',query:util.getQuery("positionList")})
              }
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
        this.inputForm.permissionIdStr=permissions.join();
      },remotePosition(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/basic/hr/position/search',{params:{name:query}}).then((response)=>{
            this.positions=response.data;
            this.remoteLoading = false;
          })
        } else {
          this.positions = [];
        }
      },getTreeNode(id){
          axios.get('/api/basic/hr/position/getTreeNode',{params:{id:id}}).then((response)=>{
            this.treeData =response.data.children;
            this.checked = response.data.checked;
          })
      }
    }
  }
</script>
