<template>
  <div>
    <head-tab active="processTypeForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('processTypeForm.name')" prop="name">
          <el-input v-model="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('processTypeForm.createdPositionIds')" prop="createdPositionIds">
          <position-select v-model="inputForm.createdPositionIdList" :multiple = "true"></position-select>
        </el-form-item>
        <el-form-item :label="$t('processTypeForm.viewPositionIds')" prop="viewPositionIds">
          <position-select v-model="inputForm.viewPositionIdList" :multiple="true"></position-select>
        </el-form-item>
        <el-form-item :label="$t('processTypeForm.auditFileType')" prop="auditFileType">
          <bool-radio-group v-model="inputForm.auditFileType"></bool-radio-group>
        </el-form-item>
        <el-form-item :label="$t('processTypeForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()" v-if="isCreate">{{$t('processTypeForm.save')}}</el-button>
        </el-form-item>
        <template>
          <el-table :data="inputForm.processFlowList" border stripe>
            <el-table-column :label="$t('processTypeForm.name')">
              <template scope="scope">
                <el-input v-model="scope.row.name"></el-input>
              </template>
            </el-table-column>
            <el-table-column :label="$t('processTypeForm.sort')">
              <template scope="scope">
                <el-input v-model="scope.row.sort"></el-input>
              </template>
            </el-table-column>
            <el-table-column :label="$t('processTypeForm.positionName')">
              <template scope="scope">
                <position-select v-model="scope.row.positionId" ></position-select>
              </template>
            </el-table-column>
            <el-table-column :label="$t('processTypeForm.operation')" :render-header="renderAction"  v-if="isCreate">
              <template scope="scope">
                <el-button size="small" type="danger" @click.prevent="removeDomain(scope.row)">{{$t('processTypeForm.delete')}}</el-button>
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
          submitDisabled:false,
          loading: false,
          inputForm:{
              extra:{},
            processFlowList:[],
          },
          rules: {
            name: [{ required: true, message: this.$t('processTypeForm.prerequisiteMessage')}],
            auditFileType: [{ required: true, message: this.$t('processTypeForm.prerequisiteMessage')}],
            createdPositionIdList: [{ required: true, message: this.$t('processTypeForm.prerequisiteMessage')}],
            viewPositionIdList: [{ required: true, message: this.$t('processTypeForm.prerequisiteMessage')}],
          }
        }
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            var processFlowList=new Array();
            for(var index in this.inputForm.processFlowList){
              if(this.inputForm.processFlowList[index].name){
                processFlowList.push(this.inputForm.processFlowList[index])
              }
            }
            if(processFlowList.length==0){
              this.$message.error('请设置流程节点');
            }
            var submitData=util.deleteExtra(this.inputForm);
            submitData.processFlowList=processFlowList;
            axios.post('/api/general/sys/processType/save', qs.stringify(submitData, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                Object.assign(this.$data,this.getData());
                this.initPage();
              }else{
                this.submitDisabled = false;
                this.$router.push({name:'processTypeList',query:util.getQuery("processTypeList")});
              }
            }).catch(function () {
              that.submitDisabled = false;
            });
          } else {
            this.submitDisabled = false;
          }
        })
      },removeDomain(item) {
        var index = this.inputForm.processFlowList.indexOf(item)
        if (index !== -1) {
        this.inputForm.processFlowList.splice(index, 1)
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
        if(this.inputForm.processFlowList.length>0 && this.inputForm.processFlowList[this.inputForm.processFlowList.length-1].sort != null) {
          sort = this.inputForm.processFlowList[this.inputForm.processFlowList.length-1].sort + 10;
        }
        this.inputForm.processFlowList.push({name:"",sort:sort,positionId:""});
      },initPage(){
          if(this.isCreate){
            for(var i = 0;i<3;i++) {
              this.inputForm.processFlowList.push({name:"",sort:(i+1)*10,positionId:""});
            }
          } else {
            axios.get('/api/general/sys/processType/getForm',{params: {id:this.$route.query.id}}).then((response)=>{
              this.inputForm=response.data;
              console.log(response.data);
              axios.get('/api/general/sys/processType/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
                util.copyValue(response.data,this.inputForm);
              });
            });
          }
      }
    },created(){
      this.initPage();
    }
  }
</script>
