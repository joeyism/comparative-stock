package com.joeyism.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.joeyism.app.domain.User;
import com.joeyism.app.repository.UserRepository;
import com.joeyism.app.security.AuthoritiesConstants;
import com.joeyism.app.security.SecurityUtils;
import com.joeyism.app.web.rest.dto.UserDTO;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;


import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

	private final Logger log = LoggerFactory.getLogger(UserResource.class);

	@Inject
	private UserRepository userRepository;

	/**
	 * GET /users -> get all users.
	 */
	@RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<User> getAll() {
		log.debug("REST request to get all Users");
		return userRepository.findAll();
	}

	/**
	 * GET /getticker -> get all users.
	 * 
	 * @throws JSONException
	 */
	@RequestMapping(value = "/getticker", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
	@Timed
	public String getTicker() throws JSONException {
		return "{}";
	}

	/**
	 * GET /getticker/{name} -> get all users.
	 * 
	 * @throws JSONException
	 */
	@RequestMapping(value = "/getticker/{name}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
	@Timed
	public String getTickerName(@PathVariable String name)
			throws JSONException {
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(
				"http://dev.markitondemand.com/Api/v2/Lookup/json?input=" + name
						+ "&callback=myFunction",
				String.class);
		JSONArray lookup = new JSONArray(result);

		JSONObject obj = new JSONObject();
		obj.put("ticker", result);
		return lookup.toString();
	}

	/**
	 * POST /submitstocks -> get all users.
	 * 
	 * @throws JSONException
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/submitstocks", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> postSubmitStocks(HttpEntity<String> httpEntity, @RequestParam("yearsback") int yearsback)
			throws JSONException, ClientProtocolException, IOException, ParseException {
		String json = httpEntity.getBody();
		JSONObject obj = new JSONObject(json);
		
		ParameterConstructor markitondemand = new ParameterConstructor();
		markitondemand.addStocks(obj, yearsback);

		RestTemplate restTemplate = new RestTemplate();
		String url = "http://dev.markitondemand.com/Api/v2/InteractiveChart/json?parameters="
				+ URLEncoder.encode(markitondemand.params(), "UTF-8");
		
		//TODO: MAKE REQUEST WITH ABOVE URL. URL IS CORRECT
		@SuppressWarnings("deprecation")
		HttpClient httpclient = new DefaultHttpClient();	
		String responseBody = null;
		HighchartsDataConstructor results = new HighchartsDataConstructor();
		
		try {
            HttpGet httpget = new HttpGet(url);

            // Create a response handler
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            responseBody = httpclient.execute(httpget, responseHandler);
            
            results.setYearsBack(yearsback);
            results.parseResponse(responseBody);
            

        } 
		catch (Exception e){
			System.out.println(e.toString());
		}
		finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
		
		return new ResponseEntity<>(results.toString(), HttpStatus.OK); 
	}

	/**
	 * GET /users/:login -> get the "login" user.
	 */
	@RequestMapping(value = "/users/{login}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	ResponseEntity<User> getUser(@PathVariable String login) {
		return userRepository.findOneByLogin(login)
				.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
}
