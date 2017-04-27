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
            <el-form-item :label="$t('officeForm.officeName')" prop="name">
              <el-input v-model="inputForm.name"></el-input>
            </el-form-item>
            <el-form-item :label="$t('officeForm.type')" prop="type">
              <el-select v-model="inputForm.type" filterable @change="typeChange">
                <el-option v-for="item in inputForm.officeTypeList" :key="item.value" :label="item.name" :value="item.value |toString "></el-option>
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
                check-strictly="true"
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
        offices: [],
        inputForm: {},
        submitData: {
          id: this.$route.query.id,
          parentId: '',
          name: '',
          type: '',
          jointType: '',
          point: '',
          taskPoint: '',
          sort: '',
          officeIdStr:""
        },
        rules: {
          name: [{required: true, message: this.$t('officeForm.prerequisiteMessage')}],
          officeType: [{required: true, message: this.$t('officeForm.prerequisiteMessage')}],
          jointType: [{required: true, message: this.$t('officeForm.prerequisiteMessage')}]
        },
        remoteLoading: false,
        officeTypeMap:new Map(),
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
              this.$message(response.data.message);
              if (this.isCreate) {
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name: 'officeList', query: util.getQuery("officeList")})
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
          var officeType=this.officeTypeMap.get(evl);
          if(officeType!=null&&officeType.typeLabel=="后勤部门"){
            axios.get('/api/basic/sys/office/getOfficeTree', {params: {id: this.inputForm.id}}).then((response) => {
              this.treeData =new Array(response.data);
              this.checked = response.data.checked;
              this.inputForm.permissionIdStr = response.data.checked.join();
            })
          }else {
            this.treeData =new Array();
            this.checked = new Array();
            this.inputForm.permissionIdStr = "";
          }
      }
    }, created(){
      axios.get('/api/basic/sys/office/findForm', {params: {id: this.$route.query.id}}).then((response) => {
        this.inputForm = response.data;
        console.log(response.data)
        if (response.data.parentId != null) {
          this.offices = new Array({id: response.data.parentId, name: response.data.parentName})
        }
        if(this.inputForm.officeTypeList!=null&&this.inputForm.officeTypeList.length>0){
            for(var index in this.inputForm.officeTypeList){
              var officeType=this.inputForm.officeTypeList[index];
              this.officeTypeMap.set(officeType.value.toString(),officeType);
            }
        }
      })
    }
  }
</script>
