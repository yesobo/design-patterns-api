package com.jea.patterns.api.resources;

import java.io.IOException;

import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import junit.framework.Assert;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.atsistemas.xml.validator.XMLValidator;
import com.jea.jersey.test.TestSupport;
import com.jea.patterns.api.model.Pattern;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.representation.Form;

public class PatternsResourceTest extends TestSupport {
	
	private static final String PATTERN_XSD_PATH = 
			"src/test/resources/pattern.xsd";
	private static final String PATTERNS_XSD_PATH =
			"src/test/resources/patterns.xsd";
	private static final String PATH_ALL_ELEMENTS =
			"/patterns";
	private static final String ELEMENT_TAG = "pattern";
	private static final int DB_SIZE = 2;
	
	private static final String ID_FIELD_NAME = "id";
	private static final String NAME_FIELD_NAME = "name";
	private static final String INTENT_FIELD_NAME = "intent";
	
	private static final int[] ARR_DB_IDS = {1, 2};
	
	private static final int NEW_PATTERN_ID = 3;
	private static final String NEW_PATTERN_NAME =
			"Test Pattern Name";
	private static final String NEW_PATTERN_AKA =
			"Test Pattern aka";
	private static final String NEW_PATTERN_INTENT =
			"Test Pattern intent";
	private static final String NEW_PATTERN_MOTIV =
			"Test pattern motivation";
	
	private static final String FORM_ID_FIELD = "id";
	private static final String FORM_NAME_FIELD = "name";
	private static final String FORM_AKA_FIELD = "aka";
	private static final String FORM_INTENT_FIELD = "intent";
	private static final String FORM_MOTIV_FIELD = "motivation";
	
	private static final String SUBPATH_TO_ELEMENT = "patterns/";
	private static final String PATH_TO_COUNTER = "patterns/count";
	private static final int EXISTING_ITEM_ID = 1;
	private static final String EXISTING_ITEM_NAME = "Singleton";
	private static final String EXISTING_ITEM_INTENT = 
			"Ensure a class only has one instance, and provide a global point of acccess to it";
	
	public PatternsResourceTest() throws Exception {
		super();
	}
	
	@Test
	public void getPatternsBrowserTest() throws SAXException, 
		ParserConfigurationException, IOException, 
		TransformerConfigurationException, TransformerException, 
		TransformerFactoryConfigurationError {
		
		String response = resource().path(PATH_ALL_ELEMENTS)
				.accept(MediaType.TEXT_XML)
				.get(String.class);
		
		Document doc = XMLValidator.stringToDom(response);
		NodeList patterns = doc.getElementsByTagName(ELEMENT_TAG);
		Assert.assertEquals(DB_SIZE, patterns.getLength());
		Assert.assertTrue(XMLValidator.isValidXMLAgainstXSD(doc, 
				PATTERNS_XSD_PATH));
	}
	
	@Test
	public void getTodosAsXML() throws ParserConfigurationException,
		SAXException, TransformerConfigurationException, 
		TransformerException, TransformerFactoryConfigurationError, 
		IOException {
		Document response = resource().path(PATH_ALL_ELEMENTS)
				.accept(MediaType.APPLICATION_XML)
				.get(Document.class);
		
		NodeList patterns = response.getElementsByTagName(ELEMENT_TAG);
		Assert.assertEquals(DB_SIZE, patterns.getLength());
		Assert.assertTrue(XMLValidator.isValidXMLAgainstXSD(response, 
				PATTERNS_XSD_PATH));
	}

	@Test
	public void getTodosAsJSON() throws JSONException {
		JSONObject response = resource().path(PATH_ALL_ELEMENTS)
				.accept(MediaType.APPLICATION_JSON)
				.get(JSONObject.class);	
		JSONArray array = response.getJSONArray(ELEMENT_TAG);
		Assert.assertEquals(DB_SIZE, array.length());
		
		// check every id on result is on our ids constant array
		boolean resultIdIsInDB;
		int result_id;
		for (int i = 0; i < array.length(); i++) {
			result_id = ((JSONObject)array.get(i)).getInt(ID_FIELD_NAME);
			resultIdIsInDB = false;
			for(int id: ARR_DB_IDS) {
				if(result_id == id) {
					resultIdIsInDB = true;
				}
			}
			Assert.assertTrue(resultIdIsInDB);
		}
	}
	
	@Test
	public void postNewTodoAsForm() throws JSONException {
		try {	
			//check if the item already exists
			JSONObject jsonResponse = resource()
				.path(String.format(SUBPATH_TO_ELEMENT + "%s", NEW_PATTERN_ID))
				.accept(MediaType.APPLICATION_JSON)
				.get(JSONObject.class);
		
			// if it exists delete.
			if( jsonResponse.getInt(ID_FIELD_NAME) == NEW_PATTERN_ID) {
				resource().path(SUBPATH_TO_ELEMENT + String.valueOf(NEW_PATTERN_ID))
				.delete();
			}
		} catch (RuntimeException ex) {
			// pattern does not exists
		}

		Form form = new Form();
		form.add(FORM_ID_FIELD, String.valueOf(NEW_PATTERN_ID));
		form.add(FORM_NAME_FIELD, NEW_PATTERN_NAME);
		ClientResponse response = resource().path(PATH_ALL_ELEMENTS)
				.type(MediaType.APPLICATION_FORM_URLENCODED)
				.post(ClientResponse.class, form);
		// check if new item has been inserted
		assertIfTodoExistsByParamsAsJSON(NEW_PATTERN_ID, NEW_PATTERN_NAME, 
				null);
		resource().path(SUBPATH_TO_ELEMENT + String.valueOf(NEW_PATTERN_ID))
			.delete();
	}
	
	@Test
	public void countURITest() {
		String response = resource().path(PATH_TO_COUNTER)
				.accept(MediaType.TEXT_PLAIN)
				.get(String.class);
		Assert.assertEquals(Integer.valueOf(DB_SIZE), 
				Integer.valueOf(response));
	}
	
	@Test
	public void getExistingToDoAsPlainText() 
			throws TransformerConfigurationException, TransformerException, 
			TransformerFactoryConfigurationError, SAXException, 
			XpathException, IOException, ParserConfigurationException {
		
		String resp = resource().path(PATH_TO_COUNTER)
				.accept(MediaType.TEXT_PLAIN)
				.get(String.class);
		
		Document response = resource().path(SUBPATH_TO_ELEMENT + 
					String.valueOf(EXISTING_ITEM_ID))
				.accept(MediaType.APPLICATION_XML)
				.get(Document.class);
		
	    Assert.assertTrue(XMLValidator.isValidXMLAgainstXSD(response, 
	    		PATTERN_XSD_PATH));
	    Node aux = XMLValidator.xPathNodeValue(ELEMENT_TAG + "/" + ID_FIELD_NAME, 
	    		response).item(0);
	    Assert.assertEquals(String.valueOf(EXISTING_ITEM_ID), aux.getTextContent());
	    aux = XMLValidator.xPathNodeValue(ELEMENT_TAG + "/" + NAME_FIELD_NAME, 
	    		response).item(0);
	    Assert.assertEquals(EXISTING_ITEM_NAME, aux.getTextContent());
	    aux = XMLValidator.xPathNodeValue(ELEMENT_TAG + "/" + INTENT_FIELD_NAME, 
	    		response).item(0);
		Assert.assertEquals(EXISTING_ITEM_INTENT, aux.getTextContent());
	}
	
	@Test
	public void getTodoWithIdEquals1AsJSON() throws JSONException {
		assertIfTodoExistsByParamsAsJSON(EXISTING_ITEM_ID, 
				EXISTING_ITEM_NAME, EXISTING_ITEM_INTENT);
	}

	@Test
	public void putNewTodoAsXML() throws JSONException {
		Pattern pattern = new Pattern(NEW_PATTERN_ID, NEW_PATTERN_NAME);
		ClientResponse response = 
				resource()
				.path(SUBPATH_TO_ELEMENT)
				.path(String.valueOf(pattern.getId()))
				.accept(MediaType.APPLICATION_XML)
				.type(MediaType.APPLICATION_XML)
				.put(ClientResponse.class, pattern);
		Assert.assertEquals(201, response.getStatus());
		assertIfTodoExistsByParamsAsJSON(NEW_PATTERN_ID, NEW_PATTERN_NAME, null);
		resource().path(SUBPATH_TO_ELEMENT + NEW_PATTERN_ID).delete();
	}
	
	@Test
	public void deleteExistingTodoTest() throws JSONException {
		
		// insert new pattern to be deleted
		Pattern pattern = new Pattern(NEW_PATTERN_ID, NEW_PATTERN_NAME);
		ClientResponse cliResponse = 
				resource()
				.path(SUBPATH_TO_ELEMENT)
				.path(String.valueOf(pattern.getId()))
				.accept(MediaType.APPLICATION_XML)
				.type(MediaType.APPLICATION_XML)
				.put(ClientResponse.class, pattern);

		// check if new pattern is in our dataSource.
		JSONObject response = resource().path(SUBPATH_TO_ELEMENT + NEW_PATTERN_ID)
				.accept(MediaType.APPLICATION_JSON)
				.get(JSONObject.class);
		Assert.assertEquals(NEW_PATTERN_ID, response.getLong(ID_FIELD_NAME));
		// delete existing item
		resource().path(SUBPATH_TO_ELEMENT + NEW_PATTERN_ID).delete();
		// check is it's been deleted
		try {
			response = resource().path(SUBPATH_TO_ELEMENT + NEW_PATTERN_ID)
					.accept(MediaType.APPLICATION_JSON)
					.get(JSONObject.class);
			Assert.fail("get() should have thrown a RuntimeException");
			// check if id=1 is not in the result.
		} catch (Exception ex) {
			// ...
		}
	}
	
	public void assertIfTodoExistsByParamsAsJSON(int id, String summary, 
			String description) throws JSONException {
		
		
		JSONObject response = resource()
				.path(String.format(SUBPATH_TO_ELEMENT + "%s", id))
				.accept(MediaType.APPLICATION_JSON)
				.get(JSONObject.class);
		Assert.assertEquals(id, response.getInt(ID_FIELD_NAME));
		Assert.assertEquals(summary, response.getString(NAME_FIELD_NAME));
		if (description != null) {
			Assert.assertEquals(description, 
					response.getString(INTENT_FIELD_NAME));
		}
	}
}
