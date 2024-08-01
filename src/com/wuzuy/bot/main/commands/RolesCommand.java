package com.wuzuy.bot.main.commands;

import com.wuzuy.bot.DevBot;
import com.wuzuy.bot.database.CRUD;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Classe que lida com o comando de auto-atribuição de cargos (autorole) no bot.
 */
public class RolesCommand extends ListenerAdapter {

    private static final Logger logger = Logger.getLogger(RolesCommand.class.getName());

    private final Map<Byte, String> rolesMap = new HashMap<>();
    private final Map<String, Map<Byte, String>> guildRolesMapMap = new HashMap<>();
    private final Map<String, Boolean> isEditingAutoRole = new HashMap<>();
    private final Map<String, Member> memberEditingAutoRoleMap = new HashMap<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        TextChannel textChannel = (TextChannel) event.getChannel();
        Character prefixo = DevBot.prefixMap.get(event.getGuild().getId()); // Prefixo base: $
        String[] args = event.getMessage().getContentRaw().split(" ");

        byte roleIndex = 1;
        String gId = event.getGuild().getId();

        // Verifica se o comando recebido é o comando de autorole
        if (args[0].equalsIgnoreCase(prefixo + "autorole")) {
            logger.info("Comando autorole recebido.");

            // Verifica se o membro possui permissão de administrador
            if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                textChannel.sendMessage(event.getMember().getAsMention() + ", você não possui a permissão necessária!").queue();
                return;
            }

            // Monta a lista de cargos disponíveis no servidor
            StringBuilder stringBuilder = new StringBuilder();
            for (Role role : event.getGuild().getRoles()) {
                if (!role.isPublicRole()) {
                    rolesMap.put(roleIndex, role.getId());
                    stringBuilder.append(roleIndex).append(" - ").append(role.getName()).append("\n");
                    roleIndex++;
                }
            }

            // Armazena o mapa de cargos e atualiza os estados de edição
            guildRolesMapMap.put(gId, rolesMap);
            isEditingAutoRole.put(gId, true);
            memberEditingAutoRoleMap.put(gId, event.getMember());

            // Envia a lista de cargos disponíveis para o canal de texto
            event.getChannel().sendMessage(stringBuilder.toString()).queue();

        } else if (args[0].matches("^[0-9]{1,23}$")) { // Verifica se o argumento é um número de 1 a 23
            logger.info("Número de cargo recebido: " + args[0]);

            // Verifica se a edição de autorole está ativa e o membro é o correto
            if (isEditingAutoRole.get(gId) == null ||
                    !isEditingAutoRole.get(gId) ||
                    !event.getMember().equals(memberEditingAutoRoleMap.get(gId)) ||
                    event.getGuild().getJDA().getSelfUser().getId().equals(event.getMember().getId()) ||
                    guildRolesMapMap.get(event.getGuild().getId()) == null)
                return;

            byte selectedRoleIndex = Byte.parseByte(args[0]);
            String roleId = guildRolesMapMap.get(gId).get(selectedRoleIndex);

            // Verifica se o roleId é válido
            if (roleId == null) {
                textChannel.sendMessage("Role não encontrada.").queue();
                return;
            }

            Role selectedRole = event.getGuild().getRoleById(roleId);
            if (selectedRole == null) {
                textChannel.sendMessage("Role não encontrada no servidor.").queue();
                return;
            }

            // Informa o cargo selecionado no canal de texto
            event.getChannel().sendMessage("O cargo selecionado foi: " + selectedRole.getName()).queue();

            // Tenta atualizar o banco de dados com o cargo selecionado
            try {
                CRUD.update("autorole", roleId, gId);
                logger.info("Comando autorole atualizado no banco de dados.");
            } catch (SQLException e) {
                textChannel.sendMessage("Erro ao salvar no banco de dados: " + e.getMessage()).queue();
                logger.severe("Erro ao salvar no banco de dados: " + e.getMessage());
                e.printStackTrace();
            }

            // Atualiza o estado de edição de autorole para false
            isEditingAutoRole.put(gId, false);
        }
    }
}
