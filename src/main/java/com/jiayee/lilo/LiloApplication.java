package com.jiayee.lilo;

import com.jiayee.lilo.broker.LiloConsumer;
import com.jiayee.lilo.broker.LiloProducer;
import com.jiayee.lilo.db.ElasticsearchConnector;
import com.jiayee.lilo.models.Employer;
import com.jiayee.lilo.models.Job;
import com.jiayee.lilo.repositories.EmployerRepository;
import com.jiayee.lilo.repositories.JobRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
			Timestamp now = Timestamp.valueOf(LocalDateTime.now());

			// One-off fetching
			final JobRepository jobRepository = context.getBean(JobRepository.class);
			final List<Job> jobs = jobRepository.getJobs();
			final EmployerRepository employerRepository = context.getBean(EmployerRepository.class);
			final List<Employer> employers = employerRepository.getEmployers();

			// One-off indexing
			final ElasticsearchConnector es = context.getBean(ElasticsearchConnector.class);
			es.bulkInsert(jobs);
			es.bulkInsert(employers);

			// One-off streaming
			final LiloProducer producer = context.getBean(LiloProducer.class);
			final LiloConsumer consumer = context.getBean(LiloConsumer.class);
			producer.runKafkaProducerOnce(now);
			consumer.runKafkaConsumerOnce();

			// Stream once every 11 seconds
			now = Timestamp.valueOf(LocalDateTime.now());
			while (true) {
				producer.runKafkaProducerOnce(now);
				now = Timestamp.valueOf(LocalDateTime.now());
				consumer.runKafkaConsumerOnce();
				Thread.sleep(10000);
			}
		};
	}
}
