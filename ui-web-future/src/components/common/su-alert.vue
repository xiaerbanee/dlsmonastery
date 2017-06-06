
<template>
  <div v-show="text !=null && text !=''"  class="alert"  :class="typeClass">
    {{text}}
  </div>
</template>
<script>
  export default {
    props: ['type','text'],
    computed: {
      typeClass:function () {
        if(!this.type) {
          return "alert-success";
        } else {
          return "alert-" + this.type;
        }
      }
    }
  };
</script>
<style type="text/css">
  .alert {
    padding: .75rem 1.25rem;
    margin-bottom: 1rem;
    border: 1px solid transparent;
    border-radius: .25rem;
  }
  .alert-danger {
    background-color: #f2dede;
    border-color: #ebcccc;
    color: #a94442;
  }
  .alert-success {
    background-color: #dff0d8;
    border-color: #d0e9c6;
    color: #3c763d;
  }
  .alert-warning {
    background-color: #fcf8e3;
    border-color: #faf2cc;
    color: #8a6d3b;
  }
  .alert-info {
    background-color: #d9edf7;
    border-color: #bcdff1;
    color: #31708f;
  }
</style>
