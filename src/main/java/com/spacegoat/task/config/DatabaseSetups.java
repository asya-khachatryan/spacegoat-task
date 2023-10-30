package com.spacegoat.task.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetups {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/taskdb?user=postgres";
        String user = "postgres";
        String password = "pass123";
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("drop table if exists users");
                statement.execute("drop table if exists transactions");


                statement.execute("CREATE TABLE users(ID serial PRIMARY KEY NOT NULL , " +
                        "username VARCHAR(50) NOT NULL UNIQUE, " +
                        "first_name VARCHAR(50) NOT NULL, " +
                        "last_name VARCHAR(50) NOT NULL, " +
                        "city VARCHAR(100) NOT NULL, " +
                        "balance NUMERIC(20,2) default 0)");

                statement.execute("CREATE TABLE transactions(ID serial PRIMARY KEY NOT NULL , " +
                        "sender_user_id INT NOT NULL REFERENCES users(ID), " +
                        "receiver_user_id INT NOT NULL REFERENCES users(ID), " +
                        "amount NUMERIC(25,5) NOT NULL, " +
                        "timestamp TIMESTAMP" +
                        ")");

                //add foreign key

                statement.execute("INSERT INTO users VALUES" +
                        "(default ,'asya.khachatryan', 'Asya', 'Khachatryan', 'Yerevan', 21232)");
                statement.execute("INSERT INTO users VALUES" +
                        "(default,'ted.mosby', 'Ted', 'Mosby', 'New York City', 12503.41)");
                statement.execute("INSERT INTO users VALUES" +
                        "(default,'barno', 'Barney', 'Stinson', 'New York City', 35902.22)");
                statement.execute("INSERT INTO users VALUES" +
                        "(default,'lll', 'Lily', 'Aldrin', 'New York City', 10092)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}