<template>
  <div>
    <head-tab active="productImeStockReport"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:productIme:stockReport'">{{$t('productImeStockReport.filter')}}</el-button>
        <el-button type="primary" @click="exportData" icon="check" v-permit="'crm:productIme:stockReport'">{{$t('productImeStockReport.export')}}</el-button>
        <el-button type="primary" @click="viewDetail" icon="check" v-permit="'crm:productIme:stockReport'">{{$t('productImeStockReport.viewDetail')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('productImeStockReport.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.sumType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.sumType" clearable>
                  <el-option v-for="item in formData.sumTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.outType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.outType" clearable>
                  <el-option v-for="item in formData.outTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.areaType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.areaType" clearable>
                  <el-option v-for="item in formData.areaTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.townType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.townType" clearable>
                  <el-option v-for="item in formData.townTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.date.label"  :label-width="formLabelWidth">
                <date-picker  v-model="formData.date"  ></date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.scoreType.label"  :label-width="formLabelWidth">
                <bool-select  v-model="formData.scoreType"  ></bool-select>
              </el-form-item>
              <el-form-item :label="formLabel.productIds.label"  :label-width="formLabelWidth">
                <product-select  v-model="formData.productIds"  multiple ></product-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('productImeStockReport.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading"    :element-loading-text="$t('productImeStockReport.loading')" @sort-change="sortChange" stripe border>
        <el-table-column  prop="depotName" :label="$t('productImeStockReport.depotName')" width="180" ></el-table-column>
        <el-table-column prop="qty" :label="$t('productImeStockReport.qty')"  ></el-table-column>
        <el-table-column prop="percent" :label="$t('productImeStockReport.percent')"></el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>

  import productSelect from 'components/future/product-select'

  export default{
    components:{
      productSelect,

    },
      data(){
          return{
            isCreate:this.$route.query.id===null,
            submitDisabled:false,

            formData:{},
            submitData:{
              sumType:'',
              outType:"",
              areaType:"",
              townType:"",
              date:"",
              scoreType:"",
              productIds:''
            },formLabel:{
              sumType:{label:this.$t('productImeStockReport.sumType')},
              outType:{label:this.$t('productImeStockReport.outType')},
              areaType:{label:this.$t('productImeStockReport.areaType')},
              townType:{label:this.$t('productImeStockReport.townType')},
              date:{label:this.$t('productImeStockReport.date')},
              scoreType:{label:this.$t('productImeStockReport.scoreType')},
              productIds:{label:this.$t('productImeStockReport.productIds')},

            },

          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              util.copyValue(this.reportScore, this.submitData);
              axios.post('/api/ws/future/crm/reportScore/save',qs.stringify(this.submitData)).then((response)=> {
                this.$message(response.data.message);
                this.submitDisabled = false;
                if(response.data.success){
                  if(this.isCreate){
                    form.resetFields();
                  } else {
                    this.$router.push({name:'reportScoreList',query:util.getQuery("reportScoreList")})
                  }
                }
              }).catch(function () {
                this.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        }
      },created(){
        axios.get('/api/ws/future/crm/productIme/getStockReportQuery').then((response)=>{
          this.formData=response.data;
        });
      }
    }
</script>
