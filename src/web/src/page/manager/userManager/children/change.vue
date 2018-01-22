<template>
  <div id="addUser">
    <el-row>
      <el-col :span="12">
        <div class="detail">
          <label class="explain"><Must></Must>用户账号</label>
          <label class="result2"><el-input  :value="sendInfo.account" size="small" name="account" :disabled="true"></el-input></label>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="detail">
          <label class="explain"><Must></Must>用户名称</label>
          <label class="result2"><el-input placeholder="请输入用户名称" :value="sendInfo.name" size="small" name="name" @change.native="inputChange"></el-input></label>
        </div>
      </el-col>
    </el-row>
  <!--  <el-row>
      <el-col :span="12">
        <div class="detail">
          <label class="explain"><Must></Must>性别</label>
          <label class="result">
            <el-radio-group :value="sendInfo.sex" name="sex" @change.native="radioChange">
              <el-radio :label="1">男</el-radio>
              <el-radio :label="0">女</el-radio>
            </el-radio-group>
          </label>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="detail">
          <label class="explain"><Must></Must>用户名称</label>
          <label class="result"><el-input placeholder="请输入用户名称" :value="sendInfo.name" size="small" name="name" @change.native="inputChange"></el-input></label>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <div class="detail">
          <label class="explain"><Must></Must>用户账号</label>
          <label class="result"><el-input  :value="sendInfo.account" size="small" name="account" :disabled="true"></el-input></label>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="detail">
          <label class="explain"><Must></Must>用户密码</label>
          <label class="result"><el-input placeholder="请输入用户密码" type="password" :value="sendInfo.password" size="small" :disabled="true" name="password" @change.native="inputChange"></el-input></label>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <div class="detail">
          <label class="explain">用户电话</label>
          <label class="result">
            <el-input placeholder="请输入用户电话" :value="sendInfo.phone" size="small" name="phone" @change.native="inputChange"></el-input>
          </label>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="detail">
          <label class="explain">用户身份证</label>
          <label class="result">
            <el-input placeholder="请输入身份证号码" :value="sendInfo.idCard" size="small" name="idCard" @change.native="inputChange"></el-input>
          </label>
        </div>
      </el-col>
    </el-row>-->
    <el-row >
      <el-col :span="24">
        <div class="detail">
          <label class="explain">关联角色</label>
          <label class="result">
            <Transfer  v-bind:transferData="data" v-bind:checked="transferChecked" v-bind:titles="transferTitles"></Transfer>
          </label>
        </div>

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
  import Must from "framework/components/must"
  import Transfer from "framework/components/transfer"

  export default {
    data() {
      return {
        namespace: "userManager",
        transferTitles:["角色列表","已选角色"],
        data:[]
      }
    },
    components: {
      Must,
      Transfer
    },
    computed: {
      ...mapGetters("userManager", {
        treeData: 'treeData',
        sendInfo: "sendInfo",
        transferData:"transferData",
        transferChecked:"transferChecked"
      })
    },
    methods: {
      inputChange(e) {
        let srcElement = e.srcElement || e.target, name = srcElement.getAttribute("name"), value = srcElement.value;
        this.$store.dispatch(this.namespace + '/setAddInfo', {[name]: value});
      },
      radioChange(e){
        let srcElement = e.srcElement || e.target, value = srcElement.value;
        this.$store.dispatch(this.namespace + '/setAddInfo', {sex: parseInt(value)});
      }
    },
    watch:{
      transferData:{
        handler:function(val){
          let data = JSON.parse(JSON.stringify(val));
          if(data.length>0){
            data.map((item)=>{
              item.key = parseInt(item.id);
              item.label = item.desc;
              item.disabled = false;
            })
          }
          this.data = data;
        },
        deep:true
      }
    },
    created() {
      this.$store.dispatch(this.namespace + '/transferData');
    }
  }
</script>
