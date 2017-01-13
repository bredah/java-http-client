package bredah.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;

public class HttpRequest {

	private String url;
	private Object httpRequest;
	private Method method;
	private HttpResponse httpResponse;
	private HttpClient httpClient;
	private BasicCookieStore cookieStore;
	private List<NameValuePair> parameters;

	/**
	 * Create a http request
	 * 
	 * @param url
	 * @param method
	 * 
	 */
	public HttpRequest(Method method, String url) {
		this.url = url;
		this.method = method;
		// Instance a new cookie manager
		cookieStore = new BasicCookieStore();
		// Create a new client with proxy authentication
		httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy())
				.setDefaultCookieStore(cookieStore).build();
	}

	/**
	 * Execute request
	 * 
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 */
	public void execute() throws URISyntaxException {
		URI uri;
		// Check if exist parameters
		if (parameters == null) {
			uri = new URIBuilder(url).build();
		} else {
			uri = new URIBuilder(url).addParameters(parameters).build();
		}
		// Create a request
		switch (method) {
		case DELETE:
			httpRequest = new HttpDelete(uri);
			break;
		case GET:
			httpRequest = new HttpGet(uri);
			break;
		case HEAD:
			httpRequest = new HttpHead(uri);
			break;
		case OPTIONS:
			httpRequest = new HttpOptions(uri);
			break;
		case POST:
			httpRequest = new HttpPost(uri);
			break;
		case PUT:
			httpRequest = new HttpPut(uri);
			break;
		case TRACE:
			httpRequest = new HttpTrace(uri);
			break;
		}
		try {
			httpResponse = httpClient.execute((HttpUriRequest) httpRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Add parameter in request
	 * 
	 * @param parameter
	 *            Parameter name
	 * @param value
	 *            Parameter value
	 */
	public void addParameters(String parameter, String value) {
		if (parameters == null) {
			parameters = new ArrayList<>();
		}
		parameters.add(new BasicNameValuePair(parameter, value));
	}

	/**
	 * Set HTTP with PROXY authentication
	 * 
	 * @param url
	 *            Proxy server address
	 * @param port
	 *            Proxy server port
	 * @param user
	 *            User authentication
	 * @param password
	 *            Password from authentication
	 * @return HTTP Client authenticated
	 */
	public void setProxy(String url, int port, String user, String password) {
		// Set credential with user and password
		Credentials credentials = new UsernamePasswordCredentials(user, password);
		// Set PROXY host and port
		HttpHost proxy = new HttpHost(url, port);
		// Define scope authentication
		AuthScope authScope = new AuthScope(url, port);
		// Set credential in authentication scope
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(authScope, credentials);
		// Create a new client with proxy authentication
		httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).setProxy(proxy)
				.setDefaultCredentialsProvider(credsProvider).setDefaultCookieStore(cookieStore).build();
	}

	/**
	 * Retrieve status code from response
	 * 
	 * @return Status code
	 */
	public int getResponseStatusCode() {
		return httpResponse.getStatusLine().getStatusCode();
	}

	/**
	 * Retrieve headers from response
	 * 
	 * @return Response header
	 */
	public Header[] getResponseHeader() {
		return httpResponse.getAllHeaders();
	}

	/**
	 * Retrieve cookies from response
	 * 
	 * @return Response cookies
	 */
	public List<Cookie> getResponseCookies() {
		return cookieStore.getCookies();
	}

	/**
	 * Retrieve body from response
	 * 
	 * @return Response body
	 */
	public InputStream getResponseBody() {
		try {
			return httpResponse.getEntity().getContent();
		} catch (UnsupportedOperationException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Retrieve url from request
	 * 
	 * @return Request URL
	 */
	public String getRequestUrl() {
		return url;
	}

}
