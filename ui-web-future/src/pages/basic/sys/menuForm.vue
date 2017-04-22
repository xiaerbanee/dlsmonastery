<template>
  <div>
    <head-tab active="menuForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter = "20">
          <el-col :span="10">
            <el-form-item :label="$t('menuForm.menuCategory')" prop="menuCategoryId">
            <el-select v-model="inputForm.menuCategoryId" filterable :placeholder="$t('menuForm.selectGroup')">
              <el-option v-for="category in inputForm.menuCategoryList" :key="category.id" :label="category.name" :value="category.id"></el-option>
            </el-select>
            </el-form-item>
            <el-form-item :label="$t('menuForm.name')" prop="name">
              <el-input v-model="inputForm.name"></el-input>
            </el-form-item>
            <el-form-item :label="$t('menuForm.code')" prop="code">
              <el-input v-model="inputForm.code"></el-input>
            </el-form-item>
            <el-form-item :label="$t('menuForm.sort')" prop="sort">
              <el-input v-model="inputForm.sort"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item :label="$t('menuForm.mobile')" prop="mobile">
              <el-radio-group v-model="inputForm.mobile">
                <el-radio v-for="(value,key) in inputForm.boolMap" :key="key" :label="value">{{key | bool2str}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="$t('menuForm.visible')" prop="visible">
              <el-radio-group v-model="inputForm.visible">
                <el-radio v-for="(value,key) in inputForm.boolMap"  :key="key" :label="value">{{key | bool2str}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="$t('menuForm.permissionStr')" prop="permissionStr">
              <el-input v-model="inputForm.permissionStr" type="textarea":rows="5"></el-input>
            </el-form-item>
            <el-form-item :label="$t('menuForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('menuForm.save')}}</el-button>
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
              id:'',
              menuCategoryId:'',
              name:'',
              code:'',
              sort:'',
              mobile:true,
              visible:true,
              permissionStr:"",
              remarks:''
            },
            rules: {
              menuCategoryId: [{ required: true, message: this.$t('menuForm.prerequisiteMessage')}],
              name: [{ required: true, message: this.$t('menuForm.prerequisiteMessage')}],
              menuCode: [{ required: true, message: this.$t('menuForm.prerequisiteMessage')}],
              sort: [{ required: true, message: this.$t('menuForm.prerequisiteMessage')}],
            }
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              util.copyValue(this.inputForm,this.submitData);
              axios.post('/api/basic/sys/menu/save',qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'menuList',query:util.getQuery("menuList")})
                }
              });
            }else{
              this.submitDisabled = false;
            }
          })
        }
      },created(){
        axios.get('/api/basic/sys/menu/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm = response.data;
          this.inputForm.mobile = response.data.mobile?"1":"0";
          this.inputForm.visible = response.data.visible?"1":"0";
        })
      }
    }
</script>
