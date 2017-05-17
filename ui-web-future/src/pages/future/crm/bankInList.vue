<template>
  <div>
    <head-tab active="bankInList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:bankIn:edit'">{{$t('bankInList.add')}}</el-button>
        <el-button type="primary" @click="batchPass" icon="check" v-permit="'crm:bankIn:audit'">{{$t('bankInList.batchPass')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:bankIn:view'">{{$t('bankInList.filter')}}</el-button>
        <search-tag  :formData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('bankInList.filter')" v-model="formVisible" size="large" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="10">
              <el-form-item :label="formLabel.id.label" :label-width="formLabelWidth">
                <el-input v-model="formData.id" auto-complete="off" :placeholder="$t('bankInList.preciseSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off" :placeholder="$t('bankInList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.billDateRange.label" :label-width="formLabelWidth">
                <date-range-picker  v-model="formData.billDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="formLabel.amount.label" :label-width="formLabelWidth">
                <el-input v-model="formData.amount" auto-complete="off" :placeholder="$t('bankInList.preciseSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.inputDateRange.label" :label-width="formLabelWidth">
                <date-range-picker  v-model="formData.inputDateRange"></date-range-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="formLabel.outCode.label" :label-width="formLabelWidth">
                <el-input v-model="formData.outCode" auto-complete="off" :placeholder="$t('bankInList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.bankName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.bankName" auto-complete="off" :placeholder="$t('bankInList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.processStatus.label" :label-width="formLabelWidth">
                <el-select v-model="formData.processStatus" clearable filterable :placeholder="$t('bankInList.selectProcessStatus')">
                  <el-option v-for="status in formData.processStatusList" :key="status" :label="status" :value="status"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.createdBy.label" :label-width="formLabelWidth">
                <account-select  v-model="formData.createdBy" ></account-select>
              </el-form-item>
              <el-form-item :label="formLabel.createdDateRange.label" :label-width="formLabelWidth">
                <date-range-picker  v-model="formData.createdDateRange" ></date-range-picker>
              </el-form-item>
              <el-form-item :label="formLabel.serialNumber.label" :label-width="formLabelWidth">
                <el-input v-model="formData.serialNumber" auto-complete="off" :placeholder="$t('bankInList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('bankInList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" @selection-change="selectionChange"  :element-loading-text="$t('bankInList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column type="selection" width="55" :selectable="checkSelectable"></el-table-column>
        <el-table-column fixed prop="formatId" :label="$t('bankInList.id')" sortable width="160"></el-table-column>
        <el-table-column prop="shopName" :label="$t('bankInList.shopName')" sortable></el-table-column>
        <el-table-column prop="shopClientName" :label="$t('bankInList.shopClientName')" width="140" sortable></el-table-column>
        <el-table-column prop="bankName" :label="$t('bankInList.bankName')" sortable></el-table-column>
        <el-table-column prop="amount" :label="$t('bankInList.amount')" sortable></el-table-column>
        <el-table-column prop="serialNumber" :label="$t('bankInList.serialNumber')"sortable></el-table-column>
        <el-table-column prop="billDate" :label="$t('bankInList.billDate')" width="140" sortable></el-table-column>
        <el-table-column prop="inputDate" :label="$t('bankInList.inputDate')" width="140" sortable></el-table-column>
        <el-table-column prop="createdByName" :label="$t('bankInList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('bankInList.createdDate')"></el-table-column>
        <el-table-column prop="outCode" :label="$t('bankInList.outCode')"></el-table-column>
        <el-table-column prop="processStatus" :label="$t('bankInList.processStatus')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('bankInList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('bankInList.operation')" width="100">
          <template scope="scope">
            <el-button size="small"  type="text"  v-permit="'crm:bankIn:view'" @click.native="itemAction(scope.row.id, 'detail')">{{$t('bankInList.detail')}}</el-button>
            <el-button size="small"  type="text" v-if="scope.row.auditable"   v-permit="'crm:bankIn:audit'" @click.native="itemAction(scope.row.id, 'audit')">{{$t('bankInList.audit')}}</el-button>
            <el-button size="small"   type="text"  v-if="scope.row.editable"   v-permit="'crm:bankIn:edit'" @click.native="itemAction(scope.row.id, 'edit')">{{$t('bankInList.edit')}}</el-button>
             <el-button size="small"  type="text"   v-if="scope.row.editable" v-permit="'crm:bankIn:delete'" @click.native="itemAction(scope.row.id, 'delete')">{{$t('bankInList.delete')}}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  import accountSelect from 'components/basic/account-select'

  export default{
    components:{
      accountSelect,

    },
    data() {
      return {
        pageLoading: false,
        pageHeight:600,
        page:{},
        formData:{},
        submitData:{
          page:0,
          size:25,
          id:'',
          shopName:"",
          billDateRange:"",
          amount:"",
          inputDateRange:"",
          outCode:"",
          bankName:"",
          processStatus:"",
          createdBy:"",
          createdDateRange:"",
          serialNumber:""
        },formLabel:{
          id:{label:this.$t('bankInList.id')},
          shopName:{label:this.$t('bankInList.shopName')},
          billDateRange:{label:this.$t('bankInList.billDate')},
          amount:{label:this.$t('bankInList.amount')},
          inputDateRange:{label:this.$t('bankInList.inputDate')},
          outCode:{label:this.$t('bankInList.outCode')},
          bankName:{label:this.$t('bankInList.bankName')},
          processStatus:{label:this.$t('bankInList.processStatus')},
          createdBy:{label:this.$t('bankInList.createdBy')},
          createdDateRange:{label:this.$t('bankInList.createdDate')},
          serialNumber:{label:this.$t('bankInList.serialNumber')}
        },
        formLabelWidth: '120px',
        formVisible: false,
        selects:new Array(),
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.copyValue(this.formData,this.submitData);
        util.setQuery("bankInList",this.submitData);
        axios.get('/api/ws/future/crm/bankIn?'+qs.stringify(this.submitData)).then((response) => {
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
        this.$router.push({ name: 'bankInForm'})
      },itemAction:function(id, action){
        if(action=="edit") {
          this.$router.push({ name: 'bankInForm', query: { id: id }})
        }else if(action=="detail"){
          this.$router.push({ name: 'bankInDetail', query: { id: id}})
        }else if(action=="audit"){
          this.$router.push({ name: 'bankInAudit', query: { id: id}})
        }else if(action=="delete"){

          util.confirmBeforeDelRecord(this).then(() => {
            axios.get("/api/ws/future/crm/bankIn/delete",{params:{id:id}}).then((response)=>{
              this.$message(response.data.message);
              this.pageRequest();
            })
          });
        }
      },checkSelectable(row) {
        return row.processStatus !== '已通过' && row.processStatus !== '未通过'
      },selectionChange(selection){
        console.log(selection);
        this.selects=new Array();
        for(var key in selection){
          this.selects.push(selection[key].id);
        }
      },batchPass(){
        axios.get('/api/ws/future/crm/bankIn/batchAudit',{params:{ids:this.selects,pass:true}}).then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        });
      }
    },created () {
      var that = this;
      that.pageHeight = window.outerHeight -320;
      axios.get('/api/ws/future/crm/bankIn/getQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(that.$route.query,that.formData);
        that.pageRequest();
      });

    }
  };
</script>

