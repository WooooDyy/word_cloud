// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import VueResource from 'vue-resource';

import Vue from 'vue'
import App from './App'
import router from './router'
import ant from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'
import echarts from 'echarts'
Vue.prototype.$echarts = echarts

Vue.use(ant);
// import VueResource from 'vue-resource';
Vue.config.productionTip = false
Vue.use(VueResource)
/* eslint-disable */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
