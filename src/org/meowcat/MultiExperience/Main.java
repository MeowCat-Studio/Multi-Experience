package org.meowcat.MultiExperience;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	public static Main Main;
	
    @Override
    public void onEnable() {
        saveDefaultConfig();
        Main=this;
        getServer().getPluginManager().registerEvents(new ExpListener(), this);
        if (getConfig().getBoolean("configuration.auto-broadcast-enabled")) {
            new ExpThread().start();
        }
        Plugin plugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if(plugin != null){
            System.out.println("Hooking PlaceholderAPI");
            boolean success = new PAPIHooker(this).hook();
            if(success == true){
                System.out.println("Hook PlaceholderAPI Successfully!");
            }else{
                System.out.println("Hook PlaceholderAPI Failed!");
            }
        }
        this.getLogger().info("Multi Experience by MeowCat Studio");
        this.getLogger().info("Offical Website http://www.meowcat.org/");
        this.getLogger().info("[MultiExperience]Plugin Enabled.");
    }

    @Override
    public void onDisable() {
        saveConfig();
        this.getLogger().info("Multi Experience by MeowCat Studio");
        this.getLogger().info("Offical Website http://www.meowcat.org/");
        this.getLogger().info("[MultiExperience]Plugin Disabled.");
    }

    @Override
    public boolean onCommand(CommandSender player, Command command, String label, String[] arguments) {
        if (arguments.length > 0) {
            if (arguments[0].equalsIgnoreCase("enable")) {
                float Multiple = 2;
                long MultiTime = 60;
                long CurrentTime = System.currentTimeMillis();
                if (arguments.length > 1) {
                	Multiple = Float.valueOf(arguments[1]);
                    if (arguments.length > 2) {
                    	MultiTime = Long.valueOf(arguments[2]);
                    }
                }
                long RemainingTime = MultiTime * 60000 + CurrentTime;
                getConfig().set("multiexp.enabled", true);
                getConfig().set("multiexp.multiple", Multiple);
                getConfig().set("multiexp.tick-time", RemainingTime);
                player.sendMessage(getConfig().getString("language.msg-signal-1") + Multiple + getConfig().getString("language.msg-signal-2") + MultiTime + getConfig().getString("language.msg-signal-3"));
                getServer().broadcastMessage(getConfig().getString("language.msg-broadcast-1") + Multiple + getConfig().getString("language.msg-broadcast-2") + MultiTime + getConfig().getString("language.msg-broadcast-3"));
            } else {
            	player.sendMessage(getConfig().getString("language.usage"));
            }
        } else {
        	player.sendMessage("¡ìbMulti Experience by MeowCat Studio");
        	player.sendMessage("¡ìbhttp://www.meowcat.org/");
        	player.sendMessage(getConfig().getString("language.usage"));
        }
        return true;
    }
}
