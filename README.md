# Jecon
Economy base plugin for Bukkit/Spigot  
[日本語解説](https://e-craft.io/bukkit/plugin/jecon/)  
  
※一部のサーバー向けに作成しています。

## Features
* UUID Ready
* 1.20 Ready
* Vault Ready
* MySQL(+SQLite) Ready
* Command tab complete
* Easy to use
* API available

## Installation
1. [Download this plugin.](https://github.com/kuripasanda/Jecon/releases/latest "Get Jecon")
2. drop in plugins directory.
3. Server start.
4. config edit.
5. reload.

## Command/Permission
|Command|Permission|Description|Default|
|:------|:---------|:----------|:------|
|/money|jecon.show|Show your balance.|ALL|
|/money show [player]|jecon.show.other|Show [player] balance.|OP|
|/money pay &lt;player&gt; &lt;amount&gt;|jecon.pay|Send &lt;amount&gt; to &lt;player&gt;.|ALL|
|/money set &lt;player&gt; &lt;balance&gt;|jecon.set|Set the balance of &lt;player&gt; to &lt;balance&gt;.|OP|
|/money give &lt;player&gt; &lt;amount&gt;|jecon.give|Give &lt;amount&gt; to &lt;player&gt;.|OP|
|/money take &lt;player&gt; &lt;amount&gt;|jecon.take|Take &lt;amount&gt; on &lt;player&gt;.|OP|
|/money create &lt;player&gt; [balance]|jecon.create|Create &lt;player&gt; account.|OP|
|/money remove &lt;player&gt;|jecon.remove|Remove &lt;player&gt; account.|OP|
|/money top [page]|jecon.top|Show billionaires ranking.|OP|
|/money convert|jecon.convert|Convert database.|OP|
|/money reload|jecon.reload|Reload the config.|OP|
|/money version|jecon.version|Show version and check new version.|OP|
|/money help|N/A|Show helps.|ALL|
|/pay &lt;player&gt; &lt;amount&gt;|jecon.pay|`/money pay` alias|ALL|

# API
## Maven
```xml
<project>
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
    
    <dependencies>
        <dependency>
            <groupId>com.github.kuripasanda</groupId>
            <artifactId>Jecon</artifactId>
            <version>2.2.5</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
</project>
```
## Gradle
```gradle
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }

    dependencies {
        implementation 'com.github.kuripasanda:Jecon:Tag'
    }
```

## Usage
```java
public class Main extends JavaPlugin {
    private Jecon jecon;

    @Override
    public void onEnable() {
        // get plugin
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Jecon");
        if(plugin == null || !plugin.isEnabled()) {
            // not available
            getLogger().warning("Jecon is not available.");
        }

        this.jecon = (Jecon) plugin;
    }

    public void usage(UUID uuid) {
        // get
        jecon.getRepository().getDecimal(uuid);

        // set
        jecon.getRepository().set(uuid, BigDecimal.ZERO);
    }
}
```
