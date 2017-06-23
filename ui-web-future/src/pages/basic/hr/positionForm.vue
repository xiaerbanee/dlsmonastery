<template>
  <div>
    <head-tab active="positionForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter = "20">
          <el-col :span = "7">
            <el-form-item :label="$t('positionForm.name')" prop="name">
              <el-input v-model="inputForm.name"></el-input>
            </el-form-item>
            <el-form-item label="绑定角色" prop="roleId">
              <el-select v-model="inputForm.roleId" filterable  :placeholder="$t('accountForm.inputWord')" >
                <el-option v-for="item in inputForm.extra.roleList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('positionForm.permission')" prop="permission">
              <el-input v-model="inputForm.permission"></el-input>
            </el-form-item>
            <el-form-item :label="$t('positionForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('positionForm.save')}}</el-button>
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
      return this.getData();
    },
    methods:{
      getData(){
        return {
          remoteLoading: false,
          isCreate: this.$route.query.id == null,
          submitDisabled: false,
          inputForm: {
            id: this.$route.query.id,
            extra: {}
          },
          roleList: [],
          rules: {
            name: [{required: true, message: this.$t('positionForm.prerequisiteMessage')}],
            permission: [{required: true, message: this.$t('positionForm.prerequisiteMessage')}],
          },
        };
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/basic/hr/position/save',qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
              this.$message(response.data.message);
              this.submitDisabled = false;
              if(!this.isCreate){
                this.$router.push({name:'positionList',query:util.getQuery("positionList")})
              }else {
                Object.assign(this.$data, this.getData());
                this.initPage();
              }
            }).catch( ()=> {
              that.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
      initPage() {
        axios.get('/api/basic/hr/position/getForm').then((response)=>{
          this.inputForm=response.data;
          axios.get('/api/basic/hr/position/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
          })
        })
      }
    },created () {
      this.initPage();
    }
  }
</script>
