package com.example.transactionstries.dao.repos.JDBC;

import com.example.transactionstries.dao.entities.SomeEntity;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class SomeRepository {

    private static final String URL = "jdbc:mysql://localhost:3306/transactions_tries?useSSL=false&requireSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public void dirtyRead() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);

            statement.execute("update some_entity set name = 'Uncommitted Name' where id=1");

            /**Reading uncommitted changes*/
            Thread thread = new Thread(this::readUncommitted);
            thread.start();
            thread.join();

            /**Rolling back Transaction to make dirty read*/
            connection.rollback();

            readFromDb();
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void repeatableRead() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            /**Change to TRANSACTION_REPEATABLE_READ - so even if there's update in the middle of transaction
             * both outputs will be the same*/
            connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);//Default

            /**First read*/
            ResultSet resultSet1 = statement.executeQuery("select name from some_entity where id=1");
            resultSet1.next();
            System.err.println("First read - " + resultSet1.getString(1));

            /**Update*/
            Thread thread = new Thread(this::update);
            thread.start();
            thread.join();

            /**Second read*/
            ResultSet resultSet2 = statement.executeQuery("select name from some_entity where id=1");
            resultSet2.next();
            System.err.println("Second read - " + resultSet2.getString(1));

            readFromDb();
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void serializable() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            /**Change to TRANSACTION_SERIALIZABLE - so even if there's insert in the middle of transaction
             * both outputs will be the same
             * change it to TRANSACTION_READ_COMMITTED, for example, to see that Second read count is 2*/
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            /**First read count*/
            ResultSet resultSet1 = statement.executeQuery("select count(*) from some_entity");
            resultSet1.next();
            System.err.println("First read count - " + resultSet1.getString(1));

            /**Update*/
            Thread thread = new Thread(this::create);
            thread.start();
            Thread.sleep(1000);/**Can't use thread.join()
             * cause in TRANSACTION_SERIALIZABLE - transactions are executed one by one
             * with thread.join() will be Exception*/

            /**Second read*/
            ResultSet resultSet2 = statement.executeQuery("select count(*) from some_entity");
            resultSet2.next();
            System.err.println("Second read count - " + resultSet2.getString(1));

            connection.commit();

            readFromDbCount();
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void readFromDb() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet2 = statement.executeQuery("select name from some_entity where id=1");
            resultSet2.next();
            System.err.println("in DB - " + resultSet2.getString(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void readFromDbCount() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet2 = statement.executeQuery("select count(*) from some_entity");
            resultSet2.next();
            System.err.println("in DB count - " + resultSet2.getString(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void update() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("update some_entity set name = 'New Name' where id=1");
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void create() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("insert into some_entity (name) values ('BBB')");
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void readUncommitted() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            /**Change to READ_COMMITTED to get rid of dirty read*/
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            ResultSet resultSet = statement.executeQuery("select name from some_entity where id=1");
            resultSet.next();
            System.err.println("Read - " + resultSet.getString(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveInit() {
        new SomeEntity(1L, "AAA");
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            statement.execute("insert into some_entity (name) values ('AAA')");
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private Connection getConnection() {
        Connection connection;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
