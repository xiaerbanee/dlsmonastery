<template>
  <div>
    <head-tab active="dutyAnnualList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'hr:dutyAnnual:edit'">{{$t('dutyAnnualList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search">{{$t('dutyAnnualList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible=false" :title="$t('bankList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" :label-width="formLabelWidth">
              <el-form-item :label="$t('dutyAnnualList.employeeName')">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('dutyAnnualList.likeSearch')"></el-input>
              </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('dutyAnnualList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('dutyAnnualList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="officeName" :label="$t('dutyAnnualList.officeName')" ></el-table-column>
        <el-table-column prop="positionName" :label="$t('dutyAnnualList.positionName')" ></el-table-column>
        <el-table-column prop="employeeName" :label="$t('dutyAnnualList.employeeName')"></el-table-column>
        <el-table-column prop="annualYear" :label="$t('dutyAnnualList.annualYear')"></el-table-column>
        <el-table-column prop="hour" :label="$t('dutyAnnualList.hour')" ></el-table-column>
        <el-table-column prop="leftHour" :label="$t('dutyAnnualList.leftHour')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('dutyAnnualList.createdDate')"></el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        page:{},
        formData:{
          extra:{}
        },
        initPromise:{},
        searchText:'',
        formLabelWidth: '25%',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      setSearchText() {
        this.$nextTick(function () {
          this.searchText = util.getSearchText(this.$refs.searchDialog);
        });
      },
      pageRequest() {
        this.pageLoading = true;
        this.setSearchText();
        var submitData = util.deleteExtra(this.formData);
        util.setQuery("dutyAnnualList",submitData);
        axios.get('/api/basic/hr/dutyAnnual?'+qs.stringify(submitData)).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getSort(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'dutyAnnualForm'})
      },
    },created () {
       this.pageHeight = 0.75*window.innerHeight;
      this.initPromise = axios.get('/api/basic/hr/dutyAnnual/getQuery').then((response)=> {
        this.formData = response.data;
        util.copyValue(this.$route.query,this.formData);
      })

    },activated() {
      this.initPromise.then(()=> {
        this.pageRequest();
      })
    }
  };
</script>

