package org.ashok.notificationservice;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class) 
public class NotificationFunctionsStreamIntegrationTests {

	@Autowired
	private InputDestination input; //represents input bindling - smsemail-in-0
	
	@Autowired
	private OutputDestination otput; //represents output bindling - smsemail-out-0
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Test
	void whenPaymentAcceptedThenUserNotified() throws IOException {
		
		PaymentMessage pm = new PaymentMessage(1L, 200L, 600);
		PaymentNotifiedMessage expectedOutput = new PaymentNotifiedMessage(pm.paymentId()) ;
		
		Message<PaymentMessage> inputMessage = MessageBuilder.withPayload(pm).build();
		
		System.out.println("inputMessage:" +inputMessage);
		
		this.input.send(inputMessage);
		assertThat(objectMapper.readValue(otput.receive().getPayload(), PaymentNotifiedMessage.class))
			.isEqualTo(expectedOutput);
		
	}
}
