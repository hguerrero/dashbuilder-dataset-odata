package org.dashbuilder.dataprovider.backend.odata.olingo;

import java.net.URI;
import java.util.List;

import org.apache.olingo.client.api.ODataClient;
import org.apache.olingo.client.api.communication.response.ODataRetrieveResponse;
import org.apache.olingo.client.api.domain.ClientEntity;
import org.apache.olingo.client.api.domain.ClientEntitySet;
import org.apache.olingo.client.api.domain.ClientPrimitiveValue;
import org.apache.olingo.client.api.domain.ClientProperty;
import org.apache.olingo.client.api.domain.ClientValue;
import org.apache.olingo.client.api.uri.URIBuilder;
import org.apache.olingo.client.core.ODataClientFactory;
import org.dashbuilder.dataprovider.backend.odata.exception.ODataClientGenericException;
import org.dashbuilder.dataset.DataSetLookup;
import org.dashbuilder.dataset.DataSetMetadata;
import org.dashbuilder.dataset.def.DataSetDef;
import org.dashbuilder.dataset.filter.ColumnFilter;
import org.dashbuilder.dataset.filter.CoreFunctionFilter;
import org.dashbuilder.dataset.filter.CoreFunctionType;
import org.dashbuilder.dataset.filter.DataSetFilter;

public class OlingoODataClient 
{
	protected String serverURL;
	protected String serviceName;
	protected String segmentValue;
	
	private ODataClient client;
	
	public OlingoODataClient(DataSetDef odDef) 
	{
		this.serverURL = odDef.getProperty("serverURL");
		this.serviceName = odDef.getProperty("serviceName");
		this.segmentValue = odDef.getProperty("segmentValue"); 
	}

	public ClientEntitySet search(DataSetDef definition, DataSetMetadata metadata, DataSetLookup lookup)
			throws ODataClientGenericException 
	{
		checkClient();

		String serviceRoot = serverURL + "/" + serviceName;// "http://localhost:8887/ODataInfinispanEndpoint.svc";

		URIBuilder builder = client.newURIBuilder(serviceRoot);

		builder.appendEntitySetSegment(segmentValue);// "odataCache_get")

        // Filter operations.
        List<DataSetFilter> filters = lookup.getOperationList(DataSetFilter.class);
        if (filters != null && !filters.isEmpty()) {
            // Check columns for the filter operations.
            for (DataSetFilter filterOp  : filters) {
                List<ColumnFilter> columnFilters = filterOp.getColumnFilterList();
                if (columnFilters != null && !columnFilters.isEmpty()) {
                    for (ColumnFilter filter: columnFilters) {
//                        String fcId = filter.getColumnId();
                        //TODO assuming column is available
//                        if (fcId != null && !existColumn(metadata, fcId)) throw new IllegalArgumentException("Filtering by a non existing column [" + fcId + IN_DATASET);
                        // Core functions.
                        if (filter instanceof CoreFunctionFilter) {
                            String result = buildColumnCoreFunctionFilter((CoreFunctionFilter) filter, metadata);
                            builder.filter(result);
                        }
                    }
                }
            }
        }

		// Pagination constraints.
		if (lookup != null) {
			int numRows = lookup.getNumberOfRows(); 
			if (numRows > 0) {
				builder.top(lookup.getNumberOfRows());
			}
			int rowOffset = lookup.getRowOffset();
			builder.skip(rowOffset);
		}

		// Perform the query to the OData server instance.
		ODataRetrieveResponse<ClientEntitySet> response = client.getRetrieveRequestFactory()
				.getEntitySetRequest(builder.build()).execute();

		//TODO process response
		
		ClientEntitySet entitySet = response.getBody();
		
		return entitySet;
	}
	
	private String buildColumnCoreFunctionFilter(CoreFunctionFilter filter, DataSetMetadata metadata) {
		String columnId = filter.getColumnId();		
//		ColumnType columnType = metadata.getColumnType(columnId);
		
		CoreFunctionType type = filter.getType();
        List<?> params = filter.getParameters();
        
        String strFilter = null;
        
        if (CoreFunctionType.EQUALS_TO.equals(type)) {
        	
        	strFilter = String.format("%s eq '%s'", columnId, params.get(0));
        	
        } else {
            throw new IllegalArgumentException("Core function type not supported: " + type);
        }
        
		return strFilter;
	}

	private void checkClient() throws ODataClientGenericException 
	{
        if ( null == client ) 
        {
            try 
            {
                buildClient();
            } catch (Exception e) {
                throw new ODataClientGenericException( "Error while building the odata client.", e );
            }
        }
    }
	
    private ODataClient buildClient() throws Exception 
    {
        if ( null == client ) {
        	client = ODataClientFactory.getClient();
        }
        
        return client;
    }


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
			        ClientPrimitiveValue pValue = property.getPrimitiveValue();
//			        String valueType = value.getTypeName();
			        System.out.printf("Entity property %s value %s \n", name, pValue);
			    }				
			}
		    
		} catch(Exception ex) {
		    ex.printStackTrace();
		}
	}

}
