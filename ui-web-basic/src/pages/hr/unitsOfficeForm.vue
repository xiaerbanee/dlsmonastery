<template>
  <div>
    <head-tab active="unitsOfficeForm"></head-tab>
    <div>
      <el-alert :title="$t('unitsOfficeForm.title')" type="info" style="margin-bottom: 30px"></el-alert>

      <el-form :inline="true" :model="inputForm" class="demo-form-inline">
        <el-row>
          <el-form-item :label="$t('unitsOfficeForm.officeId')">
            <el-select v-model="inputForm.officeId"  filterable remote  :placeholder="$t('unitsOfficeForm.inputKey')" :remote-method="remoteSelect" :loading="remoteLoading"  :clearable=true @change="selectOffice">
              <el-option v-for="item in officeList"  :key="item.id" :label="item.fullName" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
              <el-button type="primary" @click="batchUnits" >{{$t('unitsOfficeForm.batchUnits')}}</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="formSubmit"  icon="plus">{{$t('unitsOfficeForm.save')}}</el-button>
          </el-form-item>
        </el-row>
        <el-row>
          <el-col :span="24">
          <el-form-item>
            <template>
              <el-transfer v-model="inputForm.depotIds" :props="{  key: 'id', label: 'name' }" filterable  @change="handleChange"  :titles="[$t('unitsOfficeForm.allDepot'), $t('unitsOfficeForm.officeId')]"
                           :button-texts="['', '']" :footer-format="{  noChecked: '${total}',   hasChecked: '${checked}/${total}' }"  :data="depotList" >
                <el-button class="transfer-footer" slot="left-footer" size="small">{{$t('unitsOfficeForm.operation')}}</el-button>
                <el-button class="transfer-footer" slot="right-footer" size="small">{{$t('unitsOfficeForm.operation')}}</el-button>
              </el-transfer>
            </template>
          </el-form-item>
          </el-col>
        </el-row>
        <el-row>
        <el-col :span="24">
          <el-form-item>
            <template>
              <el-transfer v-model="inputForm.accountIds"  :props="{  key: 'id', label: 'loginName' }" filterable  @change="handleChange" :titles="[$t('unitsOfficeForm.allAccount'),$t('unitsOfficeForm.officeId')]"
                           :button-texts="['', '']" :footer-format="{  noChecked: '${total}',   hasChecked: '${checked}/${total}' }" :data="accountList.allAccount">
                <el-button class="transfer-footer" slot="left-footer" size="small">{{$t('unitsOfficeForm.operation')}}</el-button>
                <el-button class="transfer-footer" slot="right-footer" size="small">{{$t('unitsOfficeForm.operation')}}</el-button>
              </el-transfer>
            </template>
          </el-form-item>
          </el-col>
        </el-row>
        </el-form>
    </div>
  </div>
</template>
<script>
  import officeSelect from "components/basic/office-select"
  export default {
    components:{officeSelect},
    data() {
        return {
          inputForm:{
              officeId:'',
              depotIds:[],
              accountIds:[]
            },
          accountList:{},
          depotList:[],
          officeList:[],
          remoteLoading:false,
        };
      },

      methods: {
        handleChange(value, direction, movedKeys) {
        },
        renderFunc(){

        },
        findDepotList(){
            return axios.get("/api/ws/future/basic/depot/findByOfficeId?officeId="+this.inputForm.officeId)
        },
        findAccountList(){
          return axios.get("/api/basic/hr/account/findByOfficeId?officeId="+this.inputForm.officeId)
        },
        selectOffice(){
            axios.all([this.findDepotList(),this.findAccountList()]).then(axios.spread((depotRes,accountRes)=>{
               this.depotList=depotRes.data;
              this.accountList=accountRes.data;
            }))
        },
        batchUnits(){
          this.$router.push({ name: 'batchUnitsForm'})
        },
        formSubmit(){
          var that = this;
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
        },
        remoteSelect(query) {
          this.remoteLoading = true;
          axios.get('/api/basic/sys/office/search',{params:{name:query}}).then((response)=>{
              if(query!=""){
                this.officeList =  response.data;
                this.remoteLoading = false;
              }else{
                  this.officeList=[];
              }
          })
        },
      }
    };
</script>

<style>
  .transfer-footer {
    margin-left: 20px;
    padding: 6px 5px;
  }
  .el-transfer-panel{
       width:380px
     }
</style>
