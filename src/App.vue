npm
<template>
  <div>
  <div id="app">
    <wordcloud
      :data="defaultWords"
      nameKey="name"
      valueKey="value"
      :color="myColors"
      :showTooltip="true"
      :wordClick="wordClickHandler">
    </wordcloud>
  </div>

  <div id="chartLineBox" style="width: 90%;height: 70vh;"> </div>
  </div>
</template>

<script>
import wordcloud from 'vue-wordcloud'
// import VueResource from 'vue-resource';
import echarts from 'echarts'

export default {
  name: 'app',
  components: {
    wordcloud
  },
  created() {
    // 实现轮询
    window.setInterval(() => {
      setTimeout(this.showDetails(), 0);
    }, 3000);
    window.setInterval(() => {
      setTimeout(this.getLineChartDatas(), 0);
    }, 3000);
  },
  methods: {
    wordClickHandler(name, value, vm) {
      console.log('wordClickHandler', name, value, vm);
    },
    // 请求是否有新消息
    getNewMessage: function () {
      console.log("请求" + this.num++ + "次");
    },
    showDetails: function () {
      // 安装插件
      // 填充url
      // 禁用eslint
      this.$http.get('http://172.31.2.77:5050/getApi').then(function (res) {

        this.msg = res.body;
        var bodyTXT = res.bodyText;
        var reg = new RegExp("\'", "g");
        bodyTXT = bodyTXT.replace(reg, "\"");
        // console.log("bodytxt")
        // console.log(bodyTXT)
        var dataword = JSON.parse(bodyTXT)
        this.defaultWords = dataword;
      });
    },

    getLineChartDatas(){
      this.$http.get('http://127.0.0.1:5000/').then(function (res) {
        var bodyTXT = res.bodyText;
        var len = bodyTXT.length;
        bodyTXT = bodyTXT.substr(0,len);
        var datt = bodyTXT.split("\"");

        var d1 = datt[1];
        var d2 = datt[3];
        var d3 = datt[5];
        var reg = new RegExp("\'", "g");
        var data1 = d1.substr(1,d1.length-2).replace(reg,"").split(",")

        var data2 = d2.substr(1,d2.length-2).split(",").map(Number)
        var data3 = d3.substr(1,d3.length-2).split(",").map(Number)

        this.defaultxAxyisData = data1;
        this.defaultDataOfLine1 = data2;
        this.defaultDataOfLine2 = data3;

        console.log(d1);
        console.log(d2);
        console.log(d3);

        var option = {
          tooltip: {              //设置tip提示
            trigger: 'axis'
          },

          legend: {               //设置区分（哪条线属于什么）
            data:['平均成绩','学生成绩']
          },
          color: ['#8AE09F', '#1f77b4'],       //设置区分（每条线是什么颜色，和 legend 一一对应）
          xAxis: {                //设置x轴
            type: 'category',
            boundaryGap: false,     //坐标轴两边不留白
            data: this.defaultxAxyisData,
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
            name: 'SALES VOLUME',
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
              name: '平均成绩',
              data:  this.defaultDataOfLine1,
              type: 'line',               // 类型为折线图
              lineStyle: {                // 线条样式 => 必须使用normal属性
                normal: {
                  color: '#8AE09F',
                }
              },
            },
            {
              name: '学生成绩',
              data: this.defaultDataOfLine2,
              type: 'line',
              lineStyle: {
                normal: {
                  color: '#1f77b4',
                }
              },
            }
          ]
        };
        this.chartLine.setOption(option);


      });
    }
  },
  mounted: function () {
    this.showDetails();
    this.getLineChartDatas();

    //line chart
    this.chartLine = echarts.init(document.getElementById('chartLineBox'));

    // 指定图表的配置项和数据
    var option = {
      tooltip: {              //设置tip提示
        trigger: 'axis'
      },

      legend: {               //设置区分（哪条线属于什么）
        data:['平均成绩','学生成绩']
      },
      color: ['#8AE09F', '#1f77b4'],       //设置区分（每条线是什么颜色，和 legend 一一对应）
      xAxis: {                //设置x轴
        type: 'category',
        boundaryGap: false,     //坐标轴两边不留白
        data: this.defaultxAxyisData,
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
        name: 'SALES VOLUME',
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
          name: '平均成绩',
          data:  this.defaultDataOfLine1,
          type: 'line',               // 类型为折线图
          lineStyle: {                // 线条样式 => 必须使用normal属性
            normal: {
              color: '#8AE09F',
            }
          },
        },
        {
          name: '学生成绩',
          data: this.defaultDataOfLine2,
          type: 'line',
          lineStyle: {
            normal: {
              color: '#1f77b4',
            }
          },
        }
      ]
    };

    // 使用刚指定的配置项和数据显示图表。
    this.chartLine.setOption(option);

  },
  data() {

    return {
      num: 0,
      msg: [],
      myColors: ['#1f77b4', '#629fc9', '#94bedb', '#c9e0ef'],
      defaultWords: [
        {"name": "Cat", "value": 26},
        {"name": "fish", "value": 19},
        {"name": "things", "value": 18},
        {"name": "look", "value": 16},
        {
          "name": "two",
          "value": 15
        },
        {
          "name": "fun",
          "value": 9
        },
        {
          "name": "know",
          "value": 9
        },
        {
          "name": "good",
          "value": 9
        },
        {
          "name": "play",
          "value": 6
        }
      ],
      defaultxAxyisData:['2019-1-1', '2019-2-1', '2019-3-1', '2019-4-1', '2019-5-1', '2019-6-1', '2019-7-1',],
      defaultDataOfLine1:[220, 232, 201, 234, 290, 230, 220],
      defaultDataOfLine2:[120, 200, 150, 80, 70, 110, 130],

    };

  }
}
</script>
