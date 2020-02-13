package com.jiayee.lilo;

import com.jiayee.lilo.repositories.EmployerRepository;
import com.jiayee.lilo.repositories.JobRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LiloApplication {
	public static void main(String[] args) {
		SpringApplication.run(LiloApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext context) {
		return args -> {
			final JobRepository jobRepository = context.getBean(JobRepository.class);
			jobRepository.getJobs()
					.forEach(job -> System.out.println(job.getLocation()));
			final EmployerRepository employerRepository = context.getBean(EmployerRepository.class);
			employerRepository.getEmployers()
					.forEach(employer -> System.out.println(employer.getIndustry()));
		};
	}
}
