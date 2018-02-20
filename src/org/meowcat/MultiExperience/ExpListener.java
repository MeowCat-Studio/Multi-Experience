package org.meowcat.MultiExperience;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class ExpListener implements Listener {

    FileConfiguration getConfig = Main.plugin.getConfig();

    @EventHandler
    public void ExperienceListener(PlayerExpChangeEvent event) {
        if (getConfig.getBoolean("multiexp.enabled")) {
            long CurrentTime = System.currentTimeMillis();
            long MultiTime = getConfig.getLong("multiexp.tick-time");
            if (CurrentTime <= MultiTime) {
                float Multiple = (float) getConfig.getDouble("multiexp.multiple");
                int PickedExp = event.getAmount();
                float CalculatedExp = PickedExp * Multiple;
                String message = getConfig.getString("language.msg-getexp-1") + Multiple + getConfig.getString("language.msg-getexp-2") + CalculatedExp + getConfig.getString("language.msg-getexp-3");
                event.setAmount(Math.round(CalculatedExp));
                event.getPlayer().sendMessage(message);
            } else {
                getConfig.set("multiexp.enabled", false);
                String expired = getConfig.getString("language.msg-expired");
                Main.plugin.getServer().broadcastMessage(expired);
            }
        }
    }
}
