package com.dws.challenge.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.repository.AccountsRepository;
import com.dws.challenge.utils.CommonConstants;
import com.dws.challenge.utils.EmailHelper;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InternalAccountTransferService extends TransferServiceImpl implements InternalAccountTransfer {

	@Getter
	private EmailHelper emailHelper;

	@Getter
	private final AccountsRepository accountsRepository;

	@Autowired
	public InternalAccountTransferService(AccountsRepository accountsRepository, EmailHelper emailHelper) {

		this.accountsRepository = accountsRepository;
		this.emailHelper = emailHelper;

	}

	/*
	 * This method is to transfer POSTIVE (+ve) amount from one one existing account
	 * to another existing account. Once transfer is successful, it will send email
	 * Asynchronously.
	 */
	public void internalAccountTransfer(String accountFromId, String accountToId, BigDecimal amount)
			throws IllegalArgumentException {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException(
					emailHelper.getMessage(CommonConstants.ERROR_TRANSFER_AMOUNT_GREATER_KEY));
		}

		if (accountFromId.equalsIgnoreCase(accountToId)) {
			throw new IllegalArgumentException(
					emailHelper.getMessage(CommonConstants.ERROR_TRANSFER_ACCOUNTS_SAME_KEY));
		}

		Account fromAccount = accountsRepository.getAccount(accountFromId);
		Account toAccount = accountsRepository.getAccount(accountToId);

		if (fromAccount == null || toAccount == null) {
			throw new IllegalArgumentException(emailHelper.getMessage(CommonConstants.ERROR_ACCOUNT_NOT_FOUND_KEY));
		}

		// Ensure that the account objects are locked in a consistent order
		Account firstLock = fromAccount.hashCode() < toAccount.hashCode() ? fromAccount : toAccount;
		Account secondLock = fromAccount.hashCode() < toAccount.hashCode() ? toAccount : fromAccount;
		synchronized (firstLock) {
			synchronized (secondLock) {
				// if withdraw is successful then only deposit amount to another account.
				if (withdraw(amount, fromAccount, firstLock, secondLock)) {
					log.info("Withdraw is successful for " + amount + " amount from account "
							+ fromAccount.getAccountId());
					deposit(amount, toAccount, firstLock, secondLock);
					log.info("Transaction is successful");
					// send Email asynchronously(if required this can be made synchronous call)
					emailHelper.sendSuccessEmailAsync(fromAccount, toAccount, amount);
				}
			}
		}
	}

}
