<template>
  <div>
    <head-tab active="shopPrintList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopPrint:edit'">{{$t('shopPrintList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:shopPrint:view'">{{$t('shopPrintList.filter')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('shopPrintList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.officeId.label" :label-width="formLabelWidth">
                <office-select v-model="formData.officeId"></office-select>
              </el-form-item>
              <el-form-item :label="formLabel.printType.label" :label-width="formLabelWidth">
                <dict-map-select v-model="formData.printType" category="门店_广告印刷"></dict-map-select>
              </el-form-item>
              <el-form-item :label="formLabel.processStatus.label" :label-width="formLabelWidth">
              <el-select v-model="formData.processStatus" filterable clearable :placeholder="$t('shopPrintList.inputKey')">
                <el-option v-for="item in formData.processStatusList" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
              <el-form-item :label="formLabel.createdBy.label" :label-width="formLabelWidth">
                <account-select  v-model="formData.createdBy"></account-select>
            </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('shopPrintList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('shopPrintList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('shopPrintList.code')" sortable width="150"></el-table-column>
        <el-table-column prop="officeName" :label="$t('shopPrintList.officeId')"></el-table-column>
        <el-table-column prop="printType" :label="$t('shopPrintList.printType')"></el-table-column>
        <el-table-column prop="qty" :label="$t('shopPrintList.qty')"></el-table-column>
        <el-table-column prop="content":label="$t('shopPrintList.content')" width="450"></el-table-column>
        <el-table-column prop="address" :label="$t('shopPrintList.address')" width="150"></el-table-column>
        <el-table-column prop="contator" :label="$t('shopPrintList.contact')"></el-table-column>
        <el-table-column prop="mobilePhone" :label="$t('shopPrintList.mobilePhone')"></el-table-column>
        <el-table-column prop="createdByName" :label="$t('shopPrintList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('shopPrintList.createdDate')"></el-table-column>
        <el-table-column prop="processStatus" :label="$t('shopPrintList.processStatus')" width="150">
          <template scope="scope">
            <el-tag :type="scope.row.processStatus=='已通过' ? 'primary' : 'danger'">{{scope.row.processStatus}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('shopPrintList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" v-permit="'crm:shopPrint:view'" @click.native="itemAction(scope.row.id,'detail')">{{$t('shopPrintList.detail')}}</el-button>
            <el-button size="small" v-if="scope.row.isAuditable" v-permit="'crm:shopPrint:edit'" @click.native="itemAction(scope.row.id,'audit')">{{$t('shopPrintList.audit')}}</el-button>
            <el-button size="small" v-if="scope.row.isEditable" v-permit="'crm:shopPrint:edit'" @click.native="itemAction(scope.row.id,'edit')">{{$t('shopPrintList.edit')}}</el-button>
            <el-button size="small" v-if="scope.row.isEditable" v-permit="'crm:shopPrint:delete'" @click.native="itemAction(scope.row.id,'delete')">{{$t('shopPrintList.delete')}}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import officeSelect from 'components/basic/office-select';
  import dictMapSelect from 'components/basic/dict-map-select';
  import accountSelect from 'components/basic/account-select'
  export default {
    components:{officeSelect,dictMapSelect,accountSelect},
    data() {
      return {
        page:{},
        formData:{},
        submitData:{
          page:0,
          size:25,
          officeId:'',
          printType:'',
          processStatus:'',
          createdBy:''
        },
        formLabel:{
          officeId:{label:this.$t('shopPrintList.officeId'),value:""},
          printType:{label:this.$t('shopPrintList.printType'),value:""},
          processStatus:{label:this.$t('shopPrintList.processStatus')},
          createdBy:{label:this.$t('shopPrintList.createdBy')}
        },
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formLabel.officeId.value = util.getLabel(this.formProperty.areas,this.formData.officeId);
        util.copyValue(this.formData,this.submitData);
        util.setQuery("shopPrintList",this.submitData);
        axios.get('/api/ws/future/layout/shopPrint',{params:this.submitData}).then((response) => {
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
        this.$router.push({ name: 'shopPrintForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'shopPrintForm', query: { id: id }})
        } else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/layout/shopPrint/delete', {params: {id: id}}).then((response) => {
              this.$message(response.data.message);
              this.pageRequest();
            })
          })
        }else if(action == "detail"||action == "audit"){
           this.$router.push({ name: 'shopPrintDetail', query: { id: id,action:action}});
        }
      }
    },created () {
        var that = this;
        that.pageHeight = window.outerHeight -320;
        axios.get('/api/ws/future/layout/shopPrint/getQuery').then((response) =>{
          that.formData=response.data;
          util.copyValue(that.$route.query,that.formData);
          that.pageRequest();
        });
    }
  };
</script>

