package com.wuzuy.bot.main.commands;

import com.wuzuy.bot.DevBot;
import com.wuzuy.bot.database.CRUD;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;
import java.util.Objects;

public class PrefixCommand extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        String [] args = event.getMessage().getContentRaw().split(" ");
        TextChannel textChannel = (TextChannel) event.getChannel();
        String prefixo = DevBot.prefixMap.get(event.getGuild().getId()); // Prefixo base: $

        if (args[0].equalsIgnoreCase( prefixo + "prefix")) { // Comando $prefix
            textChannel.sendMessage("O prefixo para este servidor é:" + prefixo).queue();
        }

        if (args[0].equalsIgnoreCase(prefixo + "setprefix")) {

            if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)){
                textChannel.sendMessage(event.getMember().getAsMention() + " você não tem permissão para usar esse comando!").queue();
                return;
            }
            DevBot.prefixMap.replace(event.getGuild().getId(), args[1]);
            try {
                CRUD.update("prefix", args[1], event.getGuild().getId());
                textChannel.sendMessage("O prefixo foi alterado para: " + DevBot.prefixMap.get(event.getGuild().getId())).queue();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
