<template>
  <el-dialog
    class="dialog-container"
    :title="currentDialog.title"
    :visible.sync="currentDialog.visible"
    :width="currentDialog.width"
    :modal-append-to-body="true"
    :append-to-body="true"
    :before-close="handleClose">
       <slot :name="currentDialog.template"></slot>
      <span slot="footer">
        <el-button @click="handleClose" size="mini">取 消</el-button>
        <el-button type="primary" @click.native="sure" size="mini">确 定</el-button>
      </span>
  </el-dialog>
</template>

<script>
  import { mapGetters, mapActions } from 'vuex'
  import Util from "framework/util/util"
  export default {
    data(){
      return{
        flag:{
          sure:"SURE",
          close:"CLOSE",
          event:'dialogEvent'
        }
      }
    },
    components:{

    },
    computed: {
      ...mapGetters({
         currentDialog: "resentDialog"
      }),
    },
    methods: {
      handleClose() {
        this.dispatchEvent(this.flag.close);
      },
      sure(){
        this.dispatchEvent(this.flag.sure)
      },
      dispatchEvent(flag){
        this.$store.dispatch(this.flag.event,{
          type:flag,
          dialog:this.currentDialog
        });
      }
    }
  };
</script>
<style lang="sass" type="text/css">
  @import "framework/scss/dialog";
</style>
