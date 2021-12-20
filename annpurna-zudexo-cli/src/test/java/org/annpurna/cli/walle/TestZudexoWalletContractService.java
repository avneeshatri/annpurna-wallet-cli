package org.annpurna.cli.walle;

import org.annpurna.cli.wallet.ZudexoWalletContractService;
import org.junit.Before;
import org.junit.Test;

public class TestZudexoWalletContractService {
	private ZudexoWalletContractService contractService ;
	
	@Before
	public void setUp() {
		this.contractService = new ZudexoWalletContractService();
	}

	@Test
	public void test() {
		this.contractService.main(null);
	}
}
