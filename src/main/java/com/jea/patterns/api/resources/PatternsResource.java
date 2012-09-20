package com.jea.patterns.api.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import com.jea.patterns.api.dao.DAOFactory;
import com.jea.patterns.api.dao.PatternDAO;
import com.jea.patterns.api.dao.jpa.JpaDAOFactory;
import com.jea.patterns.api.model.Pattern;

@Path("/patterns")
public class PatternsResource {
	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.JPA);
	PatternDAO dao = factory.getPatternDAO();
	
	// Returns the list of Patterns to the user in the browser
	@GET
	@Produces(MediaType.TEXT_XML)
	public List<Pattern> getPatternsBrowser() {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.addAll(dao.getAllPatterns());
		return patterns;
	}
	
	// Returns the list of patterns for applications
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Pattern> getPatterns() {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.addAll(dao.getAllPatterns());
		return patterns;
	}
	
	// Returns the number of patterns
	@GET
	@Path("/count")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount() {
		int count = dao.getAllPatterns().size();
		return String.valueOf(count);
	}
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newPattern(@FormParam("id") int id,
			@FormParam("name") String name,
			@FormParam("intent") String intent,
			@Context HttpServletResponse servletResponse) throws IOException {
		Pattern pattern = new Pattern(id,name);
		if (intent != null) {
			pattern.setIntent(intent);
		}
		dao.insertPattern(pattern);
		servletResponse.sendRedirect("../create_pattern.html");
	}
	
	// Defines that the next path parameter after patterns is
	// treated as a parameter and passed to the PatternResources
	// Allows to type http://localhost:8080/patterns/patterns/1
	// 1 will be treaded as parameter pattern and passed to 
	// PatternResource
	@Path("/{pattern}")
	public PatternResource getPattern(@PathParam("pattern") int id) {
		return new PatternResource(uriInfo, request, id);
	}
}
