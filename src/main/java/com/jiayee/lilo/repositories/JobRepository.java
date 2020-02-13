package com.jiayee.lilo.repositories;

import com.jiayee.lilo.db.PostgresConnector;
import com.jiayee.lilo.models.ImmutableJob;
import com.jiayee.lilo.models.Job;
import com.jiayee.lilo.models.JobStatus;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class JobRepository {
  private final PostgresConnector postgresConnector;

  public JobRepository(final PostgresConnector postgresConnector) {
    this.postgresConnector = postgresConnector;
  }

  public List<Job> getJobs() throws SQLException {
    final List<Job> jobs = new ArrayList<>();
    final Optional<ResultSet> resultSetOptional = postgresConnector.getRecords();
    if (resultSetOptional.isPresent()) {
      final ResultSet resultSet = resultSetOptional.get();
      while (resultSet.next()) {
        jobs.add(ImmutableJob.builder()
            .jobID(resultSet.getInt("job_id"))
            .location(resultSet.getString("location"))
            .status(JobStatus.valueOf(resultSet.getString("status")))
            .employerID(resultSet.getInt("employer_id"))
            .build());
      }
    }
    return jobs;
  }
}
