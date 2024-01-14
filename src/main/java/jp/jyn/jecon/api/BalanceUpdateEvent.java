package jp.jyn.jecon.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class BalanceUpdateEvent extends Event {

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
