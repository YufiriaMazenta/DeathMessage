<div align="center">
<h1>DeathMessage</h1>
<h6>自定义玩家死亡消息</h6>
</div>

## 效果说明

服主自定义玩家的死亡消息，支持颜色代码。

## 效果展示

![a.png](a.png)

## 使用方法

在配置文件中修改对应死亡类型的消息即可，`%1$s`代表死者，如果有杀手则`%2$s`代表击杀者，如果杀手手持命名的物品则`%3$s`代表该物品。

### 指令：
`/deathmessagereload`简写`/dmrl`，作用是重新加载配置文件

权限节点`deathmessage.command.reload`

`/deathmessagefilter`简写`/dmf`，作用是开启/关闭死亡消息屏蔽，参数`on/off`

无权限节点

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