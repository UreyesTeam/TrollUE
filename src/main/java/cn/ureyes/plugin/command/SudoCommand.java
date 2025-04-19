package cn.ureyes.plugin.command;

import cn.ureyes.plugin.TrollUE;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SudoCommand {

    private final TrollUE plugin;

    public SudoCommand(TrollUE plugin) {
        this.plugin = plugin;
    }

    /**
     * 命令执行工具
     * 用法: /trollue sudo <playerName> <message or /command>
     */
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(plugin.getPrefix() + ChatColor.YELLOW + "用法: /trollue sudo <玩家名> <消息或/指令>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "玩家未在线: " + args[1]);
            return true;
        }

        // 拼接剩余内容
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        String rest = sb.toString().trim();

        if (rest.startsWith("/")) {
            // 去掉前导斜杠并以玩家身份执行指令
            String cmd = rest.substring(1);
            Bukkit.dispatchCommand(target, cmd);
        } else {
            // 作为聊天消息发送
            target.chat(rest);
        }

        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + sender.getName() + " -> " + args[1] + " 执行: " + rest);
        return true;
    }
}