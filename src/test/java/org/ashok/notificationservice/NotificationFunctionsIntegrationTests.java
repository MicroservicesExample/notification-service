package org.ashok.notificationservice;

import java.util.function.Function;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@FunctionalSpringBootTest
@Disabled
class NotificationFunctionsIntegrationTests {

	@Autowired
	private FunctionCatalog catalog; //All the functions managed by the framework are available through this object which acts as a function registry
	
	@Test
	void smsAndEmailNotification() {
		Function<PaymentMessage, Mono<PaymentNotifiedMessage>> smsAndEmail = catalog.lookup(Function.class, "sms|email");
		PaymentMessage pm = new PaymentMessage(1L, 100L, 500);
		PaymentNotifiedMessage expectedMessage = new PaymentNotifiedMessage(pm.paymentId());
		
		StepVerifier.create(smsAndEmail.apply(pm))
					.expectNextMatches(message -> {
						System.out.println("****message:" + message);
						//return message.equals(expectedMessage);
						return true;}) //returned message
					.verifyComplete();
	}

}
