package com.dws.challenge.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.dws.challenge.domain.Account;
import com.dws.challenge.service.AccountsService;
import com.dws.challenge.service.InternalAccountTransfer;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
public class InternalAccountTransferControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private AccountsService accountsService;
	
	@Autowired
	InternalAccountTransfer internalAccountTransferService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@BeforeEach
	void prepareMockMvc() {
		this.mockMvc = webAppContextSetup(this.webApplicationContext).build();

		Account fromAccount = new Account("123", new BigDecimal("1000"));
		Account toAccount = new Account("456", new BigDecimal("500"));
		// Reset the existing accounts before each test.
		accountsService.getAccountsRepository().clearAccounts();
		accountsService.getAccountsRepository().createAccount(fromAccount);
		accountsService.getAccountsRepository().createAccount(toAccount);
	}

	@Test
	public void testTransferSuccess() throws Exception {
		String accountFromId = "123";
		String accountToId = "456";
		BigDecimal amount = new BigDecimal("100.00");

		mockMvc.perform(
				post("/v1/accounts/transfer").param("accountFromId", accountFromId).param("accountToId", accountToId)
						.param("amount", amount.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string("Transfer successful"));
	}

	@Test
	public void testTransferBadRequest() throws Exception {
		String accountFromId = "123";
		String accountToId = "456";
		BigDecimal amount = new BigDecimal("-100.00");
		

		mockMvc.perform(
				post("/v1/accounts/transfer").param("accountFromId", accountFromId).param("accountToId", accountToId)
						.param("amount", amount.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(content().string("Transfer Amount should be greater than 0"));
	}

}
