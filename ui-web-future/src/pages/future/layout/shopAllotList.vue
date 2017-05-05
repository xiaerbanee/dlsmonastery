<template>
  <div>
    <head-tab active="shopAllotList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopAllot:edit'">{{$t('shopAllotList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:shopAllot:view'">{{$t('shopAllotList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('shopAllotList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.businessId.label" :label-width="formLabelWidth">
                <el-input v-model="formData.businessId" auto-complete="off" :placeholder="$t('shopAllotList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.fromShopId.label" :label-width="formLabelWidth">
                <su-depot type="shop" v-model="formData.fromShopId"  ></su-depot>
              </el-form-item>
              <el-form-item :label="formLabel.toShopId.label" :label-width="formLabelWidth">
                <su-depot  type="shop" v-model="formData.toShopId"  ></su-depot>
              </el-form-item>
              <el-form-item :label="formLabel.createdDateRange.label" :label-width="formLabelWidth">
                <su-date-range-picker v-model="formData.createdDateRange" ></su-date-range-picker>
              </el-form-item>
              <el-form-item :label="formLabel.status.label" :label-width="formLabelWidth">
                <el-select v-model="formData.status" filterable clearable :placeholder="$t('shopAllotList.inputKey')">
                  <el-option v-for="item in formData.status" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.businessIds.label" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="formData.businessIds" auto-complete="off" :placeholder="$t('shopAllotList.multiEnterOrComma')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('shopAllotList.sure')}}</el-button>
        </div>
      </el-dialog>
      <su-table  v-model="queryData" getUrl="/api/ws/future/crm/shopAllot">
        <el-table-column fixed prop="businessId" :label="$t('shopAllotList.billCode')" sortable width="150"></el-table-column>
        <el-table-column prop="createdByName" :label="$t('shopAllotList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('shopAllotList.createdDate')" width="120" sortable></el-table-column>
        <el-table-column prop="fromShopName" :label="$t('shopAllotList.fromShop')"  sortable></el-table-column>
        <el-table-column prop="toShopName" :label="$t('shopAllotList.toShop')" sortable></el-table-column>
        <el-table-column prop="outReturnCode" :label="$t('shopAllotList.outReturnCode')" sortable></el-table-column>
        <el-table-column prop="outSaleCode" :label="$t('shopAllotList.outSaleCode')" sortable></el-table-column>


        <el-table-column prop="status" :label="$t('shopAllotList.status')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.status=='已通过' ? 'primary' : 'danger'">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" :label="$t('shopAllotList.remarks')"></el-table-column>
        <el-table-column prop="enabled" :label="$t('shopAllotList.enabled')" width="70">
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'primary' : 'danger'">{{scope.row.enabled | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" :label="$t('shopAllotList.operation')" width="140">
          <template scope="scope">
            <el-button size="small"  v-permit="'crm:shopAllot:edit'" @click.native="itemEdit(scope.row.id)">{{$t('shopAllotList.edit')}}</el-button>
            <el-button size="small"  v-permit="'crm:shopAllot:edit'" @click.native="itemDelete(scope.row.id)">{{$t('shopAllotList.delete')}}</el-button>
            <el-button size="small"  v-permit="'crm:shopAllot:view'" @click.native="itemView(scope.row.id)">{{$t('shopAllotList.detail')}}</el-button>
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
          fromShopId:"",
          toShopId:"",
          businessId:"",
          businessIds:"",
          status:"",
          createdDateRange:'',
        },formLabel:{
          createdDateRange:{label: this.$t('shopAllotList.createdDate')},
          fromShopId:{label:this.$t('shopAllotList.fromShop')},
          toShopId:{label:this.$t('shopAllotList.toShop')},
          businessId:{label:this.$t('shopAllotList.billCode')},
          businessIds:{label:this.$t('shopAllotList.billCode')},
          status:{label:this.$t('shopAllotList.status')},
        },
        formLabelWidth: '120px',
        formVisible: false,
      };
    },
    methods: {
     search() {
       this.formVisible = false;
       this.queryData = util.cloneAndCopy(this.formData, this.queryData);
      },itemAdd(){
        this.$router.push({ name: 'shopAllotForm'})
      },itemEdit:function(id){
          this.$router.push({ name: 'shopAllotForm', query: { id: id, action:'修改'}})
      },itemView:function(id){
        this.$router.push({ name: 'shopAllotForm', query: { id: id, action:'详细'}})
      },itemDelete:function(id){
          this.$router.push({ name: 'shopAllotDetail', query: { id: id,action:'删除' }})
      }
    },created () {
      axios.get('/api/ws/future/crm/shopAllot/getQuery').then((response) =>{
        this.formData=response.data;
        this.queryData = util.cloneAndCopy(this.formData, this.queryData);
      });
    }
  };
</script>

