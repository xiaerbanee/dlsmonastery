<template>
  <div>
    <head-tab active="afterSaleForm"></head-tab>
    <div class="form input-form ">
      <el-row class="button">
        <el-button type="primary" @click="formSubmit" icon="check">{{$t('afterSaleForm.save')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('afterSaleForm.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <el-dialog :title="$t('afterSaleForm.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData" :label-width="formLabelWidth">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="this.$t('afterSaleForm.ime')" >
                <el-input type="textarea" v-model="formData.imeStr" :autosize="autosize" auto-complete="off" ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="getImeStr(formData.imeStr)">{{$t('afterSaleForm.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-alert :title="$t('afterSaleForm.alertSearchAfterSaleIme')" type="error"  :closable="false" v-if="formData.imeStr =='' "></el-alert>
      <el-alert :title="message" type="error"  :closable="false" v-if="message !==''"></el-alert>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="150px">
        <el-row>
          <el-col :span="24">
        <el-form-item></el-form-item>
        <el-form-item :label="$t('afterSaleForm.toStoreDate')" prop="toStoreDate">
          <date-picker v-model="inputForm.toStoreDate"></date-picker>
        </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <div id="grid" ref="handsontable" style="width:100%;height:600px;overflow:hidden;margin-top: 20px;"></div>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js'
  var table = null;
  export default{
    data(){
      return{
        searchText:"",
        message:"",
        submitDisabled:false,
        formData:{ imeStr:''},
        inputForm:{
          data:null,
          toStoreDate:"",
        },
        formLabelWidth: '120px',
        formVisible: false,
        rules:{},
        autosize: { minRows: 5},

        settings: {
          data:{},
          colHeaders: ["坏机串码","坏机型号","核销门店","替换串码","替换机型","地区","包装","退机类型","内存","退机备注"],
          rowHeaders:true,
          maxRows:1000,
          columns: [
            {data: "ime",strict:true, readOnly: true,width:120},
            {data: "productName",strict:true, readOnly: true,width:120},
            {data: "retailDepotName",strict:true, readOnly: true,width:120},
            {data:"toAreaProductIme",type: "autocomplete",strict:true, allowEmpty:true,
              imes:[],
              source:function (query, process) {
                var that = this;
                if(that.imes.indexOf(query)>=0) {
                  process(that.imes);
                } else {
                  var imeList = new Array();
                  if(query.length>=6) {
                    axios.get('/api/ws/future/crm/productIme/search?productIme='+query).then((response)=>{
                      console.log(response.data)
                      if(response.data.length>0) {
                      for(var index in response.data) {
                        var ime = response.data[index].ime;
                        imeList.push(ime);
                        if(that.imes.indexOf(ime)<0) {
                          that.imes.push(ime);
                        }
                      }
                    }
                    process(imeList);
                  });
                  } else {
                    process(imeList);
                  }
                }
              } , width:150},
            {data:"toAreaProductName",strict:true, width:150},
            {data:"areaDepotName",type: "autocomplete",strict:true,
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
              },width:150},
            {data:"packageStatus",type: "autocomplete",strict:true,width:150},
            {data:"toStoreType",type: "autocomplete",strict:true,width:100},
            {data:"memory",type: "autocomplete",strict:true,width:100},
            {data: "remarks",width:150}
          ]
        },
      }
    }, mounted () {
      let categoryList=new Array();
      categoryList.push("退机类型")
      categoryList.push("内存")
      categoryList.push("包装")
      axios.get('/api/basic/sys/dictEnum/findByCategoryList',{params:{categoryList:categoryList}}).then((response)=> {
        this.settings.columns[6].source=util.getLabelList(response.data.PACKAGES_STATUS,'value');
        this.settings.columns[7].source=util.getLabelList(response.data.TOS_TORE_TYPE,'value');
        this.settings.columns[8].source=util.getLabelList(response.data.MEMORY,'value');
        this.inputForm.toStoreDate=util.currentDate();
        table = new Handsontable(this.$refs["handsontable"], this.settings)
      })
    },
    methods: {
      formSubmit() {
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.inputForm.data = new Array();
            let list = table.getData();
            for (var item in list) {
              if (!table.isEmptyRow(item)) {
                this.inputForm.data.push(list[item]);
              }
            }
            this.inputForm.data = JSON.stringify(this.inputForm.data);
            axios.post('/api/ws/future/crm/afterSale/save', qs.stringify(this.inputForm, {allowDots: true})).then((response) => {
              this.$message(response.data.message);
              if (response.data.success) {
                this.$router.push({name: 'afterSaleList', query: util.getQuery("afterSaleList"),params:{_closeFrom:true}})
                Object.assign(this.$data, this.getData());
              }
              this.submitDisabled = false;
            });
          } else {
            this.submitDisabled = false;
          }
        })
      }, getImeStr(imeStr) {
        this.formVisible = false;
        axios.get("/api/ws/future/crm/afterSale/formData", {params: {imeStr: imeStr}}).then((response) => {
          this.settings.data = response.data.list;
          table.loadData(this.settings.data);
          this.message ="";
          if (response.data.message != "") {
            this.message = response.data.message
          }
        })
      }
    }
  }
</script>
<style>
  .button{
    margin-bottom:20px;
  }
</style>
