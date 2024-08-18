package com.dws.challenge.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import com.dws.challenge.domain.Account;
import com.dws.challenge.service.EmailNotificationService;

@ExtendWith(MockitoExtension.class)
public class EmailHelperTest {

	@Mock
	private MessageSource messageSource;

	@Mock
	private EmailNotificationService emailNotificationService;

	@InjectMocks
	private EmailHelper emailHelper;

	private Account fromAccount;
	private Account toAccount;
	private BigDecimal amount;

	@BeforeEach
	public void setup() {
		fromAccount = new Account("12345");
		toAccount = new Account("67890");
		amount = new BigDecimal("100.00");

	}

	@Test
	public void testSendSuccessEmailAsync() throws Exception {
		String fromMessage = "You have sent 100.00 to account 67890";
		String toMessage = "You have received 100.00 from account 12345";

		// Mock the messageSource to return predefined messages
		when(messageSource.getMessage(eq(CommonConstants.FROM_EMAIL_MESSAGE_KEY), any(), any(Locale.class)))
				.thenReturn(fromMessage);
		when(messageSource.getMessage(eq(CommonConstants.TO_EMAIL_MESSAGE_KEY), any(), any(Locale.class)))
				.thenReturn(toMessage);

		// Mock the emailNotificationService to do nothing when notified
		doNothing().when(emailNotificationService).notifyAboutTransfer(any(Account.class), any(String.class));

		// Call the async method
		emailHelper.sendSuccessEmailAsync(fromAccount, toAccount, amount);

		// Verify that the emailNotificationService was called with the correct
		// parameters
		verify(emailNotificationService).notifyAboutTransfer(fromAccount, fromMessage);
		verify(emailNotificationService).notifyAboutTransfer(toAccount, toMessage);
	}

	@Test
	public void testGetMessage() {
		String expectedMessage = "You have sent 100.00 to account 67890";

		// Mock the messageSource to return the expected message
		when(messageSource.getMessage(eq(CommonConstants.FROM_EMAIL_MESSAGE_KEY), any(), any(Locale.class)))
				.thenReturn(expectedMessage);

		// Call the getMessage method
		String actualMessage = emailHelper.getMessage(CommonConstants.FROM_EMAIL_MESSAGE_KEY, "100.00", "67890");

		// Verify the result
		assertEquals(expectedMessage, actualMessage);
	}
}
