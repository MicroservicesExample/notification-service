package org.ashok.notificationservice;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Mono;

/**
 * Spring Cloud Function supports both imperative and reactive code, so you’re
free to implement functions using reactive APIs like Mono and Flux. You
can also mix and match.

Once you define the functions, the framework can
expose them in different ways depending on your needs. For example,
Spring Cloud Function can automatically expose the functions defined in
spring.cloud.function.definition as REST endpoints.
Or you can use one of the adapters provided by the framework
to package the application and deploy it on AWS Lambda, Azure Functions,
or Google Cloud Functions. Or you can combine it with Spring Cloud
Stream and bind the function to message channels in an event broker like
RabbitMQ or Kafka.
 * @return
 */

@Configuration
public class NotificationFunctions {

	private static final Logger logger = LoggerFactory.getLogger(NotificationFunctions.class);
	
	@Bean
	public Function<PaymentMessage, PaymentMessage> sms() {
		
		return paymentMessage -> {
			logger.info("SMS: Your invoice with id {} and amount: {} is paid. Payment refernece number: {}", 
					paymentMessage.billRefNumber(), paymentMessage.amount(), paymentMessage.paymentId());
			return paymentMessage;
		};
	}
	
	@Bean
	public Function<Mono<PaymentMessage>, Mono<PaymentNotifiedMessage>> email() {
		return paymentMessage -> paymentMessage.map(message -> {
			logger.info("EMAIL: Your invoice with id {} and amount: {} is paid. Payment refernece number: {}", 
					message.billRefNumber(), message.amount(), message.paymentId());
			return new PaymentNotifiedMessage(message.paymentId());
		});
	}
	
}
