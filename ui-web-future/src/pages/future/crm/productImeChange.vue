<template>
  <div>
    <head-tab active="productImeChange"></head-tab>
    <search-dialog :title="$t('productImeChange.filter')" v-model="searchFormVisible" size="small" class="search-form" z-index="1500">
      <el-form  label-width="120px">
        <el-row :gutter="4">
          <el-col :span="12">
            <el-form-item :label="$t('productImeChange.ime')" prop="imeStr">
              <el-input type="textarea" :rows="6" v-model="imeStr"  ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="search" icon="search">{{$t('productImeChange.sure')}}</el-button>
      </div>
    </search-dialog>
    <div>
      <el-form  :model="inputForm"  ref="inputForm"  :rules="rules"  label-width="120px">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-button type="primary" @click="formSubmit" icon="check">保存</el-button>
            <el-button type="primary" @click="searchFormVisible = true"  >过滤</el-button>
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
<script>

  var table = null;
  export default {

    data(){
      return this.getData()
    },

    methods: {
      getData() {
        return {
          searchFormVisible:false,
          imeStr:'',
          inputForm: {
              extra:{},
          },
          settings: {
            rowHeaders: true,
            minSpareRows: 100,
            startRows: 100,
            startCols: 4,
            colHeaders: [ '串码', '仓库', '产品型号',  '调整后型号'],
            columns: [{
              data: "ime",
              strict: true,
              allowEmpty: true,
              width: 200
            }, {
              data: "depotName",
              readOnly: true,
              width: 300
            }, {
              data: "productName",
              readOnly: true,
              width: 300
            }, {
              type: "autocomplete",
              allowEmpty: false,
              strict: true,
              source: [],
              width: 300
            }],
            contextMenu: ['row_above', 'row_below', 'remove_row'],
          }, rules: {},
          submitDisabled: false,
        };
      },
      search() {
        axios.get('/api/ws/future/crm/productIme/findDtoListByImes', {params: {imeStr:this.imeStr}}).then((response) => {
          table.loadData(response.data);
        })
      },
      formSubmit(){
        this.submitDisabled = true;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            let tableData = [];
            let list = table.getData();
            for (let item in list) {
              if (!table.isEmptyRow(item)) {
                let row = list[item];
                let changeForm = {};
                changeForm.ime = row[0];
                changeForm.productName = row[3];
                tableData.push(changeForm);
              }
            }
            this.inputForm.productImeChangeFormList = tableData;

            axios.post('/api/ws/future/crm/productIme/batchChange', qs.stringify(util.deleteExtra(this.inputForm), {allowDots: true})).then((response) => {
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
      },initPage(){
        axios.get('/api/ws/future/crm/productIme/getBatchChangeForm').then((response)=>{
          this.inputForm = response.data;
          this.settings.columns[3].source = response.data.extra.productNameList;
          table = new Handsontable(this.$refs["handsontable"], this.settings);
        });
      }
    },
    created() {
        this.initPage();
    },
  }
</script>
