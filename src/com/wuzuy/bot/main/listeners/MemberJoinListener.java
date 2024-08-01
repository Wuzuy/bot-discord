package com.wuzuy.bot.main.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class MemberJoinListener extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(MemberJoinListener.class);
    private static final long CHANNEL_ID = 1167106335012307045L; // Verifique se o ID está correto

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        TextChannel textChannel = event.getGuild().getTextChannelById(CHANNEL_ID);

        Member joined = event.getMember();
        Guild guild = event.getGuild();

        // Adicionar cargo ao entrar
        guild.addRoleToMember(UserSnowflake.fromId(joined.getId()), Objects.requireNonNull(guild.getRoleById(1167132871505481742L))).queue();
        logger.info("Novo membro entrou: {} ID:{}", event.getUser().getName(), event.getUser().getId());  // Log para depuração
        logger.info("Guild: {}", event.getGuild().getName()); // Adicionar log do nome do servidor

        // Verificação nulo
        if (textChannel == null) {
            logger.error("Channel with ID {} not found!", CHANNEL_ID);
            return;
        }

        // log
//        String username = event.getUser().getName();
//        textChannel.sendMessage(username + " entrou no servidor!").queue(
//                success -> logger.info("Message sent successfully."),
//                error -> logger.error("Failed to send message.", error)
//        );
    }
}