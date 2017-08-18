<template>
  <div>
    <head-tab active="officeForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
            <el-form-item :label="$t('officeForm.parentId')" prop="parentId">
              <office-select v-model="inputForm.parentId" ></office-select>
            </el-form-item>
            <el-form-item :label="$t('officeForm.officeName')" prop="name">
              <el-input v-model="inputForm.name"></el-input>
            </el-form-item>
            <el-form-item label="部门管理人" prop="leaderIdList">
              <account-select v-model="inputForm.leaderIdList" :multiple="true"></account-select>
            </el-form-item>
            <el-form-item label="业务类型" prop="officeRuleId">
              <el-select v-model="inputForm.officeRuleId" filterable >
                <el-option v-for="item in inputForm.extra.officeRuleList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('officeForm.jointType')" prop="jointType">
              <el-select v-model="inputForm.jointType" filterable>
                <el-option v-for="item in inputForm.extra.jointTypeList" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="机构级别" prop="jointLevel">
              <el-select v-model="inputForm.jointLevel" filterable>
                <el-option v-for="item in inputForm.extra.jointLevelList" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('officeForm.point')" prop="point" >
              <el-input v-model="inputForm.point"></el-input>
            </el-form-item>
            <el-form-item :label="$t('officeForm.taskPoint')" prop="taskPoint" >
              <el-input v-model="inputForm.taskPoint"></el-input>
            </el-form-item>
            <el-form-item :label="$t('officeForm.sort')" prop="sort">
              <el-input v-model="inputForm.sort"></el-input>
            </el-form-item>
           <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('officeForm.save')}}
              </el-button>
            </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import accountSelect from 'components/basic/account-select'
  import boolRadioGroup from 'components/common/bool-radio-group'
  import officeSelect from 'components/basic/office-select'
  export default{
    components:{
      officeSelect,
      accountSelect,
      boolRadioGroup
    },
    data:function () {
      return this.getData();
    },
    methods: {
      getData(){
        return {
          isCreate: this.$route.query.id == null,
          multiple:true,
          submitDisabled: false,
          isBusiness:false,
          inputForm: {
              extra:{}
          },
          rules: {
            name: [{required: true, message: this.$t('officeForm.prerequisiteMessage')}],
            officeRuleId: [{required: true, message: this.$t('officeForm.prerequisiteMessage')}],
            jointLevel: [{required: true, message: this.$t('officeForm.prerequisiteMessage')}],
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
      formSubmit(){
        var that=this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.inputForm.type='业务部门'
            axios.post('/api/basic/sys/office/save', qs.stringify(util.deleteExtra(this.inputForm))).then((response) => {
              if(response.data.success){
                this.$message(response.data.message);
                if (this.isCreate) {
                  Object.assign(this.$data, this.getData());
                  this.initPage();
                }else {
                    util.closeAndBackToPage(this.$router,"officeList")
                }
              }else {
                that.submitDisabled = false;
                this.$message({
                  showClose: true,
                  message: response.data.message,
                  type: 'error'
                });
              }
            }).catch( ()=> {
              that.submitDisabled = false;
            });
          } else {
            this.submitDisabled = false;
          }
        })
      }, initPage(){
        axios.get('/api/basic/sys/office/getForm').then((response) => {
          this.inputForm = response.data;
          axios.get('/api/basic/sys/office/findOne', {params: {id: this.$route.query.id}}).then((response) => {
            util.copyValue(response.data,this.inputForm);
          })
        })
      }
    },created () {
      this.initPage();
    }
  }
</script>
