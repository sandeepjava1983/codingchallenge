package com.dws.challenge.service;

import java.math.BigDecimal;

public interface InternalAccountTransfer {
	public void internalAccountTransfer(String accountFromId, String accountToId, BigDecimal amount);
}
