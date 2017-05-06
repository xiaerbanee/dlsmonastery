<template>
  <div>
    <head-tab active="storeAllotList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:storeAllot:edit'">{{$t('storeAllotList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:storeAllot:view'">{{$t('storeAllotList.filterOrExport')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('storeAllotList.filter')" v-model="formVisible" size="small" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="formLabel.createdDateRange.label" :label-width="formLabelWidth">
                <su-date-range-picker v-model="formData.createdDateRange" ></su-date-range-picker>
              </el-form-item>
              <el-form-item :label="formLabel.status.label" :label-width="formLabelWidth" >
                <el-select v-model="formData.status" clearable>
                  <el-option v-for="item in formData.statusList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.remarks.label" :label-width="formLabelWidth">
                <el-input v-model="formData.remarks" auto-complete="off" :placeholder="$t('storeAllotList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="formLabel.fromStoreId.label" :label-width="formLabelWidth">
                <su-depot type="store" v-model="formData.fromStoreId"  ></su-depot>
              </el-form-item>
              <el-form-item :label="formLabel.toStoreId.label" :label-width="formLabelWidth">
                <su-depot type="store" v-model="formData.toStoreId"  ></su-depot>
              </el-form-item>
              <el-form-item :label="formLabel.outCode.label" :label-width="formLabelWidth">
                <el-input v-model="formData.outCode" auto-complete="off" :placeholder="$t('storeAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.businessIds.label" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="formData.businessIds" auto-complete="off" :placeholder="$t('storeAllotList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="exportData">{{$t('storeAllotList.export')}}</el-button>
          <el-button type="primary" @click="search()">{{$t('storeAllotList.sure')}}</el-button>
        </div>
      </el-dialog>
      <su-table v-model="queryData" getUrl="/api/ws/future/crm/storeAllot"  >
        <el-table-column fixed prop="businessId" :label="$t('storeAllotList.businessId')" sortable ></el-table-column>
        <el-table-column prop="fromStoreName" :label="$t('storeAllotList.fromStore')"></el-table-column>
        <el-table-column prop="toStoreName" :label="$t('storeAllotList.toStore')"></el-table-column>
        <el-table-column prop="outCode" :label="$t('storeAllotList.outCode')" width="120"></el-table-column>
        <el-table-column prop="status" :label="$t('storeAllotList.status')"></el-table-column>
        <el-table-column prop="createdByName" :label="$t('storeAllotList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('storeAllotList.createdDate')" width="120" sortable></el-table-column>
        <el-table-column prop="lastModifiedByName" :label="$t('storeAllotList.lastModifiedBy')" width=120></el-table-column>
        <el-table-column prop="lastModifiedDate" :label="$t('storeAllotList.lastModifiedDate')" sortable width=140></el-table-column>
        <el-table-column prop="remarks" :label="$t('storeAllotList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('storeAllotList.operation')" width="140">
          <template scope="scope">
            <el-button size="small"  v-permit="'crm:storeAllot:view'" @click.native="itemView(scope.row.id)">{{$t('storeAllotList.detail')}}</el-button>
            <el-button v-if="scope.row.status === '待发货' || scope.row.status === '发货中'"   size="small"  v-permit="'crm:storeAllot:ship'" @click.native="itemShip(scope.row.id)">{{$t('storeAllotList.ship')}}</el-button>
            <el-button v-if="scope.row.status === '待发货'"   size="small"  v-permit="'crm:storeAllot:delete'" @click.native="itemDelete(scope.row.id)"> {{$t('storeAllotList.delete')}}</el-button>
            <el-button v-if="scope.row.isPrint"   size="small"  v-permit="'crm:storeAllot:ship'" @click.native="itemPrint(scope.row.id)">{{$t('storeAllotList.print')}}</el-button>
            <el-button v-if="!scope.row.isPrint"  style="color:red;"  size="small"  v-permit="'crm:storeAllot:ship'" @click.native="itemPrint(scope.row.id)">{{$t('storeAllotList.print')}}</el-button>
            <el-button v-if="scope.row.isShipPrint"   size="small"  v-permit="'crm:storeAllot:ship'" @click.native="itemShipPrint(scope.row.id)">{{$t('storeAllotList.shipPrint')}}</el-button>
            <el-button v-if="!scope.row.isShipPrint"   style="color:#ff0000;" size="small"  v-permit="'crm:storeAllot:ship'" @click.native="itemShipPrint(scope.row.id)">{{$t('storeAllotList.shipPrint')}}</el-button>

          </template>
        </el-table-column>
      </su-table>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        formData:{},
        queryData:{
          outCode:"",
          businessIds:'',
          status:'',
          createdDateRange:"",
          remarks:"",
          toStoreId:"",
          fromStoreId:""
        },formLabel:{
          outCode:{label:this.$t('storeAllotList.outCode')},
          businessIds:{label:this.$t('storeAllotList.businessId')},
          status:{label:this.$t('storeAllotList.status')},
          remarks:{label:this.$t('storeAllotList.remarks')},
          fromStoreId:{label:this.$t('storeAllotList.fromStore'),value:""},
          toStoreId:{label:this.$t('storeAllotList.toStore'),value:""},
          createdDateRange:{label:this.$t('storeAllotList.createdDate')}
        },
        formLabelWidth: '120px',
        formVisible: false,
        multipleSelection:[],
      };
    },
    methods: {
      search() {
        this.formVisible = false;
        this.queryData = util.cloneAndCopy(this.formData, this.queryData);

      },exportData(){
        window.location.href= "/api/crm/storeAllot/export?"+qs.stringify(this.formData);
      },itemAdd(){
        this.$router.push({ name: 'storeAllotForm'})
      },itemView(){
        this.$router.push({ name: 'storeAllotDetail', query: { id: id }})
      },itemDelete(){
        util.confirmBeforeDelRecord(this).then(() => {
          axios.get('/api/ws/future/crm/storeAllot/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        });

      },itemShip(){
        this.$router.push({ name: 'storeAllotShip', query: { id: id }});
      },itemPrint(){
      },itemShipPrint(){
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'storeAllotForm', query: { id: id }});
        }else if(action=="详细"){
          this.$router.push({ name: 'storeAllotDetail', query: { id: id }});
        }else if(action=="发货"){
          this.$router.push({ name: 'storeAllotShip', query: { id: id }});
        }else if(action=="删除") {
          axios.get('/api/crm/storeAllot/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      },handleSelectionChange(val) {
        var arrs=[];
        for(var i in val){
          arrs.push(val[i].id)
        };
        this.multipleSelection=arrs;
      }
    },created () {

      axios.get('/api/ws/future/crm/storeAllot/getQuery').then((response) =>{
        this.formData=response.data;
        this.queryData = util.cloneAndCopy(this.formData, this.queryData);
      });

    }
  };
</script>

