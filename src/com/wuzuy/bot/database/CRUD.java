package com.wuzuy.bot.database;

import com.wuzuy.bot.DevBot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Classe para operações CRUD no banco de dados.
 */
public class CRUD {

    private static final Logger logger = Logger.getLogger(CRUD.class.getName());

    /**
     * Cria a tabela tb_guild no banco de dados se não existir.
     *
     * @throws SQLException Se ocorrer um erro durante a criação da tabela.
     */
    public static void createTable() throws SQLException {
        String sql = """
                create table if not exists tb_guild
                (
                    id integer not null primary key autoincrement unique,
                    guild_id text not null unique,
                    prefix varchar(5) not null,
                    autorole text
                )""";

        Statement stmt = null;
        try {
            stmt = Objects.requireNonNull(ConnectionFactory.conexao()).createStatement();
            stmt.execute(sql);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (ConnectionFactory.conexao() != null) {
                Objects.requireNonNull(ConnectionFactory.conexao()).close();
            }
        }
    }

    /**
     * Seleciona e retorna os dados de uma guilda específica com base no guild_id.
     *
     * @param guildId O ID da guilda a ser selecionada.
     * @throws SQLException Se ocorrer um erro durante a consulta.
     */
    public static void select(String guildId) throws SQLException {
        String sql = """
                select * from tb_guild where guild_id = ?
                """;

        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            stmt = Objects.requireNonNull(ConnectionFactory.conexao()).prepareStatement(sql);
            stmt.setString(1, guildId);

            result = stmt.executeQuery();
            while (result.next()) {
                DevBot.prefixMap.put(guildId, result.getString("prefix"));
                DevBot.autoroleMap.put(guildId, result.getString("autorole"));
            }
        } finally {
            if (result != null) {
                result.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (ConnectionFactory.conexao() != null) {
                Objects.requireNonNull(ConnectionFactory.conexao()).close();
            }
        }
    }

    /**
     * Insere uma nova guilda no banco de dados ou ignora se já existir.
     *
     * @param guildId O ID da guilda a ser inserida.
     * @param prefix  O prefixo da guilda.
     * @throws SQLException Se ocorrer um erro durante a inserção.
     */
    public static void insert(String guildId, String prefix) throws SQLException {
        String sql = """
                INSERT OR IGNORE INTO tb_guild (guild_id, prefix) VALUES (
                ?, ?
                )""";

        PreparedStatement stmt = null;
        try {
            stmt = Objects.requireNonNull(ConnectionFactory.conexao()).prepareStatement(sql);
            stmt.setString(1, guildId);
            stmt.setString(2, prefix);
            stmt.execute();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (ConnectionFactory.conexao() != null) {
                Objects.requireNonNull(ConnectionFactory.conexao()).close();
            }
        }
    }

    /**
     * Atualiza uma coluna específica na tabela tb_guild do banco de dados.
     *
     * @param column   Nome da coluna a ser atualizada.
     * @param guild_id ID da guilda a ser atualizada.
     * @param value    Novo valor para a coluna.
     * @throws SQLException Se ocorrer um erro durante a operação de atualização.
     */
    public static void update(String column, String value, String guild_id) throws SQLException {
        String sql = """
                UPDATE tb_guild SET %s = ? WHERE guild_id = ?
                """.formatted(column);

        PreparedStatement stmt = null;
        try {
            stmt = Objects.requireNonNull(ConnectionFactory.conexao()).prepareStatement(sql);
            stmt.setString(1, value);
            stmt.setString(2, guild_id);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (ConnectionFactory.conexao() != null) {
                Objects.requireNonNull(ConnectionFactory.conexao()).close();
            }
        }
    }
}
