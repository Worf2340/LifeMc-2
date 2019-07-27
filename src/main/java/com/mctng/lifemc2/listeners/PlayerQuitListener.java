package com.mctng.lifemc2.listeners;

import com.mctng.lifemc2.LifeMc2;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerQuitListener implements Listener {
    LifeMc2 plugin;

    public PlayerQuitListener(LifeMc2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Pattern p = Pattern.compile("lifemc.lives.max.([^.]+$)");
        Matcher m;
        int lives = 0;

        for (PermissionAttachmentInfo permission : event.getPlayer().getEffectivePermissions()) {
            m = p.matcher(permission.getPermission());
            if (m.matches()) {
                if (m.group(1).matches("\\d+")) {
                    if (Integer.parseInt(m.group(1)) > lives) {
                        lives = Integer.parseInt(m.group(1));
                    }
                }
            }
        }

        if (lives == 0){
            plugin.getDataHandler().setLives(event.getPlayer(), plugin.getDataHandler().getLives(event.getPlayer()), plugin.getConfigHandler().getDefaultMaxLives());
        }
        else {
            plugin.getDataHandler().setLives(event.getPlayer(), plugin.getDataHandler().getLives(event.getPlayer()), lives);
        }

    }
}
