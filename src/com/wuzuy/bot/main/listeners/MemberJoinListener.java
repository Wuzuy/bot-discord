package com.wuzuy.bot.main.listeners;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemberJoinListener extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(MemberJoinListener.class);
    private static final long CHANNEL_ID = 1167106335012307045L; // Verifique se o ID está correto

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        logger.info("Novo membro entrou: " + event.getUser().getName() + " ID:" + event.getUser().getId());  // Log para depuração
        logger.info("Guild: " + event.getGuild().getName()); // Adicionar log do nome do servidor
        TextChannel textChannel = event.getGuild().getTextChannelById(CHANNEL_ID);

        if (textChannel == null) {
            logger.error("Channel with ID {} not found!", CHANNEL_ID);
            return;
        }

        String username = event.getUser().getName();
        textChannel.sendMessage(username + " entrou no servidor!").queue(
                success -> logger.info("Message sent successfully."),
                error -> logger.error("Failed to send message.", error)
        );
    }
}