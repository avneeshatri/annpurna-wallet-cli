package org.annpurna.wallet.cli.api.handler.factories;

import org.annpurna.wallet.cli.api.handler.WalletApiService;
import org.annpurna.wallet.cli.api.handler.impl.WalletApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2021-12-19T23:11:31.868+05:30")
public class WalletApiServiceFactory {
    private final static WalletApiService service = new WalletApiServiceImpl();

    public static WalletApiService getWalletApi() {
        return service;
    }
}
