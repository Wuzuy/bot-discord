package com.wuzuy.bot.main.commands;

import com.wuzuy.bot.DevBot;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RolesCommand extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        Character prefixo = DevBot.prefixMap.get(event.getGuild().getId()); // Prefixo base: $
        String[] args = event.getMessage().getContentRaw().split(" ");

        if(args[0].equalsIgnoreCase(prefixo + "autorole")){

            for (Role role: event.getGuild().getRoles()) {
                event.getChannel().sendMessage(role.getName()).queue();
            }
        }
    }
}
