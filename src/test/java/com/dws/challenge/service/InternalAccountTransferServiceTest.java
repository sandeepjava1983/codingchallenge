package com.dws.challenge.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.dws.challenge.domain.Account;
import com.dws.challenge.repository.AccountsRepository;
import com.dws.challenge.utils.CommonConstants;
import com.dws.challenge.utils.EmailHelper;

public class InternalAccountTransferServiceTest {

    @Mock
    private AccountsRepository accountsRepository;

    @Mock
    private EmailHelper emailHelper;

    @InjectMocks
    private InternalAccountTransferService internalAccountTransferService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInternalAccountTransfer_InvalidAmount() {
        String accountFromId = "123";
        String accountToId = "456";
        BigDecimal invalidAmount = BigDecimal.valueOf(-100);

        when(emailHelper.getMessage(CommonConstants.ERROR_TRANSFER_AMOUNT_GREATER_KEY))
                .thenReturn("Transfer amount must be positive.");

        assertThrows(IllegalArgumentException.class, () -> {
            internalAccountTransferService.internalAccountTransfer(accountFromId, accountToId, invalidAmount);
        });

        verify(emailHelper).getMessage(CommonConstants.ERROR_TRANSFER_AMOUNT_GREATER_KEY);
    }

    @Test
    public void testInternalAccountTransfer_SameAccountTransfer() {
        String accountFromId = "123";
        String accountToId = "123";
        BigDecimal amount = BigDecimal.valueOf(100);

        when(emailHelper.getMessage(CommonConstants.ERROR_TRANSFER_ACCOUNTS_SAME_KEY))
                .thenReturn("Cannot transfer to the same account.");

        assertThrows(IllegalArgumentException.class, () -> {
            internalAccountTransferService.internalAccountTransfer(accountFromId, accountToId, amount);
        });

        verify(emailHelper).getMessage(CommonConstants.ERROR_TRANSFER_ACCOUNTS_SAME_KEY);
    }

    @Test
    public void testInternalAccountTransfer_AccountNotFound() {
        String accountFromId = "123";
        String accountToId = "456";
        BigDecimal amount = BigDecimal.valueOf(100);

        when(accountsRepository.getAccount(accountFromId)).thenReturn(null);

        when(emailHelper.getMessage(CommonConstants.ERROR_ACCOUNT_NOT_FOUND_KEY))
                .thenReturn("Account not found.");

        assertThrows(IllegalArgumentException.class, () -> {
            internalAccountTransferService.internalAccountTransfer(accountFromId, accountToId, amount);
        });

        verify(accountsRepository).getAccount(accountFromId);
        verify(emailHelper).getMessage(CommonConstants.ERROR_ACCOUNT_NOT_FOUND_KEY);
    }

    @Test
    public void testInternalAccountTransfer_SuccessfulTransfer() {
        String accountFromId = "123";
        String accountToId = "456";
        BigDecimal amount = BigDecimal.valueOf(100);

        Account fromAccount = new Account(accountFromId, BigDecimal.valueOf(500));
        Account toAccount = new Account(accountToId, BigDecimal.valueOf(300));

        when(accountsRepository.getAccount(accountFromId)).thenReturn(fromAccount);
        when(accountsRepository.getAccount(accountToId)).thenReturn(toAccount);

        internalAccountTransferService.internalAccountTransfer(accountFromId, accountToId, amount);

        verify(accountsRepository, times(2)).getAccount(anyString());
        verify(emailHelper).sendSuccessEmailAsync(fromAccount, toAccount, amount);
    }
}
