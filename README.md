<div align="center">
<h1>DeathMessage</h1>
<h6>自定义玩家死亡消息</h6>
</div>

## 插件简介

Paper服务端自定义玩家死亡消息插件, 支持颜色代码, 可跨服.

## 效果展示

![a.png](a.png)

## 使用方法

### 安装

环境: 1.17以上版本Paper及其fork, Folia可用

方式: 放入plugins文件夹, 重启服务器

### 修改死亡消息

在配置文件中修改对应死亡类型的消息后重载即可, `%dead_player%`代表死者, 如果有击杀者则`%killer%`代表击杀者, 如果击杀者使用物品击败则`%kill_item%`代表该物品。

### 跨服

在需要跨服的服务器安装本插件,并打开`config.yml`中的`proxy`即可

### 指令

`/deathmessagereload`简写`/dmrl`, 作用是重新加载配置文件

权限节点`deathmessage.command.reload`

`/deathmessagefilter`简写`/dmf`, 作用是开启/关闭死亡消息屏蔽, 参数`on/off`

无权限节点


