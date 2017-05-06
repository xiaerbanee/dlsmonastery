<template>
  <div>
    <head-tab active="shopDepositList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:shopDeposit:edit'">{{$t('shopDepositList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:shopDeposit:view'">{{$t('shopDepositList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('shopDepositList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off" :placeholder="$t('shopDepositList.likeSearch')"></el-input>
              </el-form-item>

              <el-form-item :label="formLabel.type.label" :label-width="formLabelWidth">
                <el-select v-model="formData.type" filterable clearable :placeholder="$t('shopDepositList.inputKey')">
                  <el-option v-for="item in formData.typeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.createdDateRange.label" :label-width="formLabelWidth">
                <su-date-range-picker v-model="formData.createdDateRange" ></su-date-range-picker>
              </el-form-item>
              <el-form-item :label="formLabel.remarks.label" :label-width="formLabelWidth">
                <el-input v-model="formData.remarks" auto-complete="off" :placeholder="$t('shopDepositList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('shopDepositList.sure')}}</el-button>
        </div>
      </el-dialog>
      <su-table v-model="queryData"  getUrl="/api/ws/future/crm/shopDeposit" >
        <el-table-column fixed prop="id" :label="$t('shopDepositList.billCode')" sortable width="100"></el-table-column>
        <el-table-column prop="shop.name" :label="$t('shopDepositList.shopName')" sortable></el-table-column>
        <el-table-column prop="shop.area.name" :label="$t('shopDepositList.areaName')" ></el-table-column>
        <el-table-column prop="shop.office.name" :label="$t('shopDepositList.officeName')"></el-table-column>
        <el-table-column prop="type" :label="$t('shopDepositList.type')"></el-table-column>
        <el-table-column prop="amount" :label="$t('shopDepositList.amount')"></el-table-column>
        <el-table-column prop="leftAmount" :label="$t('shopDepositList.leftAmount')"></el-table-column>
        <el-table-column prop="outCode" :label="$t('shopDepositList.outCode')"></el-table-column>
        <el-table-column prop="created.fullName" :label="$t('shopDepositList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('shopDepositList.createdDate')"></el-table-column>
        <el-table-column prop="lastModified.fullName" :label="$t('shopDepositList.lastModifiedBy')"></el-table-column>
        <el-table-column prop="lastModifiedDate" :label="$t('shopDepositList.lastModifiedDate')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('shopDepositList.remarks')"></el-table-column>
        <el-table-column prop="created" :label="$t('shopDepositList.locked')"width="100">
          <template scope="scope">
            <el-tag :type="scope.row.created ? 'primary' : 'danger'">{{scope.row.created | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" :label="$t('shopDepositList.enabled')" width="100">
          <template scope="scope">
            <el-tag :type="scope.row.enabled ? 'primary' : 'danger'">{{scope.row.enabled | bool2str}}</el-tag>
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
          shopName:'',
          createdDateRange:'',
          type:'',
          remarks:''
        },formLabel:{
          shopName:{label:this.$t('shopDepositList.shopName')},
          createdDateRange:{label:this.$t('shopDepositList.createdDate')},
          type:{label:this.$t('shopDepositList.type')},
          remarks:{label:this.$t('shopDepositList.remarks')}
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
        this.$router.push({ name: 'shopDepositForm'})
      }
    },created () {
      axios.get('/api/ws/future/crm/shopDeposit/getQuery').then((response) =>{
        this.formData=response.data;
        this.queryData = util.cloneAndCopy(this.formData, this.queryData);
      });
    }
  };
</script>

