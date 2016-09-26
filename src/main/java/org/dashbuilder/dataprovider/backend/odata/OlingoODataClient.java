package org.dashbuilder.dataprovider.backend.odata;

import java.net.URI;
import java.util.List;

import org.apache.olingo.client.api.ODataClient;
import org.apache.olingo.client.api.communication.response.ODataRetrieveResponse;
import org.apache.olingo.client.api.domain.ClientEntity;
import org.apache.olingo.client.api.domain.ClientEntitySet;
import org.apache.olingo.client.api.domain.ClientProperty;
import org.apache.olingo.client.api.domain.ClientValue;
import org.apache.olingo.client.core.ODataClientFactory;

public class OlingoODataClient {

	public static void main(String[] args) 
	{
		ODataClient client = ODataClientFactory.getClient();
		
		String serviceRoot = "http://localhost:8887/ODataInfinispanEndpoint.svc";
		
		URI errorsUri = client.newURIBuilder(serviceRoot)
		          .appendEntitySetSegment("odataCache_get")
		          .filter("level eq 'ERROR'")
		          .build();
		
		try 
		{
			ODataRetrieveResponse<ClientEntitySet> response = client.getRetrieveRequestFactory().getEntitySetRequest(errorsUri).execute();
			
			ClientEntitySet entitySet = response.getBody();
			
			for (ClientEntity entity : entitySet.getEntities()) 
			{
				List<ClientProperty> properties = entity.getProperties();
			    
			    for (ClientProperty property : properties) {
			        String name = property.getName();
			        ClientValue value = property.getValue();
//			        String valueType = value.getTypeName();
			        System.out.printf("Entity property %s value %s \n", name, value);
			    }				
			}
		    
		} catch(Exception ex) {
		    ex.printStackTrace();
		}
	}

}
