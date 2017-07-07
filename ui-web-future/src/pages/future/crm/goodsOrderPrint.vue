<template>
  <div style="width:794px;height:562px;margin:auto;">
    <br/>
    <h2 style="text-align: center;">销售出库单</h2>
    <div class="row">
      <div class="span4">
        财务单号 ：{{goodsOrder.outCode}}
      </div>
      <div class="span3">
        开单日期：{{goodsOrder.billDate}}
      </div>
      <div class="span3">
        出货仓：{{goodsOrder.storeName}}，出货单号：{{goodsOrder.formatId}}
      </div>
    </div>
    <div class="row">
      <div class="span10">
        收货单位：{{goodsOrder.shopName}}&nbsp;客户：{{goodsOrder.shopClientName}}	&nbsp;&nbsp;备注摘要：{{goodsOrder.contator }}，{{goodsOrder.mobilePhone}}，{{goodsOrder.address}}，{{goodsOrder.remarks}}，{{goodsOrder.lxMallOrder?'天翼购订货':''}}
      </div>
    </div>
    <table class="table table-bordered">
      <tbody class="list">
      <tr>
        <td>货品名称</td>
        <td>单位</td>
        <td>数量</td>
        <td>单价</td>
        <td>合计</td>
      </tr>
      <tr v-for="item in goodsOrder.productList">
        <td>{{item.productName}}</td>
        <td>{{item.unit}}</td>
        <td>{{item.qty}}</td>
        <td>{{item.price}}</td>
        <td>{{item.total}}</td>
      </tr>
      </tbody>
    </table>
    <div class="row">
      <div class="span1">审核：</div>
      <div class="span1">记账：</div>
      <div class="span2">收款：</div>
      <div class="span2">仓管员：</div>
      <div class="span2">制单：{{account.createdByName}}</div>
      <div class="span2">提货人：</div>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        account:{},
        goodsOrder:{},
      }
    },created(){
      this.account=JSON.parse(window.localStorage.getItem("account"));
      axios.get('/api/ws/future/crm/goodsOrderShip/print',{params: {goodsOrderId:this.$route.query.id}}).then((response)=>{
        this.goodsOrder=response.data;
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
    font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
    font-size: 14px;
    line-height: 20px;
    color: #333;
    background-color: #fff;
  }
  .row {
    margin-left: -20px;
    *zoom: 1;
  }
  .row:before,
  .row:after {
    display: table;
    line-height: 0;
    content: "";
  }
  .row:after {
    clear: both;
  }
  [class*="span"] {
    float: left;
    min-height: 1px;
    margin-left: 20px;
  }
  .span10 {
    width: 780px;
  }
  .span4 {
    width: 300px;
  }
  .span3 {
    width: 220px;
  }
  .span2 {
    width: 140px;
  }
  .span1 {
    width: 60px;
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
