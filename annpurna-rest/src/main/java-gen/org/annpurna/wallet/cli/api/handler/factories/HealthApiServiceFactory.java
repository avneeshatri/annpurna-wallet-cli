package org.annpurna.wallet.cli.api.handler.factories;

import org.annpurna.wallet.cli.api.handler.HealthApiService;
import org.annpurna.wallet.cli.api.handler.impl.HealthApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2021-12-19T23:11:31.868+05:30")
public class HealthApiServiceFactory {
    private final static HealthApiService service = new HealthApiServiceImpl();

    public static HealthApiService getHealthApi() {
        return service;
    }
}
