<div align="center">
<h1>DeathMessage</h1>
<h6>自定义玩家死亡消息</h6>
</div>

## 效果说明

服主自定义玩家的死亡消息，支持颜色代码。

## 效果展示

![a.png](a.png)

## 使用方法

在配置文件中修改对应死亡类型的消息即可，`%1$s`代表死者，如果有击杀者则`%2$s`代表击杀者，如果击杀者使用物品击败则`%3$s`代表该物品。

### 指令：
`/deathmessagereload`简写`/dmrl`，作用是重新加载配置文件

权限节点`deathmessage.command.reload`

`/deathmessagefilter`简写`/dmf`，作用是开启/关闭死亡消息屏蔽，参数`on/off`

无权限节点

### 配置文件

```yaml
player_name_format: "%player_displayname%"
item_default_format: "&r[%item_name%&r]"
item_enchanted_format: "&b[%item_name%&b]"
#死亡消息展示的地方，支持chat（聊天栏）和action_bar（物品栏上方）和all（同时chat和action_bar），默认为chat
death_message_type: chat
death_message:
  death_attack_anvil:
    #根据死亡者的权限显示死亡消息，从上往下计算优先级
    - perm: deathmessage.death_attack_anvil
      message:
        - "%1$s被铁砧砸中了"
  death_attack_anvil_player:
    - "%1$s在与%2$s战斗时被坠落的铁砧压扁了"
  death_attack_arrow:
    - "%1$s被%2$s射杀"
  death_attack_arrow_item:
    - "%1$s被%2$s用%3$s射杀"
  death_attack_badRespawnPoint_message:
    - "%1$s被%2$s杀死了"
  bad_respawn_point:
    - "[刻意的游戏设计]"
  death_attack_cactus:
    - "%1$s被戳死了"
  death_attack_cactus_player:
    - "%1$s在试图逃离%2$s时撞上了仙人掌"
  death_attack_cramming:
    - "%1$s因被过度挤压而死"
  death_attack_cramming_player:
    - "%1$s被%2$s挤扁了"
  death_attack_dragonBreath:
    - "%1$s被龙息烤熟了"
  death_attack_dragonBreath_player:
    - "%1$s被%2$s的龙息烤熟了"
  death_attack_drown:
    - "%1$s淹死了"
  death_attack_drown_player:
    - "%1$s在试图逃离%2$s时淹死了"
  death_attack_dryout:
    - "%1$s因脱水而死"
  death_attack_dryout_player:
    - "%1$s在试图逃离%2$s时因脱水而死"
  death_attack_even_more_magic:
    - "%1$s被不为人知的魔法杀死了"
  death_attack_explosion:
    - "%1$s爆炸了"
  death_attack_explosion_player:
    - "%1$s被%2$s炸死了"
  death_attack_explosion_player_item:
    - "%1$s被%2$s用%3$s炸死了"
  death_attack_fall:
    - "%1$s落地过猛"
  death_attack_fall_player:
    - "%1$s在试图逃离%2$s时落地过猛"
  death_attack_fallingBlock:
    - "%1$s被下落的方块压扁了"
  death_attack_fallingBlock_player:
    - "%1$s在与%2$s战斗时被下落的方块压扁了"
  death_attack_fallingStalactite:
    - "%1$s被坠落的钟乳石刺穿了"
  death_attack_fallingStalactite_player:
    - "%1$s在与%2$s战斗时被坠落的钟乳石刺穿了"
  death_attack_fireball:
    - "%1$s被%2$s用火球烧死了"
  death_attack_fireball_item:
    - "%1$s被%2$s用%3$s发射的火球烧死了"
  death_attack_fireworks:
    - "%1$s随着一声巨响消失了"
  death_attack_fireworks_item:
    - "%1$s随着%2$s用%3$s发射的烟花发出的巨响消失了"
  death_attack_fireworks_player:
    - "%1$s在与%2$s战斗时随着一声巨响中消失了"
  death_attack_flyIntoWall:
    - "%1$s感受到了动能"
  death_attack_flyIntoWall_player:
    - "%1$s在试图逃离%2$s时感受到了动能"
  death_attack_freeze:
    - "%1$s被冻死了"
  death_attack_freeze_player:
    - "%1$s被%2$s冻死了"
  death_attack_generic:
    - "%1$s死了"
  death_attack_generic_player:
    - "%1$s死于%2$s"
  death_attack_genericKill:
    - "%1$s被杀死了"
  death_attack_genericKill_player:
    - "%1$s在与%2$s战斗时被杀死了"
  death_attack_hotFloor:
    - "%1$s发现了地板是熔岩做的"
  death_attack_hotFloor_player:
    - "%1$s因%2$s而步入危险之地"
  death_attack_inFire:
    - "%1$s浴火焚身"
  death_attack_inFire_player:
    - "%1$s在与%2$s战斗时踏入了火中"
  death_attack_inWall:
    - "%1$s在墙里窒息而亡"
  death_attack_inWall_player:
    - "%1$s在与%2$s战斗时在墙里窒息而亡"
  death_attack_indirectMagic:
    - "%1$s被%2$s使用的魔法杀死了"
  death_attack_indirectMagic_item:
    - "%1$s被%2$s用%3$s杀死了"
  death_attack_lava:
    - "%1$s试图在熔岩里游泳"
  death_attack_lava_player:
    - "%1$s在逃离%2$s时试图在熔岩里游泳"
  death_attack_lightningBolt:
    - "%1$s被闪电击中"
  death_attack_lightningBolt_player:
    - "%1$s在与%2$s战斗时被闪电击中"
  death_attack_magic:
    - "%1$s被魔法杀死了"
  death_attack_magic_player:
    - "%1$s在试图逃离%2$s时被魔法杀死了"
  death_attack_message_too_long:
    - "死亡文本过长，原文：%s"
  death_attack_mob:
    - "%1$s被%2$s杀死了"
  death_attack_mob_item:
    - "%1$s被%2$s用%3$s杀死了"
  death_attack_onFire:
    - "%1$s被烧死了"
  death_attack_onFire_player:
    - "%1$s在与%2$s战斗时被烤得酥脆"
  death_attack_onFire_item:
    - "%1$s在与持有%3$s的%2$s战斗时被烤得酥脆"
  death_attack_outOfWorld:
    - "%1$s掉出了这个世界"
  death_attack_outOfWorld_player:
    - "%1$s与%2$s不共戴天"
  death_attack_outsideBorder:
    - "%1$s脱离了这个世界"
  death_attack_outsideBorder_player:
    - "%1$s在与%2$s战斗时脱离了这个世界"
  death_attack_player:
    - "%1$s被%2$s杀死了"
  death_attack_player_item:
    - "%1$s被%2$s用%3$s杀死了"
  death_attack_sonic_boom:
    - "%1$s被一道音波尖啸抹除了"
  death_attack_sonic_boom_item:
    - "%1$s在试图逃离持有%2$s的%3$s时被一道音波尖啸抹除了"
  death_attack_sonic_boom_player:
    - "%1$s在试图逃离%2$s时被一道音波尖啸抹除了"
  death_attack_stalagmite:
    - "%1$s被石笋刺穿了"
  death_attack_stalagmite_player:
    - "%1$s在与%2$s战斗时被石笋刺穿了"
  death_attack_starve:
    - "%1$s饿死了"
  death_attack_starve_player:
    - "%1$s在与%2$s战斗时饿死了"
  death_attack_sting:
    - "%1$s被蛰死了"
  death_attack_sting_item:
    - "%1$s被%2$s使用%3$s蛰死了"
  death_attack_sting_player:
    - "%1$s被%2$s蛰死了"
  death_attack_sweetBerryBush:
    - "%1$s被甜浆果丛刺死了"
  death_attack_sweetBerryBush_player:
    - "%1$s在试图逃离%2$s时被甜浆果丛刺死了"
  death_attack_thorns:
    - "%1$s在试图伤害%2$s时被杀"
  death_attack_thorns_item:
    - "%1$s在试图伤害%2$s时被%3$s杀死"
  death_attack_thrown:
    - "%1$s被%2$s给砸死了"
  death_attack_thrown_item:
    - "%1$s被%2$s用%3$s给砸死了"
  death_attack_trident:
    - "%1$s被%2$s刺穿了"
  death_attack_trident_item:
    - "%1$s被%2$s用%3$s刺穿了"
  death_attack_wither:
    - "%1$s凋零了"
  death_attack_wither_player:
    - "%1$s在与%2$s战斗时凋零了"
  death_attack_witherSkull:
    - "%1$s被%2$s发射的头颅射杀"
  death_attack_witherSkull_item:
    - "%1$s被%2$s使用%3$s发射的头颅射杀"
  death_fell_accident_generic:
    - "%1$s从高处摔了下来"
  death_fell_accident_ladder:
    - "%1$s从梯子上摔了下来"
  death_fell_accident_other_climbable:
    - "%1$s在攀爬时摔了下来"
  death_fell_accident_scaffolding:
    - "%1$s从脚手架上摔了下来"
  death_fell_accident_twisting_vines:
    - "%1$s从缠怨藤上摔了下来"
  death_fell_accident_vines:
    - "%1$s从藤蔓上摔了下来"
  death_fell_accident_weeping_vines:
    - "%1$s从垂泪藤上摔了下来"
  death_fell_assist:
    - "%1$s因为%2$s注定要摔死"
  death_fell_assist_item:
    - "%1$s因为%2$s使用了%3$s注定要摔死"
  death_fell_finish:
    - "%1$s摔伤得太重并被%2$s完结了生命"
  death_fell_finish_item:
    - "%1$s摔伤得太重并被%2$s用%3$s完结了生命"
  death_fell_killer:
    - "%1$s注定要摔死"
plugin_message:
  prefix: "&8[&4Death&7Message&8] "
  command_reload: "&a插件重载完毕"
  only_player: "&c只有玩家才能使用此命令"
  filter_on: "&a已开启死亡消息屏蔽"
  filter_off: "&a已关闭死亡消息屏蔽"
```