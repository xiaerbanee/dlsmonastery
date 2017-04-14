<template>
  <div>
    <head-tab active="金蝶数据库列表"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus">添加</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">过滤</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog title="过滤" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <!--<el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">-->
                <!--<el-date-picker v-model="formData.createdDate" type="daterange" align="right" :placeholder="$t('dictMapList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>-->
              <!--</el-form-item>-->
              <!--<el-form-item :label="formLabel.category.label" :label-width="formLabelWidth">-->
                <!--<el-select v-model="formData.category" filterable clearable :placeholder="$t('dictMapList.inputKey')">-->
                  <!--<el-option v-for="category in formProperty.category" :key="category" :label="category" :value="category"></el-option>-->
                <!--</el-select>-->
              <!--</el-form-item>-->
              <!--<el-form-item :label="formLabel.value.label" :label-width="formLabelWidth">-->
                <!--<el-input v-model="formData.value" auto-complete="off" :placeholder="$t('dictMapList.likeSearch')"></el-input>-->
              <!--</el-form-item>-->
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">确定</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" element-loading-text="拼命加载中，，，" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" label="id" sortable></el-table-column>
        <el-table-column prop="companyId" label="companyId" sortable></el-table-column>
        <el-table-column prop="name" label="name"></el-table-column>
        <el-table-column prop="type" label="type"></el-table-column>
        <el-table-column prop="kingdeeUrl" label="kingdeeUrl"></el-table-column>
        <el-table-column prop="kingdeePostUrl" label="kingdeePostUrl"></el-table-column>
        <el-table-column prop="kingdeeUsername" label="kingdeeUsername"></el-table-column>
        <el-table-column prop="kingdeePassword" label="kingdeePassword"></el-table-column>
        <el-table-column prop="kingdeeDbid" label="kingdeeDbid"></el-table-column>
        <el-table-column prop="remarks" label="remarks"></el-table-column>
        <el-table-column prop="lastModifiedBy" label="lastModifiedBy"></el-table-column>
        <el-table-column prop="lastModifiedDate" label="lastModifiedDate"></el-table-column>
        <el-table-column fixed="right" label="操作" width="140">
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
        page:{},
        formData:{
          pageNumber:0,
          pageSize:25,
          createdDate:'',
          createdDateBTW:'',
          category:'',
          value:''
        },formLabel:{
//          createdDateBTW:{label: this.$t('dictMapList.createdDate')},
//          category:{label: this.$t('dictMapList.category')},
//          value:{label: this.$t('dictMapList.value')}
        },
        pickerDateOption:util.pickerDateOption,
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading:false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
//        this.formData.createdDateBTW = util.formatDateRange(this.formData.createdDate);
//        util.setQuery("dictMapList",this.formData);
        axios.get('/api/sys/kingdeeBook/',{params:this.formData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.pageNumber = pageNumber;
        this.formData.pageSize = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getOrder(column);
        this.formData.pageNumber=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'dictMapForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'dictMapForm', query: { id: id }})
        } else if(action=="删除") {
          axios.get('/api/sys/dictMap/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
//      util.copyValue(this.$route.query,this.formData);
//      axios.get('/api/sys/kingdeeBook/page').then((response) =>{
//        this.formProperty = response.data;
//      });
      this.pageRequest();
    }
  };
</script>

