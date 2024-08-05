package com.wuzuy.bot.main.listeners;

import com.wuzuy.bot.DevBot;
import com.wuzuy.bot.database.CRUD;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;

public class GuildLeaveListener extends ListenerAdapter {
    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        try {
            CRUD.insert(event.getGuild().getId(), "$");
            CRUD.select(event.getGuild().getId());
            DevBot.setActivity(event.getJDA());
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
