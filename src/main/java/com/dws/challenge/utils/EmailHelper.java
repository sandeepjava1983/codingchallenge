package com.dws.challenge.utils;

import java.math.BigDecimal;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.service.EmailNotificationService;

import lombok.extern.slf4j.Slf4j;

/*
 * This class is used to prepare the data for EmailService
 */
@Service
@Slf4j
public class EmailHelper {

	@Autowired
	MessageSource messageSource;

	private EmailNotificationService emailNotificationService = new EmailNotificationService();

	/*
	 * This is Asynchronous method. Responsibility of this method is to prepare the
	 * description required for the email and call the method to send email for both
	 * Sender and Receiver.
	 */
	@Async
	public void sendSuccessEmailAsync(Account fromAccount, Account toAccount, BigDecimal amount) {
		String senderTransferDesciption = getMessage(CommonConstants.FROM_EMAIL_MESSAGE_KEY, amount.toString(),
				toAccount.getAccountId());
		String receiverTransferDesciption = getMessage(CommonConstants.TO_EMAIL_MESSAGE_KEY, amount.toString(),
				fromAccount.getAccountId());
		log.debug("Message prepared for sender:" + senderTransferDesciption);
		log.debug("Message prepared for receiver:" + receiverTransferDesciption);
		emailNotificationService.notifyAboutTransfer(fromAccount, senderTransferDesciption);
		emailNotificationService.notifyAboutTransfer(toAccount, receiverTransferDesciption);
	}

	public String getMessage(String message, String... strings) {		
		return messageSource.getMessage(message, strings, Locale.getDefault());
	}
}
