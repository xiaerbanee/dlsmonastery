<template>
  <div>
    <head-tab active="adPricesystemForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form-item label="名称" prop="name">
              <el-input v-model="inputForm.name"></el-input>
            </el-form-item>
            <el-form-item label="备注" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
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
        inputForm: {},
        submitData: {
          id: this.$route.query.id,
          name: '',
          remarks: '',
        },
        rules: {
          name: [{required: true, message: this.$t('officeForm.prerequisiteMessage')}],
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
            axios.post('/api/ws/future/basic/adPricesystem/save', qs.stringify(this.submitData)).then((response) => {
              if(response.data.success){
                this.$message(response.data.message);
                if (this.isCreate) {
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name: 'adPricesystemList', query: util.getQuery("adPricesystemList")})
                }
              }else {
                this.$message({
                  showClose: true,
                  message: response.data.message,
                  type: 'error'
                });
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          } else {
            this.submitDisabled = false;
          }
        })
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
      }
    }, created(){
      axios.get('/api/ws/future/basic/adPricesystem/findOne', {params: {id: this.$route.query.id}}).then((response) => {
        this.inputForm = response.data;
        this.checked = response.data.officeIdList;
      })
      axios.get('/api/basic/sys/office/getOfficeTree').then((response) => {
        this.treeData =response.data.children;
      })
    }
  }
</script>
