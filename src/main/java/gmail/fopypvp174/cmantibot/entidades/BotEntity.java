package gmail.fopypvp174.cmantibot.entidades;

public final class BotEntity {

    private Long time;
    private String ipv4;

    public BotEntity(Long time, String ipv4) {
        this.time = time;
        this.ipv4 = ipv4;
    }

    public BotEntity(Long time) {
        this.time = time;
    }

    public final Long getTime() {
        return time;
    }

    public final void setTime(Long time) {
        this.time = time;
    }

    public final String getIpv4() {
        return ipv4;
    }

    public final void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }
}
