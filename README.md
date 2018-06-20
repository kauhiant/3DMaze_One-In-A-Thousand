# 3D迷宮-練蠱

*3DMaze_One-In-A-Thousand*

![](https://i.imgur.com/yPPzdUK.jpg)

* 在迷宮內把其他生物殺光，只有你存活。
* 地圖大小 64 \* 64 \* layers
* **3D地圖，2D視角**

* kill other animals, only you leave
* map size : 64 \* 64 \* layers
* map is 3D , senen is 2D

---

## 角色 animal

* HP 上限100
* EP 使用技能會消耗，攻擊力=EP\*2
* Position 你在地圖中的位置

* HP upper bound : 100
* EP : reduce when putting skill, *ATK = EP \* 2*
* position : your position in a map

---

## 技能 skill

* 攻擊:攻擊前方1格
* 直劈:攻擊前方直線3格
* 橫切:攻擊前方橫向3格
* 偽裝:外型變得像牆壁一樣
* 視角切換
   * X:只能看到(Y,Z)平面
   * Y:只能看到(Z,X)平面
   * Z:只能看到(X,Y)平面

* attack   : attack one grid in front of you
* straight : attack 3 grids  
* horizon  : attack 3 grids
* mask     : hide yourself, until moving
* plain switch 
  * X : a plain (Y,Z)
  * Y : a plain (Z,X)
  * Z : a plain (X,Y)

---

## 移動 move

按鈕移動和感測器移動  
可按按鍵切換

moving by vector button or sensor
switching by the button

---

## 食物 food

* 可增加 HP:5,EP:1
* 如果把敵人殺死，可以吃到等於對方EP的食物

* add 5HP, 1EP
* if you kill enemy, you can eat the foods equal its EP

---

## 開始畫面 start scene

![](https://i.imgur.com/tjTGFyT.jpg)

* 可選擇你的顏色
* 根據你的Position決定這個地圖有幾層
* 根據你的EP決定有幾個敵人
* 決定好再按"開始遊戲"

* choose your color
* choose layers according your position
* set number of enemys according your EP
* ready? click  button "開始遊戲"

---
