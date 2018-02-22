package org.meowcat.MultiExperience;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import me.clip.placeholderapi.external.EZPlaceholderHook;

public class PAPIHooker extends EZPlaceholderHook{

    FileConfiguration getConfig = Main.Main.getConfig();
	
    public PAPIHooker(Plugin plugin){
        super(plugin, "multiexp");
    }
    
    @Override
    public String onPlaceholderRequest(Player player, String string) {
    	if (string.equals("time")) {
			if (getConfig.getBoolean("configuration.multiexp-enabled")) {
				long CurrentTime = System.currentTimeMillis();
				long MultiTime = getConfig.getLong("multiexp.tick-time");
				if (CurrentTime <= MultiTime) {
					long RemainingTime = (MultiTime - CurrentTime) / 60000;
					int Time=(int) Math.floor(RemainingTime);
					return Time + getConfig.getString("language.papi-minutes");
				} else {
	        		String message = getConfig.getString("language.papi-expired");
	        		return message;
				} 
			} else {
        		String message = getConfig.getString("language.papi-expired");
        		return message;
			}
        } else if (string.equals("multiple")) {
        	String Multiple = getConfig.getString("multiexp.multiple");
        	return Multiple;
        }
        return null;
    }
}