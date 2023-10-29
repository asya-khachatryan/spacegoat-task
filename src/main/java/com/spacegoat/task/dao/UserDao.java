package com.spacegoat.task.dao;

import com.spacegoat.task.domain.Transaction;
import com.spacegoat.task.exception.BalanceOperationException;
import com.spacegoat.task.exception.InsufficientFundsException;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDao {
    private final DataSource dataSource;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void transfer(long senderId, long receiverId, double amount) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            String getSenderBalanceQuery = "SELECT balance FROM users WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(getSenderBalanceQuery);
            preparedStatement.setLong(1, senderId);
            //check this, big decimal
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            double senderBalance = resultSet.getDouble("balance");

            if (senderBalance < amount) {
                //custom exception
                //could be checked exception
                throw new InsufficientFundsException("Not enough funds");
            }

            //check parametrized query
            //sql sanitation
            String updateSenderBalanceQuery = "UPDATE users SET balance = ? WHERE id = ?";
            PreparedStatement preparedStatement2 = connection.prepareStatement(updateSenderBalanceQuery);
            preparedStatement2.setDouble(1, senderBalance - amount);
            preparedStatement2.setLong(2, senderId);
            preparedStatement2.executeUpdate();

            String updateReceiverBalanceQuery = "UPDATE users SET balance = balance + ? WHERE id = ?";
            PreparedStatement preparedStatement3 = connection.prepareStatement(updateReceiverBalanceQuery);
            preparedStatement3.setDouble(1, amount);
            preparedStatement3.setLong(2, receiverId);
            preparedStatement3.executeUpdate();

            String createTransactionQuery = "INSERT INTO transactions VALUES(default, ?, ?, ?, CURRENT_TIMESTAMP)";
            PreparedStatement preparedStatement4 = connection.prepareStatement(createTransactionQuery);
            preparedStatement4.setLong(1, senderId);
            preparedStatement4.setLong(2, receiverId);
            preparedStatement4.setDouble(3, amount);
            preparedStatement4.execute();

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getBalance(long userId) {
        try (Connection connection = dataSource.getConnection()) {

            String getBalanceQuery = "SELECT balance FROM users WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(getBalanceQuery);
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            resultSet.getDouble("balance");
            return resultSet.getDouble("balance");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BalanceOperationException("Failed to retrieve balance for user with id", userId);
        }
    }

    public void addFunds(long userId, double amount) {
        try (Connection connection = dataSource.getConnection()) {
            //big decimal or long
            String updateReceiverBalanceQuery = "UPDATE users SET balance = balance + ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateReceiverBalanceQuery);
            preparedStatement.setDouble(1, amount);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BalanceOperationException("Failed to add funds for user with id ", userId);
        }
    }

    public List<Transaction> getAllTransactionsForUserId(long userId) {
        try (Connection connection = dataSource.getConnection()) {
            String getUserTransactionsQuery = "SELECT * FROM transactions WHERE sender_user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(getUserTransactionsQuery);
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Transaction> transactionList = new ArrayList<>();

            while (resultSet.next()){
                Transaction transaction = new Transaction();
                transaction.setId(resultSet.getLong("id"));
                transaction.setAmount(resultSet.getDouble("amount"));
                transaction.setReceiverUserId(resultSet.getLong("sender_user_id"));
                transaction.setSenderUserId(resultSet.getLong("receiver_user_id"));
                transaction.setTimestamp(resultSet.getTimestamp("timestamp"));
                transactionList.add(transaction);
            }
            return transactionList;
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new RuntimeException();
        }
    }


}
