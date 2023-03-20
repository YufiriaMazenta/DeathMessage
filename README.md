# 死亡消息（DeathMessage）

英文名称：DeathMessage

中文名称：死亡消息

插件来源：重置

适用服务端：Spigot、Paper

插件类型：娱乐

语言支持：简体中文

适用版本：1.19+

下载地址：https://github.com/YufiriaMazenta/DeathMessage/actions

原贴地址：https://github.com/YufiriaMazenta/DeathMessage

插件简介：全局自定义死亡消息

作者：YufiriaMazenta

## 它能做到什么

服主设置玩家的死亡消息、加入游戏和退出游戏消息，以及第一次进入服务器的消息，且死亡消息支持颜色代码（无法为玩家名覆盖颜色代码，玩家名的显示和原版一致，这意味着用其他插件设置的具有颜色代码的玩家名能够正常显示）。

**注意**：一切都由服主在该插件名文件夹下的config.yml配置文件处修改。
说这么多你可能听不懂，一会看效果图就OK。

## 效果展示

![QQ截图20220616114335.png](https://pic.rmb.bdstatic.com/bjh/3632ad8e11d5a5b2ad3f74ea259cf615.png)

## 使用方法

在配置文件中修改对应死亡类型的消息即可，`%1$s`代表死者，如果有杀手则`%2$s`代表杀手，如果杀手手持命名的物品则`%3$s`代表该物品。

如需重置某种类型的死亡消息，只需要把该键对应的值删除即可，双引号也要删。

`helloMessage`代表有玩家首次加入服务器向全服广播的消息，`%1$s`代表该玩家。

`joinMessage`代表玩家进入服务器的消息，其中`{}`代表该玩家。

`quitMessage`代表玩家退出服务器的消息，其中`{}`代表该玩家。

配置文件中设置的消息均支持颜色代码`&`，文末有具体的颜色代码使用方法。

如果不小心搞乱了，把config.yml删了，reload一下服务器他会重新生成一个。

**指令：`/deathmessagereload`也可以简写做`/dmrl`，作用是重新加载配置文件，这样就不用reload服务器了（这个指令并不能重新生成一个config.yml）**

## 配置文件：

```yaml
helloMessage:
  - "&b新人&c&l来了奥！"
joinMessage:
  - "{}&b&l来了喵~"
quitMessage:
  - "{}&b&l走了喵~"
death_attack_anvil:
  - "%1$s被坠落的铁砧压扁了"
death_attack_anvil_player:
  - "%1$s在与%2$s战斗时被坠落的铁砧压扁了"
death_attack_arrow:
  - "%1$s被%2$s射杀"
death_attack_arrow_item:
  - "%1$s被%2$s用%3$s射杀"
death_attack_badRespawnPoint_message:
  - "%1$s被%2$s杀死了"
bedRespawnPoint:
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
death_attack_mob:
  - "%1$s被%2$s杀死了"
death_attack_mob_item:
  - "%1$s被%2$s用%3$s杀死了"
death_attack_onFire:
  - "%1$s被烧死了"
death_attack_onFire_player:
  - "%1$s在与%2$s战斗时被烤得酥脆"
death_attack_outOfWorld:
  - "%1$s掉出了这个世界"
death_attack_outOfWorld_player:
  - "%1$s与%2$s不共戴天"
death_attack_player:
  - "%1$s被%2$s杀死了"
death_attack_player_item:
  - "%1$s被%2$s用%3$s杀死了"
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
```

### 死亡类型——消息对照表

```json
{
    "death.attack.anvil": 					"%1$s被坠落的铁砧压扁了",
    "death.attack.anvil.player": 			"%1$s在与%2$s战斗时被坠落的铁砧压扁了",
    "death.attack.arrow": 					"%1$s被%2$s射杀",
    "death.attack.arrow.item": 				"%1$s被%2$s用%3$s射杀",
    "death.attack.badRespawnPoint.message": "%1$s被%2$s杀死了",
    "death.attack.cactus": 					"%1$s被戳死了",
    "death.attack.cactus.player": 			"%1$s在试图逃离%2$s时撞上了仙人掌",
    "death.attack.cramming": 				"%1$s因被过度挤压而死",
    "death.attack.cramming.player": 		"%1$s被%2$s挤扁了",
    "death.attack.dragonBreath": 			"%1$s被龙息烤熟了",
    "death.attack.dragonBreath.player": 	"%1$s被%2$s的龙息烤熟了",
    "death.attack.drown": 					"%1$s淹死了",
    "death.attack.drown.player": 			"%1$s在试图逃离%2$s时淹死了",
    "death.attack.dryout": 					"%1$s因脱水而死",
    "death.attack.dryout.player": 			"%1$s在试图逃离%2$s时因脱水而死",
    "death.attack.even_more_magic": 		"%1$s被不为人知的魔法杀死了",
    "death.attack.explosion": 				"%1$s爆炸了",
    "death.attack.explosion.player": 		"%1$s被%2$s炸死了",
    "death.attack.explosion.player.item": 	"%1$s被%2$s用%3$s炸死了",
    "death.attack.fall": 					"%1$s落地过猛",
    "death.attack.fall.player": 			"%1$s在试图逃离%2$s时落地过猛",
    "death.attack.fallingBlock": 			"%1$s被下落的方块压扁了",
    "death.attack.fallingBlock.player": 	"%1$s在与%2$s战斗时被下落的方块压扁了",
    "death.attack.fallingStalactite": 		"%1$s被坠落的钟乳石刺穿了",
    "death.attack.fallingStalactite.player": "%1$s在与%2$s战斗时被坠落的钟乳石刺穿了",
    "death.attack.fireball": 				"%1$s被%2$s用火球烧死了",
    "death.attack.fireball.item": 			"%1$s被%2$s用%3$s发射的火球烧死了",
    "death.attack.fireworks": 				"%1$s随着一声巨响消失了",
    "death.attack.fireworks.item": 			"%1$s随着%2$s用%3$s发射的烟花发出的巨响消失了",
    "death.attack.fireworks.player": 		"%1$s在与%2$s战斗时随着一声巨响中消失了",
    "death.attack.flyIntoWall": 			"%1$s感受到了动能",
    "death.attack.flyIntoWall.player": 		"%1$s在试图逃离%2$s时感受到了动能",
    "death.attack.freeze": 					"%1$s被冻死了",
    "death.attack.freeze.player": 			"%1$s被%2$s冻死了",
    "death.attack.generic": 				"%1$s死了",
    "death.attack.generic.player": 			"%1$s死于%2$s",
    "death.attack.hotFloor": 				"%1$s发现了地板是熔岩做的",
    "death.attack.hotFloor.player": 		"%1$s因%2$s而步入危险之地",
    "death.attack.inFire": 					"%1$s浴火焚身",
    "death.attack.inFire.player": 			"%1$s在与%2$s战斗时踏入了火中",
    "death.attack.inWall": 					"%1$s在墙里窒息而亡",
    "death.attack.inWall.player": 			"%1$s在与%2$s战斗时在墙里窒息而亡",
    "death.attack.indirectMagic": 			"%1$s被%2$s使用的魔法杀死了",
    "death.attack.indirectMagic.item": 		"%1$s被%2$s用%3$s杀死了",
    "death.attack.lava": 					"%1$s试图在熔岩里游泳",
    "death.attack.lava.player": 			"%1$s在逃离%2$s时试图在熔岩里游泳",
    "death.attack.lightningBolt": 			"%1$s被闪电击中",
    "death.attack.lightningBolt.player": 	"%1$s在与%2$s战斗时被闪电击中",
    "death.attack.magic": 					"%1$s被魔法杀死了",
    "death.attack.magic.player": 			"%1$s在试图逃离%2$s时被魔法杀死了",
    "death.attack.mob": 					"%1$s被%2$s杀死了",
    "death.attack.mob.item": 				"%1$s被%2$s用%3$s杀死了",
    "death.attack.onFire": 					"%1$s被烧死了",
    "death.attack.onFire.player": 			"%1$s在与%2$s战斗时被烤得酥脆",
    "death.attack.outOfWorld": 				"%1$s掉出了这个世界",
    "death.attack.outOfWorld.player": 		"%1$s与%2$s不共戴天",
    "death.attack.player": 					"%1$s被%2$s杀死了",
    "death.attack.player.item": 			"%1$s被%2$s用%3$s杀死了",
    "death.attack.stalagmite": 				"%1$s被石笋刺穿了",
    "death.attack.stalagmite.player": 		"%1$s在与%2$s战斗时被石笋刺穿了",
    "death.attack.starve": 					"%1$s饿死了",
    "death.attack.starve.player": 			"%1$s在与%2$s战斗时饿死了",
    "death.attack.sting": 					"%1$s被蛰死了",
    "death.attack.sting.player": 			"%1$s被%2$s蛰死了",
    "death.attack.sweetBerryBush": 			"%1$s被甜浆果丛刺死了",
    "death.attack.sweetBerryBush.player": 	"%1$s在试图逃离%2$s时被甜浆果丛刺死了",
    "death.attack.thorns": 					"%1$s在试图伤害%2$s时被杀",
    "death.attack.thorns.item": 			"%1$s在试图伤害%2$s时被%3$s杀死",
    "death.attack.thrown": 					"%1$s被%2$s给砸死了",
    "death.attack.thrown.item": 			"%1$s被%2$s用%3$s给砸死了",
    "death.attack.trident": 				"%1$s被%2$s刺穿了",
    "death.attack.trident.item": 			"%1$s被%2$s用%3$s刺穿了",
    "death.attack.wither": 					"%1$s凋零了",
    "death.attack.wither.player": 			"%1$s在与%2$s战斗时凋零了",
    "death.attack.witherSkull": 			"%1$s被%2$s发射的头颅射杀",
    "death.fell.accident.generic": 			"%1$s从高处摔了下来",
    "death.fell.accident.ladder": 			"%1$s从梯子上摔了下来",
    "death.fell.accident.other_climbable": 	"%1$s在攀爬时摔了下来",
    "death.fell.accident.scaffolding": 		"%1$s从脚手架上摔了下来",
    "death.fell.accident.twisting_vines": 	"%1$s从缠怨藤上摔了下来",
    "death.fell.accident.vines": 			"%1$s从藤蔓上摔了下来",
    "death.fell.accident.weeping_vines": 	"%1$s从垂泪藤上摔了下来",
    "death.fell.assist": 					"%1$s因为%2$s注定要摔死",
    "death.fell.assist.item": 				"%1$s因为%2$s使用了%3$s注定要摔死",
    "death.fell.finish": 					"%1$s摔伤得太重并被%2$s完结了生命",
    "death.fell.finish.item": 				"%1$s摔伤得太重并被%2$s用%3$s完结了生命",
    "death.fell.killer": 					"%1$s注定要摔死"
}
```

### 颜色代码

![QQ截图20220615172616.png](https://pic.rmb.bdstatic.com/bjh/ad4369e66d63e4cd6a086a216c9b64fd.png)

![QQ截图20220615172632.png](https://pic.rmb.bdstatic.com/bjh/aae99485d99a90e96f3246a866a57850.png)

**注：具体使用时要用`&`代替上面的分节符`§`，这是为了方便输入。**