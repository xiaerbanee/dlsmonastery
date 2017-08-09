<template>
  <div>
    <head-tab active="employeeEditForm"></head-tab>
    <div>
      <el-form :model="employeeForm" ref="employeeForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item :label="$t('employeeEditForm.loginName')" prop="loginName">
              <el-input v-model="accountForm.loginName" readonly></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeEditForm.password')" prop="password">
              <el-input v-model="accountForm.password"  type="password"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeEditForm.confirmPassword')" prop="confirmPassword">
              <el-input v-model="confirmPassword" type="password"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeEditForm.bankName')" prop="bankName">
              <el-input v-model="employeeForm.bankName"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeEditForm.bankItem')" prop="bankItem">
              <el-input v-model="employeeForm.bankItem"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeEditForm.remarks')" prop="remarks">
              <el-input  v-model="employeeForm.remarks"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('employeeEditForm.name')" prop="name">
              <el-input  v-model="employeeForm.name" ></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeEditForm.sexLabel')" prop="sex">
              <el-radio-group v-model="employeeForm.sex">
                <el-radio :label="$t('employeeEditForm.man')">{{$t('employeeEditForm.man')}}</el-radio>
                <el-radio :label="$t('employeeEditForm.women')">{{$t('employeeEditForm.women')}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="$t('employeeEditForm.birthday')" prop="birthday">
              <el-input v-model="employeeForm.birthday"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeEditForm.school')" prop="school">
              <el-input v-model="employeeForm.school"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeEditForm.major')" prop="major">
              <el-input  v-model="employeeForm.major"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeEditForm.education')" prop="education">
              <el-select v-model="employeeForm.education"  clearable >
                <el-option v-for="item in employeeForm.extra.educationList" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <!--<el-form-item :label="$t('employeeEditForm.image')" prop="image">-->
              <!--<el-upload action="/api/general/sys/folderFile/upload?uploadPath=/员工管理" :on-change="handleChange" :on-remove="handleRemove" :on-preview="handlePreview" :file-list="fileList" list-type="picture">-->
                <!--<el-button size="small" type="primary">{{$t('employeeEditForm.clickUpload')}}</el-button>-->
                <!--<div slot="tip" class="el-upload__tip">{{$t('employeeEditForm.uploadImageSizeFor5000KB')}}</div>-->
              <!--</el-upload>-->
            <!--</el-form-item>-->
            <el-form-item>
              <el-button type="primary"  :disabled="submitDisabled" @click="formSubmit()">{{$t('employeeEditForm.save')}}</el-button>
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
        var validateConfigPass = (rule, value, callback) => {
          var password=this.accountForm.password;
          var configPassword=this.confirmPassword;
          if (password&&configPassword == '') {
            callback(new Error('请再次输入密码'));
          } else if (password&&configPassword !== password) {
            callback(new Error('两次输入密码不一致!'));
          } else {
            callback();
          }
        };
        return{
          isCreate:this.$route.query.id==null,
          submitDisabled:false,
          employeeForm:{
              extra:[],
          },
          accountForm:{
            extra:[],
          },
          rules: {
            confirmPassword: [{ validator: validateConfigPass, trigger: 'blur' }],
          },
          confirmPassword:"",
          fileList:[],
        }
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["employeeForm"];
        form.validate((valid) => {
          if (valid) {
            this.employeeForm.image = util.getFolderFileIdStr(this.fileList);
            this.employeeForm.accountForm=util.deleteExtra(this.accountForm)
            axios.post('/api/basic/hr/employee/save', qs.stringify(this.employeeForm, {allowDots: true})).then((response)=> {
              this.$message("员工"+response.data.message);
                 Object.assign(this.$data, this.getData());
                  this.$router.push({name:'home', params:{_closeFrom:true}})
              }).catch(function () {
                that.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },handlePreview(file) {
        window.open(file.url);
      },handleChange(file, fileList) {
        this.fileList = fileList;
      },handleRemove(file, fileList) {
        this.fileList = fileList;
      }
    },created () {
      axios.get('/api/basic/hr/employee/getForm').then((response)=>{
        this.employeeForm = response.data;
        axios.get('/api/basic/hr/account/getForm').then((response)=>{
          this.accountForm = response.data;
          axios.get('/api/basic/hr/employee/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            console.log("employee",response.data);
            util.copyValue(response.data, this.employeeForm);
            axios.get('/api/basic/hr/account/findOne',{params: {id:this.employeeForm.accountId}}).then((response)=>{
              console.log("account",response.data);
              util.copyValue(response.data, this.accountForm);
            })
            if(this.employeeForm.image !=null) {
              axios.get('/api/general/sys/folderFile/findByIds',{params: {ids:this.employeeForm.image}}).then((response)=>{
                this.fileList= response.data;
              });
            }
          })
        })
      });
    }
  }
</script>
