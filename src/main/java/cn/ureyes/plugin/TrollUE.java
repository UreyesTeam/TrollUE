package cn.ureyes.plugin;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import cn.ureyes.plugin.command.CommandDispatcher;

public final class TrollUE extends JavaPlugin {
    private CommandDispatcher dispatcher;

    @Override
    public void onEnable() {
        // 保存默认配置
        this.saveDefaultConfig();

        // 注册执行器指令
        dispatcher = new CommandDispatcher(this);
        getCommand("trollue").setExecutor(dispatcher);
        getCommand("trollue").setTabCompleter(dispatcher);

        getLogger().info("TrollUE插件已加载");
    }

    @Override
    public void onDisable() {
        // 停止所有执行器任务
        dispatcher.onDisable();
    }

    public String getPrefix() {
        String raw = getConfig().getString("prefix", "&eTheUrEyesServer &f&l>>> ");
        return ChatColor.translateAlternateColorCodes('&', raw);
    }

    public void reloadPluginConfig() {
        this.reloadConfig();
    }
}