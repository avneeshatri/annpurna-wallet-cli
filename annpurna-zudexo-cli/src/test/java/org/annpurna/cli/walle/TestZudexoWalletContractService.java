package org.annpurna.cli.walle;

import org.annpurna.cli.identity.AddToIdentityWallet;
import org.annpurna.cli.wallet.ZudexoWalletContractService;
import org.junit.Before;
import org.junit.Test;

public class TestZudexoWalletContractService {
	private AddToIdentityWallet walletService ;
	private ZudexoWalletContractService contractService ;
	
	@Before
	public void setUp() {
		this.contractService = new ZudexoWalletContractService();
		this.walletService = new AddToIdentityWallet();
	}

	@Test
	public void test() {
		this.walletService.main(null);
		this.contractService.main(null);
	}
}
