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
public class PostgresConnector {
  private String URL;

  private String username;

  private String password;

  private String query;

  public PostgresConnector(
      @Value("${postgres.URL}") final String URL,
      @Value("${postgres.username}") final String username,
      @Value("${postgres.password}") final String password,
      @Value("${postgres.query}") final String query
  ) {
    this.URL = URL;
    this.username = username;
    this.password = password;
    this.query = query;
  }

  public Optional<ResultSet> getRecords() {
    try (final Connection connection = DriverManager.getConnection(URL, username, password)) {
      final Statement statement = connection.createStatement();
      return Optional.of(statement.executeQuery(query));
    } catch (final SQLException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  public Optional<ResultSet> getUpdatedRecords() {
    try (final Connection connection = DriverManager.getConnection(URL, username, password)) {
      final Statement statement = connection.createStatement();
      return Optional.of(statement.executeQuery(query));
    } catch (final SQLException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }
}
