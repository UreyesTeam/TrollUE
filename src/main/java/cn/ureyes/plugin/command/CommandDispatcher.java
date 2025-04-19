package cn.ureyes.plugin.command;

import cn.ureyes.plugin.TrollUE;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandDispatcher implements CommandExecutor, TabCompleter {
    private final TrollUE plugin;
    private final SudoCommand sudoCmd;
    private final SpinningCommand spinCmd;
    private final RocketCommand rocketCmd;

    public CommandDispatcher(TrollUE plugin) {
        this.plugin = plugin;
        this.sudoCmd   = new SudoCommand(plugin);
        this.spinCmd   = new SpinningCommand(plugin);
        this.rocketCmd = new RocketCommand(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(plugin.getPrefix() + ChatColor.YELLOW +
                    "用法: /trollue <reload/sudo/spinning/rocket> [...]");
            return true;
        }

        String sub = args[0].toLowerCase();
        switch (sub) {
            case "reload":
                // 重新加载配置
                if (!sender.hasPermission("trollue.reload")) {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "你没有权限执行此操作");
                } else {
                    plugin.reloadPluginConfig();
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "配置已重新加载");
                }
                return true;

            case "sudo":
                return sudoCmd.execute(sender, args);

            case "spinning":
                return spinCmd.execute(sender, args);

            case "rocket":
                return rocketCmd.execute(sender, args);

            default:
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "未知子命令: " + sub);
                return true;
        }
    }

    public void onDisable() {
        spinCmd.cancelAll();
    }

    /**
     * Tab 补全实现
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command,
                                      String alias, String[] args) {
        // args[0]
        if (args.length == 1) {
            List<String> subs = List.of("reload", "sudo", "spinning", "rocket");
            return subs.stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        // args[1] 补全在线玩家名
        if (args.length == 2) {
            String sub = args[0].toLowerCase();
            if (sub.equals("sudo") || sub.equals("spinning") || sub.equals("rocket")) {
                return Bukkit.getOnlinePlayers()
                        .stream()
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
            }
        }

        // 其他情况不补全
        return Collections.emptyList();
    }
}
