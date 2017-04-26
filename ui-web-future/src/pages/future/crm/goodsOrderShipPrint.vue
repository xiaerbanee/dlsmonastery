<template>
  <div style="width:794px;height:400px;margin:auto;">
    <div style="margin-top: 215px;">
      <span style="margin-left:  110px;">{{goodsOrder.shop.contator}}</span>
      <span style="margin-left:  118px;">{{goodsOrder.shop.mobilePhone}}</span>
    </div>
    <div style="margin-top: 3px;">
      <span style="margin-left:  110px;width:350px;">{{goodsOrder.shop.name}}</span>
    </div>
    <div style="margin-top: 5px;">
      <span style="margin-left:  110px;width:350px;">{{goodsOrder.shop.address}}</span>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        goodsOrder:{
            shop:{
              contator:"",
              mobilePhone:"",
              name:"",
              address:"",
            }
        },
        rules: {
          pass: [{ required: true, message: this.$t('expressOrderList.prerequisiteMessage')}],
        },
        activityEntity:{},
      }
    },
    mounted() {
      setTimeout("window.print()",2000);
    },
    methods:{
      findOne(){
        axios.get('/api/crm/goodsOrder/print',{params: {id:this.$route.query.id}}).then((response)=>{
          this.goodsOrder=response.data;
        })
      }
    },created(){
      this.findOne();
    }
  }
</script>
<style type="text/css">
  span {
    font-size: 19px;
  }
  body {
    display: block;
    margin: 8px;
  }
</style>

