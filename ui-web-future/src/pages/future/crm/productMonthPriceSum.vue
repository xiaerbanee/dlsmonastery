<template>
  <div>
    <head-tab active="productMonthPriceSum"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">过滤</el-button>
        <el-button type="primary" @click="  " icon="upload" >导出</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>

     <el-dialog :title="$t('productMonthPriceSum.filter')"  v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData" method="get" >
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="month" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.month" type="date" align="right"  :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="baokaStatus" :label-width="formLabelWidth">
                <el-select v-model="formData.statue" clearable filterable >
                  <!--el-option v-for="item in formProperty.statues" :key="item.id" :label="item.name" :value="item.id"></el-option-->
                </el-select>
              </el-form-item>
              <el-form-item :label="area" :label-width="formLabelWidth">
                <el-select v-model="formData.officeId" clearable filterable >
                  <!--el-option v-for="item in formProperty.offices" key="item.id" :label="item.name" :value="item.id"></el-option-->
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">搜索</el-button>
        </div>
      </el-dialog>

      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('expressOrderList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="" :label="办事处" ></el-table-column>
        <el-table-column prop="" :label="考核区域" ></el-table-column>
        <el-table-column prop="" :label="门店" ></el-table-column>
        <el-table-column prop="" :label="促销"></el-table-column>
        <el-table-column prop="" :label="数量合计"></el-table-column>
        <el-table-column prop="" :label="保卡合计"></el-table-column>
        <el-table-column prop="" :label="销售合计"></el-table-column>
        <el-table-column prop="" :label="销售总合计"></el-table-column></el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
    export default{
      data(){
          return{
            page:{},
            formData:{},
            submitData:{
              page:0,
              size:25,
            },
            formLabel:{
              month:{label:this.$t('productMonthPriceSum.month')},
              baokaStatus:{label:this.$t('productMonthPriceSum.baokaStatus')},
              area:{label:this.$t('productMonthPriceSum.area')},
            },
            formVisible: false
          }
      },
      methods:{
        pageRequest() {
          this.pageLoading = true;
          util.copyValue(this.formData,this.submitData);
          util.setQuery("productMonthPriceSum",this.submitData);
          axios.get('/api/ws/future/crm/productMonthPriceSum',{params:this.submitData}).then((response) => {
            this.page = response.data;
            this.pageLoading = false;
          })
        }
      },
      search() {
        this.formVisible = false;
        this.pageRequest();
      },
      created(){
        this.pageHeight = window.outerHeight -325;
        this.pageRequest();
      }
    }
</script>
