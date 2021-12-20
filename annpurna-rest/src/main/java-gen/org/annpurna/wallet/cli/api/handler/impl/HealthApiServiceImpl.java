package org.annpurna.wallet.cli.api.handler.impl;

import org.annpurna.wallet.cli.exception.AnnpurnaServiceException;

import org.annpurna.wallet.cli.api.handler.*;
import org.annpurna.wallet.cli.api.model.*;


import java.util.List;
import org.annpurna.wallet.cli.api.handler.NotFoundException;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2021-12-19T23:11:31.868+05:30")
public class HealthApiServiceImpl extends HealthApiService {
    @Override
    public Response health(SecurityContext securityContext,HttpServletRequest httpServletRequest) throws NotFoundException , AnnpurnaServiceException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
