<template>
  <div>
    <head-tab active="dutySignList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary"@click="formVisible = true" icon="search">{{$t('dutySignList.filterOrExport')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" size="medium" :title="$t('dutySignList.filter')" v-model="formVisible" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData"  :label-width="formLabelWidth">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="$t('dutySignList.dutyDate')">
                <date-range-picker v-model="formData.dutyDate"></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('dutySignList.employeeName')">
                <el-input v-model="formData.employeeName" auto-complete="off" :placeholder="$t('dutySignList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('dutySignList.address')">
                <el-input v-model="formData.address" auto-complete="off" :placeholder="$t('dutySignList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="$t('dutySignList.officeName')">
                <el-select v-model="formData.officeName" filterable clearable :placeholder="$t('dutySignList.inputKey')">
                  <el-option v-for="office in formData.extra.officeList" :key="office.name" :label="office.name" :value="office.name"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('dutySignList.positionName')">
                <el-select v-model="formData.positionName" filterable clearable :placeholder="$t('dutySignList.inputKey')">
                  <el-option v-for="position in formData.extra.positionList" :key="position.name" :label="position.name" :value="position.name"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="exportData" icon="upload">{{$t('dutySignList.export')}}</el-button>
          <el-button type="primary" @click="search()">{{$t('dutySignList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('dutySignList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="employeeName" :label="$t('dutySignList.employeeName')" sortable></el-table-column>
        <el-table-column prop="dutyDate" :label="$t('dutySignList.dutyDate')"></el-table-column>
        <el-table-column prop="week" :label="$t('dutySignList.week')" ></el-table-column>
        <el-table-column prop="dutyTime" :label="$t('dutySignList.dutyTime')" ></el-table-column>
        <el-table-column prop="address" :label="$t('dutySignList.address')" ></el-table-column>
        <el-table-column prop="uuid" :label="$t('dutySignList.uuid')" ></el-table-column>
        <el-table-column prop="netType" :label="$t('dutySignList.netType')" ></el-table-column>
        <el-table-column prop="attachment" :label="$t('dutySignList.attachment')" >
          <template scope="scope">
            <img :src="scope.row.attachment" width="100px" height="100px"/>
          </template>
        </el-table-column>
        <el-table-column prop="status" :label="$t('dutySignList.status')">
          <template scope="scope">
            <el-tag :type="scope.row.status == '申请中' ? 'primary' : 'danger'">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>
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
        searchText:'',
        initPromise:{},
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
        util.setQuery("dutySignList",submitData);
        axios.get('/api/basic/hr/dutySign?'+qs.stringify(submitData)).then((response) => {
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
      },exportData(){
        this.formVisible = false;
        var submitData = util.deleteExtra(this.formData);
        window.location.href="/api/basic/hr/dutySign/export?"+qs.stringify(submitData);
			}
    },created () {
       this.pageHeight = 0.75*window.innerHeight;
      this.initPromise = axios.get('/api/basic/hr/dutySign/getQuery').then((response) =>{
        this.formData=response.data;
        util.copyValue(this.$route.query,this.formData);
      });
    },activated() {
      this.initPromise.then(()=> {
        this.pageRequest();
      })
    }
  };
</script>

