package jp.jyn.jecon.repository;

import jp.jyn.jecon.Jecon;
import jp.jyn.jecon.api.BalanceUpdateEvent;
import jp.jyn.jecon.config.MainConfig;
import jp.jyn.jecon.db.Database;
import org.bukkit.Bukkit;

import java.util.OptionalLong;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

public class SyncRepository extends AbstractRepository {
    public SyncRepository(MainConfig config, Database db) {
        super(config, db);
    }

    @Override
    protected OptionalLong getRaw(UUID uuid) {
        return db.getBalance(getId(uuid));
    }

    @Override
    protected boolean set(UUID uuid, long balance) {
        return db.setBalance(getId(uuid), balance);
    }

    @Override
    protected boolean deposit(UUID uuid, long amount) {
        OptionalLong balance = getRaw(uuid);
        if (!balance.isPresent()) {
            return false;
        }

        double doubleBalance = ((double) balance.getAsLong()) * 0.01;
        AtomicBoolean updateBalanceEventIsCancelled = new AtomicBoolean(false);
        Bukkit.getScheduler().runTask(Jecon.getInstance(), () -> {
            BalanceUpdateEvent event = BalanceUpdateEvent.CallBalanceUpdateEvent(uuid, doubleBalance, (double) amount *0.01);

            if (event.isCancelled) {
                updateBalanceEventIsCancelled.set(true);
            }
        });
        if (updateBalanceEventIsCancelled.get()) {
            return false;
        }

        return db.deposit(getId(uuid), amount);
    }

    @Override
    protected boolean createAccount(UUID uuid, long balance) {
        return !hasAccount(uuid) && db.createAccount(getId(uuid), balance);
    }

    @Override
    public boolean removeAccount(UUID uuid) {
        return db.removeAccount(getId(uuid));
    }
}
