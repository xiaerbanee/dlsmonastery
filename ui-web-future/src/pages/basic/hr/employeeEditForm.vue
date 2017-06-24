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
            <el-form-item :label="$t('employeeEditForm.outId')" prop="outId">
              <el-input v-model="accountForm.outId"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeEditForm.outPassword')" prop="outPassword">
              <el-input  v-model="accountForm.outPassword" type="password"></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeEditForm.remarks')" prop="remarks">
              <el-input  v-model="employeeForm.remarks"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('employeeEditForm.name')" prop="name">
              <el-input  v-model="employeeForm.name" ></el-input>
            </el-form-item>
            <el-form-item :label="$t('employeeEditForm.sexLabel')" prop="sexLabel">
              <el-radio-group v-model="employeeForm.sexLabel">
                <el-radio :label="1">{{$t('employeeEditForm.man')}}</el-radio>
                <el-radio :label="0">{{$t('employeeEditForm.women')}}</el-radio>
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
                <el-option v-for="item in formProperty.educationsList" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('employeeEditForm.image')" prop="image">
              <el-upload action="/api/basic/sys/folderFile/upload?uploadPath=/员工管理" :on-change="handleChange" :on-remove="handleRemove" :on-preview="handlePreview" :file-list="fileList" list-type="picture" >
                <el-button size="small" type="primary">{{$t('employeeEditForm.clickUpload')}}</el-button>
                <div slot="tip" class="el-upload__tip">{{$t('employeeEditForm.uploadImageSizeFor5000KB')}}</div>
              </el-upload>
            </el-form-item>
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
          isInit:false,
          isCreate:this.$route.query.id==null,
          submitDisabled:false,
          formProperty:{},
          employeeForm:{
            id:"",
            bankName:'',
            bankItem:'',
            remarks:'',
            name:'',
            sex:'',
            sexLabel:'',
            birthday:'',
            school:'',
            major:'',
            education:'',
            image:''
          },
          accountForm:{
            id:"",
            loginName:'',
            password:'',
            outId:'',
            outPassword:'',
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
            this.employeeForm.sex=this.employeeForm.sexLabel==1?"男":"女";
              axios.post('/api/basic/hr/employee/save', qs.stringify(this.employeeForm)).then((response)=> {
                  this.$message("员工"+response.data.message);
                  axios.post('/api/basic/hr/account/save', qs.stringify(this.accountForm)).then((response)=> {
                  this.$message("账户"+response.data.message);
                  });
                Object.assign(this.$data, this.getData());
               this.$router.push({name:'employeeList',query:util.getQuery("employeeList")})
              }).catch(function () {
                that.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },getForm(){
          axios.get('/api/basic/hr/employee/editForm').then((response)=>{
              this.formProperty=response.data
          })
      },handlePreview(file) {
        window.open(file.url);
      },handleChange(file, fileList) {
        this.fileList = fileList;
      },handleRemove(file, fileList) {
        this.fileList = fileList;
      }
    },activated () {
      if(!this.$route.query.headClick || this.isInit) {
        this.getForm();
        if(!this.isCreate){
          axios.get('/api/basic/hr/employee/getForm',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.employeeForm);
            util.copyValue(response.data.account,this.accountForm);
            this.employeeForm.sexLabel=response.data.sex=="男"?1:0;
            if(this.employeeForm.image !=null) {
              axios.get('/api/basic/sys/folderFile/findByIds',{params: {ids:this.employeeForm.image}}).then((response)=>{
                this.fileList= response.data;
              });
            }
          })
        }
      }
      this.isInit = true;
    }
  }
</script>
