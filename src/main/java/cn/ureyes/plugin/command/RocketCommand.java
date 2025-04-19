package cn.ureyes.plugin.command;

import cn.ureyes.plugin.TrollUE;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class RocketCommand {
    private final TrollUE plugin;

    public RocketCommand(TrollUE plugin) {
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(plugin.getPrefix() + ChatColor.YELLOW + "用法: /trollue rocket <玩家名> <Power(0-5)>");
            return true;
        }

        if (Integer.parseInt(args[2]) > 5)
        {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Power超出限制: " + args[2]);
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "玩家未在线: " + args[1]);
            return true;
        }

        // 在玩家身上生成烟花
        Firework firework = target.getWorld().spawn(target.getLocation(), Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.addEffect(FireworkEffect.builder()
                .withColor(Color.RED)
                .with(Type.BALL_LARGE)
                .trail(true)
                .flicker(true)
                .build());
        meta.setPower(Integer.parseInt(args[2]));
        firework.setFireworkMeta(meta);

        firework.addPassenger(target);

        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "已将烟花捆绑到玩家 " + args[1]);
        return true;
    }
}
