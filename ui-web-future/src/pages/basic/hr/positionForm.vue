<template>
  <div>
    <head-tab active="positionForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter = "20">
          <el-col :span = "7">
            <el-form-item :label="$t('positionForm.jobId')" prop="jobId">
              <el-select v-model="inputForm.jobId" filterable >
                <el-option v-for="job in inputForm.jobList" :key="job.id" :label="job.name" :value="job.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('positionForm.name')" prop="name">
              <el-input v-model="inputForm.name"></el-input>
            </el-form-item>
            <el-form-item :label="项目权限" prop="backendIdList">
              <el-select v-model="inputForm.backendIdList" multiple filterable remote :placeholder="$t('positionForm.inputWord')" :remote-method="remoteBackend" :loading="remoteLoading">
                <el-option v-for="item in backendList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('positionForm.reportName')" prop="reportName">
              <el-input v-model="inputForm.reportName"></el-input>
            </el-form-item>
            <el-form-item :label="$t('positionForm.dataScope')" prop="dataScope">
              <el-select v-model="inputForm.dataScope" filterable >
                <el-option v-for="(value,key) in inputForm.dataScopeMap"  :key="key" :label="value" :value="key"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('positionForm.permission')" prop="permission">
              <el-input v-model="inputForm.permission"></el-input>
            </el-form-item>
            <el-form-item :label="$t('positionForm.sort')" prop="sort">
              <el-input v-model="inputForm.sort"></el-input>
            </el-form-item>
            <el-form-item :label="$t('positionForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('positionForm.save')}}</el-button>
            </el-form-item>
          </el-col>
          <el-col :span = "10">
            <el-form-item  :label="$t('positionForm.permissionIdStr')" prop="permissionIdStr">
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
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        inputForm:{},
        submitData:{
          id:this.$route.query.id,
          jobId:'',
          name:'',
          reportName:'',
          dataScope:'',
          permission:'',
          sort:'',
          remarks:'',
          permissionIdStr:""
        },
        rules: {
          jobId: [{ required: true, message: this.$t('positionForm.prerequisiteMessage')}],
          name: [{ required: true, message: this.$t('positionForm.prerequisiteMessage')}],
          dataScope: [{ required: true, message: this.$t('positionForm.prerequisiteMessage')}],
          permission: [{ required: true, message: this.$t('positionForm.prerequisiteMessage')}],
          sort: [{ required: true, message: this.$t('positionForm.prerequisiteMessage')}],
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
            axios.post('/api/basic/hr/position/save',qs.stringify(this.submitData)).then((response)=> {
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
      },
      handleCheckChange(data, checked, indeterminate) {
        var permissions=new Array()
        var check=this.$refs.tree.getCheckedKeys();
        for(var index in check){
          if(check[index].indexOf("p")!=0&& check[index].indexOf("m")!=0){
            permissions.push(check[index])
          }
        }
        this.inputForm.permissionIdStr=permissions.join();
      },remoteBackend(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/sys/backend/search',{params:{name:query}}).then((response)=>{
            this.backendList=response.data;
            this.remoteLoading = false;
        })
        } else {
          this.backendList = [];
        }
      }
    },created(){
      axios.get('/api/basic/hr/position/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm=response.data;
        this.treeData =new Array( response.data.permissionTree);
        this.checked = response.data.permissionTree.checked;
        this.inputForm.permissionIdStr = response.data.permissionTree.checked.join();
      })

    }
  }
</script>
