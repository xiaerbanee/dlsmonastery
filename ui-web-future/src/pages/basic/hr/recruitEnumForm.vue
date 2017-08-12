<template>
  <div>
    <head-tab active="recruitEnumForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('recruitEnumForm.category')" prop="category">
          <el-select v-model="inputForm.category" filterable :placeholder="$t('recruitEnumForm.selectCategory')">
            <el-option v-for="category in inputForm.extra.categoryList" :key="category" :label="category" :value="category" @change="findValueByCategory(inputForm.category)"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('recruitEnumForm.value')" prop="value" v-if="inputForm.category!='初试人'&&inputForm.category!='复试人'">
          <el-input type="textarea" v-model="inputForm.value"></el-input>
        </el-form-item>
        <el-form-item :label="$t('recruitEnumForm.value')" prop="value" v-if="inputForm.category=='初试人'||inputForm.category=='复试人'">
          <account-select v-model="inputForm.value" multiple></account-select>
        </el-form-item>
        <el-form-item :label="$t('recruitEnumForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('recruitEnumForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import accountSelect from "components/basic/account-select";
  export default{
    components:{
      accountSelect
    },
    data(){
      return this.getData()
    },
    methods:{
      getData() {
        return{
          isCreate:this.$route.query.id==null,
          submitDisabled:false,
          inputForm:{
            extra:{}
          },
          rules: {
            category: [{ required: true, message: this.$t('recruitEnumForm.prerequisiteMessage'),trigger:"blur"}],
          }
        }
      },findValueByCategory(category){
        if(category){

        }
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            if(this.inputForm.category == "初试人"||this.inputForm.category == "复试人"){
              this.inputForm.value=this.inputForm.value.join();
            }
            axios.post('/api/basic/hr/recruitEnum/save', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                Object.assign(this.$data, this.getData());
                this.initPage();
              }else{
                this.$router.push({name:'recruitEnumList',query:util.getQuery("recruitEnumList"), params:{_closeFrom:true}})
              }
            }).catch(function () {
              that.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },initPage(){
        axios.get('/api/basic/hr/recruitEnum/getForm').then((response)=>{
          this.inputForm = response.data;
          axios.get('/api/basic/hr/recruitEnum/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
          });
        });
      }
    },created () {
      this.initPage();
    }
  }
</script>
