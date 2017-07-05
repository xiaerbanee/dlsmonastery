<template>
  <div>
    <head-tab active="voucherDetail"></head-tab>
    <div>
      <el-form :model="formData"  label-width="80px">
        <el-row :gutter="4">
          <el-col :span="12">
            <el-form-item label="编号">
              {{formData.id}}
            </el-form-item>
            <el-form-item label="凭证日期">
              {{formData.fdate}}
            </el-form-item>
            <el-form-item label="状态">
              {{formData.status}}
            </el-form-item>
            <el-form-item label="外部编码" >
              {{formData.outCode}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建人">
              {{formData.createdBy}}
            </el-form-item>
            <el-form-item label="创建时间">
              {{formData.createdDate}}
            </el-form-item>
            <el-form-item label="更新人">
              {{formData.lastModifiedBy}}
            </el-form-item>
            <el-form-item label="更新时间">
              {{formData.lastModifiedDate}}
            </el-form-item>
          </el-col>
        </el-row>
        <div id="grid" ref="handsontable" style="width:100%;height:600px;overflow:hidden;"></div>
      </el-form>
    </div>
  </div>
</template>
<script>
  import ElInput from "../../../../node_modules/element-ui/packages/input/src/input";
  import ElInputNumber from "../../../../node_modules/element-ui/packages/input-number/src/input-number";
  import ElFormItem from "../../../../node_modules/element-ui/packages/form/src/form-item";
  var table = null;
  export default {
    data() {
      return {
        table:null,
        settings: {
          rowHeaders:true,
          autoColumnSize:true,
          stretchH: 'all',
          minSpareRows: 10,
          height: 650,
          colHeaders: [],
          columns: [],
          data:{},
        },
        formData:{}
      };
    },
    mounted() {
      axios.get('/api/global/cloud/sys/voucher/detail',{params:{id:this.$route.query.id}}).then((response)=>{
        let colHeaders = response.data.headerList;
        this.settings.colHeaders = colHeaders;
        this.settings.columns.push({type: 'text', strict: true, allowEmpty: false,readOnly: true});
        this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: false, readOnly: true});
        for (let i=0;i<colHeaders.length;i++){
            if(colHeaders[i] === "供应商") {
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true,readOnly: true});
            }else if(colHeaders[i] === "客户"){
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, readOnly: true});
            }else if(colHeaders[i] === "银行账号"){
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true,readOnly: true});
            }else if(colHeaders[i] === "其他类"){
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, readOnly: true});
            }else if(colHeaders[i] === "部门") {
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, readOnly: true});
            }else if(colHeaders[i] === "费用类") {
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, readOnly: true});
            }else if(colHeaders[i] === "员工") {
              this.settings.columns.push({type: "autocomplete", strict: true, allowEmpty: true, readOnly: true});
            }
        }
        this.settings.columns.push({type: 'numeric', format:"0,0.00", allowEmpty: true, strict: true,readOnly: true});
        this.settings.columns.push({type: 'numeric', format:"0,0.00", allowEmpty: true, strict: true,readOnly: true});
        this.settings.data = response.data.data;
        table = new Handsontable(this.$refs["handsontable"], this.settings);
      });
    },
    methods: {
      initPage(){
        if (this.$route.query.id){
          axios.get('/api/global/cloud/sys/voucher/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            this.formData = response.data;
          });
        }
      }
    },created(){
    this.initPage();
    }
  }
</script>
