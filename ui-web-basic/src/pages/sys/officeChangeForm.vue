<template>
  <div>
    <head-tab active="officeChangeForm"></head-tab>
    <div>
      <el-form :model="formData" method="get" ref="inputForm" :rules="rules">
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form-item :label="$t('officeChangeForm.stockNumber')"   prop="stockNumber">
              <office-select v-model="formData.id"></office-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="getTableData" icon="view">{{$t('officeChangeForm.search')}}</el-button>
              <el-button type="primary" :disabled="submitDisabled = false " @click="formSubmit" icon="check">{{$t('officeChangeForm.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item :label="$t('officeChangeForm.oldPoint')" >
          <span id="oldPoint"></span>
        </el-form-item>
        <el-form-item :label="$t('officeChangeForm.newPoint')">
          <span  id="newPoint"></span>
        </el-form-item>
        <div id="grid" ref="handsontable" style="width:100%;height:600px;overflow:hidden;"></div>
      </el-form>
    </div>
  </div>
</template>
<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
<script>
  import officeSelect from 'components/basic/office-select';
  import Handsontable from 'handsontable/dist/handsontable.full.js';
  var table = null;
  var newPoint = 0;
  var oldPoint = 0;
  var setNewPoint = function (datas) {
    let newPoint=0;
    for(let i=0;i<datas.length; i++) {
      if(datas[i][7]) {
        newPoint = newPoint + datas[i][7]*1;
      }
    }
    document.getElementById("newPoint").innerHTML = newPoint;
  };
  var setOldPoint = function (datas) {
    let oldPoint=0;
    for(let i=0;i<datas.length; i++) {
      if(datas[i][4]) {
        oldPoint = oldPoint + datas[i][4]*1;
      }
    }
    document.getElementById("newPoint").innerHTML = oldPoint;
    document.getElementById("oldPoint").innerHTML = oldPoint;
  };
  export default {
    components:{
      officeSelect
    },
    data() {
      return {
        officeList: {},
        settings: {
          rowHeaders: true,
          autoColumnSize: true,
          stretchH: 'all',
          height: 650,
          colHeaders: ['机构ID', '上级名称', '名称', '类型', "任务点位", '修改后上级', "修改后名称", "修改后任务点位"],
          columns: [
            {type: "text", data: "id", allowEmpty: false, readOnly: true, strict: true},
            {type: "text", data: "parentName", allowEmpty: false, readOnly: true, strict: true},
            {type: "text", data: "name", allowEmpty: true, readOnly: true, strict: true},
            {type: 'text', data: "type", allowEmpty: false, readOnly: true,},
            {type: "numeric", data: "point",  format:"0,0.00000",allowEmpty: false, readOnly: true,},
            {type: "autocomplete", data: "newParentName", allowEmpty: false, strict: true, officeName: [], source: this.officeName},
            {type: "text", data: "newName", allowEmpty: true, strict: true},
            {type: "numeric", data: "newPoint",  format:"0,0.00000",allowEmpty: false}
          ],
         contextMenu: util.contextMenu(this.$store.state.global.lang),
          afterChange: function (changes, source) {
            if (source !== 'loadData') {
              for (let i = changes.length - 1; i >= 0; i--) {
                let row = changes[i][0];
                let column = changes[i][1];
                if (column === 'point') {//修改后任务点位
                  if (changes[i][3] !== '') {
                    setNewPoint(table.getData());
                  }
                }
              }
            }
          }
        },
        formData: {
          id: '',
          json: [],
        },
        submitDisabled: false,
        remoteLoading: false,
        rules: {
          id: [{required: true, message: '必填项'}],
        },
      }
    },
    mounted() {
      axios.get('/api/basic/sys/office/getForm').then((response)=>{
        let extra = response.data.extra;
        this.settings.columns[5].source = extra.officeNameList;
        table = new Handsontable(this.$refs["handsontable"], this.settings);
        this.getTableData();
      });
    },
    methods: {
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.formData.json =new Array();
            let list = table.getData();
            for(let item in list){
              if(!table.isEmptyRow(item)){
                this.formData.json.push(list[item]);
              }
            }
            this.formData.json = JSON.stringify(this.formData.json);
            axios.post('/api/basic/hr/officeChange/save', qs.stringify(this.formData,{allowDots:true})).then((response)=> {
              if(response.data.success){
                this.$message(response.data.message);
              }else{
                this.$alert(response.data.message);
                this.submitDisabled = false;
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else {
            this.submitDisabled = false;
          }
        })
      },getTableData(){
          if (this.formData.id !== '') {
            util.setQuery("officeChangeForm", this.formData.id);
            axios.get('/api/basic/hr/officeChange/findByOfficeId?officeId=' + this.formData.id).then((response) => {
              this.settings.data = response.data;
              table.loadData(this.settings.data);
              setOldPoint(table.getData());
            });
          }
      },
    }
  }
</script>
