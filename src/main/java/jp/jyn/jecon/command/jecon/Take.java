package jp.jyn.jecon.command.jecon;

import jp.jyn.jbukkitlib.command.SubCommand;
import jp.jyn.jbukkitlib.config.parser.template.variable.StringVariable;
import jp.jyn.jbukkitlib.config.parser.template.variable.TemplateVariable;
import jp.jyn.jbukkitlib.uuid.UUIDRegistry;
import jp.jyn.jecon.config.MessageConfig;
import jp.jyn.jecon.repository.BalanceRepository;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.List;
import java.util.Queue;

public class Take extends SubCommand {
    private final MessageConfig message;
    private final UUIDRegistry registry;
    private final BalanceRepository repository;

    public Take(MessageConfig message, UUIDRegistry registry, BalanceRepository repository) {
        this.message = message;
        this.registry = registry;
        this.repository = repository;
    }

    @SuppressWarnings("Duplicates")
    @Override
    protected Result onCommand(CommandSender sender, Queue<String> args) {
        String to = args.remove();
        BigDecimal amount = CommandUtils.parseDecimal(args.element());
        if (amount == null) {
            sender.sendMessage(message.invalidArgument.toString("value", args.element()));
            return Result.OK;
        }

        registry.getUUIDAsync(to).thenAcceptSync(uuid -> {
            TemplateVariable variable = StringVariable.init().put("name", to);
            if (!uuid.isPresent()) {
                sender.sendMessage(message.playerNotFound.toString(variable));
                return;
            }
            if (!repository.hasAccount(uuid.get())) {
                sender.sendMessage(message.accountNotFound.toString(variable));
                return;
            }
            repository.withdraw(uuid.get(), amount);
            sender.sendMessage(message.take.toString(variable.put("amount", repository.format(amount))));
        });
        return Result.OK;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, Deque<String> args) {
        return CommandUtils.tabCompletePlayer(args);
    }

    @Override
    protected String requirePermission() {
        return "jecon.take";
    }

    @Override
    protected int minimumArgs() {
        return 2;
    }

    @Override
    public CommandHelp getHelp() {
        return new CommandHelp(
            "/money take <player> <amount>",
            message.help.take.toString(),
            "/money take notch 100"
        );
    }
}

