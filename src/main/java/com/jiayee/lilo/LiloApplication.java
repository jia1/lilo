package com.jiayee.lilo;

import com.jiayee.lilo.broker.LiloConsumer;
import com.jiayee.lilo.broker.LiloProducer;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
			// One-off fetching
			/*
			final JobRepository jobRepository = context.getBean(JobRepository.class);
			final List<Job> jobs = jobRepository.getJobs();
			jobs.forEach(job -> System.out.println(job.getLocation()));
			final EmployerRepository employerRepository = context.getBean(EmployerRepository.class);
			final List<Employer> employers = employerRepository.getEmployers();
			employers.forEach(employer -> System.out.println(employer.getIndustry()));
			*/

			// One-off indexing
			// final ElasticsearchConnector es = context.getBean(ElasticsearchConnector.class);
			// System.out.println(es.bulkInsert(jobs));
			// System.out.println(es.bulkInsert(employers));

			// One-off streaming
			// final LiloProducer producer = context.getBean(LiloProducer.class);
			// final LiloConsumer consumer = context.getBean(LiloConsumer.class);
			// producer.runKafkaProducerOnce();
			// consumer.runKafkaConsumerOnce();

			final LiloProducer producer = context.getBean(LiloProducer.class);
			final LiloConsumer consumer = context.getBean(LiloConsumer.class);
			Timestamp now = Timestamp.valueOf(LocalDateTime.now());
			while (true) {
				producer.runKafkaProducerOnce(now);
				now = Timestamp.valueOf(LocalDateTime.now());
				consumer.runKafkaConsumerOnce();
				Thread.sleep(10000);
			}
		};
	}
}
