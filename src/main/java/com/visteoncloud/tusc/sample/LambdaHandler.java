package com.visteoncloud.tusc.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;


import sun.net.www.protocol.http.HttpURLConnection;

public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>  {

	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		
		// get logger
		LambdaLogger logger = context.getLogger();
		
		// get body
		JSONObject body = new JSONObject(input.getBody());
		String foo = body.getString("foo");
		Integer baz = body.getInt("baz");
		
		// log body
		logger.log("Received request");
		logger.log("Method " + input.getHttpMethod());
		logger.log("Path " + input.getPath());
		logger.log("Raw body " + input.getBody());
		logger.log("foo " + foo);
		logger.log("baz " + baz);
		
		// TODO: handle request data here
		
		JSONObject responseBody = new JSONObject();
		responseBody.put("status", "ok");
		
		// create and return response
		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		response.setStatusCode(200);
		response.setBody(responseBody.toString());
		return response;
	}
	
	public static void GetRequest() throws IOException{
		URL getReqestURL = new URL ("https://g9eyv3jby5.execute-api.us-east-1.amazonaws.com/Prod/data");
		String readLine = null;
		HttpURLConnection getConnection = (HttpURLConnection) getReqestURL.openConnection();
		getConnection.setRequestMethod("GET");
		getConnection.setRequestProperty("distance", "testParameters");
		int responseCode = getConnection.getResponseCode();
		System.out.println("Get response code : " + responseCode);
		System.out.println("Get response message : " + getConnection.getResponseMessage());
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader( new InputStreamReader(getConnection.getInputStream()));
			StringBuffer response = new StringBuffer();
			while ((readLine = in.readLine()) != null) {
				response.append(readLine);
			}
			in.close();
			System.out.println("Response: " + response.toString());
		}
		else {
			System.out.println("ERROR");
			System.out.println("Code : " + getConnection.getResponseCode());
		}
	}

	public static void PostReqest() throws IOException, MalformedURLException {
		String PostParams = "[{ \"time\": 1562514130" + "\"value\": 42.3" ;
		URL dataURL = new URL("https://g9eyv3jby5.execute-api.us-east-1.amazonaws.com/Prod/data");
		try {
			HttpURLConnection postConnecton = (HttpURLConnection) dataURL.openConnection();
			postConnecton.setRequestMethod("POST");
			postConnecton.setRequestProperty("Data: " ,PostParams);
			OutputStream out = postConnecton.getOutputStream();
		    out.write(PostParams.getBytes());
		    out.flush();
		    out.close();
		    int responseCode = postConnecton.getResponseCode();
		    System.out.println("Post response code " + responseCode);
		    System.out.println("Post reponse message" + postConnecton.getResponseMessage());
		    if(responseCode == HttpURLConnection.HTTP_CREATED) {
		    	BufferedReader in = new BufferedReader(new InputStreamReader(postConnecton.getInputStream()));
		    	String inputLine;
		    	StringBuffer response = new StringBuffer();
		    	while((inputLine = in.readLine())!=null) {
		    		in.close();
		    	}
		    System.out.println("Response" + response.toString());
		    }
		    else if(responseCode == HttpURLConnection.HTTP_BAD_GATEWAY) {
		    System.out.println("Error in post request : BAD GATEWAY");
		    }
		    else if(responseCode == HttpURLConnection.HTTP_BAD_METHOD) {
		    	System.out.println("Error in post request : bad metod");
		    }
		    else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
		    	System.out.println("Error in post request : bad request");
		    }
		}
		    catch(MalformedURLException e) {
		    }
	}
}
