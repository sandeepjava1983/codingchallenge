package com.dws.challenge.web;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dws.challenge.service.InternalAccountTransfer;

import lombok.extern.slf4j.Slf4j;

/*
 * This class is used  Transfer of amount within bank Accounts only
 */
@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class InternalAccountTransferController {

	@Autowired
	private InternalAccountTransfer internalAccountTransferService;

	@Autowired
	public InternalAccountTransferController(InternalAccountTransfer internalAccountTransferService) {
		this.internalAccountTransferService = internalAccountTransferService;
	}

	/*
	 * This Api will be used for Transfer of amount between two Accounts
	 */
	@PostMapping(value = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> transfer(@RequestParam String accountFromId, @RequestParam String accountToId,
			@RequestParam BigDecimal amount) {

		try {
			internalAccountTransferService.internalAccountTransfer(accountFromId, accountToId, amount);
		} catch (IllegalArgumentException illegalArgException) {
			return new ResponseEntity<>(illegalArgException.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("Transfer successful", HttpStatus.OK);
	}
}
