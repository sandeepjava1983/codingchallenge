package com.dws.challenge.utils;

import org.springframework.stereotype.Component;

@Component
public class CommonConstants {

	public static final String ERROR_POSITIVE_AMOUNT = "Amount must be positive";
	public static final String FROM_EMAIL_MESSAGE_KEY = "email.transfer.from.success.message";
	public static final String TO_EMAIL_MESSAGE_KEY = "email.transfer.to.success.message";

	public static final String ERROR_TRANSFER_AMOUNT_GREATER_KEY = "error.transfer.amount.greater";
	public static final String ERROR_ACCOUNT_NOT_FOUND_KEY = "error.account.not.found";
	public static final String ERROR_WITHDRAWL_AMOUNT_GREATER_KEY = "error.withdrawl.amount.greater";
	public static final String ERROR_INSUFFICIENT_FUNDS_KEY = "error.insufficient.funds.message";
	public static final String ERROR_TRANSFER_ACCOUNTS_SAME_KEY = "error.transfer.accounts.same";

}
