<template>
  <div>
    <head-tab :active="$t('home.home')"></head-tab>
    <el-row>
      <el-col :span="16">
      <el-alert :title="title" type="info" :closable="false">{{$t('home.messageForDutyOvertimeRest')}}{{labelData.overtimeHour}} {{$t('home.hour')}} {{$t('home.messageForDutyAnnualRest')}} {{labelData.annualHour}} {{$t('home.hour')}}  {{$t('home.messageForTimeDue')}}{{labelData.expiredHour}}{{$t('home.hour')}} </el-alert>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="16">
        <full-calendar class="test-fc" :events="fcEvents" first-day='0' lang="zh" @changeMonth="changeMonth" @eventClick="eventClick" @dayClick="dayClick" @moreClick="moreClick">
          <template slot="fc-event-card" scope="row">
            <p><i class="fa"></i> {{row.scoped.title}}</p>
          </template>
        </full-calendar>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span style="font-size:20px">{{$t('home.personMessage')}}</span>
            <el-dropdown split-button type="primary" style="float: right;" @click="handleClick(account.employeeId)">
              {{$t('home.edit')}}
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item><div  @click="updateMsg(account.id,'手机')">{{$t('home.mobilePhoneEdit')}}</div></el-dropdown-item>
                <el-dropdown-item><div  @click="updateMsg(account.id,'身份证')">{{$t('home.idCardEdit')}}</div></el-dropdown-item>
                <el-dropdown-item><div  @click="updateMsg(account.id,'银行卡号')">{{$t('home.bankAccountEdit')}}</div></el-dropdown-item>
                <el-dropdown-item><div  @click="updateMsg(account.id,'部门')">{{$t('home.officeEdit')}}</div></el-dropdown-item>
                <el-dropdown-item><div  @click="updateMsg(account.id,'岗位')">{{$t('home.positionEdit')}}</div></el-dropdown-item>
                <el-dropdown-item><div  @click="updateMsg(account.id,'上级')">{{$t('home.readerEdit')}}</div></el-dropdown-item>
                <el-dropdown-item><div  @click="updateMsg(account.id,'转正')">{{$t('home.regularEdit')}}</div></el-dropdown-item>
                <el-dropdown-item><div  @click="updateMsg(account.id,'离职')">{{$t('home.leaveEdit')}}</div></el-dropdown-item>

              </el-dropdown-menu>
            </el-dropdown>
          </div>
          <table class="table">
            <tbody>
            <tr>
              <td class="td">{{$t('home.companyName')}}</td>
              <td  class="td">{{account.companyName}}</td>
            </tr>

            <tr>
              <td class="td">{{$t('home.employeeName')}}</td>
              <td  class="td">{{account.employeeName}}</td>
            </tr>
            <tr>
              <td class="td">{{$t('home.officeName')}}</td>
              <td  class="td">{{account.officeName}}</td>
            </tr>
            <tr>
              <td class="td">{{$t('home.positionName')}}</td>
              <td  class="td">{{account.positionName}}</td>
            </tr>
            <tr>
              <td class="td">{{$t('home.leaderName')}}</td>
              <td  class="td">{{account.leaderId==null?'':account.leaderName}}</td>
            </tr>
            <tr>
              <td class="td">{{$t('home.regularDate')}}</td>
              <td  class="td">{{account.regularDate}}</td>
            </tr>
            </tbody>
          </table>
        </el-card>
        <el-card class="box-card margin">
          <div slot="header" class="clearfix">
            <span style="font-size:20px">{{$t('home.noticeCenter')}}</span>
          </div>
          <table class="table">
            <tbody>
            <tr>
              <td class="td">{{$t('home.waitDuty')}}</td>
              <td  class="td">{{$t('home.youHave')}}<router-link  :to="{ name: 'dutyTaskList'}"><span style="color:red">{{labelData.dutySize}}</span></router-link>{{$t('home.dutySize')}}</td>
            </tr>

            <tr>
              <td class="td">{{$t('home.waitAccountTask')}}</td>
              <td  class="td">{{$t('home.youHave')}}<router-link :to="{ name: 'accountTaskList'}"><span style="color:red">{{labelData.accountTaskSize}}</span></router-link>{{$t('home.accountTaskSize')}}</td>
            </tr>
            <tr>
              <td class="td">{{$t('home.waitReadMessage')}}</td>
              <td  class="td">{{$t('home.youHave')}}<span style="color:red">{{labelData.accountMessageSize}}</span>{{$t('home.accountMessageSize')}}</td>
            </tr>
            </tbody>
          </table>
        </el-card>
        <el-card class="box-card margin">
          <div slot="header" class="clearfix">
            <span style="font-size:20px">{{$t('home.synData')}}</span>
          </div>
          <table class="table">
            <tbody >
                <el-date-picker v-model="synDate" align="right" :placeholder="$t('home.selectSynFactoryDate')" type="date"></el-date-picker>
                <el-button type="primary"  @click="synFactory">{{$t('home.synFactory')}}</el-button>
                <el-button type="primary"  @click="factoryOrder">{{$t('home.factoryOrder')}}</el-button>
            </tbody>
          </table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
  import fullCalendar from 'vue-fullcalendar';

export default {

  data () {
    return {
      fcEvents: [],
      synDate:new Date(),
      account:{
          employeeId:'',
          employee:{name:'',regularDate:''},
          office:{name:''},
          position:{name:''},
          leader:{fullName:''}
      },
      labelData:{},
      pickerDateOption:util.pickerDateOption,
      title:"",
    }
  },
  methods : {
    changeMonth(start, end, current) {
      axios.get('/api/basic/hr/duty/events',{params:{start:start,end:end}}).then((response)=>{
        this.fcEvents=response.data;
      });
    }, eventClick(event, jsEvent, pos) {
    }, dayClick(day, jsEvent) {
    }, moreClick(day, events, jsEvent) {
    },handleClick(id){
      this.$router.push({ name: 'employeeEditForm', query: { id:id}})
    },updateMsg(accountId,type){
      this.$router.push({ name: 'accountChangeForm', query: { accountId: accountId,type:type }})
    },getAccount(){
      axios.get('/api/basic/hr/account/home').then((response) =>{
          this.account=response.data.account;
          this.labelData=response.data;
      })
    },synFactory(){
      if(this.synDate!==''){
        this.synDate=util.formatLocalDate(this.synDate);
//        window.open("http://ncoppo.com:1234/future/syn?companyName="+this.account.companyName.toUpperCase( )+"&date=" + this.synDate,"_blank");
      }
    },factoryOrder(){
//      if(this.account.companyName.toUpperCase( )=='JXVIVO'){
//        window.open("http://ncoppo.com:1234/factory/vivo/factoryOrder");
//      }
//      if(this.account.companyName.toUpperCase( )=='IDVIVO'){
//        window.open("http://idvivo.com:1234/factory/vivo/idFactoryOrder");
//      }
    }
  },
  created(){
      this.getAccount();
  }

}
</script>
<style lang='scss'>
  .margin{
    margin-top:30px;
  }
  .table{
    top:50px;
    table-layout:fixed;
    empty-cells:show;
    border-collapse: collapse;
    margin:0 auto;
    width:100%;
  }
  tr{
    border-bottom:1px solid #f2f2f2;
  }
  .td{
    height:20px;
    padding:10px 5px;
  }
</style>

