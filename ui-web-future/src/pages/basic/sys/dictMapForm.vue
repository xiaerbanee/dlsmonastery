<template>
  <div>
    <head-tab active="dictMapForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('dictMapForm.category')" prop="category">
          <el-select v-model="inputForm.category" filterable :placeholder="$t('dictMapForm.selectCategory')">
            <el-option v-for="category in inputProperty.categoryList" :key="category" :label="category" :value="category"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('dictMapForm.name')" prop="name">
          <el-input v-model="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('dictMapForm.value')" prop="value">
          <el-input v-model="inputForm.value"></el-input>
        </el-form-item>
        <el-form-item :label="$t('dictMapForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"  :disabled="submitDisabled" @click="formSubmit()">{{$t('dictMapForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
    export default{
      data(){
        return this.getData()
      },
      methods:{
        getData() {
          return{
            isInit:false,
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            inputForm:{},
            inputProperty:{},
            submitData:{
              id:'',
              category:'',
              name:'',
              value:'',
              remarks:''
            },
            rules: {
              category: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
              name: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
              value: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}]
            }
          }
        },
        formSubmit(){
          var that = this;
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
                util.copyValue(this.inputForm,this.submitData);
              axios.post('/api/basic/sys/dictMap/save', qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                Object.assign(this.$data, this.getData());
                if(!this.isCreate){
                  this.$router.push({name:'dictMapList',query:util.getQuery("dictMapList")})
                }
              }).catch(function () {
                that.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        }
      },activated () {
        if(!this.$route.query.headClick || !this.isInit) {
          Object.assign(this.$data,this.getData());
          axios.get('/api/basic/sys/dictMap/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            this.inputForm = response.data;
          })
          axios.get('/api/basic/sys/dictMap/getForm',{params: {id:this.$route.query.id}}).then((response)=>{
            this.inputProperty = response.data;
          })
        }
        this.isInit = true;
      }
    }
</script>
