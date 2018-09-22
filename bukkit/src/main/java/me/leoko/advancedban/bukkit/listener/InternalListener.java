package me.leoko.advancedban.bukkit.listener;

import lombok.RequiredArgsConstructor;
import me.leoko.advancedban.AdvancedBan;
import me.leoko.advancedban.bukkit.event.PunishmentEvent;
import me.leoko.advancedban.bukkit.event.RevokePunishmentEvent;
import me.leoko.advancedban.punishment.PunishmentType;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Date;

/**
 *
 * @author Beelzebu
 */
@RequiredArgsConstructor
public class InternalListener implements Listener {
    private final AdvancedBan advancedBan;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPunish(PunishmentEvent e) {
        BanList banlist;
        String reason = advancedBan.getMessageManager().getReasonOrDefault(e.getPunishment().getReason());
        if (e.getPunishment().getType().equals(PunishmentType.BAN) || e.getPunishment().getType().equals(PunishmentType.TEMP_BAN)) {
            banlist = Bukkit.getBanList(BanList.Type.NAME);
            banlist.addBan(e.getPunishment().getName(), reason, new Date(e.getPunishment().getEnd()), e.getPunishment().getOperator());
        } else if (e.getPunishment().getType().equals(PunishmentType.IP_BAN) || e.getPunishment().getType().equals(PunishmentType.TEMP_IP_BAN)) {
            banlist = Bukkit.getBanList(BanList.Type.IP);
            banlist.addBan(e.getPunishment().getName(), reason, new Date(e.getPunishment().getEnd()), e.getPunishment().getOperator());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRevokePunishment(RevokePunishmentEvent e) {
        BanList banlist;
        if (e.getPunishment().getType().equals(PunishmentType.BAN) || e.getPunishment().getType().equals(PunishmentType.TEMP_BAN)) {
            banlist = Bukkit.getBanList(BanList.Type.NAME);
            banlist.pardon(e.getPunishment().getName());
        } else if (e.getPunishment().getType().equals(PunishmentType.IP_BAN) || e.getPunishment().getType().equals(PunishmentType.TEMP_IP_BAN)) {
            banlist = Bukkit.getBanList(BanList.Type.IP);
            banlist.pardon(e.getPunishment().getName());
        }
    }
}