<template>
  <div>
    <head-tab active="adPricesystemChangeForm"></head-tab>
    <el-row>
      <el-button type="primary" @click="formSubmit()" icon="check">{{$t('adPricesystemChangeForm.save')}}</el-button>
      <el-button type="primary" @click="formVisible = true" icon="search">{{$t('adPricesystemChangeForm.filter')}}</el-button>
      <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
    </el-row>
    <el-dialog :title="$t('adPricesystemChangeForm.filter')"  v-model="formVisible"  size="tiny" class="search-form">
      <el-form :model="formData">
        <el-form-item :label="formLabel.productName.label" :label-width="formLabelWidth">
          <el-input v-model="formData.productName" auto-complete="off" :placeholder="$t('adPricesystemChangeForm..likeSearch')"></el-input>
        </el-form-item>
        <el-form-item :label="formLabel.productCode.label" :label-width="formLabelWidth">
          <el-input v-model="formData.productCode" auto-complete="off" :placeholder="$t('adPricesystemChangeForm..likeSearch')"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="search()">{{$t('adPricesystemChangeForm.sure')}}</el-button>
      </div>
    </el-dialog>
    <div ref="handsontable" style="width:100%;height:600px;overflow:hidden;margin-top:20px"></div>
  </div>
</template>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js'

  export default{
    data(){
      return{
        formData:{
          productName:'',
          productCode:''
        },formLabel:{
          productName:{label:this.$t('adPricesystemChangeForm.productName')},
          productCode:{label:this.$t('adPricesystemChangeForm.productCode')}
        },
        inputForm:{
            data:''
        },
        rules:{},
        productTypes:[],
        formLabelWidth: '120px',
        formVisible: false,
        submitDisabled:false,
        table:null,
        settings: {
          colHeaders: [this.$t('adPricesystemChangeForm.id'),this.$t('adPricesystemChangeForm.productCode'),this.$t('adPricesystemChangeForm.productName'),this.$t('adPricesystemChangeForm.volume'),this.$t('adPricesystemChangeForm.shouldGet'),this.$t('adPricesystemChangeForm.materialA'),this.$t('adPricesystemChangeForm.materialB')],
          rowHeaders:true,
          autoColumnSize:true,
			    allowInsertRow:false,
          maxRows:1000,
          columns: [{
            data:"id",
            readOnly: true,
            width:100
          },{
            data:"code",
            readOnly: true,
            width:150
          },{
            data:"name",
            readOnly: true,
            width:300
          },{
            data:"volume",
            type:"numeric",
            width:150
          },{
            data:"shouldGet",
            type:"numeric",
            width:150
          },{
            width:150,
            type:"numeric",
          },{
            width:150,
            type:"numeric",
          }]
        },
      }

    },
    mounted () {
      this.table = new Handsontable(this.$refs["handsontable"], this.settings)
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        this.inputForm.data = new Array();
        let list = this.table.getData();
        for(var item in list){
          if(!this.table.isEmptyRow(item)){
            this.inputForm.data.push(list[item]);
           }
        }
       this.inputForm.data = JSON.stringify(this.inputForm.data);
       axios.post('/api/ws/future/layout/adPricesystemChange/save',qs.stringify({data:this.inputForm.data},{allowDots:true})).then((response)=> {
          this.$message(response.data.message);
          this.submitDisabled = false;
        }).catch(function () {
         this.submitDisabled = false;
       });
      },search() {
        this.formVisible = false;
        this.getData();
      },getData(){
        axios.get('/api/ws/future/layout/adPricesystemChange/findFilter',{params:this.formData}).then((response)=>{
          this.settings.data = response.data;
          this.table.loadData(this.settings.data);
        })
      }
    }, created(){
        this.getData();
    }
  }
</script>
<style>

</style>


