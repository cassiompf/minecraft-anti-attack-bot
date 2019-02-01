package gmail.fopypvp174.cmantibot.eventos;

import gmail.fopypvp174.cmantibot.CmAntiBot;
import gmail.fopypvp174.cmantibot.entidades.BotEntity;
import gmail.fopypvp174.cmlogincaptcha.api.LoginApi;
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

        if (LoginApi.hasRegistred(e.getName())) {
            return;
        }

        BotEntity botEntrou = new BotEntity(System.currentTimeMillis());
        timeAnterior = System.currentTimeMillis();
        Long timer = botEntrou.getTime() - timeAnterior;
        if (timer < 250) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "O servidor está passando por dificuldades no momento! \nEspere um pouco e tente entrar novamente!");
            return;
        }

        String ipv4 = e.getAddress().getHostName().replace("\\", "");
        byte contador = 0;
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (ipv4.equals(target.getAddress().getHostName().replace("\\", ""))) {
                contador++;
            }
            if (contador > 2) {
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Só pode ter 2 contas logadas por IP!");
                return;
            }
        }

        if (cmAntiBot.checkIpv4(ipv4) != 0) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Não entre com proxy no servidor!");
            return;
        }

        botEntrou.setIpv4(ipv4);
        if (!ipsAllowJoin.containsKey(e.getName())) {
            ipsAllowJoin.put(e.getName(), botEntrou);
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "O seu nick foi verificado! \nEntre novamente para jogar!");
            return;
        }

        if (!ipsAllowJoin.get(e.getName()).getIpv4().equals(ipv4)) {
            ipsAllowJoin.replace(e.getName(), botEntrou);
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "O seu nick foi verificado! \nEntre novamente para jogar!");
            return;
        }

        ipsAllowJoin.remove(e.getName());
        e.allow();
        return;
    }
}
