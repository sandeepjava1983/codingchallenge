package com.dws.challenge.service;

import java.math.BigDecimal;

import com.dws.challenge.domain.Account;

public interface TransferService {

	public void deposit(BigDecimal amount, Account toAccount, Account first, Account second);

	public boolean withdraw(BigDecimal amount, Account fromAccount, Account first, Account second);
}
