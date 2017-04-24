<template>
  <div>
    <head-tab active="officeForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('officeForm.parentId')" prop="parentId">
          <el-select v-model="inputForm.parentId" filterable remote :placeholder="$t('officeForm.inputWord')" :remote-method="remoteOffice" :loading="remoteLoading" :clearable=true>
            <el-option v-for="office in offices" :key="office.id" :label="office.name" :value="office.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('officeForm.officeName')" prop="name">
          <el-input v-model="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('officeForm.type')" prop="type">
          <el-select v-model="inputForm.type" filterable >
            <el-option v-for="(key,value) in inputForm.officeTypeList" :key="value" :label="key" :value="value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('officeForm.jointType')" prop="jointType">
          <el-select v-model="inputForm.jointType" filterable >
            <el-option v-for="item in inputForm.jointTypeList" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('officeForm.point')" prop="point">
          <el-input v-model="inputForm.point"></el-input>
        </el-form-item>
        <el-form-item :label="$t('officeForm.taskPoint')" prop="taskPoint">
          <el-input v-model="inputForm.taskPoint"></el-input>
        </el-form-item>
        <el-form-item :label="$t('officeForm.sort')" prop="sort">
          <el-input v-model="inputForm.sort"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('officeForm.save')}}</el-button>
        </el-form-item>
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
        offices:[],
        inputForm:{},
        submitData:{
          id:this.$route.query.id,
          parentId:'',
          name:'',
          type:'',
          jointType:'',
          point:'',
          taskPoint:'',
          sort:''
        },
        rules: {
          name: [{ required: true, message: this.$t('officeForm.prerequisiteMessage')}],
          officeType: [{ required: true, message: this.$t('officeForm.prerequisiteMessage')}],
          jointType:[{ required: true, message: this.$t('officeForm.prerequisiteMessage')}]
        },
         remoteLoading:false
      };
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            util.copyValue(this.inputForm,this.submitData);
            axios.post('/api/basic/sys/office/save', qs.stringify(this.submitData)).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'officeList',query:util.getQuery("officeList")})
              }
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },remoteOffice(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/basic/sys/office/search',{params:{name:query}}).then((response)=>{
            this.offices=response.data;
            this.remoteLoading = false;
          })
        }
      }
    },created(){
      axios.get('/api/basic/sys/office/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm=response.data;
        console.log(this.inputForm)
        if(response.data.parentId!=null){
           this.offices=new Array({id:response.data.parentId,name:response.data.parentName})
        }
    })
    }
  }
</script>
