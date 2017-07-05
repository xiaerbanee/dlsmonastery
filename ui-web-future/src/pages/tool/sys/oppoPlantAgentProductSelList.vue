<template>
  <div>
    <head-tab active="oppoPlantAgentProductSelList"></head-tab>
    <div  v-loading="loading" element-loading-text="正在同步工厂数据..." >
      <el-row>
        <div style="float:left">
          <el-button type="primary" @click="formSubmit" icon="check">保存</el-button>
          <el-button type="primary" @click="formVisible = true" icon="search">过滤</el-button>
        </div>
        <div style="float: left;margin-left: 10px">
          <date-picker v-model="date"></date-picker>
        </div>
        <div style="float: left;margin-left: 10px">
          <el-button  type="primary" @click="synData" icon="plus">工厂同步</el-button>
        </div>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('过滤')" v-model="formVisible"  size="small" class="search-form"  z-index="1500" ref="searchDialog">
        <el-form :model="formData"  ref="inputForm" >
          <el-row :gutter="8">
            <el-col :span="12">
              <el-form-item :label="$t('物料描述')" :label-width="formLabelWidth">
                <el-input v-model="formData.itemDesc" auto-complete="off" :placeholder="$t('模糊查询')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('物料编号')" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="formData.itemNumberStr" auto-complete="off" :placeholder="$t('模糊查询')"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="$t('型号')" :label-width="formLabelWidth">
                <el-input v-model="formData.productName" auto-complete="off" :placeholder="$t('模糊查询')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('确定')}}</el-button>
        </div>
      </search-dialog>
      <div ref="handsontable" style="height:1200px;overflow:hidden;margin-top:20px"></div>
    </div>
  </div>
</template>
<script>
  import util from "../../../utils/util";
  var table = null;
  export default {
    data(){
      return this.getData()
    },mounted() {
      axios.get('/api/global/tool/oppo/oppoPlantAgentProductSel/form').then((response) => {
        this.inputForm = response.data;
        if(this.inputForm.lx){
          this.settings.colHeaders.push("LX对应货品");
          this.settings.columns.push({data:'lxProductName',type: "autocomplete",allowEmpty: true,strict: true,productNames:[],source:this.productNames})
          this.settings.colHeaders.push("货品型号");
          this.settings.columns.push({data:'typeName',strict: true,readOnly: true});
          this.settings.columns[6].source = this.inputForm.extra.productNames;
          this.settings.columns[7].source = this.inputForm.extra.productNames;
        }else{
          this.settings.colHeaders.push("货品型号");
          this.settings.columns.push({data:'typeName',strict: true,readOnly: true});
          this.settings.columns[6].source = this.inputForm.extra.productNames;
        }
        //Handsontable初始化操作
        this.search();
        table = new Handsontable(this.$refs["handsontable"], this.settings);
      });
    },methods: {
      setSearchText() {
        this.$nextTick(function () {
          this.searchText = util.getSearchText(this.$refs.searchDialog);
        })
      },getData() {
        return {
          formData:{
          },
          inputForm:{
            extra:{}
          },
          loading:false,
          searchText:'',
          productNames:[],
          settings: {
            rowHeaders: true,
            autoColumnSize: true,
            stretchH: 'all',
            height: 650,
            startCols: 5,
            colHeaders: ['ID', '颜色Id', '颜色', '类型', '物料描述', '物料编码', 'TD对应货品'],
            columns: [
              {data:'id',strict: true,readOnly: true},
              {data:'colorId',strict: true,readOnly: true},
              {data:'colorName',strict: true,readOnly: true},
              {data:'brandType',strict: true,readOnly: true},
              {data:'itemDesc',strict: true,readOnly: true},
              {data:'itemNumber',strict: true,readOnly: true},
              {data:'productName',type: "autocomplete",allowEmpty: true,productNames:[],source:this.productNames}
            ],
            contextMenu: ['row_above', 'row_below', 'remove_row'],
          },
          date:util.currentDate(),
          rules: {},
          submitDisabled: false,
          formLabelWidth: '120px',
          formVisible: false,
        };
      },formSubmit(){
        this.submitDisabled = true;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.inputForm.dataList = new Array();
            let tableData = [];
            let list = table.getData();
            for (let item in list) {
              if (!table.isEmptyRow(item)) {
                let row = list[item];
                let createForm = {};
                if(this.inputForm.lx){
                  createForm.id = row[0];
                  createForm.productName = row[6];
                  createForm.lxProductName = row[7];
                }else{
                  createForm.id = row[0];
                  createForm.productName = row[6];
                }
                tableData.push(createForm);
              }
            }
            console.log(tableData.length);
            this.inputForm.dataList = JSON.stringify(tableData);
            axios.post('/api/global/tool/oppo/oppoPlantAgentProductSel/save', qs.stringify({data: this.inputForm.dataList}, {allowDots: true})).then((response) => {
              this.$message(response.data.message);
              this.submitDisabled = false;
              if(response.data.success){
                Object.assign(this.$data, this.getData());
                this.initPage();
              }
            }).catch( () => {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },getTableData(){
        let submitData = util.deleteExtra(this.formData);
        util.setQuery("oppoPlantAgentProductSelList",submitData);
        axios.get('/api/global/tool/oppo/oppoPlantAgentProductSel/filter', {params: submitData}).then((response) => {
          this.settings.data = response.data;
          console.log(response.data.length);
          table.loadData(this.settings.data);
        });
      },search() {
        this.formVisible = false;
        this.setSearchText();
        this.getTableData();
      },synData(){
        if(this.date){
          this.loading = true;
          axios.get('/api/ws/future/third/factory/oppo/synIme?date='+this.date).then((response)=>{
            this.loading = false;
            this.$message(response.data);
          }).catch(function () {
            this.loading = false;
            this.$message({message:"同步失败",type:'error'});
          });
        }else{
          this.$message({message:"请选择同步日期",type:'warning'});
        }
      },initPage(){
        axios.get('/api/global/tool/oppo/oppoPlantAgentProductSel/getQuery').then((response)=>{
          this.formData = response.data;
        })
      }
    },created(){
      this.initPage();
    }
  }
</script>
