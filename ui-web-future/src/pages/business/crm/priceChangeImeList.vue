<template>
  <div>
    <head-tab :active="$t('priceChangeImeList.priceChangeImeList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'crm:priceChangeIme:edit'">{{$t('priceChangeImeList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:priceChangeIme:view'">{{$t('priceChangeImeList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('priceChangeImeList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.status.label" :label-width="formLabelWidth">
                <el-select v-model="formData.status" filterable clearable :placeholder="$t('priceChangeImeList.inputKey')">
                  <el-option v-for="item in formProperty.status" :key="item":label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.officeId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.officeId" filterable clearable :placeholder="$t('priceChangeImeList.inputKey')">
                  <el-option v-for="item in formProperty.offices"  :key="item.id" :label="item.name" :value="item .id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.productName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.productName" auto-complete="off" :placeholder="$t('priceChangeImeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.shopName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.shopName" auto-complete="off" :placeholder="$t('priceChangeImeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.isCheck.label" :label-width="formLabelWidth">
                <el-select v-model="formData.isCheck" filterable clearable :placeholder="$t('priceChangeImeList.inputKey')">
                  <el-option v-for="(value,key) in formProperty.bools"  :key="key":label="key | bool2str" :value="value"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.image.label" :label-width="formLabelWidth">
                <el-select v-model="formData.image" filterable clearable :placeholder="$t('priceChangeImeList.inputKey')">
                  <el-option v-for="(value,key) in formProperty.bools"  :key="key" :label="key | bool2str" :value="value"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.ime.label" :label-width="formLabelWidth">
                <el-input v-model="formData.ime" auto-complete="off" :placeholder="$t('priceChangeImeList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.priceChangeName.label" :label-width="formLabelWidth">
                <el-input v-model="formData.priceChangeName" auto-complete="off" :placeholder="$t('priceChangeImeList.likeSearch')"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('priceChangeImeList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('priceChangeImeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column  prop="productIme.ime" :label="$t('priceChangeImeList.ime')" sortable width="150"></el-table-column>
        <el-table-column prop="saleDate" :label="$t('priceChangeImeList.saleDate')" ></el-table-column>
        <el-table-column prop="productIme.product.name" :label="$t('priceChangeImeList.type')" ></el-table-column>
        <el-table-column prop="shop.area.name" :label="$t('priceChangeImeList.areaName')"></el-table-column>
        <el-table-column prop="shop.office.name" :label="$t('priceChangeImeList.officeName')"></el-table-column>
        <el-table-column prop="shop.name" :label="$t('priceChangeImeList.shopName')" ></el-table-column>
        <el-table-column prop="priceChange.name"  :label="$t('priceChangeImeList.priceChangeName')"></el-table-column>
        <el-table-column prop="auditDate" :label="$t('priceChangeImeList.auditDate')"></el-table-column>
        <el-table-column prop="isCheck"  :label="$t('priceChangeImeList.isCheck')"width="120">
          <template scope="scope">
            <el-tag :type="scope.row.isCheck ? 'primary' : 'danger'">{{scope.row.isCheck | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="image" :label="$t('priceChangeImeList.image')"></el-table-column>
        <el-table-column prop="status"  :label="$t('priceChangeImeList.status')"width="120">
          <template scope="scope">
            <el-tag :type="scope.row.status=='已通过' ? 'primary' : 'danger'">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" :label="$t('priceChangeImeList.remarks')"></el-table-column>
        <el-table-column prop="created.fullName" :label="$t('priceChangeImeList.createdBy')"></el-table-column>
        <el-table-column  :label="$t('priceChangeImeList.operation')" width="140">
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
        page:{},
        formData:{
          page:0,
          size:25,
          priceChangeName:'',
          status:'',
          officeId:'',
          productName:'',
          shopName:'',
          isCheck:'',
          image:'',
          ime:''
        },formLabel:{
          priceChangeName:{label:this.$t('priceChangeImeList.priceChangeName')},
          status:{label:this.$t('priceChangeImeList.status')},
          officeId:{label:this.$t('priceChangeImeList.priceChangeName'),value:''},
          productName:{label:this.$t('priceChangeImeList.type')},
          shopName:{label:this.$t('priceChangeImeList.shopName')},
          isCheck:{label:this.$t('priceChangeImeList.isCheck'),value:''},
          image:{label:this.$t('priceChangeImeList.image'),value:''},
          ime:{label:this.$t('priceChangeImeList.ime')},
        },
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formLabel.isCheck.value = util.bool2str(this.formData.isCheck);
        this.formLabel.image.value = util.bool2str(this.formData.image);
        this.formLabel.officeId.value = util.getLabel(this.formProperty.offices, this.formData.officeId);

        util.setQuery("priceChangeImeList",this.formData);
        axios.get('/api/crm/priceChangeIme',{params:this.formData}).then((response) => {
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
        this.$router.push({ name: 'priceChangeImeForm'})
      },itemAction:function(id,action){
          this.$router.push({ name: 'priceChangeImeDetail', query: { id: id ,action:action }})
      },getQuery(){
        axios.get('/api/crm/priceChangeIme/getQuery').then((response) =>{
          this.formProperty=response.data;
          this.pageRequest();
        });
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.getQuery();
    }
  };
</script>

