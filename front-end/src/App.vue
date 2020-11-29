<template>
  <div>
    <a-page-header
      style="border: 1px solid rgb(235, 237, 240);margin: 0 auto;"
      title="知乎热词分析"
      sub-title="每30s刷新一次"
    />
    <div style="border-top: 10px">
      <a-row style="height: 50px"></a-row>
      <a-row>
        <a-col :span="1"></a-col>
        <a-col :span="10">
          <div>
            <wordcloud
              :data="defaultWords"
              nameKey="name"
              valueKey="value"
              :color="myColors"
              :showTooltip="true"
              :wordClick="wordClickHandler">
            </wordcloud>
          </div>
        </a-col>
        <a-col :span="2"></a-col>
        <a-col :span="10" >
          <div id="right" style="width: 90%;height:400px;"></div>
        </a-col>
        <a-col :span="1"></a-col>
      </a-row>
      <a-divider />
      <a-row>
        <a-col :span="24">
          <div style="display: flex;justify-content: center">
            <div id="bottom" style="width: 90%;height: 45vh;"> </div>
          </div>
        </a-col>
      </a-row>
    </div>
  </div>
</template>

<script>
  import wordcloud from 'vue-wordcloud'
  // import VueResource from 'vue-resource';

  // 指定图表的配置项和数据
  const option = {
    title: {
      text: '累计最热词汇',
      subtext: '数据来自知乎'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: ['热度']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'value',
      boundaryGap: [0, 0.01]
    },
    yAxis: {
      type: 'category',
      data: ['巴西', '印尼', '美国', '印度', '中国', '世界人口(万)']
    },
    series: [
      {
        name: '实时',
        type: 'bar',
        data: [18203, 23489, 29034, 104970, 131744, 630230],
        color: '#0078B1'
      }
    ]
  }
  var option_line = {
    tooltip: {              //设置tip提示
      trigger: 'axis'
    },
    legend: {               //设置区分（哪条线属于什么）
      data: ['11, 22, 33']
    },
    color: ['#8AE09F', '#e23f3f', '#1f77b4'],       //设置区分（每条线是什么颜色，和 legend 一一对应）
    xAxis: {                //设置x轴
      type: 'category',
      boundaryGap: false,     //坐标轴两边不留白
      data: ["11", '22', '33'],
      name: 'DATE',           //X轴 name
      nameTextStyle: {        //坐标轴名称的文字样式
        color: '#696969',
        fontSize: 16,
        padding: [0, 0, 0, 20]
      },
      axisLine: {             //坐标轴轴线相关设置。
        lineStyle: {
          color: '#696969',
        }
      }
    },
    yAxis: {
      name: '折线图',
      nameTextStyle: {
        color: '#696969',
        fontSize: 16,
        padding: [0, 0, 10, 0]
      },
      axisLine: {
        lineStyle: {
          color: '#696969',
        }
      },
      type: 'value'
    },
    series: [
      {
        name: "11",
        data: 1,
        type: 'line',               // 类型为折线图
        lineStyle: {                // 线条样式 => 必须使用normal属性
          normal: {
            color: '#8AE09F',
          }
        },
      },
      {
        name: "22",
        data: 22,
        type: 'line',               // 类型为折线图
        lineStyle: {                // 线条样式 => 必须使用normal属性
          normal: {
            color: '#e23f3f',
          }
        },
      },
      {
        name: "33",
        data: 33,
        type: 'line',
        lineStyle: {
          normal: {
            color: '#1f77b4',
          }
        },
      }
    ]
  }

  export default {
    name: 'app',
    components: {
      wordcloud
    },
    created () {
      // 实现轮询
      window.setInterval(() => {
        setTimeout(this.showDetails(), 0)
      }, 30000)
      window.setInterval(() => {
        setTimeout(this.getLineChartDatas(), 0)
      }, 30000)
      window.setInterval(() => {
        setTimeout(this.refreshRight(), 0)
      }, 30000)

    },
    methods: {
      wordClickHandler (name, value, vm) {
        console.log('wordClickHandler', name, value, vm)
      },
      // 请求是否有新消息
      getNewMessage: function () {
        console.log('请求' + this.num++ + '次')
      },
      refreshRight () {
        // 基于准备好的dom，初始化echarts实例
        // 使用刚指定的配置项和数据显示图表。
        this.$http.get('http://47.116.133.213:5050/leiji').then(function (res) {
          this.msg = res.body
          var bodyTXT = res.bodyText
          var reg = new RegExp('\'', 'g')
          bodyTXT = bodyTXT.replace(reg, '"')
          var dataword = JSON.parse(bodyTXT)
          this.option.yAxis.data = dataword.map(({name}) => name).slice(0, 10)
          console.log(this.option.yAxis.data)
          this.option.series[0].data = dataword.map(({value}) => value).slice(0, 10)
          console.log(this.option.series[0].data)
          let myChart = this.$echarts.init(document.getElementById("right"));
          myChart.setOption(this.option);
        })
      },
      showDetails () {
        // 安装插件
        // 填充url
        // 禁用eslint
        var dataword = []
        this.defaultWords = dataword
        this.option.yAxis.data = dataword.map(({name}) => name).slice(0, 5)
        this.option.series[0].data = dataword.map(({value}) => value).slice(0, 5)
        // 实际使用:
        this.$http.get('http://47.116.133.213:5050/ciyun').then(function (res) {
          this.msg = res.body
          var bodyTXT = res.bodyText
          var reg = new RegExp('\'', 'g')
          bodyTXT = bodyTXT.replace(reg, '"')
          // console.log("bodytxt")
          // console.log(bodyTXT)
          var dataword = JSON.parse(bodyTXT)

          this.defaultWords = dataword
          this.option.yAxis.data = dataword.map(({name}) => name)
          this.option.series.data = dataword.map(({value}) => value)
        })
      },
      getLineChartDatas () {
        this.$http.get('http://localhost:8081/api/getChart').then(function (res) {
          var bodyTXT = res.bodyText
          var originData = JSON.parse(bodyTXT)
          console.log(originData)
          var curNames = originData['names']
          var values_0 = originData['values_0']
          var values_1 = originData['values_1']
          var values_2 = originData['values_2']
          var times = originData['times']

          this.option_line.xAxis.data = times

          this.option_line.legend.data = curNames
          this.option_line.series[0].name = curNames[0]
          this.option_line.series[1].name = curNames[1]
          this.option_line.series[2].name = curNames[2]

          this.option_line.series[0].data = values_0
          this.option_line.series[1].data = values_1
          this.option_line.series[2].data = values_2
          let myChart = this.$echarts.init(document.getElementById("bottom"));
          myChart.setOption(this.option_line)

        })
      },
      drawLineChart() {
        // line chart
        let myChart = this.$echarts.init(document.getElementById("bottom"));
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(this.option_line)
      }
    },
    mounted: function () {
      this.showDetails()
      this.getLineChartDatas()
      this.drawLineChart()
      this.refreshRight()
    },
    computed: {

    },
    data () {
      return {
        option,
        option_line,
        num: 0,
        msg: [],
        myColors: ['#1f77b4', '#629fc9', '#94bedb', '#c9e0ef'],
        defaultWords: [
          {'name': 'Cat', 'value': 26},
          {'name': 'fish', 'value': 19},
          {'name': 'things', 'value': 18},
          {'name': 'look', 'value': 16},
          {
            'name': 'two',
            'value': 15
          },
          {
            'name': 'fun',
            'value': 9
          },
          {
            'name': 'know',
            'value': 9
          },
          {
            'name': 'good',
            'value': 9
          },
          {
            'name': 'play',
            'value': 6
          }
        ],
        defaultxAxyisData: ['2019-1-1', '2019-2-1', '2019-3-1', '2019-4-1', '2019-5-1', '2019-6-1', '2019-7-1',],
        defaultDataOfLine1: [220, 232, 201, 234, 290, 230, 220],
        defaultDataOfLine2: [120, 200, 150, 80, 70, 110, 130],
        defaultDataOfLine0: [120, 200, 150, 80, 70, 110, 130]

      }

    }
  }
</script>
