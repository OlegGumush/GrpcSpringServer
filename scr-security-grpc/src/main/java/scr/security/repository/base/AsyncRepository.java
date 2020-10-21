package scr.security.repository.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import io.grpc.Status;

public abstract class AsyncRepository {

	protected final static String BASE_URL = "http://localhost:8090/";
	
	@Autowired
	protected RestTemplate restTemplate;
	
	protected <T> ResponseEntity<T> getRequest(String URI, Class<T> returnType) {
		
		ResponseEntity<T> result = null;
		
		try {
			result = restTemplate.exchange(URI, HttpMethod.GET, new HttpEntity<>(getHeaders()), returnType);		
			
		} catch (Exception e) {
			
			throw Status.INTERNAL.withDescription("An error occurred while trying to send GET")
									.augmentDescription(e.getMessage())
									.asRuntimeException();
		}
		
		return result;
	}
	
	protected <T, V> ResponseEntity<T> deleteRequest(String URI, Class<T> returnType) {
		
		System.out.println(URI);
		ResponseEntity<T> result = null;

		try {
			result = restTemplate.exchange(URI, HttpMethod.DELETE, new HttpEntity<>(getHeaders()), returnType);		
			
		} catch (Exception e) {
			
			throw Status.INTERNAL.withDescription("An error occurred while trying to send DELETE")
									.augmentDescription(e.getMessage())
									.asRuntimeException();
		}
		
		return result;
	}

	protected <T, V> ResponseEntity<T> postRequest(String URI, Class<T> returnType, V body) {
		
		ResponseEntity<T> result = null;

		try {
			result = restTemplate.exchange(URI, HttpMethod.POST, new HttpEntity<>(body, getHeaders()), returnType);		
			
		} catch (Exception e) {
			
			throw Status.INTERNAL.withDescription("An error occurred while trying to send POST")
									.augmentDescription(e.getMessage())
									.asRuntimeException();
		}
		
		return result;
	}
	
	protected <T, V> ResponseEntity<T> putRequest(String URI, Class<T> returnType, V body) {
		
		ResponseEntity<T> result = null;
		
		try {
			result = restTemplate.exchange(URI, HttpMethod.PUT, new HttpEntity<>(body, getHeaders()), returnType);							
		
		} catch (Exception e) {
			
			throw Status.INTERNAL.withDescription("An error occurred while trying to send DELETE")
									.augmentDescription(e.getMessage())
									.asRuntimeException();
		}
		
		return result;
	}

	protected HttpHeaders getHeaders() {
		
	    HttpHeaders headers = new HttpHeaders();  
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    return headers;
	}
}
