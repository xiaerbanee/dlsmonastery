<template>
  <div>
    <head-tab active="salaryTemplateForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('salaryTemplateForm.name')" prop="name">
          <el-input v-model="inputForm.name" :readonly="!isCreate"></el-input>
        </el-form-item>
        <el-form-item :label="$t('salaryTemplateForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks" :readonly="!editable&&!isCreate"></el-input>
        </el-form-item>
        <el-form-item >
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('salaryTemplateForm.save')}}</el-button>
        </el-form-item>
        <template>
          <el-table :data="inputForm.salaryTemplateDetailList" border stripe>
            <el-table-column :label="$t('salaryTemplateForm.productName')" >
              <template scope="scope">
                <el-input v-model="scope.row.name"></el-input>
              </template>
            </el-table-column>
            <el-table-column :label="$t('salaryTemplateForm.sort')" >
              <template scope="scope">
                <el-input v-model="scope.row.sort"></el-input>
              </template>
            </el-table-column>
            <el-table-column :label="$t('salaryTemplateForm.operation')" :render-header="renderAction" >
              <template scope="scope">
                <el-button size="small" type="danger" @click.prevent="removeDomain(scope.row)">{{$t('salaryTemplateForm.delete')}}</el-button>
              </template>
            </el-table-column>
          </el-table>
        </template>
      </el-form>
    </div>
  </div>
</template>
<script>
  import positionSelect from 'components/basic/position-select'
  import boolRadioGroup from 'components/common/bool-radio-group'
  export default{
    components:{positionSelect,boolRadioGroup},
    data:function () {
      return this.getData();
    },
    methods:{
      getData(){
        return{
          isCreate:this.$route.query.id==null,
          editable: this.$route.query.editable,
          submitDisabled:false,
          loading: false,
          inputForm:{
            extra:{},
            salaryTemplateDetailList:[],
          },
          rules: {
            name: [{ required: true, message: this.$t('salaryTemplateForm.prerequisiteMessage')}],
          }
        }
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            var salaryTemplateDetailList=new Array();
            for(var index in this.inputForm.salaryTemplateDetailList){
              if(this.inputForm.salaryTemplateDetailList[index].name){
                salaryTemplateDetailList.push(this.inputForm.salaryTemplateDetailList[index])
              }
            }
            if(salaryTemplateDetailList.length==0){
              this.$message.error('请设置流程节点');
            }
            var submitData=util.deleteExtra(this.inputForm);
            submitData.salaryTemplateDetailList=salaryTemplateDetailList;
            axios.post('/api/basic/salary/salaryTemplate/save', qs.stringify(submitData, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                Object.assign(this.$data,this.getData());
                this.initPage();
              }else{
                this.submitDisabled = false;
                this.$router.push({name:'salaryTemplateList',query:util.getQuery("salaryTemplateList")});
              }
            }).catch(function () {
              that.submitDisabled = false;
            });
          } else {
            this.submitDisabled = false;
          }
        })
      },removeDomain(item) {
        var index = this.inputForm.salaryTemplateDetailList.indexOf(item)
        if (index !== -1) {
          this.inputForm.salaryTemplateDetailList.splice(index, 1)
        }
      },renderAction(createElement) {
        return createElement(
          'a',{
            attrs: {
              class: 'el-button el-button--primary el-button--small'
            }, domProps: {
              innerHTML: '增加'
            },on: {
              click: this.addDomain
            }
          }
        );
      },addDomain(){
        var sort = 10;
        if(this.inputForm.salaryTemplateDetailList.length>0 && this.inputForm.salaryTemplateDetailList[this.inputForm.salaryTemplateDetailList.length-1].sort != null) {
          sort = this.inputForm.salaryTemplateDetailList[this.inputForm.salaryTemplateDetailList.length-1].sort + 10;
        }
        this.inputForm.salaryTemplateDetailList.push({name:"",sort:sort,positionId:""});
      },initPage(){
        if(this.isCreate){
          for(var i = 0;i<3;i++) {
            this.inputForm.salaryTemplateDetailList.push({name:"",sort:(i+1)*10,positionId:""});
          }
        } else {
          axios.get('/api/basic/salary/salaryTemplate/getForm',{params: {id:this.$route.query.id}}).then((response)=>{
            axios.get('/api/basic/salary/salaryTemplate/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
              console.log(response.data,"findOne");
              this.inputForm=response.data;
            });
          });
        }
      }
    },created(){
      this.initPage();
    }
  }
</script>
