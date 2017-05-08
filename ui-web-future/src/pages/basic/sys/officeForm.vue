<template>
  <div>
    <head-tab active="officeForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form-item :label="$t('officeForm.parentId')" prop="parentId">
              <el-select v-model="inputForm.parentId" filterable remote :placeholder="$t('officeForm.inputWord')" :remote-method="remoteOffice" :loading="remoteLoading" :clearable=true>
                <el-option v-for="office in offices" :key="office.id" :label="office.name" :value="office.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="部门管理人" prop="leaderIdList">
              <el-select v-model="inputForm.leaderIdList" filterable remote multiple :placeholder="$t('officeForm.inputWord')" :remote-method="remoteOfficeLeader" :loading="remoteLoading" :clearable=true>
                <el-option v-for="item in accountList" :key="item.id" :label="item.loginName" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="类型" prop="type">
              <el-select v-model="inputForm.type" filterable @change="typeChange">
                <el-option v-for="item in inputForm.officeTypeList" :key="item" :label="$t('OfficeRuleEnum.'+item)"  :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('officeForm.officeName')" prop="name">
              <el-input v-model="inputForm.name"></el-input>
            </el-form-item>
            <el-form-item label="业务类型" prop="officeRuleId" v-show="isBusiness">
              <el-select v-model="inputForm.officeRuleId" filterable >
                <el-option v-for="item in inputForm.officeRuleList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('officeForm.jointType')" prop="jointType">
              <el-select v-model="inputForm.jointType" filterable>
                <el-option v-for="item in inputForm.jointTypeList" :key="item" :label="$t('JointTypeEnum.'+item)" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('officeForm.point')" prop="point">
              <el-input v-model="inputForm.point"></el-input>
            </el-form-item>
            <el-form-item :label="$t('officeForm.taskPoint')" prop="taskPoint">
              <el-input v-model="inputForm.taskPoint"></el-input>
            </el-form-item>
            <el-form-item :label="$t('officeForm.sort')" prop="sort">
              <el-input v-model="inputForm.sort"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('officeForm.save')}}
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span = "10" v-show="treeData.length>0">
            <el-form-item  label="授权" prop="businessOfficeStr">
              <el-tree
                :data="treeData"
                show-checkbox
                node-key="id"
                ref="tree"
                check-strictly
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
      return {
        isCreate: this.$route.query.id == null,
        submitDisabled: false,
        isBusiness:false,
        offices: [],
        accountList: [],
        inputForm: {},
        submitData: {
          id: this.$route.query.id,
          parentId: '',
          name: '',
          officeRuleId: '',
          type:"",
          jointType: '',
          point: '',
          taskPoint: '',
          sort: '',
          officeIdStr:"",
          leaderIdList:"",
        },
        rules: {
          name: [{required: true, message: this.$t('officeForm.prerequisiteMessage')}],
          officeType: [{required: true, message: this.$t('officeForm.prerequisiteMessage')}],
          jointType: [{required: true, message: this.$t('officeForm.prerequisiteMessage')}]
        },
        remoteLoading: false,
        treeData:[],
        checked:[],
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
            util.copyValue(this.inputForm, this.submitData);
            axios.post('/api/basic/sys/office/save', qs.stringify(this.submitData)).then((response) => {
              if(response.data.success){
                this.$message(response.data.message);
                if (this.isCreate) {
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name: 'officeList', query: util.getQuery("officeList")})
                }
              }else {
                this.$message({
                  showClose: true,
                  message: response.data.message,
                  type: 'error'
                });
              }
            });
          } else {
            this.submitDisabled = false;
          }
        })
      }, remoteOffice(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/basic/sys/office/search', {params: {name: query}}).then((response) => {
            this.offices = response.data;
            this.remoteLoading = false;
          })
        }
      }, remoteOfficeLeader(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/basic/hr/account/search', {params: {key: query}}).then((response) => {
            this.accountList = response.data;
            this.remoteLoading = false;
          })
        }
      },
      handleCheckChange(data, checked, indeterminate) {
        var officeIdList=new Array()
        var check=this.$refs.tree.getCheckedKeys();
        for(var index in check){
          if(check[index]!=0){
            officeIdList.push(check[index])
          }
        }
        this.inputForm.officeIdStr=officeIdList.join();
      },typeChange(evl){
          if(evl!=null&&evl=="SUPPORT"){
            this.isBusiness=false;
            axios.get('/api/basic/sys/office/getOfficeTree', {params: {id: this.inputForm.id}}).then((response) => {
              this.treeData =new Array(response.data);
              this.checked = response.data.checked;
              this.inputForm.permissionIdStr = response.data.checked.join();
            })
          }else {
            this.treeData =new Array();
            this.checked = new Array();
            this.inputForm.permissionIdStr = "";
            this.isBusiness=true;
          }
      }
    }, created(){
      axios.get('/api/basic/sys/office/findForm', {params: {id: this.$route.query.id}}).then((response) => {
        this.inputForm = response.data;
        if (response.data.parentId != null) {
          this.offices = new Array({id: response.data.parentId, name: response.data.parentName})
        }
        if(response.data.type =="SUPPORT" ){
            this.isBusiness=false;
        }else {
            this.isBusiness=true;
        }
        if (response.data.leaderIdList != null) {
            let officeLeaderList=new Array();
          for(var index in response.data.leaderIdList){
              officeLeaderList.push({id:response.data.leaderIdList[index],loginName:response.data.leaderNameList[index]});
          }
          this.accountList=officeLeaderList;
        }
      })
    }
  }
</script>
