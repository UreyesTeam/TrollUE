package cn.ureyes.plugin.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class RotationTask extends BukkitRunnable {
    private final Plugin plugin;
    private final Player player;
    private float yaw = 0F;

    public RotationTask(Plugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public int start() {
        // delay 0, period 2 ticks
        return this.runTaskTimer(plugin, 0L, 2L).getTaskId();
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            this.cancel();
            return;
        }
        // 获取当前位置并更新偏航角
        Location loc = player.getLocation();
        yaw = (yaw + 20F) % 360F;
        loc.setYaw(yaw);
        // 传送玩家到更新后的方向
        player.teleport(loc);
    }
}