package com.dws.challenge.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dws.challenge.domain.Account;
import com.dws.challenge.utils.CommonConstants;
import com.dws.challenge.utils.EmailHelper;

@ExtendWith(MockitoExtension.class)
public class TransferServiceImplTest {

	@Mock
	private EmailHelper emailHelper;

	@InjectMocks
	private TransferServiceImpl transferService;

	private Account account1;
	private Account account2;

	@BeforeEach
	public void setUp() {
		account1 = new Account("12345", new BigDecimal("500.00"));
		account2 = new Account("67890", new BigDecimal("300.00"));
	}

	@Test
	public void testDepositSuccess() {
		BigDecimal depositAmount = new BigDecimal("100.00");

		transferService.deposit(depositAmount, account2, account1, account2);

		// Check that the balance was updated correctly
		assertTrue(account2.getBalance().compareTo(new BigDecimal("400.00")) == 0);
	}

	@Test
	public void testDepositInvalidAmount() {
		BigDecimal depositAmount = new BigDecimal("-50.00");

		when(emailHelper.getMessage(CommonConstants.ERROR_TRANSFER_AMOUNT_GREATER_KEY))
				.thenReturn("Amount must be greater than zero");

		assertThrows(IllegalArgumentException.class, () -> {
			transferService.deposit(depositAmount, account2, account1, account2);
		});

		verify(emailHelper).getMessage(CommonConstants.ERROR_TRANSFER_AMOUNT_GREATER_KEY);
	}

	@Test
	public void testWithdrawSuccess() {
		BigDecimal withdrawAmount = new BigDecimal("200.00");

		boolean result = transferService.withdraw(withdrawAmount, account1, account1, account2);

		// Check that the balance was updated correctly
		assertTrue(account1.getBalance().compareTo(new BigDecimal("300.00")) == 0);
		assertTrue(result);
	}

	@Test
	public void testWithdrawInvalidAmount() {
		BigDecimal withdrawAmount = new BigDecimal("-100.00");

		when(emailHelper.getMessage(CommonConstants.ERROR_WITHDRAWL_AMOUNT_GREATER_KEY))
				.thenReturn("Amount must be greater than zero");

		assertThrows(IllegalArgumentException.class, () -> {
			transferService.withdraw(withdrawAmount, account1, account1, account2);
		});

		verify(emailHelper).getMessage(CommonConstants.ERROR_WITHDRAWL_AMOUNT_GREATER_KEY);
	}

	@Test
	public void testWithdrawInsufficientFunds() {
		BigDecimal withdrawAmount = new BigDecimal("600.00");

		when(emailHelper.getMessage(eq(CommonConstants.ERROR_INSUFFICIENT_FUNDS_KEY), any()))
				.thenReturn("Insufficient funds");

		assertThrows(IllegalArgumentException.class, () -> {
			transferService.withdraw(withdrawAmount, account1, account1, account2);
		});

		verify(emailHelper).getMessage(eq(CommonConstants.ERROR_INSUFFICIENT_FUNDS_KEY), any());
	}
}
