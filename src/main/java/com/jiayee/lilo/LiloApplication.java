package com.jiayee.lilo;

import com.jiayee.lilo.db.ElasticsearchConnector;
import com.jiayee.lilo.models.Employer;
import com.jiayee.lilo.models.Job;
import com.jiayee.lilo.repositories.EmployerRepository;
import com.jiayee.lilo.repositories.JobRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LiloApplication {
	public static void main(String[] args) {
		SpringApplication.run(LiloApplication.class, args)
				.close();
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext context) {
		return args -> {
			final JobRepository jobRepository = context.getBean(JobRepository.class);
			final List<Job> jobs = jobRepository.getJobs();
			jobs.forEach(job -> System.out.println(job.getLocation()));
			final EmployerRepository employerRepository = context.getBean(EmployerRepository.class);
			final List<Employer> employers = employerRepository.getEmployers();
			employers.forEach(employer -> System.out.println(employer.getIndustry()));
			final ElasticsearchConnector es = context.getBean(ElasticsearchConnector.class);
			// System.out.println(es.bulkInsert(jobs));
			// System.out.println(es.bulkInsert(employers));
		};
	}
}
