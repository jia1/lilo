package com.jiayee.lilo.repositories;

import com.google.common.collect.ImmutableList;
import com.jiayee.lilo.db.PostgresConnector;
import com.jiayee.lilo.models.ImmutableJob;
import com.jiayee.lilo.models.Job;
import com.jiayee.lilo.models.JobStatus;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
    final Optional<ResultSet> resultSetOptional = postgresConnector.getRecords();
    if (!resultSetOptional.isPresent()) {
      return ImmutableList.of();
    }
    return resultSetToJobs(resultSetOptional.get());
  }

  public List<Job> getUpdatedJobs(final Timestamp now) throws SQLException {
    final Optional<ResultSet> resultSetOptional = postgresConnector.getUpdatedRecords(now);
    if (!resultSetOptional.isPresent()) {
      return ImmutableList.of();
    }
    return resultSetToJobs(resultSetOptional.get());
  }

  private List<Job> resultSetToJobs(final ResultSet resultSet) throws SQLException {
    final List<Job> jobs = new ArrayList<>();
    while (resultSet.next()) {
      jobs.add(ImmutableJob.builder()
          .jobID(resultSet.getInt("job_id"))
          .location(resultSet.getString("location"))
          .status(JobStatus.valueOf(resultSet.getString("status")))
          .employerID(resultSet.getInt("employer_id"))
          .build());
    }
    return jobs;
  }
}
