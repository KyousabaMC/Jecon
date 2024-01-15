package jp.jyn.jecon.command.pay;

import jp.jyn.jbukkitlib.command.SubCommand;
import jp.jyn.jbukkitlib.config.parser.template.variable.StringVariable;
import jp.jyn.jbukkitlib.config.parser.template.variable.TemplateVariable;
import jp.jyn.jbukkitlib.uuid.UUIDRegistry;
import jp.jyn.jecon.command.jecon.CommandUtils;
import jp.jyn.jecon.command.jecon.Pay;
import jp.jyn.jecon.config.MessageConfig;
import jp.jyn.jecon.repository.BalanceRepository;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.*;

public class PayCommandExecutor implements CommandExecutor, TabExecutor {
    private final MessageConfig message;
    private final UUIDRegistry registry;
    private final BalanceRepository repository;
    private final Pay payCmd;

    public PayCommandExecutor(MessageConfig message, UUIDRegistry registry, BalanceRepository repository) {
        this.message = message;
        this.registry = registry;
        this.repository = repository;
        this.payCmd = new Pay(message, registry, repository);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("jecon.pay")) {
            sender.sendMessage(message.doNotHavePermission.toString());
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("========== Jecon ==========");
            sender.sendMessage(payCmd.getHelp().usage);
            sender.sendMessage(payCmd.getHelp().description);
            sender.sendMessage("\nExample:");
            sender.sendMessage(payCmd.getHelp().example);
            return true;
        }

        Player player = (Player) sender;
        UUID from = player.getUniqueId();
        String to = args[0];
        BigDecimal amount = CommandUtils.parseDecimal(args[1]);
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            player.sendMessage(message.invalidArgument.toString("value", args[1]));
            return true;
        }

        // self check
        if (player.getName().equalsIgnoreCase(to)) {
            player.sendMessage(message.invalidArgument.toString("value", to));
            return true;
        }

        registry.getUUIDAsync(to).thenAcceptSync(uuid -> {
            if (!uuid.isPresent()) {
                sender.sendMessage(message.playerNotFound.toString("name", to));
                return;
            }
            if (!repository.has(from, amount)) {
                sender.sendMessage(message.notEnough.toString());
                return;
            }
            if (!repository.hasAccount(uuid.get())) {
                sender.sendMessage(message.accountNotFound.toString("name", to));
                return;
            }

            // pay money
            repository.withdraw(from, amount);
            repository.deposit(uuid.get(), amount);

            // send message
            TemplateVariable variable = StringVariable.init().put("amount", repository.format(amount));
            sender.sendMessage(message.paySuccess.toString(variable.put("name", to)));

            // If the recipient is online send a message
            Player receiver = Bukkit.getPlayer(uuid.get());
            if (receiver != null) {
                receiver.sendMessage(message.payReceive.toString(variable.put("name", sender.getName())));
            }
        });
        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] strings) {
        return payCmd.onTabComplete(sender, command, s, strings);
    }
}
