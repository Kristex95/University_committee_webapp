package com.kristex.university_committee.dao.impl;

import com.kristex.university_committee.connection.ConnectionPool;
import com.kristex.university_committee.dao.UserTokenDao;
import com.kristex.university_committee.model.Role;
import com.kristex.university_committee.model.User;
import com.kristex.university_committee.model.UserToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserTokenDaoImpl implements UserTokenDao {

    private static UserTokenDaoImpl instance;

    public static synchronized UserTokenDaoImpl getInstance(){
        if(instance == null){
            instance = new UserTokenDaoImpl();
        }
        return instance;
    }

    @Override
    public UserToken getById(int id) {
        UserToken userToken = null;

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String query =  "Select * FROM token WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                Integer token_id = resultSet.getInt(1);
                Integer user_id = resultSet.getInt(2);
                String access = resultSet.getString(3);
                String refresh = resultSet.getString(4);

                userToken = new UserToken(token_id, user_id, access, refresh);
            }

            resultSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            System.out.println(e);
        }

        return userToken;
    }

    @Override
    public List<UserToken> getAll() {
        List<UserToken> userTokenList = new ArrayList<>();

        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String query = "SELECT id FROM token";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultIdSet = statement.executeQuery();

            while(resultIdSet.next()){
                int id = resultIdSet.getInt(1);
                UserToken userToken = getById(id);

                System.out.println(userToken);
                userTokenList.add(userToken);
            }
            System.out.println("<End of list>");
            resultIdSet.close();
            statement.close();
            connectionPool.releaseConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }

        return userTokenList;
    }

    @Override
    public void createToken(UserToken userToken) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){


            String query = "INSERT INTO token (user_id, access_token, refresh_token)" +
                    "VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userToken.getUserId());
            statement.setString(2, userToken.getAccess());
            statement.setString(3, userToken.getRefresh());


            if (statement.executeUpdate() <= 0){
                System.out.println("SQL cannot create User");
            }

            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void updateToken(UserToken userToken) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){

            String query =  "UPDATE token " +
                            "SET user_id = ?, access_token = ?, refresh_token = ? " +
                            "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userToken.getUserId());
            statement.setString(2, userToken.getAccess());
            statement.setString(3, userToken.getRefresh());
            statement.setInt(4, userToken.getId());

            if (statement.executeUpdate() <= 0){
                System.out.println("SQL cannot update User");
            }

            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void deleteToken(int id) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try(Connection connection = connectionPool.getConnection()){
            String query =  "DELETE FROM token " +
                            "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);


            if (statement.executeUpdate() <= 0){
                System.out.println("SQL cannot delete User");
            }


            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public UserToken getByUserId(int id) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        UserToken userToken = null;

        try(Connection connection = connectionPool.getConnection()){
            String query =  "SELECT * FROM token WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                Integer token_id = resultSet.getInt(1);
                Integer user_id = resultSet.getInt(2);
                String access = resultSet.getString(3);
                String refresh = resultSet.getString(4);

                userToken = new UserToken(token_id, user_id, access, refresh);
            }


            statement.close();
            connectionPool.releaseConnection(connection);
        }catch (Exception e){
            System.out.println(e);
        }
        return userToken;
    }
}
