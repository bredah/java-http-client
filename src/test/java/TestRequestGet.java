import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Assert;
import org.junit.Test;

import bredah.httpclient.HttpRequest;
import bredah.httpclient.Method;

public class TestRequestGet {

	@Test
	public void getRequestFromGoogle() throws ClientProtocolException, IOException, URISyntaxException {
		HttpRequest request = new HttpRequest(Method.GET, "http://www.google.com");
		request.execute();
		Assert.assertEquals(200, request.getResponseStatusCode());
	}

	@Test
	public void getRequestFromGoogleBR() throws ClientProtocolException, IOException, URISyntaxException {
		HttpRequest request = new HttpRequest(Method.GET, "http://www.google.com.br");
		request.execute();
		Assert.assertEquals(200, request.getResponseStatusCode());
	}

	@Test(expected = UnknownHostException.class)
	public void getRequestFromGoogleException() throws ClientProtocolException, IOException, URISyntaxException {
		HttpRequest request = new HttpRequest(Method.GET, "http://www.google.com.nn");
		request.execute();
	}

}
