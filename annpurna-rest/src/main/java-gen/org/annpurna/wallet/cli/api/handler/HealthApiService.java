package org.annpurna.wallet.cli.api.handler;

import org.annpurna.wallet.cli.exception.AnnpurnaServiceException;

import org.annpurna.wallet.cli.api.handler.*;
import org.annpurna.wallet.cli.api.model.*;

import org.annpurna.wallet.cli.api.handler.impl.ApiServiceImplementation;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.servlet.http.HttpServletRequest;



import java.util.List;
import org.annpurna.wallet.cli.api.handler.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2021-12-19T23:11:31.868+05:30")
public abstract class HealthApiService extends ApiServiceImplementation {
    public abstract Response health(SecurityContext securityContext,HttpServletRequest httpServletRequest) throws NotFoundException , AnnpurnaServiceException;
}
