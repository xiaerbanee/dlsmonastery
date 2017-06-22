<template>

  <div style="width:794px;height:562px;margin:auto;">
    <br/>

    <div v-if="printType ==='salePrint'">
      <h2 style="text-align: center;">销售出货单</h2>
    </div>

    <div v-if="printType === 'returnPrint'">
      <h2 style="text-align: center;">销售退货单</h2>
    </div>
    <div style="width: 794px; height: 562px; margin: auto;">
      <br />
      <div class="row">
        <div v-if="printType === 'returnPrint'">
          <div class="span4">退货类型：</div>
          <div class="span4">
            发票类型{{shopAllot.createdDate}}
          </div>
        </div>
        <div class="span4">
          开单日期：{{shopAllot.createdDate}}
        </div>
        <div class="span4">
          <div v-if="printType === 'salePrint'">
            销售开单单号：{{shopAllot.outSaleCode}}
          </div>
          <div v-if="printType === 'returnPrint'">
            销售退货单号：{{shopAllot.outReturnCode}}
          </div>
        </div>
        <div class="span4">
          <div v-if="printType === 'salePrint'">
            客户名称：{{shopAllot.toShopName }}
          </div>
          <div v-if="printType === 'returnPrint'">
            客户名称：{{shopAllot.fromShopName }}
          </div>
        </div>
        <div v-if="printType === 'salePrint'">
          <div class="span4">客户：</div>
        </div>
      </div>
      <div class="row">
        <div class="span4">
          <div v-if="printType === 'salePrint'">送货地址：</div>
          <div v-if="printType === 'returnPrint'">付款方式</div>
        </div>
        <div class="span4">
          <div v-if="printType === 'salePrint'">付款账户</div>
        </div>
        <div class="span4">
          收款期限：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;（天）（天）</div>
      </div>

      <table class="table table-bordered">
        <tbody class="list">
        <tr>
            <td rowspan="2" v-if="printType === 'salePrint'">购货单位</td>
            <td rowspan="2" v-if="printType === 'returnPrint'">退货单位</td>

          <td>名称</td>
          <td  colspan="2" ></td>
          <td>税务登记号</td>
          <td colspan="3"></td>
        </tr>
        <tr>
          <td>地址、电话</td>
          <td colspan="2"></td>
          <td>开户银行及账号</td>
          <td colspan="3"></td>
        </tr>
        <tr>
          <td>货品名称</td>
          <td>规格</td>
          <td>单位</td>
          <td>数量</td>
          <td>单价<td>
          <td>贷款</td>
        </tr>
        <tr v-for="shopAllotDetail in shopAllotDetailList">
          <td>{{shopAllotDetail.productName}}</td>
          <td></td>
          <td>台</td>
          <td>
            {{shopAllotDetail.qty}}
          </td>
          <td>
            <div v-if="printType === 'salePrint'">{{shopAllotDetail.salePrice}}</div>
            <div v-if="printType === 'returnPrint'">{{shopAllotDetail.returnPrice}}</div>
          </td>
          <td>
            <div v-if="printType === 'salePrint'">{{shopAllotDetail.saleAmount}}</div>
            <div v-if="printType === 'returnPrint'">{{shopAllotDetail.returnAmount}}</div>
          </td>
        </tr>

        <tr>
          <td>合计</td>
          <td colspan="2"></td>
          <td>{{totalQty}}</td>
          <td></td>
          <td  v-if="printType === 'salePrint'">{{totalPrice.saleTotalPrice}}</td>
          <td v-if="printType === 'returnPrint'">{{totalPrice.returnTotalPrice}}</td>
        </tr>
        <tr>
          <td>价税合计</td>
          <td colspan="4">
            <div v-if="printType === 'salePrint'">{{totalPrice.chineseReturnTotalPrice }}</div>
            <div v-if="printType === 'returnPrint'">{{totalPrice.chineseReturnTotalPrice }}</div>
          </td>
          <td colspan="3">
            <div v-if="printType === 'salePrint'">{{totalPrice.chineseReturnTotalPrice }}</div>
            <div v-if="printType === 'returnPrint'">{{totalPrice.chineseReturnTotalPrice }}</div>
          </td>
        </tr>
        <tr>
          <td>摘要</td>
          <td colspan="7">调拨单：{{shopAllot.formatId}}</td>
        </tr>
        <tr>
          <td rowspan="2">销货单位</td>
          <td>名称</td>
          <td colspan="2">oppo2016</td>
          <td>税务登记号</td>
          <td colspan="3"></td>
        </tr>
        <tr>
          <td>地址、电话</td>
          <td colspan="2"></td>
          <td>开户银行及账号</td>
          <td colspan="3"></td>
        </tr>
        </tbody>
      </table>

      <div class="row">
        <div class="span3">客户签名：</div>
        <div class="span2">仓管员：</div>
        <div class="span2">业务员：</div>
        <div class="span2">开单人：</div>
      </div>


    </div>
  </div>

</template>
<script>
  export default{
    data(){
      return{
        shopAllot : {},
        shopAllotDetailList:[],
        totalPrice : {},
        totalQty:null,
        printType:this.$route.query.printType,
      }
    }, methods:{

      findDetailList() {
        return axios.get('/api/ws/future/crm/shopAllot/findDetailListForViewOrAudit', {params: {id: this.$route.query.id}});
      },
      findDto() {
        return axios.get('/api/ws/future/crm/shopAllot/findDto', {params: {id: this.$route.query.id}});
      },
      findTotalPrice() {
        return axios.get('/api/ws/future/crm/shopAllot/findTotalPrice', {params: {id: this.$route.query.id}});
      }
    },
    mounted(){
      setTimeout("window.print()",2000);
    },

    created(){

      axios.all([this.findDetailList(), this.findDto(), this.findTotalPrice()])
        .then(axios.spread( (findDetailListRes, findDtoRes, findTotalPriceRes) => {
          this.shopAllotDetailList = findDetailListRes.data;
          this.shopAllot = findDtoRes.data;
          this.totalPrice = findTotalPriceRes.data;
          let result = 0;
          if(this.shopAllotDetailList){
            for(let storeAllotDetail of this.shopAllotDetailList){
              result += storeAllotDetail.qty;
            }
          }
          this.totalQty = result;

          this.$nextTick(()=>{
            window.print();
            this.$router.push({ name: 'shopAllotList'});
          });
        }));
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
