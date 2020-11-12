<template>
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
</template>

<script>
  import wordcloud from 'vue-wordcloud'
  // import VueResource from 'vue-resource';

  export default {
    name: 'app',
    components: {
      wordcloud
    },
    methods: {
      wordClickHandler(name, value, vm) {
        console.log('wordClickHandler', name, value, vm);
      },
      showDetails:function() {
        //安装插件
        //填充url
        //禁用eslint
        this.$http.get('http://localhost:8080/api/Time/getList').then(function (res) {
          console.log(res)

          this.msg = res.body;
          this.defaultWords = res.body.content;
          console.log(this.defaultWords);

        });
      }

    },
    mounted:function () {
      this.showDetails();
    } ,
    data() {

      return {

        msg:[],
        myColors: ['#1f77b4', '#629fc9', '#94bedb', '#c9e0ef'],
        defaultWords: [{"name": "Cat", "value": 26}, {"name": "fish", "value": 19}, {"name": "things", "value": 18}, {"name": "look", "value": 16},
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
        ]
      }
    }
  }
</script>
