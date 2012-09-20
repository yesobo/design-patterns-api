package com.jea.patterns.api.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import com.jea.patterns.api.dao.DAOFactory;
import com.jea.patterns.api.dao.PatternDAO;
import com.jea.patterns.api.model.Pattern;

public class PatternResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	int id;
	
	DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.JPA);
	PatternDAO dao = factory.getPatternDAO();
	
	public PatternResource(UriInfo uriInfo, Request request, int id) {
		super();
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
	}
	
	// Application integration
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Pattern getPattern() {
		Pattern pattern = dao.findPatternById(id);
		if(pattern == null) {
			throw new RuntimeException("Get: Pattern with " + id + " not found");
		}
		return pattern;
	}
	
	// For the browser
	@PUT
	@Produces(MediaType.TEXT_XML)
	public Pattern getPatternHTML() {
		Pattern pattern = dao.findPatternById(id);
		if(pattern == null) {
			throw new RuntimeException("Get: Pattern with " + id + " not found");
		}
		return pattern;
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	public Response putPattern(JAXBElement<Pattern> pattern) {
		Pattern c = pattern.getValue();
		return putAndGetResponse(c);
	}
	
	@DELETE
	public void deletePattern() {
		boolean c = dao.deletePattern(id);
		if (!c) {
			throw new RuntimeException("Delete: Pattern with " + id + " not found");
		}
	}
	
	private Response putAndGetResponse(Pattern pattern) {
		Response res;
		if (dao.containsPattern(pattern.getId())) {
			res = Response.noContent().build();
		} else {
			res = Response.created(uriInfo.getAbsolutePath()).build();
		}
		dao.insertPattern(pattern);
		return res;
	}
}
