package gmail.fopypvp174.cmantibot;

import com.ip2proxy.IP2Proxy;
import gmail.fopypvp174.cmantibot.logger.LoggerApi;
import gmail.fopypvp174.cmantibot.entidades.BotEntity;
import gmail.fopypvp174.cmantibot.eventos.EventoJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;

public final class CmAntiBot extends JavaPlugin {

    private final IP2Proxy Proxy = new IP2Proxy();
    private final EventoJoinEvent eventoJoinEvent = new EventoJoinEvent(this);;
    private final LoggerApi loggerApi = new LoggerApi();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(eventoJoinEvent, this);
        this.saveResource("IP2PROXY-LITE-PX1.BIN", false);
        loggerApi.registerFilter();

        try {
            if (Proxy.Open("IP2PROXY-LITE-PX1.BIN", IP2Proxy.IOModes.IP2PROXY_MEMORY_MAPPED) == 0) {
                System.out.println("[cmAntiBot] Proxys carregadas com sucesso.");
            } else {
                System.out.println("[cmAntiBot] Error reading BIN file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        setupTimer();
    }

    public final Integer checkIpv4(String ipv4) {
        try {
            return Proxy.IsProxy(ipv4);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public final void setupTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                ArrayList<String> nomesDeletar = new ArrayList<>();
                eventoJoinEvent.getIpsAllowJoin().forEach((String nome, BotEntity entidade) -> {
                    if ((entidade.getTime() + (1000 * 20)) < System.currentTimeMillis()) {
                        nomesDeletar.add(nome);
                    }
                });

                nomesDeletar.forEach(nome -> eventoJoinEvent.getIpsAllowJoin().remove(nome));
            }
        }.runTaskTimerAsynchronously(this, 0L, 400L);
    }

    @Override
    public void onDisable() {
        Proxy.Close();
    }
}
