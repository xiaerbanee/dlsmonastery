<template>
  <div>
    <head-tab active="afterSaleAreaFleeInput"></head-tab>
    <el-row>
      <el-button type="primary" @click="formSubmit()" icon="check">{{$t('adPricesystemChangeForm.save')}}</el-button>
    </el-row>
    <div class="position:relative;margin-top:20px">
      <el-form :model="formData">
        <el-form-item label="类型" :label-width="formLabelWidth">
          <el-select v-model="type" placeholder="请选择" @change="onchange(type)">
            <el-option v-for="item in options" :key="item" :label="item" :value="item">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </div>
    <div ref="handsontable" style="width:100%;height:600px;overflow:hidden;margin-top:20px"></div>
  </div>
</template>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js'

  export default{
    data(){
      return {
        formData: {
          ime: '',
        }, formLabel: {
          ime: {label: '串码'},
        },
        inputForm: {
          data: ''
        },
        colHeaders:['窜货机串码','窜货机型号','窜货机门店','包装','内存','坏机来源','坏机所在库','替换机串码','替换机型号','返还金额','窜货机门店','联系人','手机号','地址','购买金额'],
        columns: [{
          data:"badProductImeId",
          width:100
        },{
          data:"badProductId",
          width:100
        },{
          data:"badDepotId",
          readOnly: true,
          width:100
        },{
          data:"badType",
          width:100
        },{
          data:"packageStatus",
          width:100
        },{
          data:"memory",
          width:100,
        },{
          data:"fromDepotId",
          width:100
        },{
          data:"toDepotId",
          width:100
        },{
          data:"replaceProductImeId",
          width:100
        },{
          data:"replaceProductId",
          width:100
        },{
          data:"replaceAmount",
          width:100
        },{
          data:"ime",
          width:100
        },{
          data:"fleeShopName",
          width:100
        },{
          data:"contact",
          width:100
        },{
          data:"mobilePhone",
          width:100
        },{
          data:"address",
          width:100
        },{
          data:"buyAmount",
          width:100
        }],
        rules: {},
        type: '窜货机',
        options: ['售后机', '窜货机'],
        formLabelWidth: '120px',
        formVisible: false,
        submitDisabled: false,
        table: null,
        settings: {
          colHeaders: this.colHeaders,
          rowHeaders: true,
          autoColumnSize: true,
          allowInsertRow: false,
          maxRows: 1000,
          columns: this.columns,
        },
      }

    },
    mounted () {
      this.onchange(this.type);
    },
    methods: {
      formSubmit(){
        this.submitDisabled = true;
        this.inputForm.data = new Array();
        let list = this.table.getData();
        for (var item in list) {
          if (!this.table.isEmptyRow(item)) {
            this.inputForm.data.push(list[item]);
          }
        }
        this.inputForm.data = JSON.stringify(this.inputForm.data);
        axios.post('/api/ws/future/crm/afterSale//save', qs.stringify({data: this.inputForm.data}, {allowDots: true})).then((response) => {
          this.$message(response.data.message);
          this.submitDisabled = false;
        }).catch(function () {
          this.submitDisabled = false;
        });
      }, search() {
        this.formVisible = false;
        this.getData();
      }
      }, onchange(type){
        if (this.type == '售后机') {
          this.$router.push({ name: 'afterSaleAreaInput'})
        }
      }
    }, created(){
    }
  }
</script>
<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>


