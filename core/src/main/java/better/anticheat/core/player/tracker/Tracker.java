package better.anticheat.core.player.tracker;

import better.anticheat.core.player.Player;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import lombok.Getter;

public class Tracker {
    @Getter
    private final Player player;

    public Tracker(Player player) {
        this.player = player;
    }

    public void handlePacketPlayReceive(PacketPlayReceiveEvent event) {}

    public void handlePacketPlaySend(PacketPlaySendEvent event) {}
}
