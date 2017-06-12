<template>
  <div>
    <head-tab active="dutyTripList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('dutyTripList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('dutyTripList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
              <el-form-item :label="$t('dutyTripList.date')" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.dutyDate"></date-range-picker>
              </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('dutyTripList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('dutyTripList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="dateStart" :label="$t('dutyTripList.dateStart')"></el-table-column>
        <el-table-column prop="dateEnd"  :label="$t('dutyTripList.dateEnd')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('dutyTripList.reason')"></el-table-column>
        <el-table-column prop="status" :label="$t('dutyTripList.status')">
          <template scope="scope">
            <el-tag :type="scope.row.status == '已通过' ? 'primary' : 'danger'">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import SearchDialog from "../../../components/common/search-dialog.vue";

  export default {
    components: {SearchDialog},
    data() {
      return {
        page:{},
        formData:{
          extra:{},
          dutyDate:'',
        },
        searchText:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
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
        util.setQuery("dutyTripList",submitData);
        axios.get('/api/basic/hr/dutyTrip?'+qs.stringify(submitData)).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getOrder(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.pageRequest();
    }
  };
</script>

