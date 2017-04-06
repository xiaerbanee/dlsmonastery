<template>
  <div>
    <head-tab :active="$t('processTypeForm.processTypeForm') "></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('processTypeForm.name')" prop="name">
          <el-input v-model="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('processTypeForm.createPermissionId')" prop="createPermissionId">
          <el-select v-model="inputForm.createPermissionId" filterable remote :placeholder="$t('processTypeForm.inputWord')" :remote-method="createPermissionMethod" :loading="loading">
            <el-option v-for="item in createPermissions" :key="item.id" :label="item.fullName" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('processTypeForm.viewPermissionId')" prop="viewPermissionId">
          <el-select v-model="inputForm.viewPermissionId" filterable remote :placeholder="$t('processTypeForm.inputWord')"  :remote-method="viewPermissionMethod" :loading="loading">
            <el-option v-for="item in viewPermissions" :key="item.id" :label="item.fullName" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('processTypeForm.auditFileType')" prop="auditFileType">
          <el-radio-group v-model="inputForm.auditFileType">
            <el-radio v-for="(value,key) in formProperty.bools" :key="key" :label="value">{{key | bool2str}}</el-radio>
          </el-radio-group>
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
                <el-select v-model="scope.row.positionId" filterable :placeholder="$t('processTypeForm.selectPositionName')" :loading="loading">
                  <el-option v-for="item in formProperty.positions":key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
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
    export default{
      data(){
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            formProperty:{},
            createPermissions:[],
            viewPermissions:[],
            loading: false,
            inputForm:{
              name:'',
              auditFileType:"1",
              remarks:'',
              createPermissionId:"",
              viewPermissionId:"",
              processFlowList:[]
            },
            rules: {
              name: [{ required: true, message: this.$t('processTypeForm.prerequisiteMessage')}],
              auditFileType: [{ required: true, message: this.$t('processTypeForm.prerequisiteMessage')}],
              createPermissionId: [{ required: true, message: this.$t('processTypeForm.prerequisiteMessage')}],
              viewPermissionId: [{ required: true, message: this.$t('processTypeForm.prerequisiteMessage')}],
            }
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/sys/processType/save', qs.stringify(this.inputForm, {allowDots:true})).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'processTypeList',query:util.getQuery("processTypeList")})
                }
              });
            } else {
              this.submitDisabled = false;
            }
          })
        },createPermissionMethod(query){
          if(query!==''){
            this.loading = true;
            axios.get('/api/sys/permission/search',{params: {query:query}}).then((response)=>{
              this.createPermissions=response.data;
              this.loading = false;
            });
          }
        },viewPermissionMethod(query){
          if(query!==''){
            this.loading = true;
            axios.get('/api/sys/permission/search',{params: {query:query}}).then((response)=>{
              this.viewPermissions=response.data;
              this.loading = false;
            });
          }
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
        }
      },created(){
        axios.get('/api/sys/processType/getFormProperty').then((res)=>{
          this.formProperty=res.data;
        });
        if(this.isCreate){
          for(var i = 0;i<3;i++) {
            this.inputForm.processFlowList.push({name:"",sort:(i+1)*10,positionId:""});
          }
        } else {
          axios.get('/api/sys/processType/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
            this.inputForm.auditFileType=response.data.auditFileType?"1":"0";
            this.inputForm.processFlowList=response.data.processFlowList;
            if(response.data.createPermission!=null){
              this.createPermissions=new Array(response.data.createPermission);
            }
            if(response.data.viewPermission!=null){
              this.viewPermissions=new Array(response.data.viewPermission);
            }
          });
        }
      }
    }
</script>
