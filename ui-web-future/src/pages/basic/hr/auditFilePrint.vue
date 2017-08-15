<template>
  <div style="width:794px;height:562px;margin:auto;">
    <br/>
  <div>
    <p>文件审批</p>
  </div>
  <h4 class="panel-title" align="center">{{auditFile.title}}</h4>
  <div v-html="auditFile.content"></div>
  <table class="table">
    <tbody>
    <tr>
      <td>文件编号:</td>
      <td>{{auditFile.id}}</td>
    </tr>
    <tr>
      <td>文件类型:</td>
      <td>{{auditFile.processTypeName}}</td>
    </tr>
    <tr>
      <td>审批状态:</td>
      <td><span class='label label-info'>{{auditFile.processStatus}}</span></td>
    </tr>
    <tr>
      <td>创建人</td>
      <td>{{auditFile.createdByName}}</td>
    </tr>
    <tr>
      <td>创建时间:</td>
      <td>{{auditFile.createdDate}}</td>
    </tr>
    <tr>
      <td>更新人:</td>
      <td>{{auditFile.lastModifiedByName}}</td>
    </tr>
    <tr>
      <td>更新时间:</td>
      <td>{{auditFile.lastModifiedDate}}</td>
    </tr>
    <tr>
      <td>备注:</td>
      <td>{{auditFile.remarks}}</td>
    </tr>
    </tbody>
  </table>
  <h4 class="panel-title" align="center">审批节点</h4>
  <table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
      <th>节点名称</th>
      <th>审批人</th>
      <th>审批时间</th>
      <th>审批备注</th>
    </tr>
    </thead>
    <tbody v-for="item in auditFile.activitiDetailList">
    <tr>
      <td>{{item.processStatus}}</td>
      <td>{{item.auditByName}}</td>
      <td>{{item.auditDate}}</td>
      <td>{{item.comment}}</td>
    </tr>
    </tbody>
  </table>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        auditFile:{},
      }
    },created(){
      axios.get('/api/basic/hr/auditFile/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          console.log(response.data);
        this.auditFile=response.data;
        this.$nextTick(()=>{
          window.print();
        });
      });
    }
  }
</script>
<style type="text/css">
  body {
    margin: 0;
    font-size: 18px;
    font-weight:500;
    line-height: 26px;
    color: #333;
    background-color: #fff;
  }
  table {
    max-width: 100%;
    background-color: transparent;
    border-collapse: collapse;
    border-spacing: 0;
  }

  .table {
    width: 100%;
    margin-bottom: 20px;
  }

  .table th,
  .table td {
    padding: 8px;
    line-height: 20px;
    text-align: left;
    vertical-align: top;
    border-top: 1px solid #dddddd;
  }

  .table th {
    font-weight: bold;
  }

  .table thead th {
    vertical-align: bottom;
  }
  .table-bordered {
    border: 1px solid #dddddd;
    border-collapse: separate;
    *border-collapse: collapse;
    border-left: 0;
    -webkit-border-radius: 4px;
    -moz-border-radius: 4px;
    border-radius: 4px;
  }

  .table-bordered th,
  .table-bordered td {
    border-left: 1px solid #dddddd;
  }
  .table td.span1,
  .table th.span1 {
    float: none;
    width: 44px;
    margin-left: 0;
  }

  .table td.span2,
  .table th.span2 {
    float: none;
    width: 124px;
    margin-left: 0;
  }

  .table td.span3,
  .table th.span3 {
    float: none;
    width: 204px;
    margin-left: 0;
  }

  .table td.span4,
  .table th.span4 {
    float: none;
    width: 284px;
    margin-left: 0;
  }

  .table td.span10,
  .table th.span10 {
    float: none;
    width: 764px;
    margin-left: 0;
  }
</style>

