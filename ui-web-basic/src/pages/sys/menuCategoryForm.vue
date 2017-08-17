<template>
  <div>
    <head-tab active="menuCategoryForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item label="所属模块" prop="backendModuleId">
          <el-select v-model="inputForm.backendModuleId" clearable>
            <el-option v-for="item in inputForm.extra.backendModuleList" :key="item.id" :label="$t('menus.backendModule.'+item.code)" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('menuCategoryForm.name')" prop="name">
          <el-input v-model.number="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item label="Code" prop="code">
          <el-input v-model="inputForm.code"></el-input>
        </el-form-item>
        <el-form-item :label="$t('menuCategoryForm.sort')"  prop="sort">
          <el-input v-model.number="inputForm.sort"></el-input>
        </el-form-item>
        <el-form-item :label="$t('menuCategoryForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('menuCategoryForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
    export default{
      data:function () {
        return this.getData();
      },
      methods:{
        getData(){
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            inputForm:{
                extra:{}
            },
            rules: {
              backendModuleId: [{ required: true, message: this.$t('menuCategoryForm.prerequisiteMessage')}],
              name: [{ required: true, message: this.$t('menuCategoryForm.prerequisiteMessage')}],
              code: [{ required: true, message: this.$t('menuCategoryForm.prerequisiteMessage')}],
              sort: [{ required: true, message: this.$t('menuCategoryForm.prerequisiteMessage')},{ type: 'number', message: this.$t('menuCategoryForm.inputLegalValue')}]
            }
          }
        },
        formSubmit(){
          var that = this;
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/basic/sys/menuCategory/save', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  Object.assign(this.$data, this.getData());
                  this.initPage();
                }else{
                  this.$router.push({name:'menuCategoryList',query:util.getQuery("menuCategoryList"),params:{_closeFrom:true}})
                }
              }).catch( ()=> {
                that.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },initPage(){
          axios.get('/api/basic/sys/menuCategory/getForm').then((response)=>{
            this.inputForm = response.data;
          axios.get('/api/basic/sys/menuCategory/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
            })
          })
        }
      },created(){
        this.initPage();
      }
    }
</script>
