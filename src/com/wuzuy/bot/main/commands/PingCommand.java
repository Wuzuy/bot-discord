package com.wuzuy.bot.main.commands;

import com.wuzuy.bot.DevBot;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingCommand extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(PingCommand.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (DevBot.jda == null) {
            logger.error("JDA não foi inicializado corretamente!");
            event.getChannel().sendMessage("Erro: JDA não foi inicializado corretamente!").queue();
            return;
        }

        String [] args = event.getMessage().getContentRaw().split(" ");
        TextChannel textChannel = (TextChannel) event.getChannel();

        if (args[0].equalsIgnoreCase(
                DevBot.prefixMap.get(event.getGuild().getId()) + "ping"))
            event.getChannel()
                    .sendMessage("Ping: " + DevBot.jda.getGatewayPing() + "ms").queue();

    }
}
