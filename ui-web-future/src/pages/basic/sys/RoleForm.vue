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
            <el-form-item  label="授权" prop="moduleIdList">
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
          moduleIdList:""
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
              form.resetFields();
              this.submitDisabled = false;
              if(!this.isCreate){
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
          if (check[index].match("\^(0|[1-9][0-9]*)$") && check[index] != 0) {
            modules.push(check[index])
          }
        }
        this.inputForm.moduleIdList=modules;
      },initPage() {
        axios.get('/api/basic/sys/role/findOne', {params: {id: this.$route.query.id}}).then((response) => {
          this.inputForm = response.data;
        })
        axios.get('/api/basic/sys/role/getForm', {params: {id: this.$route.query.id}}).then((response) => {
          this.treeData = new Array(response.data.treeNode);
          this.checked = response.data.moduleIdList;
        })
      }
    },activated () {
      if(!this.$route.query.headClick) {
        this.initPage();
      }
    }
  }
</script>
