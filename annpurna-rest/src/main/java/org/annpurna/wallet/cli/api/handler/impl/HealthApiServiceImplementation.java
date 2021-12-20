package org.annpurna.wallet.cli.api.handler.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.annpurna.wallet.cli.api.handler.HealthApiService;
import org.annpurna.wallet.cli.api.handler.NotFoundException;
import org.annpurna.wallet.cli.exception.AnnpurnaServiceException;
import org.annpurna.wallet.cli.util.ResponseUtil;

public class HealthApiServiceImplementation extends HealthApiService {

	@Override
	public Response health(SecurityContext securityContext, HttpServletRequest httpServletRequest)
			throws NotFoundException, AnnpurnaServiceException {
		return ResponseUtil.getSuccessResponse("All OK!", null);
	}

}
