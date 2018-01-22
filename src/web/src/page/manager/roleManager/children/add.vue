<template>
  <div id="addRole">
    <el-row >
      <el-col :span="12">
        <el-row>
          <el-col :span="24">
            <div class="detail">
              <label class="explain">角色账号</label>
              <label class="result">
                <el-input placeholder="请输入角色账号" size="mini" :value="sendInfo.name" name="name" @change.native="inputChange"></el-input>
              </label>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <div class="detail">
              <label class="explain">角色描述</label>
              <label class="result">
                <el-input placeholder="请输入角色描述" size="mini" :value="sendInfo.desc" name="desc" @change.native="inputChange"></el-input>
              </label>
            </div>
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="12">
        <TreeAdd v-bind:treeData="treeData">
          <template slot="treeTitle">
            <label class="tree-title">角色关联菜单</label>
          </template>
        </TreeAdd>
      </el-col>
    </el-row>
  </div>
</template>

<style type="text/css" lang="scss">
  @import "framework/scss/detail"
</style>

<script>
  import { mapGetters, mapActions } from 'vuex'
  import Util from "framework/util/util"
  import TreeAdd from "framework/components/treeAdd"

  export default {
    data() {
      return {
        namespace: "roleManager"
      }
    },
    components: {
      TreeAdd
    },
    computed: {
      ...mapGetters("roleManager", {
        treeData: 'treeData',
        sendInfo: "sendInfo"
      })
    },
    methods: {
      inputChange(e) {
        let srcElement = e.srcElement || e.target, name = srcElement.getAttribute("name"), value = srcElement.value;
        this.$store.dispatch(this.namespace + '/setAddInfo', {[name]: value});
      }
    },
    created() {
      this.$store.dispatch(this.namespace + '/getTreeData');
    }
  }
</script>
