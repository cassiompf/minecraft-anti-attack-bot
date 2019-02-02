package gmail.fopypvp174.cmantibot.eventos;

import gmail.fopypvp174.cmantibot.CmAntiBot;
import gmail.fopypvp174.cmantibot.entidades.BotEntity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.LinkedHashMap;

public final class EventoJoinEvent implements Listener {

    private CmAntiBot cmAntiBot;
    private Long timeAnterior;
    private LinkedHashMap<String, BotEntity> ipsAllowJoin = new LinkedHashMap<>();

    public EventoJoinEvent(CmAntiBot plugin) {
        cmAntiBot = plugin;
    }

    public final LinkedHashMap<String, BotEntity> getIpsAllowJoin() {
        return ipsAllowJoin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public final void asyncPlayerLoginEvent(AsyncPlayerPreLoginEvent e) {

        if (timeAnterior == null) {
            timeAnterior = System.currentTimeMillis();
            return;
        }

        if (cmAntiBot.getPluginConfig().getBoolean("settings.verify_player_registed") == false) {
            if (cmAntiBot.getServer().getPluginManager().isPluginEnabled("AuthMe")) {
                if (fr.xephi.authme.api.v3.AuthMeApi.getInstance().isRegistered(e.getName())) {
                    return;
                }
            } else {
                if(cmAntiBot.getServer().getPluginManager().isPluginEnabled("nLogin") && (nickultracraft.login.api.nLoginAPI.getInstance().estaRegistrado(e.getName()))) {
            		return;
            	}
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[cmAntiBot] É preciso estar utilizando o AuthMe-Reloaded ou nLogin");
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[cmAntiBot] para a opção settings.verify_player_registed");
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[cmAntiBot] estar definida como 'false'");
            }
        }
        StringBuilder mensagem = new StringBuilder();

        BotEntity botEntrou = new BotEntity(System.currentTimeMillis());
        Long timer = botEntrou.getTime() - timeAnterior;
        timeAnterior = System.currentTimeMillis();
        if (timer < cmAntiBot.getPluginConfig().getInt("settings.time_check")) {
            cmAntiBot.getPluginConfig().getStringList("messages.time_check").forEach(msg ->
                    mensagem.append(msg));
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, mensagem.toString());
            return;
        }

        String ipv4 = e.getAddress().getHostName().replace("\\", "");
        if (cmAntiBot.getPluginConfig().getInt("settings.limit_ip") != 0) {
            byte contador = 0;
            for (Player target : Bukkit.getOnlinePlayers()) {
                if (ipv4.equals(target.getAddress().getHostName().replace("\\", ""))) {
                    contador++;
                }
                if (contador > cmAntiBot.getPluginConfig().getInt("settings.limit_ip")) {

                    cmAntiBot.getPluginConfig().getStringList("messages.limit_ip").forEach(msg ->
                            mensagem.append(msg));

                    e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, mensagem.toString());
                    return;
                }
            }
        }

        if (cmAntiBot.getPluginConfig().getBoolean("settings.proxy_verify") == true) {
            if (cmAntiBot.checkIpv4(ipv4) != 0) {
                cmAntiBot.getPluginConfig().getStringList("messages.proxy_detected").forEach(msg ->
                        mensagem.append(msg));
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, mensagem.toString());
                return;
            }
        }
        botEntrou.setIpv4(ipv4);

        if (cmAntiBot.getPluginConfig().getBoolean("settings.verify_nick") == true) {
            cmAntiBot.getPluginConfig().getStringList("messages.verify_nick").forEach(msg ->
                    mensagem.append(msg));

            if (!ipsAllowJoin.containsKey(e.getName())) {
                ipsAllowJoin.put(e.getName(), botEntrou);
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, mensagem.toString());
                return;
            }

            if (!ipsAllowJoin.get(e.getName()).getIpv4().equals(ipv4)) {
                ipsAllowJoin.replace(e.getName(), botEntrou);
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, mensagem.toString());
                return;
            }
        }

        ipsAllowJoin.remove(e.getName());
        e.allow();
        return;
    }
}
