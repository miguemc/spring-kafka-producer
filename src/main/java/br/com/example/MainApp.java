package br.com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@EnableKafka
@EnableScheduling
@SpringBootApplication(scanBasePackages = { "br.com.example" })
public class MainApp {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(MainApp.class, args);
	}

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Value("${topic.producer.id}")
	private String producerId;

	int count = 0;

	@Scheduled(fixedRate = 100)
	public void demo() {
		count++;
		ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send("SpringKafkaTopic",
				"Producer: " + producerId + "Messsage:" + count);
		future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
			@Override
			public void onFailure(Throwable ex) {
				System.out.println("Producer: " + producerId + ". Failed to send message");
			}

			@Override
			public void onSuccess(SendResult<String, String> result) {
				System.out.println("Producer: " + producerId + ". Sent message: " + result);
			}
		});
	}

}
