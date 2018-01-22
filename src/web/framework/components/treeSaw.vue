<template>
  <el-tree
    ref="simpleTree"
    class="tree-common scrollbar"
    :data="menus"
    show-checkbox
    node-key="id"
    :default-checked-keys="hasMenus"
    :props="defaultProps">
  </el-tree>
</template>

<style>

</style>

<script>
  import { mapGetters, mapActions } from 'vuex'
  export default {
    name:"treeSaw",
    computed: mapGetters({
     /* roleMenus:"roleMenus"*/
      menus: 'allMenuDisabled',
      hasMenus:"roleMenusId"
    }),
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'name'
        }
      }
    },
    watch:{
      hasMenus:{
        handler:function(val,old){
          console.log(val,old);
          this.$refs.simpleTree.setCheckedKeys(val);
        },
        deep:true//对象内部的属性监听，也叫深度监听
      },
    }
  }
</script>
