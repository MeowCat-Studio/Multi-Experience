package org.meowcat.MultiExperience;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    public static Main plugin;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        plugin = this;
        ExpListener listener = new ExpListener();
        getServer().getPluginManager().registerEvents(listener, plugin);
        boolean isBroadcast = getConfig().getBoolean("configuration.auto-broadcast-enabled");
        if (isBroadcast) {
            ExpThread ExpThread = new ExpThread();
            ExpThread.start();
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Multi Experience by MeowCat Studio");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Offical Website http://www.meowcat.org/");
    }

    @Override
    public void onDisable() {
        saveConfig();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Multi Experience by MeowCat Studio");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Offical Website http://www.meowcat.org/");
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
                String message = getConfig().getString("language.msg-signal-1") + Multiple + getConfig().getString("language.msg-signal-2") + MultiTime + getConfig().getString("language.msg-signal-3");
                String broadcast = getConfig().getString("language.msg-broadcast-1") + Multiple + getConfig().getString("language.msg-broadcast-2") + MultiTime + getConfig().getString("language.msg-broadcast-3");
                player.sendMessage(message);
                getServer().broadcastMessage(broadcast);
            } else {
            	String usage = getConfig().getString("language.usage");
            	player.sendMessage(usage);
            }
        } else {
        	String usage = getConfig().getString("language.usage");
        	player.sendMessage("¡ìbMulti Experience by MeowCat Studio");
        	player.sendMessage("¡ìbhttp://www.meowcat.org/");
        	player.sendMessage(usage);
        }
        return true;
    }
}
