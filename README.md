# NikoBot [![Build Status](https://api.travis-ci.com/skw-nikobot/NikoBot.svg?branch=master)](https://travis-ci.com/skw-nikobot/NikoBot)

<img align="right" src="https://i.imgur.com/mLzy2i2.png?raw=true" height="200" width="200">

#### A basic Discord bot.
## Introduction
This bot is developed based on [JDA](https://github.com/DV8FromTheWorld/JDA).
Support Customized plugin. You can add new features by yourself, and just add plugins you made into the ```plugin``` folder. Then restart this bot, the new features will auto add to your bot.
## Start bot
Java version : jdk11 or above
```
java -Dfile.encoding=UTF8 -jar NikoBot.jar
```
The first time you start this bot, it will generate cfg, libs, and plugins folder. You need to put your discord app token into ```cfg/token``` and discord user id (e.g. 2XXXXXXXX313887746) into ```cfg/admin```, if you have more than one id, put id into file line by line.
Then restart bot to reload the config.
## Built-in commands
```/info```show bot information.
```/help```show all commands usage including plugins'.
```/shutdown```shutdown bot.
```/reboot```reboot bot.
```/wake```set bot's status to online mode.
```/sleep```set bot's status to idle mode. On this mode bot will not response plugins' common commands.
```/setgame <Game>```set bot's current playing game's name.
## Plugin
You can install plugin by putting its jar file into ```plugins``` folder, and then reboot bot.
When making plugin, remember to add ```NikoBot.jar``` to your project's build path.
If your plugin need additional libraries (e.g. lavaplayer.jar), please put them into ```libs``` folder.
Before exporting your plugin's jar file, you need to create ```Niko.yml``` file in project directory. Its formate is shown as follows.
### Example

#### Say
Function：When someone type ```/say <something>```, bot will send a message:```<something>```to discord.

##### Say.java
```
package com.github.smallru8.plugin.Say;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import com.github.smallru8.NikoBot.event.Event.MessageEvent;
import com.github.smallru8.NikoBot.plugins.PluginsInterface;

public class Say implements PluginsInterface{

	public void onDisable() {
		EventBus.getDefault().unregister(this);
	}

	public void onEnable() {
		EventBus.getDefault().register(this);
	}
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onMessageRecved(MessageEvent e) {
		if(e.msg.getContentRaw().startsWith("/say")&&!e.msg.getAuthor().isBot()) {
			String[] str = e.msg.getContentRaw().split(" ",2);
			if(str.length==2)
				e.msg.getChannel().sendMessage(str[1]).queue();
		}
	}
	public String pluginsName() {
		return "Say";
	}
}
```
##### Niko.yml
First line use to tell bot the plugin start path.
Others is "Help information" which will be shown when user type ```/help```.
```
com.github.smallru8.plugin.Say.Say
Say Help:
    /say .....
```
