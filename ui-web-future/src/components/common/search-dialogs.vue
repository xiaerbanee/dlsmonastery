<template>
  <transition name="vodal-fade">
    <div
      v-show="show"
      tabindex="-1"
      :style="style"
      :class="['vodal',sizeClass, className]"
      @keyup.esc="onEsc"
    >
      <div class="vodal-mask" v-if="mask" @click="$emit('hide')" />
      <transition name="vodal-zoom">
        <div class="vodal-dialog" :style="dialogStyle" v-show="show">
          <div class="header">{{title}}</div>
          <span class="vodal-close" v-if="closeButton" @click="$emit('hide')" />
          <slot></slot>
          <div class="el-dialog__footer" v-show="show && $slots.footer">
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
      duration: {
        type: Number,
        default: 300
      },
      measure: {
        type: String,
        default: 'px'
      },
      height: {
        type:String,
        default: 'auto'
      },
      animation: {
        type: String,
        default: 'zoom'
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
      style() {
        return {
          animationDuration: `${this.duration}ms`,
          webkitAnimationDuration: `${this.duration}ms`
        };
      },
      dialogStyle() {
        return {
          ...this.style,
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
    z-index: 100;
  }

  .vodal {
    position: fixed;
  }

  .vodal-mask {
    position: absolute;
    background: rgba(0, 0, 0, .3);
  }
  .vodal.vodal-dialog-tiny .vodal-dialog{
    width: 25%;
  }
  .vodal.vodal-dialog-medium .vodal-dialog{
    width: 50%;
  }
  .vodal-dialog {
    position: absolute;
    top: 25%;
    left: 0;
    right: 0;
    margin: auto;
    z-index: 101;
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

  /* -- fade -- */
  @-webkit-keyframes vodal-fade-enter {
    from {
      opacity: 0;
    }
  }

  @keyframes vodal-fade-enter {
    from {
      opacity: 0;
    }
  }

  .vodal-fade-enter-active {
    -webkit-animation: vodal-fade-enter both ease-in;
    animation: vodal-fade-enter both ease-in;
  }

  @-webkit-keyframes vodal-fade-leave {
    to {
      opacity: 0
    }
  }

  @keyframes vodal-fade-leave {
    to {
      opacity: 0
    }
  }

  .vodal-fade-leave-active {
    -webkit-animation: vodal-fade-leave both ease-out;
    animation: vodal-fade-leave both ease-out;
  }
  /* -- zoom -- */
  @-webkit-keyframes vodal-zoom-enter {
    from {
      -webkit-transform: scale3d(.3, .3, .3);
      transform: scale3d(.3, .3, .3);
    }
  }

  @keyframes vodal-zoom-enter {
    from {
      -webkit-transform: scale3d(.3, .3, .3);
      transform: scale3d(.3, .3, .3);
    }
  }

  .vodal-zoom-enter-active {
    -webkit-animation: vodal-zoom-enter both cubic-bezier(0.4, 0, 0, 1.5);
    animation: vodal-zoom-enter both cubic-bezier(0.4, 0, 0, 1.5);
  }

  @-webkit-keyframes vodal-zoom-leave {
    to {
      -webkit-transform: scale3d(.3, .3, .3);
      transform: scale3d(.3, .3, .3);
    }
  }

  @keyframes vodal-zoom-leave {
    to {
      -webkit-transform: scale3d(.3, .3, .3);
      transform: scale3d(.3, .3, .3);
    }
  }

  .vodal-zoom-leave-active {
    -webkit-animation: vodal-zoom-leave both;
    animation: vodal-zoom-leave both;
  }
</style>
