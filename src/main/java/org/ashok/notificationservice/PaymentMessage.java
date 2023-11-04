package org.ashok.notificationservice;

public record PaymentMessage(Long paymentId, Long billRefNumber, Integer amount) {

}
