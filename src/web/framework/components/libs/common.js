/*
* 全局注册组件，供全局的vue使用
* author:why
* date:2017-11-08
*/

import Vue from "vue"

Vue.component('async-example', function (resolve, reject) {
  setTimeout(function () {
    // 将组件定义传入 resolve 回调函数
    resolve({
      template: '<div>I am async!</div>'
    })
  }, 1000)
})
