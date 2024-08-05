package com.wuzuy.bot;

import com.wuzuy.bot.database.CRUD;
import com.wuzuy.bot.database.Config;
import com.wuzuy.bot.main.commands.PingCommand;
import com.wuzuy.bot.main.commands.PrefixCommand;
import com.wuzuy.bot.main.commands.RolesCommand;
import com.wuzuy.bot.main.listeners.GuildJoinListener;
import com.wuzuy.bot.main.listeners.MemberJoinListener;
import com.wuzuy.bot.main.listeners.MemberLeaveListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static net.dv8tion.jda.api.entities.Activity.playing;

public class DevBot {
    public static JDA jda;
    public static Map<String, String> prefixMap = new HashMap<>();
    public static Map<String, String> autoroleMap = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(DevBot.class);

    public static void main(String[] args) throws Exception {
        logger.info("Inicializando o bot...");
        JDALogger.setFallbackLoggerEnabled(false);

        String token = System.getenv("BOT_TOKEN");

        if (token == null || token.isEmpty()) {
            logger.error("O token do bot não foi definido.");
            return;
        }

        Config.createFilesAndTable();

        jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT)
                .build();
        logger.info("Bot inicializado com sucesso!");

        // Commands
        jda.addEventListener(new PingCommand(),
                new PrefixCommand(),
                new RolesCommand()
        );

        // Listeners
        jda.addEventListener(new MemberJoinListener(),
                new MemberLeaveListener(),
                new GuildJoinListener()
        );

        // After ready:
        jda.awaitReady();

        for (Guild guild : jda.getGuilds()) {

            CRUD.insert(guild.getId(), "$");
        }

        for (Guild guild : jda.getGuilds()) {
            CRUD.select(guild.getId());
        }
        setActivity(jda);
    }
    public static void setActivity(JDA jda) throws InterruptedException {
        jda.getPresence().setPresence(OnlineStatus.IDLE,
                playing("em " + jda.awaitReady().getGuilds().size() + " servidores!"));
    }
}