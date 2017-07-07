<template>
  <div>
    <head-tab active="shopOrder"></head-tab>
    <div class="box">
      <el-form :model="formData" label-width="120px">
        <el-row :gutter="50">
          <el-col :span="10">
            <el-form-item :label="$t('shopOrder.orderCode')">

            </el-form-item>
            <el-form-item :label="$t('shopOrder.shipDepot')">

            </el-form-item>
            <el-form-item :label="$t('shopOrder.areaDepot')">

            </el-form-item>
            <el-form-item :label="$t('shopOrder.shipDate')">

            </el-form-item>
            <el-form-item :label="$t('shopOrder.outerBill')">

            </el-form-item>
            <el-form-item :label="$t('shopOrder.mallDepot')">
              <el-select clearable filterable>

              </el-select>
            </el-form-item>
            <el-form-item :label="$t('shopOrder.mallShipInfo')">
              <el-input
                type="textarea"
                :rows="4"
                placeholder="请输入内容">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10" :offset="2">
            <el-form-item :label="$t('shopOrder.creator')">

            </el-form-item>
            <el-form-item :label="$t('shopOrder.createdTime')">

            </el-form-item>
            <el-form-item :label="$t('shopOrder.ifETicket')">

            </el-form-item>
            <el-form-item :label="$t('shopOrder.expressCode')">

            </el-form-item>
            <el-form-item :label="$t('shopOrder.shipTpe')">

            </el-form-item>
            <el-form-item :label="$t('shopOrder.mallGoodsInfo')">

            </el-form-item>
            <el-form-item :label="$t('shopOrder.remarks')">

            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-button type="success" icon="check">{{$t('shopOrder.save')}}</el-button>
        </el-row>
      </el-form>
      <el-row>
        <h1>{{$t('shopOrder.billDetail')}}</h1>
        <el-table
          :data="tableData"
          border
          style="width: 100%"
          :default-sort = "{prop: 'date', order: 'descending'}"
        >
          <el-table-column
            prop="date"
            :label="$t('shopOrder.goodsName')"
            sortable
            width="180">
          </el-table-column>
          <el-table-column
            prop="name"
            :label="$t('shopOrder.orderNum')"
            sortable
            width="180">
          </el-table-column>
          <el-table-column
            prop="address"
            sortable
            :label="$t('shopOrder.billInfact')"
            :formatter="formatter">
          </el-table-column>
          <el-table-column
            prop="address"
            sortable
            :label="$t('shopOrder.shippedNum')"
            :formatter="formatter">
          </el-table-column>
          <el-table-column
            prop="address"
            sortable
            :label="$t('shopOrder.price')"
            :formatter="formatter">
          </el-table-column>
          <el-table-column
            prop="address"
            sortable
            :formatter="formatter">
            <el-button>111</el-button>
          </el-table-column>
        </el-table>
      </el-row>
    </div>
  </div>
</template>
<script>

</script>
<style>
  .box{
    width: 70%;
    margin: 0 auto;
    text-align: center;
  }

  .el-select{
    width: 100%;
  }
</style>
