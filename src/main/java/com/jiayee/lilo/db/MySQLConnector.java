package com.jiayee.lilo.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MySQLConnector {
  private final String URL;

  private final String username;

  private final String password;

  private final String query;

  private final String queryForUpdatedAt;

  public MySQLConnector(
      @Value("${mysql.URL}") final String URL,
      @Value("${mysql.username}") final String username,
      @Value("${mysql.password}") final String password,
      @Value("${mysql.query}") final String query,
      @Value("${mysql.query.updated_at}") final String queryForUpdatedAt
  ) {
    this.URL = URL;
    this.username = username;
    this.password = password;
    this.query = query;
    this.queryForUpdatedAt = queryForUpdatedAt;
  }

  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, username, password);
  }

  // FIXME: This does not work. Unable to debug.
  public Optional<ResultSet> getRecords() {
    try (final Connection connection = DriverManager.getConnection(URL, username, password)) {
      final Statement statement = connection.createStatement();
      return Optional.of(statement.executeQuery(query));
    } catch (final SQLException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }
}
