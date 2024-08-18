package com.dws.challenge.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.utils.CommonConstants;
import com.dws.challenge.utils.EmailHelper;

/*
 * For Any Transfer amount or withdraw of bank account this class
 * will be used. 
 *   
 */
@Service
public class TransferServiceImpl implements TransferService {

	@Autowired
	private EmailHelper emailHelper;

	@Override
	public void deposit(BigDecimal amount, Account toAccount, Account firstLock, Account secondLock) {
		// check if amount is less than or equal to 0
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException(
					emailHelper.getMessage(CommonConstants.ERROR_TRANSFER_AMOUNT_GREATER_KEY));
		}
		synchronized (firstLock) {
			synchronized (secondLock) {

				if (amount.compareTo(BigDecimal.ZERO) > 0) {
					toAccount.setBalance(toAccount.getBalance().add(amount));
				}
			}
		}
	}

	@Override
	public boolean withdraw(BigDecimal amount, Account fromAccount, Account firstLock, Account secondLock) {
		// check if amount is less than or equal to 0
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException(
					emailHelper.getMessage(CommonConstants.ERROR_WITHDRAWL_AMOUNT_GREATER_KEY));
		}

		synchronized (firstLock) {
			synchronized (secondLock) {
				// check if withdrawl amount is greater than available funds
				if (fromAccount.getBalance().compareTo(amount) < 0) {
					throw new IllegalArgumentException(emailHelper
							.getMessage(CommonConstants.ERROR_INSUFFICIENT_FUNDS_KEY, fromAccount.getAccountId()));
				}
				fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
			}
		}
		return true;
	}
}
