package jp.jyn.jecon.api;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class BalanceUpdateEvent extends Event {

    /**
     * 同期的に[BalanceUpdateEvent]を呼び出します。
     * @param uuid
     * @param balance 所持金
     * @param value 付与した金額
     * @return イベントの結果を返します。
     */
    public static BalanceUpdateEvent CallBalanceUpdateEvent(UUID uuid, Double balance, Double value) {
        BalanceUpdateEvent updateEvent = new BalanceUpdateEvent(uuid, value, (balance + value), balance);
        Bukkit.getServer().getPluginManager().callEvent(updateEvent);
        return updateEvent;
    }


    public BalanceUpdateEvent(UUID _accountUniqueId, double _amount, double _newBalance, double _previous) {
        accountUniqueId = _accountUniqueId;
        amount = _amount;
        newBalance = _newBalance;
        previous = _previous;
    }

    private static HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public boolean isCancelled = false;
    public UUID accountUniqueId;
    public double amount;
    public double newBalance;
    public double previous;

}
