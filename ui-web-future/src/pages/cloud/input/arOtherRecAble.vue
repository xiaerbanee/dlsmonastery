<template>
  <div>
    <head-tab active="arOtherRecAble"></head-tab>
    <div>
      <el-form :model="formData" method="get" ref="inputForm" :rules="rules" :inline="true">
        <el-form-item label="日期"  prop="billDate">
          <date-picker v-model="formData.billDate"></date-picker>
        </el-form-item>
        <el-button type="primary" :disabled="submitDisabled"  @click="formSubmit" icon="check">保存</el-button>
        <div id="grid" ref="handsontable" style="width:100%;height:600px;overflow:hidden;"></div>
      </el-form>
    </div>
  </div>
</template>
<script>
  var table = null;
  export default {
    data() {
      return {
        table:null,
        settings: {
          rowHeaders:true,
          autoColumnSize:true,
          stretchH: 'all',
          height: 650,
          minSpareRows: 1,
          colHeaders: ["客户", "用承担部门", "对方科目", "金额", "备注", '员工', "其他类", "费用类", '对方关联客户'],
          columns: [
            {type: "autocomplete", strict: true, allowEmpty: false, customerName:[],source: this.customerName},
            {type: "autocomplete", strict: true, allowEmpty: false, departmentName:[],source: this.departmentName},
            {type: "autocomplete", strict: true, allowEmpty: false, accountName:[],source: this.accountName},
            {type: 'numeric', format:"0,0.00", allowEmpty: false, strict: true},
            {type: "text", allowEmpty: true, strict: true},
            {type: "autocomplete", strict: true, allowEmpty: false, staffName:[],source: this.staffName},
            {type: "autocomplete", strict: true, allowEmpty: false, otherTypeName:[],source: this.otherTypeName},
            {type: "autocomplete", strict: true, allowEmpty: false, expenseTypeName:[],source: this.expenseTypeName},
            {type: "autocomplete", strict: true, allowEmpty: false, customerForName:[],source: this.customerForName},
          ],
          contextMenu: ['row_above', 'row_below', 'remove_row'],
          afterChange: function (changes, source) {
            if (source !== 'loadData') {
              let data=table.getData();
              for(let i=0;i<data.length; i++) {
                let otherTypeName = "";
                let accountNumber = "";
                if(data[i][2]) {
                  accountNumber =data[i][2];
                }
                if(data[i][6]) {
                  otherTypeName = data[i][6];
                }
                if(otherTypeName !== "" && accountNumber !== "" && otherTypeName!== '无'){
                  axios.get('/api/global/cloud/kingdee/basAssistant/findNumberSubByName?name=' + otherTypeName).then((response) => {
                    let number = response.data;
                    if (accountNumber !== number){
                      table.setDataAtCell(i, 6, '');
                      let j = i=1;
                      alert('第'+j+"其他类的编码前4位必须和对应科目的编码一致");
                    }
                  });
                }
              }
            }
          }
        },
        formData:{
          billDate:new Date().toLocaleDateString(),
          json:[],
        },rules: {
          billDate: [{ required: true, message: '必填项'}],
        },
        submitDisabled:false
      };
    },
    mounted() {
      axios.get('/api/global/cloud/input/arOtherRecAble/form').then((response)=>{
        let extra = response.data.extra;
        this.settings.columns[0].source = extra.customerNameList;
        this.settings.columns[1].source = extra.departmentNameList;
        this.settings.columns[2].source = extra.accountNameList;
        this.settings.columns[5].source = extra.staffNameList;
        this.settings.columns[6].source = extra.otherTypeNameList;
        this.settings.columns[7].source = extra.expenseTypeNameList;
        this.settings.columns[8].source = extra.customerNameList;
        table = new Handsontable(this.$refs["handsontable"], this.settings);
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
            this.formData.billDate = util.formatLocalDate(this.formData.billDate);
            axios.post('/api/global/cloud/input/arOtherRecAble/save', qs.stringify(this.formData,{allowDots:true})).then((response)=> {
              this.$message(response.data.message);
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      }
    }
  }
</script>
