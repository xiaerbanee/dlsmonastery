<template>
  <div>
    <head-tab active="recruitEnumForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('recruitEnumForm.category')" prop="category">
          <el-select v-model="inputForm.category" filterable :placeholder="$t('recruitEnumForm.selectCategory')" @change="findValueByCategory(inputForm.category)">
            <el-option v-for="category in inputForm.extra.categoryList" :key="category" :label="category" :value="category" ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('recruitEnumForm.value')" prop="value" v-if="inputForm.category!='初试人'&&inputForm.category!='复试人'">
          <el-input type="textarea" :autosize="autosize" v-model="inputForm.value"></el-input>
        </el-form-item>
        <el-form-item :label="$t('recruitEnumForm.value')" prop="accountIdList" v-if="inputForm.category=='初试人'||inputForm.category=='复试人'">
          <account-select v-model="accountIdList" multiple></account-select>
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
          accountIdList:[],
          autosize:{ minRows: 2, maxRows: 8 },
          inputForm:{
            extra:{}
          },
          rules: {
            category: [{ required: true, message: this.$t('recruitEnumForm.prerequisiteMessage')}],
          }
        }
      },findValueByCategory(category){
        if(category){
          axios.get('/api/basic/hr/recruitEnum/findValueByCategory?category='+category).then((response)=> {
            console.log(response.data)
            if(response.data){
              if(this.inputForm.category == "初试人"||this.inputForm.category == "复试人"){
                this.accountIdList=response.data;
              }else {
                this.inputForm.value=response.data.join();
              }
            }
          })
        }
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            if(this.inputForm.category == "初试人"||this.inputForm.category == "复试人"){
              this.inputForm.value=this.accountIdList.join();
            }
            console.log(this.inputForm)
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
        });
      }
    },created () {
      this.initPage();
    }
  }
</script>
