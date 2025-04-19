package cn.ureyes.plugin.command;

import cn.ureyes.plugin.TrollUE;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cn.ureyes.plugin.util.RotationTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpinningCommand {
    private final TrollUE plugin;
    // 存储每个玩家的任务ID
    private final Map<UUID, Integer> tasks = new HashMap<>();

    public SpinningCommand(TrollUE plugin) {
        this.plugin = plugin;
    }

    /**
     * 旋转玩家
     * 用法: /trollue spinning <playerName>
     */
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(plugin.getPrefix() + ChatColor.YELLOW + "用法: /trollue spinning <玩家名>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "玩家未在线: " + args[1]);
            return true;
        }

        UUID uuid = target.getUniqueId();
        if (tasks.containsKey(uuid)) {
            // 已在旋转 取消任务
            int taskId = tasks.remove(uuid);
            plugin.getServer().getScheduler().cancelTask(taskId);
            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + args[1] + " 已停止旋转");
        } else {
            // 启动旋转
            RotationTask task = new RotationTask(plugin, target);
            int taskId = task.start();
            tasks.put(uuid, taskId);
            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + args[1] + " 开始旋转");
        }
        return true;
    }

    // 在插件停用时取消所有旋转任务
    public void cancelAll() {
        for (int taskId : tasks.values()) {
            plugin.getServer().getScheduler().cancelTask(taskId);
        }
        tasks.clear();
    }
}