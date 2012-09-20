package com.jea.jersey.test;

import org.junit.Assert;

import com.sun.jersey.test.framework.JerseyTest;

public class TestSupport extends JerseyTest {
	
	public TestSupport() {
		super("com.jea.patterns.api");
	}
	
	protected void assertHtmlResponse(String response) {
		assertNotEmptyResponse(response);

        assertResponseContains(response, "<html>");
        assertResponseContains(response, "</html>");
    }
	
	protected void assertXmlResponse(String response) {
		Assert.assertNotNull(response);
		
		assertResponseContains(response, "<?xml version=\"1.0\"");
	}
	
	protected void assertResponseContains(String response, String text) {
        Assert.assertTrue("Response should contain " + text + " but was: " + response, response.contains(text));
    }

	protected void assertNotEmptyResponse(String response) {
		Assert.assertNotNull("No text returned!", response);
	}
}
