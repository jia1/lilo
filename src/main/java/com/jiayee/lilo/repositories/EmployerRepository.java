package com.jiayee.lilo.repositories;

import com.jiayee.lilo.db.MySQLConnector;
import com.jiayee.lilo.models.Employer;
import com.jiayee.lilo.models.ImmutableEmployer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmployerRepository {
  private final MySQLConnector mySQLConnector;

  private final String query;

  private final String queryForUpdatedAt;

  public EmployerRepository(
      final MySQLConnector mySQLConnector,
      @Value("${mysql.query}") final String query,
      @Value("${mysql.query.updated_at}") final String queryForUpdatedAt
  ) {
    this.mySQLConnector = mySQLConnector;
    this.query = query;
    this.queryForUpdatedAt = queryForUpdatedAt;
  }

  public List<Employer> getEmployers() throws SQLException {
    final List<Employer> employers = new ArrayList<>();
    /*
    final Optional<ResultSet> resultSetOptional = mySQLConnector.getRecords();
    if (resultSetOptional.isPresent()) {
      final ResultSet resultSet = resultSetOptional.get();
      while (resultSet.next()) {
        employers.add(ImmutableEmployer.builder()
            .employerID(resultSet.getInt("employer_id"))
            .industry(resultSet.getString("industry"))
            .build());
      }
    }
    return employers;
    */
    try (final Connection connection = mySQLConnector.getConnection()) {
      final Statement statement = connection.createStatement();
      final ResultSet resultSet = statement.executeQuery(query);
      while (resultSet.next()) {
        employers.add(ImmutableEmployer.builder()
            .employerID(resultSet.getInt("employer_id"))
            .industry(resultSet.getString("industry"))
            .build());
      }
      return employers;
    } catch (final SQLException e) {
      e.printStackTrace();
      return employers;
    }
  }

  // https://stackoverflow.com/questions/18614836/using-setdate-in-preparedstatement
  public List<Employer> getUpdatedEmployers() throws SQLException {
    // Query timestamp is fixed for convenience purposes. This value should be configurable in
    // production.
    final List<Employer> employers = new ArrayList<>();
    try (final Connection connection = mySQLConnector.getConnection()) {
      final PreparedStatement preparedStatement = connection.prepareStatement(queryForUpdatedAt);
      preparedStatement.setTimestamp(
          1,
          Timestamp.valueOf(LocalDateTime.of(2020, 2, 19, 15, 0))
      );
      final ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        employers.add(ImmutableEmployer.builder()
            .employerID(resultSet.getInt("employer_id"))
            .industry(resultSet.getString("industry"))
            .build());
      }
      return employers;
    } catch (final SQLException e) {
      e.printStackTrace();
      return employers;
    }
  }
}
