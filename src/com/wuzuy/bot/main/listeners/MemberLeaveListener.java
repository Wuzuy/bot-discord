package com.wuzuy.bot.main.listeners;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemberLeaveListener extends ListenerAdapter {

    private static final long CHANNEL_ID = 1167106335012307045L;
    private static final Logger logger = LoggerFactory.getLogger(MemberJoinListener.class);

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {


        TextChannel textChannel = event.getGuild().getTextChannelById(CHANNEL_ID);
        String username = event.getUser().getName();

        assert textChannel != null;
        logger.info("Membro saiu: " + event.getUser().getName() + " ID:" + event.getUser().getId());  // Log para depuração
        logger.info("Guild: " + event.getGuild().getName()); // Adicionar log do nome do servidor

        textChannel.sendMessage(username + " entrou no servidor!").queue(
                success -> logger.info("Message sent successfully."),
                error -> logger.error("Failed to send message.", error)
        );
    }
}
