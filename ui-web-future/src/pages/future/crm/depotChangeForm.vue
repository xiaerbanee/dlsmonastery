<template>
  <div>
    <head-tab active="depotChangeForm"></head-tab>
   <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('depotChangeForm.depotName')" prop="depotId">
              <el-select v-model="inputForm.depotId" filterable remote :placeholder="$t('depotChangeForm.inputWord')" :remote-method="remoteDepot" :loading="remoteLoading" :clearable=true @change="getDepot(inputForm.depotId)">
                <el-option v-for="depot in depots" :key="depot.id" :label="depot.name" :value="depot.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('depotChangeForm.type')" prop="type">
              <el-select v-model="inputForm.type" filterable clearable :placeholder="$t('depotChangeForm.selectGroup')" @change="getOldValue">
                <el-option v-for="item in formProperty.types" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('depotChangeForm.expiryDate')" prop="expiryDate">
              <el-date-picker v-model="inputForm.expiryDate" type="date" :placeholder="$t('depotChangeForm.selectDate')"></el-date-picker>
            </el-form-item>
            <el-form-item :label="$t('depotChangeForm.oldValue')" prop="oldValue">
              <el-input v-model="inputForm.oldValue" readonly></el-input>
            </el-form-item>
            <el-form-item v-show="inputForm.type=='名称' ||inputForm.type=='信用额度'" :label="$t('depotChangeForm.newValue')" prop="newValue">
              <el-input v-model="inputForm.newValue" ></el-input>
            </el-form-item>
            <el-form-item v-show="inputForm.type=='有无导购' || inputForm.type=='是否让利'" :label="$t('depotChangeForm.newValue')" prop="newValue">
              <el-select v-model="inputForm.newValue"  clearable :placeholder="$t('depotChangeForm.inputKey')">
                <el-option v-for="(value,key) in formProperty.bools" :key="key" :label="value | bool2str" :value="key"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item v-show="inputForm.type=='价格体系'" :label="$t('depotChangeForm.newValue')" prop="newValue" >
              <el-select v-model="inputForm.newValue" filterable :placeholder="$t('depotChangeForm.selectGroup')">
                <el-option v-for="item in formProperty.pricesystems" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('depotChangeForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary"  :disabled="submitDisabled" @click="formSubmit()">{{$t('depotChangeForm.save')}}</el-button>
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
        formProperty:{},
        depot:{},
        depots:[],
        inputForm:{
          id:'',
          type:'',
          depotId:"",
          expiryDate:'',
          oldValue:'',
          newValue:'',
          remarks:''
        },
        rules: {
          depotId: [{ required: true, message: this.$t('depotChangeForm.prerequisiteMessage')}],
          type: [{ required: true, message: this.$t('depotChangeForm.prerequisiteMessage')}],
          newValue: [{ required: true, message: this.$t('depotChangeForm.prerequisiteMessage')}],
        },
        remoteLoading:false
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        this.inputForm.expiryDate=util.formatLocalDate( this.inputForm.expiryDate)
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/crm/depotChange/save', qs.stringify(this.inputForm)).then((response)=> {
              if(response.data.message){
                this.$message(response.data.message);
              }
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'depotChangeList',query:util.getQuery("depotChangeList")})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },remoteDepot(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/crm/depot/search', {params: {name: query}}).then((response)=> {
            this.depots = response.data;
            this.remoteLoading = false;
          })
        }
      },getDepot(id){
        if(id){
          axios.get('/api/crm/depot/findOne',{params: {id:id}}).then((response)=>{
            this.depot = response.data;
          })
        }
      },getOldValue(){
        if(this.inputForm.type == "价格体系"){
          this.inputForm.oldValue = this.depot.pricesystem.name;
        }else if(this.inputForm.type == "名称"){
          this.inputForm.oldValue = this.depot.name;
        }else if(this.inputForm.type == "有无导购"){
          this.inputForm.oldValue = this.depot.hasGuide?"是":"否";
        }else if(this.inputForm.type == "是否让利"){
          this.inputForm.oldValue = this.depot.rebate?"是":"否";
        }else if(this.inputForm.type == "信用额度"){
          this.inputForm.oldValue = this.depot.credit;
        }
      }
    },created(){
      axios.get('/api/crm/depotChange/getFormProperty').then((response)=>{
        this.formProperty = response.data;
      });
      if(!this.isCreate){
        axios.get('/api/crm/depotChange/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          util.copyValue(response.data,this.inputForm);
          this.depots = new Array(response.data.depot)
        })
      }
    }
  }
</script>
