<template>
  <div>
    <head-tab active="demoPhoneTypeOfficeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:demoPhone:edit'">{{$t('demoPhoneTypeOfficeList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:demoPhone:view'">{{$t('demoPhoneTypeOfficeList.filter')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('demoPhoneTypeOfficeList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.officeId.label" :label-width="formLabelWidth">
                <office-select v-model="formData.officeId"></office-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('demoPhoneTypeOfficeList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('demoPhoneList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed column-key="officeId" prop="officeName" :label="$t('demoPhoneTypeOfficeList.officeName')" sortable></el-table-column>
        <el-table-column column-key="demoPhoneTypeId" prop="demoPhoneTypeName" :label="$t('demoPhoneTypeOfficeList.demoPhoneType')" sortable></el-table-column>
        <el-table-column prop="qty" :label="$t('demoPhoneTypeOfficeList.qty')" sortable></el-table-column>
        <el-table-column fixed="right" prop="realQty" :label="$t('demoPhoneTypeOfficeList.realQty')"></el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import officeSelect from 'components/basic/office-select'
  export default {
    components:{officeSelect},
    data() {
      return {
        pageLoading: false,
        pageHeight:600,
        page:{},
        formData:{},
        submitData:{
          page:0,
          size:25,
          sort:"id,DESC",
          officeId:''
        },formLabel:{
          officeId:{label: this.$t('demoPhoneTypeOfficeList.officeName')}
        },
        formLabelWidth: '120px',
        formVisible: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.copyValue(this.formData,this.submitData);
        util.setQuery("demoPhoneTypeOfficeList",this.submitData);
        axios.get('/api/ws/future/basic/demoPhoneTypeOffice?'+qs.stringify(this.submitData)).then((response)  => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.sort=util.getSort(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'demoPhoneForm'})
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/basic/demoPhoneTypeOffice/getQuery').then((response) => {
        this.formData = response.data;
        util.copyValue(this.$route.query, this.formData);
        this.pageRequest();
      })
    }
  };
</script>


