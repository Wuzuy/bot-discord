package com.wuzuy.bot.database;

import com.wuzuy.bot.DevBot;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class CRUD {

    // Create
    public static void createTable() throws SQLException {
        String sql = """
                create table tb_guild
                (
                    id integer not null primary key autoincrement unique,
                    guild_id varchar(32) not null unique,
                    prefix varchar(5) not null
                )""";

        Statement stmt = Objects.requireNonNull(ConnectionFactory.conexao()).createStatement();
        stmt.execute(sql);
        stmt.close();
        Objects.requireNonNull(ConnectionFactory.conexao()).close();
    }

    // Read
    public static void select(String guildId) throws SQLException {

        String sql = """
                select * from tb_guild where guild_id = ?
                """;

        PreparedStatement stmt = Objects.requireNonNull(ConnectionFactory.conexao()).prepareStatement(sql);
        stmt.setString(1, guildId);

        ResultSet result = stmt.executeQuery();
        while (result.next()){
            DevBot.prefixMap.put(guildId, result.getString("prefix").charAt(0));
        }

        stmt.execute();
        stmt.close();
        Objects.requireNonNull(ConnectionFactory.conexao()).close();
    }

    // Update
    public static void insert(String guildId, String prefix) throws SQLException {
        String sql = """
                INSERT OR IGNORE INTO tb_guild (guild_id, prefix) VALUES (
                ?, ?
                )""";

        PreparedStatement stmt = Objects.requireNonNull(ConnectionFactory.conexao()).prepareStatement(sql);
        stmt.setString(1, guildId);
        stmt.setString(2, prefix);
        stmt.execute();
        stmt.close();
        Objects.requireNonNull(ConnectionFactory.conexao()).close();
    }

    // Delete
    public static void update(String guild_id, char newPrefix) throws SQLException {
        String sql = """
                UPDATE tb_guild set prefix = ? where guild_id = ?
                """;

        PreparedStatement stmt = Objects.requireNonNull(ConnectionFactory.conexao()).prepareStatement(sql);
        stmt.setString(1, String.valueOf(newPrefix));
        stmt.setString(2, guild_id);
        stmt.execute();
        stmt.close();
        Objects.requireNonNull(ConnectionFactory.conexao()).close();

    }
}