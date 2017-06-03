<template>
  <div>
    <head-tab active="accountAuthorityForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="7">
            <el-form-item label="账号名称" prop="id">
              <el-select v-model="inputForm.id" filterable :clearable=true remote placeholder="请输入关键字" :remote-method="remoteAccount" @change="getTreeCheckData(inputForm.id)" :loading="remoteLoading">
                <el-option v-for="item in accountList" :key="item.id" :label="item.loginName" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">保存</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="权限" prop="permissionIdStr">
              <el-tree
                :data="treeData"
                show-checkbox
                node-key="id"
                ref="tree"
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
      return {
        remoteLoading: false,
        isCreate: this.$route.query.id != null,
        submitDisabled: false,
        inputForm: {
          id: "",
          permissionIdList: ""
        },
        rules: {
          id: [{required: true, message: "必填属性"}],
        },
        accountList: [],
        treeData: [],
        checked: [],
        defaultProps: {
          label: 'label',
          children: 'children'
        }
      };
    },
    methods: {
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/basic/hr/account/saveAuthorityList', qs.stringify(this.inputForm, {allowDots:true})).then((response) => {
              this.$message(response.data.message);
              this.submitDisabled = false;
              if (this.isCreate) {
                form.resetFields();
              } else {
                this.$router.push({name: 'accountList', query: util.getQuery("accountList")})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          } else {
            this.submitDisabled = false;
          }
        })
      }, remoteAccount(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/basic/hr/account/searchFilter', {params: {loginName: query}}).then((response) => {
            this.accountList = response.data;
            this.remoteLoading = false;
          })
        } else {
          this.accountList = [];
        }
      }, handleCheckChange(data, checked, indeterminate) {
        var permissions = new Array()
        var check = this.$refs.tree.getCheckedKeys();
        for (var index in check) {
          if (check[index].match("\^(0|[1-9][0-9]*)$") && check[index] != 0) {
            permissions.push(check[index])
          }
        }
        this.inputForm.permissionIdList = permissions;
      },getTreeCheckData(id){
        axios.get('/api/basic/hr/account/getTreeCheckData', {params: {id: id}}).then((response) => {
          this.$refs.tree.setCheckedKeys(response.data);
          this.checked=response.data
        })
      },initPage() {
        axios.get('/api/basic/hr/account/getTreeNode').then((response) => {
          this.treeData = new Array(response.data);
        })
      }
    },created(){
      this.initPage();
    },activated () {
      if(!this.$route.query.headClick) {
        this.initPage();
      }
    }
  }
</script>
