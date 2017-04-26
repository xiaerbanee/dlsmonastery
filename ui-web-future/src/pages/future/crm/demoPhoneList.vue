<template>
  <div>
    <head-tab active="demoPhoneList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:demoPhone:edit'">{{$t('demoPhoneList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:demoPhone:view'">{{$t('demoPhoneList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('demoPhoneList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.ime.label" :label-width="formLabelWidth">
                <el-input v-model="formData.ime" auto-complete="off" :placeholder="$t('demoPhoneList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off" :placeholder="$t('demoPhoneList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.demoPhoneType.label" :label-width="formLabelWidth">
              <el-input v-model="formData.demoPhoneType" auto-complete="off" :placeholder="$t('demoPhoneList.likeSearch')"></el-input>
            </el-form-item>
              <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.createdDate" type="daterange" align="right" :placeholder="$t('demoPhoneList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('demoPhoneList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('demoPhoneList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="productIme.ime" :label="$t('demoPhoneList.ime')"></el-table-column>
        <el-table-column prop="shopName" :label="$t('demoPhoneList.shopName')"></el-table-column>
        <el-table-column prop="demoPhoneType.name" :label="$t('demoPhoneList.demoPhoneType')"></el-table-column>
        <el-table-column prop="employee.name" :label="$t('demoPhoneList.employeeName')"></el-table-column>
        <el-table-column prop="status" :label="$t('demoPhoneList.status')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('demoPhoneList.createdDate')" sortable></el-table-column>
        <el-table-column prop="remarks" :label="$t('demoPhoneList.remarks')"></el-table-column>
        <el-table-column prop="locked" :label="$t('demoPhoneList.locked')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.locked ? 'danger' : 'primary'">{{scope.row.locked | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" :label="$t('demoPhoneList.enabled')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'danger' : 'primary'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('demoPhoneList.operation')" width="140">
          <template scope="scope">
            <div v-for="action in scope.row.actionList" :key="action" class="action">
              <el-button size="small" @click.native="itemAction(scope.row.id,action)">{{action}}</el-button>
            </div>
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
        pageLoading: false,
        pageHeight:600,
        page:{},
        formData:{
          page:0,
          size:25,
          ime:'',
          shopName:'',
          demoPhoneType:'',
          createdDate:'',
          createdDateBTW:'',
        },formLabel:{
          ime:{label: this.$t('demoPhoneList.ime')},
          shopName:{label: this.$t('demoPhoneList.shopName')},
          demoPhoneType:{label: this.$t('demoPhoneList.demoPhoneType')},
          createdDateBTW:{label: this.$t('demoPhoneList.createdDate')},
        },
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pickerDateOption:util.pickerDateOption
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formData.createdDateBTW=util.formatDateRange(this.formData.createdDate);
        util.setQuery("demoPhoneList",this.formData);
        axios.get('/api/crm/demoPhone',{params:this.formData}).then((response) => {
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
      },itemAdd(){
        this.$router.push({ name: 'demoPhoneForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'demoPhoneForm', query: { id: id }})
        } else if(action=="删除") {
          axios.get('/api/crm/demoPhone/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.pageRequest();
    }
  };
</script>


