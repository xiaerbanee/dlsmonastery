<template>
  <div>
    <el-select v-model="innerId"  filterable remote :multiple="multiple" :disabled="disabled" :placeholder="$t('su_district.inputKey')" :remote-method="remoteSelect" :loading="remoteLoading"  :clearable=true @change="handleChange">
      <el-option v-if="includeCashIn" :key="0" :label="$t('bankSelect.cashIn')" :value="0"></el-option>
      <el-option v-for="item in itemList"  :key="item.id" :label="item.name" :value="item.id"></el-option>
    </el-select>
  </div>
</template>
<script>
  export default {
    props: ['value', 'multiple', 'disabled', 'includeCashIn'],
    data() {
      return {
        innerId: null,
        itemList: [],
        remoteLoading: false,
      };
    }, methods: {
      remoteSelect(query) {
        if (util.isBlank(query)) {
          return;
        }
        this.remoteLoading = true;
        axios.get('/api/ws/future/basic/bank/search', {params: {key: query}}).then((response) => {
          var newList = new Array();
          var idList = new Array();
          if (this.multiple && this.innerId) {
            idList = this.innerId;
          } else {
            if (util.isNotBlank(this.innerId)) {
              idList.push(this.innerId);
            }
          }
          for (var index in this.itemList) {
            var item = this.itemList[index];
            if (idList.indexOf(item.id) >= 0) {
              newList.push(item);
            }
          }
          for (var index in response.data) {
            var item = response.data[index];
            if (idList.indexOf(item.id) < 0) {
              newList.push(item);
            }
          }
          this.itemList = newList;
          this.remoteLoading = false;
        })
      }, handleChange(newVal) {
        if (newVal !== this.value) {
          this.$emit('input', newVal);
        }
      }, setValue(val) {
          if (this.innerId === val) {
            return;
          }
          if (val) {
            this.innerId = val;
            let ids = this.innerId;
            if (this.multiple && this.innerId) {
              ids=this.innerId.join();
            }
            if (util.isBlank(ids)) {
              return;
            }
            this.remoteLoading = true;
            axios.get('/api/ws/future/basic/bank/findByIds?ids=' + ids).then((response) => {
              this.itemList = response.data;
              this.remoteLoading = false;
              this.$nextTick(() => {
                this.$emit('afterInit');
              })
            })
          } else {
            if (this.multiple) {
              this.innerId = [];
            } else {
              this.innerId = val
            }
            this.$nextTick(() => {
              this.$emit('afterInit');
            })
          }
        }
      }, created() {
        this.setValue(this.value);
      }, watch: {
        value: function (newVal) {
          this.setValue(newVal);
        }
      }
  }

</script>
