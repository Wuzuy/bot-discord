package com.wuzuy.bot;

import com.wuzuy.bot.database.CRUD;
import com.wuzuy.bot.database.Config;
import com.wuzuy.bot.main.commands.PingCommand;
import com.wuzuy.bot.main.commands.PrefixCommand;
import com.wuzuy.bot.main.commands.RolesCommand;
import com.wuzuy.bot.main.listeners.MemberJoinListener;
import com.wuzuy.bot.main.listeners.MemberLeaveListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class DevBot {
    public static JDA jda;
    public static Map<String, Character> prefixMap = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(DevBot.class);

    public static void main(String[] args) throws Exception {
        logger.info("Inicializando o bot...");
        JDALogger.setFallbackLoggerEnabled(false);

        // Defina o token diretamente aqui para fins de desenvolvimento
        // Lembre-se de remover ou substituir isso por uma solução mais segura antes de mover para produção
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
        jda.addEventListener(new PingCommand());
        jda.addEventListener(new PrefixCommand());
        jda.addEventListener(new RolesCommand());

        // Listeners
        jda.addEventListener(new MemberJoinListener());
        jda.addEventListener(new MemberLeaveListener());

        // After ready:
        jda.awaitReady();

        for (Guild guild : jda.getGuilds()) {

            CRUD.insert(guild.getId(), "$");
        }

        for (Guild guild : jda.getGuilds()) {
            CRUD.select(guild.getId());
        }

//            System.out.println(mapGuildName.get(guild.getIdLong()));
    }
}