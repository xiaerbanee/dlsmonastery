<template>
  <div>
    <head-tab active="goodsOrderBatchAdd"></head-tab>
    <div >
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
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
  var table = null;
  var shipTypes=["总部发货","总部自提","地区发货","地区自提","代理发货","代理自提"];
  var netTypes=["全网通","移动","联信"];
  export default {
    data() {
      return {
        submitDisabled: false,
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
                var that = this;
                if (that.tempShopNames.indexOf(query) >= 0) {
                  process(that.tempShopNames);
                } else {
                  var shopNames = new Array();
                  if (query.length >= 1) {
                    axios.get('/api/ws/future/basic/depot/shop?name=' + query).then((response) => {
                      if (response.data.length > 0) {
                        for (var index in response.data) {
                          var shopName = response.data[index].name;
                          shopNames.push(shopName);
                          if (that.tempShopNames.indexOf(shopName) < 0) {
                            that.tempShopNames.push(shopName);
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
              source: netTypes,
              width:100
            },{
              type: "autocomplete",
              tempType: [],
              source: function (query, process) {
                var that = this;
                if (that.tempType.indexOf(query) >= 0) {
                  process(that.tempType);
                } else {
                  var types = new Array();
                  if (query.length >= 1) {
                    axios.get('/api/ws/future/basic/productType/search?name=' + query).then((response) => {
                      if (response.data.length > 0) {
                        for (var index in response.data) {
                          var type = response.data[index].name;
                          types.push(type);
                          if (that.tempType.indexOf(type) < 0) {
                            that.tempType.push(type);
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
              source: shipTypes,
              width:150
            }]
        }
      }
    },mounted () {
      table = new Handsontable(this.$refs["handsontable"], this.settings);
    },
    methods:{
        formSubmit(){

        }
    }
  }
</script>
<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
