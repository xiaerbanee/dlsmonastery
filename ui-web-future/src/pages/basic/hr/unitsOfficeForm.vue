<template>
  <div>
    <head-tab active="unitsOfficeForm"></head-tab>
    <div>
      <el-alert title="左边框表示办事处下所有门店或者员工，右边框表示绑定到该业务单元。" type="info" style="margin-bottom: 30px"></el-alert>

      <el-form :inline="true" :model="formData" class="demo-form-inline">
        <el-row>
          <el-form-item label="业务单元">
            <office-select v-model="formData.officeId"></office-select>
          </el-form-item>
          <el-form-item>
              <el-button type="primary" @click="batchUnits"  icon="plus">业务单元编辑</el-button>
          </el-form-item>
        </el-row>
        <el-row>
          <el-col :span="12">
          <el-form-item>
            <template>
              <el-transfer v-model="formData.depotIds" filterable   :render-content="renderFunc" :titles="['所有门店', '业务单元']"
                           :button-texts="['', '']" :footer-format="{  noChecked: '${total}',   hasChecked: '${checked}/${total}' }" @change="handleChange" :data="data">
                <el-button class="transfer-footer" slot="left-footer" size="small">操作</el-button>
                <el-button class="transfer-footer" slot="right-footer" size="small">操作</el-button>
              </el-transfer>
            </template>
          </el-form-item>
          </el-col>
          <el-col :span="12">
          <el-form-item>
            <template>
              <el-transfer v-model="formData.accountIds" filterable   :render-content="renderFunc" :titles="['所有员工', '业务单元']"
                           :button-texts="['', '']" :footer-format="{  noChecked: '${total}',   hasChecked: '${checked}/${total}' }" @change="handleChange" :data="data">
                <el-button class="transfer-footer" slot="left-footer" size="small">操作</el-button>
                <el-button class="transfer-footer" slot="right-footer" size="small">操作</el-button>
              </el-transfer>
            </template>
          </el-form-item>
          </el-col>
        </el-row>
        </el-form>


    </div>
  </div>
</template>
<script>
  import officeSelect from "components/basic/office-select"
  export default {
    components:{officeSelect},
    data() {
        const generateData = _ => {
          const data = [];
          for (let i = 1; i <= 15; i++) {
            data.push({
              key: i,
              label: `备选项 ${ i }`,
            });
          }
          return data;
        };
        return {
            formData:{
              officeIds:'',
              depotIds:'',
              accountIds:''
            },
          data: generateData(),
          value3: [],
        };
      },

      methods: {
        handleChange(value, direction, movedKeys) {
          console.log(value, direction, movedKeys);
        },
        renderFunc(){

        },
        selectOffice(officeId){
            axios.get("api/baisc/sys/office?id="+officeId).then((response)=>{
                console.log(response)
            })
        },
        batchUnits(){
          this.$router.push({ name: 'batchUnitsForm'})
        }
      }
    };
</script>

<style>
  .transfer-footer {
    margin-left: 20px;
    padding: 6px 5px;
  }
</style>
