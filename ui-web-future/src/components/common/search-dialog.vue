<template>
  <transition >
    <div
      v-show="show"
      tabindex="2"
      :class="['vodal',sizeClass, className]"
      @keyup.esc="onEsc"
    >
      <div class="vodal-mask" v-if="mask" @click="$emit('hide')" />
      <transition >
        <div class="vodal-dialog"  v-show="show">
          <div class="header">{{title}}</div>
          <span class="vodal-close" v-if="closeButton" @click="$emit('hide')" />
          <slot></slot>
          <div class="el-dialog__footer" >
            <slot name="footer"></slot>
          </div>
        </div>
      </transition>
    </div>
  </transition>
</template>

<script>
  export default {
    name: 'vodal',
    props: {
      show: {
        type: Boolean,
        required: true
      },
      title: {
        type: String,
        default: ''
      },
      measure: {
        type: String,
        default: 'px'
      },
      height: {
        type:String,
        default: 'auto'
      },
      mask: {
        type: Boolean,
        default: true
      },
      size:{
        type:String,
        default:'tiny'
      },
      closeButton: {
        type: Boolean,
        default: true
      },
      className: {
        type: String,
        default: ''
      }
    },
    computed: {
      sizeClass(){
        return `vodal-dialog-${this.size}`;
      },
      dialogStyle() {
        return {
          height: `${this.height + this.measure}`
        }
      }
    },
    methods: {
      onEsc() {
        this.show && this.$emit('hide');
      }
    },
    watch: {
      show(show) {
        show && this.$nextTick(() => {
          this.$el.focus();
        })
      }
    }
  }
</script>
<style>
  .header {
    font-size: 16px;
    padding-bottom: 10px;
    border-bottom: 1px solid #e9e9e9;
    margin-bottom: 10px;
  }
  .vodal,
  .vodal-mask {
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 1400;
  }

  .vodal {
    position: fixed;
  }

  .vodal-mask {
    position: absolute;
    background: rgba(0, 0, 0, .3);
    z-index: 101;
  }
  .vodal.vodal-dialog-tiny .vodal-dialog{
    width: 25%;
  }
  .vodal.vodal-dialog-medium .vodal-dialog{
    width: 55%;
  }
  .vodal.vodal-dialog-large .vodal-dialog{
    width: 65%;
  }
  .vodal-dialog {
    position: absolute;
    top: 25%;
    left: 0;
    right: 0;
    margin: auto;
    z-index: 9999999;
    padding: 25px;
    background: #fff;
    border-radius: 3px;
    box-sizing: content-box;
    box-shadow: 0 0 10px 7px rgba(0, 0, 0, .4);
  }
  .vodal-close {
    position: absolute;
    cursor: pointer;
    top: 16px;
    right: 16px;
    width: 16px;
    height: 16px;
  }

  .vodal-close:before,
  .vodal-close:after {
    position: absolute;
    content: '';
    height: 2px;
    width: 100%;
    top: 50%;
    left: 0;
    margin-top: -1px;
    background: #999;
    border-radius: 100%;
    -webkit-transition: background .2s;
    transition: background .2s;
  }

  .vodal-close:before {
    -webkit-transform: rotate(45deg);
    transform: rotate(45deg);
  }

  .vodal-close:after {
    -webkit-transform: rotate(-45deg);
    transform: rotate(-45deg);
  }

  .vodal-close:hover:before,
  .vodal-close:hover:after {
    background: #333;
  }

</style>
