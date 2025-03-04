<template>
  <div class="map" :style="'height: ' + height + 'px'">
    <div id="mapContainer" :style="'height: ' + height + 'px;width: 100%'"></div>
  </div>
</template>

<script setup>
import * as echarts from "echarts"
import axios from "axios"
import qingdaoJson from "./qingdao.json"
import { getImageUrl } from "@/utils/index"
import { nextTick, onMounted, reactive } from "vue"
import { useRouter } from "vue-router"

const router = useRouter()
const props = defineProps({
  markers: {
    type: Array,
    default() {
      return []
    },
  },
  height: {
    type: Number,
    default: 400,
  },
})
watch(
  () => props.markers,
  (value) => setOption()
)
const emit = defineEmits()
let echartsInstance = null
function initMap() {
  // axios.get("https://geo.datav.aliyun.com/areas_v3/bound/geojson?code=370200_full").then((res) => {
  //   echartsInstance = echarts.init(document.getElementById("mapContainer"))
  //   echarts.registerMap("chinaMap", res.data)
  //   setOption()
  // })

  echartsInstance = echarts.init(document.getElementById("mapContainer"))
  echarts.registerMap("chinaMap", qingdaoJson)
  setOption()
}
function setOption() {
  let option = {
    tooltip: {
      show: false,
    },
    title: {
      show: false,
    },
    top: "top",
    bottom: "-120px",
    geo: {
      // symbol: () => {
      //   return 'image://' + require('@/assets/images/bluePoint.png')
      // },
      // symbolSize: function (val) {
      //   return [16, 20] //描点的大小 宽和高
      // },
      tooltip: {
        trigger: "item",
        // backgroundColor: "rgba(255,255,255, 0.8)",
        // textStyle: {
        //   color: "#333",
        // },
      },
      map: "chinaMap",
      label: {
        show: true,
        textStyle: {
          color: "#eaeaea",
        },
      },
      roam: true, // 是否可以拖动
      itemStyle: {
        borderWidth: 1,
        borderColor: "rgba(44,131,240,1)",
        areaColor: "rgba(44,131,240,.3)",
        // color: {
        //   image: require('@/assets/img/icon-top-bg.png'), // 支持为 HTMLImageElement, HTMLCanvasElement，不支持路径字符串
        //   repeat: `no-repeat`, // 是否平铺，可以是 'repeat-x', 'repeat-y', 'no-repeat'
        // },
      },
      // emphasis: {
      //   disabled: true,
      // },
      scaleLimit: {
        min: 1,
        max: 8,
      },
      zoom: 1.3,
    },
    series: [
      //根据经纬度在地图上描点
      {
        type: "effectScatter",
        coordinateSystem: "geo",
        // animation: true,
        rippleEffect: {
          brushType: "stroke",
          color: "#00BA4E",
        },
        tooltip: {
          show: true,
          color: "#60bc74",
          formatter: (params) => {
            return (
              params.marker +
              params.name +
              (params.data.sumValue
                ? '<br/>当年发电效率:<span style="color: red;font-size: 14px">' +
                  (params.data.sumValue || "--") +
                  "</span>"
                : "")
            )
          },
        },
        symbol: "image://" + getImageUrl("greenPoint"),
        symbolSize: function (val) {
          return [16, 20] //描点的大小 宽和高
        },
        data: props.markers.map((itemOpt) => {
          return {
            value: itemOpt.echartsPosition,
            name: itemOpt.name,
            nameValue: itemOpt.value,
            sumValue: itemOpt.value || "--",
            color: "#60bc74",
            label: {
              emphasis: {
                position: "right",
                show: false,
              },
            },
            itemStyle: {
              // shadowBlur: 10,
              shadowColor: "#00BA4E",

              //   normal: {
              //     color: "#00BA4E",
              //   },
            },
            // on: ('click', function() => {

            // })
          }
        }),
      },
    ],
  }
  echartsInstance.setOption(option)
  let that = this
  echartsInstance.on("click", function (params) {
    console.log(params)
    // todo 跳转到电站详情
  })
}
function markerSet() {
  // 添加一些分布不均的点到地图上,地图上添加三个点标记，作为参照
  props.markers.forEach(function (marker) {
    let newMarker = new AMap.Marker({
      // map: echartsInstance,
      label: {
        content: marker.name,
        // direction: "top",
        offset: [6, 0],
      }, // 鼠标滑过显示
      icon: getImageUrl("greenPoint"),
      position: [marker.position[0], marker.position[1]],
      offset: new AMap.Pixel(-13, -30),
      clickable: true, // 点标记是否可点击
      extData: marker, // 用户自定义属性，支持JavaScript API任意数据类型，如Marker的id等
      // animation: 'AMAP_ANIMATION_DROP'
    })
    newMarker.on("mouseover", mouseover)
    newMarker.on("mouseout", mouseout)
    newMarker.on("click", mousetap)
    newMarker.setMap(echartsInstance)
    echartsInstance.add(newMarker)
  })
  // 添加事件监听, 使地图自适应显示到合适的范围
  echartsInstance.setFitView(null, false, [150, 50, 100, 60])
}

const showMarkerCard = ref(false)
const top = ref(0)
const left = ref(0)
const markerInfo = ref({})
function mouseover(e) {
  console.log(e)
  showMarkerCard.value = true
  top.value = e.pixel.y + 5
  left.value = e.pixel.x + 15
  markerInfo.value = e.target._opts.extData
}

function mouseout(e) {
  showMarkerCard.value = false
}

function mousetap(e) {
  console.log(e)
  markerInfo.value = e.target._opts.extData
  router.push("/realTime/site?site=" + markerInfo.value.id)
}

onMounted(() => {
  nextTick(() => {
    initMap()
  })
})
</script>

<style scoped lang="scss">
@keyframes myfirst2 {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

@keyframes myfirst {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(-359deg);
  }
}
.card-tag {
  background-color: rgba(0, 0, 0, 0.15);
  height: 62px;
  width: 107px;
  position: absolute;
  bottom: -16px;
  left: 8px;
}
.group12 {
  color: #fff;
  height: 24px;
  font-size: 12px;
  font-family: Helvetica, "Microsoft YaHei", Arial, sans-serif;
  text-align: left;
  display: flex;
  justify-content: center;
  align-items: center;
}
.map {
  width: 100%;
  height: 500px;
  position: relative;
  // background: #294f75;
  background: #eaeaea;
}
.map1,
.map2,
.map3 {
  position: absolute;
  opacity: 0.2;
}
.map1 {
  width: 520px;
  height: 520px;
  top: 30px;
  left: calc(50% - 260px);
  z-index: 2;
}
.map1 img {
  width: 100%;
  height: 100%;
  animation: myfirst2 15s infinite linear;
}
.map2 {
  width: 490px;
  height: 490px;
  top: 45px;
  left: calc(50% - 245px);
  z-index: 3;
  opacity: 0.2;
}
.map2 img {
  width: 100%;
  height: 100%;
  animation: myfirst 10s infinite linear;
}
.map3 {
  width: 450px;
  height: 450px;
  top: 65px;
  left: calc(50% - 225px);
  z-index: 4;
}
.map3 img {
  width: 100%;
  height: 100%;
}
</style>
