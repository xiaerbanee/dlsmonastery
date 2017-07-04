<template>
  <div>
    <head-tab active="goodsOrderBatchAdd"></head-tab>
    <div >
      <el-form :model="formData" ref="inputForm"  label-width="120px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('goodsOrderBatchAdd.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="24">
            <div ref="handsontable" style="width:1500px;height:600px;overflow:hidden;"></div>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js';
  import suAlert from 'components/common/su-alert'
  let table = null;
  export default {
    components:{
      suAlert,
    },
    data() {
      return {
        formData:{
          extra:{}
        },
        submitDisabled: false,
        initPromise:{},

        settings: {
          colHeaders: [this.$t('goodsOrderBatchAdd.shopName'), this.$t('goodsOrderBatchAdd.carrierCodes'), this.$t('goodsOrderBatchAdd.receiveAddress'),
            this.$t('goodsOrderBatchAdd.receiver'),this.$t('goodsOrderBatchAdd.tel'),this.$t('goodsOrderBatchAdd.remarks'),this.$t('goodsOrderBatchAdd.netType'),
            this.$t('goodsOrderBatchAdd.type'),this.$t('goodsOrderBatchAdd.num'),this.$t('goodsOrderBatchAdd.price'),this.$t('goodsOrderBatchAdd.shipType')],
          rowHeaders: true,
          minSpareRows: 500,
          startRows: 500,
          startCols:11,
          maxRows: 1000,
          columns: [
            {
              type: "autocomplete",
              strict: true,
              tempShopNames: [],
              source: function (query, process) {
                if (this.tempShopNames.indexOf(query) >= 0) {
                  process(this.tempShopNames);
                } else {
                  let shopNames = new Array();
                  if (query.length >= 2) {
                    axios.get('/api/ws/future/basic/depot/shop?name=' + query).then((response) => {
                      if (response.data.length > 0) {
                        for (let row of response.data) {
                          let shopName = row.name;
                          shopNames.push(shopName);
                          if (this.tempShopNames.indexOf(shopName) < 0) {
                            this.tempShopNames.push(shopName);
                          }
                        }
                      }
                      process(shopNames);
                    });
                  } else {
                    process(shopNames);
                  }
                }
              },
              width: 200
            },{
              width:100
            },{
              width:100
            },{
              width:100
            },{
              width:100
            },{
              width:100
            },{
              readOnly: false,
              type: "autocomplete",
              allowEmpty: false,
              strict: true,
              width:100
            },{
              type: "autocomplete",
              tempType: [],
              source: function (query, process) {
                if (this.tempType.indexOf(query) >= 0) {
                  process(this.tempType);
                } else {
                  let types = new Array();
                  if (query.length >= 2) {
                    axios.get('/api/ws/future/basic/productType/search?name=' + query).then((response) => {
                      if (response.data.length > 0) {
                        for (let row of response.data) {
                          let type = row.name;
                          types.push(type);
                          if (this.tempType.indexOf(type) < 0) {
                            this.tempType.push(type);
                          }
                        }
                      }
                      process(types);
                    });
                  } else {
                    process(types);
                  }
                }
              },
              width:200
            },{
              width:100
            },{
              width:100
            },{
              readOnly: false,
              type: "autocomplete",
              allowEmpty: false,
              strict: true,
              width:150
            }]
        }
      }
    },created(){
        this.initPage();
    },
    methods:{
        formSubmit(){
            let list = table.getData();
            this.formData.goodsOrderBatchAddDetailFormList = new Array();
            for(let item in list){
              if(!table.isEmptyRow(item)){
                let row = list[item];
                let temData = {};
                temData.shopName = row[0];
                temData.carrierOrderId = row[1];
                temData.address = row[2];
                temData.contator = row[3];
                temData.mobilePhone = row[4];
                temData.remarks = row[5];
                temData.netType = row[6];
                temData.productName = row[7];
                temData.qty = Number.parseInt(row[8]);
                temData.price = Number.parseInt(row[9]);
                temData.shipType = row[10];

                this.formData.goodsOrderBatchAddDetailFormList.push(temData);
              }
            }
          /*this.formData.data = JSON.stringify(this.formData.data);*/
          console.log(this.formData);
          axios.post('/api/ws/future/crm/goodsOrder/batchAdd', qs.stringify(util.deleteExtra(this.formData),{allowDots: true})).then((response)=>{
            this.$message(response.data.message);
            Object.assign(this.$data, this.getData());
            this.initPage();
          }).catch( () => {
            this.submitDisabled = false;
          });
        },initPage(){
            axios.get('/api/ws/future/crm/goodsOrder/getForm').then((response)=>{
              this.settings.columns[6].source=response.data.extra.netTypeList;
              this.settings.columns[10].source=response.data.extra.shipTypeList;
              table = new Handsontable(this.$refs["handsontable"], this.settings);
            });
      }
    }
  }
</script>
<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
