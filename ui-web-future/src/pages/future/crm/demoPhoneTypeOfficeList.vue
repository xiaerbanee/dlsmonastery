<template>
  <div>
    <head-tab active="demoPhoneTypeOfficeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:demoPhone:edit'">{{$t('demoPhoneTypeOfficeList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" v-permit="'crm:demoPhone:view'">{{$t('demoPhoneTypeOfficeList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('demoPhoneTypeOfficeList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('demoPhoneTypeOfficeList.officeName')" :label-width="formLabelWidth">
                <el-select v-model="formData.areaId" filterable clearable >
                  <el-option v-for="item in formData.extra.areaList" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('demoPhoneTypeOfficeList.sure')}}</el-button>
        </div>
      </search-dialog>
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
        searchText:"",
        formData:{
            extra:{}
        },
        formLabelWidth: '120px',
        formVisible: false,
      };
    },
    methods: {
      setSearchText() {
        this.$nextTick(function () {
          this.searchText = util.getSearchText(this.$refs.searchDialog);
        })
      },
      pageRequest() {
        this.pageLoading = true;
        this.setSearchText();
        var submitData = util.deleteExtra(this.formData);
        util.setQuery("demoPhoneTypeOfficeList",submitData);
        axios.get('/api/ws/future/basic/demoPhoneTypeOffice?'+qs.stringify(submitData)).then((response)  => {
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


