<template>
  <div>
    <head-tab active="roleAuthorityForm"></head-tab>
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
    data:function () {
      return this.getData();
    },
    methods:{
      getData(){
        return{
          inInit:false,
          remoteLoading:false,
          isCreate:this.$route.query.id!=null,
          submitDisabled:false,
          inputForm:{
              extra:{}
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
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/basic/sys/role/saveAuthorityList',qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
              this.$message(response.data.message);
              Object.assign(this.$data, this.getData());
              this.$router.push({name: 'roleList', query: util.getQuery("roleList"), params:{_closeFrom:true}})
            }).catch(function () {
                that.submitDisabled = false;
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
      },getTreeNode(id){
        if(id){
          axios.get('/api/basic/sys/role/getTreeNode',{params:{id:id}}).then((response)=>{
            this.inputForm=response.data
            this.treeData =response.data.extra.treeNode.children;
            this.checked = response.data.permissionIdList;
          })
        }
      }
    },created(){
      this.getTreeNode(this.$route.query.id);
    }
  }
</script>
