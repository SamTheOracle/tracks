package com.oracolo.findmycar;

import java.security.Principal;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.keycloak.KeycloakPrincipal;

import io.quarkus.security.credential.Credential;
import io.quarkus.security.identity.SecurityIdentity;

@RequestScoped
public class BaseRest {
	@Inject
	JsonWebToken jsonWebToken;

	protected String getLoggedUserId(){
		String loggedUserId = jsonWebToken.getSubject();
		if(loggedUserId==null){
			throw new NotAuthorizedException("Error. User does not have subject");
		}
		return loggedUserId;
	}
}
