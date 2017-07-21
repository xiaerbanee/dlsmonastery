<template>
  <div>
    <head-tab active="officeChangeForm"></head-tab>
    <div>
      <el-form :model="formData" method="get" ref="inputForm">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="机构名称"   prop="stockNumber">
              <el-select v-model="formData.id" filterable clearable remote placeholder="请输入关键词" :remote-method="remoteOffice" :loading="remoteLoading">
                <el-option v-for="item in officeList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-button type="primary" :disabled="submitDisabled" @click="getTableData" icon="view">搜索</el-button>
          </el-col>
        </el-row>
        <el-form-item label="原任务点位" >
          <span id="oldTaskPoint"></span>
        </el-form-item>
        <el-form-item label="修改后任务点位">
          <span  id="newTaskPoint"></span>
        </el-form-item>
        <div id="grid" ref="handsontable" style="width:100%;height:600px;overflow:hidden;"></div>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled = false " @click="formSubmit" icon="check">保存</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js';
  var table = null;
  var newTaskPoint = 0;
  var oldTaskPoint = 0;
  var setNewTaskPoint = function (datas) {

    let newTaskPoint=0;
    for(let i=0;i<datas.length; i++) {
      if(datas[i][7]) {
        newTaskPoint = newTaskPoint + datas[i][7]*1;
      }
    }
    document.getElementById("newTaskPoint").innerHTML = newTaskPoint;
  };
  var setOldTaskPoint = function (datas) {
    let oldTaskPoint=0;
    for(let i=0;i<datas.length; i++) {
      if(datas[i][4]) {
        oldTaskPoint = oldTaskPoint + datas[i][4]*1;
      }
    }
    document.getElementById("newTaskPoint").innerHTML = oldTaskPoint;
    document.getElementById("oldTaskPoint").innerHTML = oldTaskPoint;
  };
  export default {
    data() {
      return {
        officeList:{},
        settings: {
          rowHeaders:true,
          autoColumnSize:true,
          stretchH: 'all',
          height: 650,
          colHeaders: ['机构ID','上级名称','名称','类型',"任务点位",'修改后上级',"修改后名称","修改后任务点位"],
          columns: [
            {type:"text", data:"id", allowEmpty: false, readOnly: true, strict: true},
            {type: "text", data:"parentName", allowEmpty: false, readOnly: true,strict: true},
            {type: "text", data:"name", allowEmpty: true,readOnly: true, strict: true},
            {type: 'text', data:"type",allowEmpty: false,readOnly: true,},
            {type: "numeric", data:"taskPoint", allowEmpty: false,readOnly: true,},
            {type: "autocomplete", data:"parentName", allowEmpty: false, strict: true, officeName:[],source:this.officeName},
            {type: "text", data:"name", allowEmpty: true, strict: true},
            {type: "numeric", data:"taskPoint", allowEmpty: false}
          ],
          contextMenu: ['row_above', 'row_below', 'remove_row'],
          afterChange: function (changes, source) {
            if (source !== 'loadData') {
              for (let i = changes.length - 1; i >= 0; i--) {
                let row = changes[i][0];
                let column = changes[i][1];
                if (column === 'taskPoint') {//修改后任务点位
                  if (changes[i][3] !== '') {
                    setNewTaskPoint(table.getData());
                  }
                }
              }
            }
          }
        },
        formData:{
          id:'',
          json:[],
        },
        submitDisabled:false,
        remoteLoading:false
      };
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
            axios.post('/api/basic/sys/office/saveChange', qs.stringify(this.formData,{allowDots:true})).then((response)=> {
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
            util.setQuery("adPricesystemChangeForm", this.formData.id);
            axios.get('/api/basic/sys/office/change?id=' + this.formData.id).then((response) => {
              this.settings.data = response.data;
              table.loadData(this.settings.data);
              setOldTaskPoint(table.getData());
            });
          }
      },
      remoteOffice(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/basic/sys/office/findByNameLike',{params:{name:query}}).then((response)=>{
            this.officeList = response.data;
            this.remoteLoading = false;
          })
        } else {
          this.officeList = {};
        }
      },
    }
  }
</script>
