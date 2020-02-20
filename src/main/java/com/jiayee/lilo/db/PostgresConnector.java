package com.jiayee.lilo.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PostgresConnector {
  private static final Logger LOG = LoggerFactory.getLogger(
      PostgresConnector.class.getSimpleName());

  private final String URL;

  private final String username;

  private final String password;

  private final String query;

  private final String queryForUpdatedAt;

  public PostgresConnector(
      @Value("${postgres.URL}") final String URL,
      @Value("${postgres.username}") final String username,
      @Value("${postgres.password}") final String password,
      @Value("${postgres.query}") final String query,
      @Value("${postgres.query.updated_at}") final String queryForUpdatedAt
  ) {
    this.URL = URL;
    this.username = username;
    this.password = password;
    this.query = query;
    this.queryForUpdatedAt = queryForUpdatedAt;
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

  // https://stackoverflow.com/questions/18614836/using-setdate-in-preparedstatement
  public Optional<ResultSet> getUpdatedRecords(final Timestamp timestamp) {
    // Query timestamp is fixed for convenience purposes. This value should be configurable in
    // production.
    try (final Connection connection = DriverManager.getConnection(URL, username, password)) {
      final PreparedStatement preparedStatement = connection.prepareStatement(queryForUpdatedAt);
      preparedStatement.setTimestamp(
          1,
          // Timestamp.valueOf(LocalDate.of(2020, 2, 20).atStartOfDay())
          timestamp
      );
      LOG.info(preparedStatement.toString());
      return Optional.of(preparedStatement.executeQuery());
    } catch (final SQLException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }
}
