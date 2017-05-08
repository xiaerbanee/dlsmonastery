<template>
  <div>
    <head-tab active="accountChangeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus">{{$t('accountChangeList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('accountChangeList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel = "formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('accountChangeList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-form-item :label="formLabel.createdDate.label" :label-width="formLabelWidth">
            <date-range-picker v-model="formData.createdDate"></date-range-picker>
          </el-form-item>
          <el-form-item :label="formLabel.createdByName.label" :label-width="formLabelWidth">
            <el-input v-model="formData.createdByName" auto-complete="off" :placeholder="$t('accountChangeList.likeSearch')"></el-input>
          </el-form-item>
          <el-form-item :label="formLabel.officeId.label" :label-width="formLabelWidth">
            <el-select v-model="formData.officeId" filterable clearable :placeholder="$t('accountChangeList.inputKey')">
              <el-option v-for="area in formData.areas" :key="area.id" :label="area.name" :value="area.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="formLabel.type.label" :label-width="formLabelWidth">
            <el-select v-model="formData.type" clearable filterable :placeholder="$t('accountChangeList.selectGroup')">
              <el-option v-for="type in formData.types" :key="type" :label="type" :value="type"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('accountChangeList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('accountChangeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('accountChangeList.id')" sortable></el-table-column>
        <el-table-column  prop="areaName" :label="$t('accountChangeList.areaName')" sortable></el-table-column>
        <el-table-column  prop="createdByName" :label="$t('accountChangeList.applyAccount')" sortable></el-table-column>
        <el-table-column  prop="createdDate" :label="$t('accountChangeList.applyDate')" sortable></el-table-column>
        <el-table-column  prop="type" :label="$t('accountChangeList.type')" sortable></el-table-column>
        <el-table-column  prop="oldValue" :label="$t('accountChangeList.oldValue')" sortable></el-table-column>
        <el-table-column  prop="newValue" :label="$t('accountChangeList.newValue')" sortable></el-table-column>
        <el-table-column  prop="processStatus" :label="$t('accountChangeList.processStatus')" sortable ></el-table-column>
        <el-table-column  prop="remarks" :label="$t('accountChangeList.remarks')" sortable ></el-table-column>
        <el-table-column :label="$t('accountChangeList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" @click.native="itemAction(scope.row.id,'修改')">修改</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'删除')">删除</el-button>
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
          createdDate:'',
        },formLabel:{
          createdDate:{label:this.$t('accountChangeList.createdDate')},
          createdByName:{label:this.$t('accountChangeList.createdBy')},
          type:{label:this.$t('accountChangeList.type')},
          officeId:{label:this.$t('accountChangeList.areaName'),value:''}
        },
        submitData:{
          page:0,
          size:25,
          officeId:'',
          createdDate:'',
          createdByName:'',
          type:''
        },
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formLabel.officeId.value=util.getLabel(this.formData.areas, this.formData.officeId);
        util.setQuery("accountChangeList",this.submitData);
        util.copyValue(this.formData,this.submitData);
        axios.get('/api/basic/hr/accountChange?'+qs.stringify(this.submitData)).then((response) => {
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
        this.$router.push({ name: 'accountChangeForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'accountChangeForm', query: { id: id }})
        } else if(action="删除") {
          axios.get('/api/basic/hr/accountChange/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      }},created () {
        var that=this;
      this.pageHeight = window.outerHeight -120;
      axios.get('/api/basic/hr/accountChange/getQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(that.$route.query,that.formData);
        that.pageRequest();
      });
    }
  };
</script>

