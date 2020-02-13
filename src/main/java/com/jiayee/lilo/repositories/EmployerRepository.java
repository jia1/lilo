package com.jiayee.lilo.repositories;

import com.jiayee.lilo.db.MySQLConnector;
import com.jiayee.lilo.models.Employer;
import com.jiayee.lilo.models.ImmutableEmployer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmployerRepository {
  private final MySQLConnector mySQLConnector;

  private final String query;

  public EmployerRepository(
      final MySQLConnector mySQLConnector,
      @Value("${mysql.query}") final String query
  ) {
    this.mySQLConnector = mySQLConnector;
    this.query = query;
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
}
