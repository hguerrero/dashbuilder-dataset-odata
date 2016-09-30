package org.dashbuilder.dataprovider.backend.odata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.olingo.client.api.domain.ClientEntity;
import org.apache.olingo.client.api.domain.ClientEntitySet;
import org.apache.olingo.client.api.domain.ClientPrimitiveValue;
import org.apache.olingo.client.api.domain.ClientProperty;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeException;
import org.dashbuilder.dataprovider.DataSetProvider;
import org.dashbuilder.dataprovider.DataSetProviderType;
import org.dashbuilder.dataprovider.DefaultProviderType;
import org.dashbuilder.dataprovider.backend.odata.olingo.OlingoODataClient;
import org.dashbuilder.dataset.ColumnType;
import org.dashbuilder.dataset.DataColumn;
import org.dashbuilder.dataset.DataSet;
import org.dashbuilder.dataset.DataSetFactory;
import org.dashbuilder.dataset.DataSetLookup;
import org.dashbuilder.dataset.DataSetMetadata;
import org.dashbuilder.dataset.def.DataSetDef;
import org.dashbuilder.dataset.def.DataSetDefRegistryListener;
import org.dashbuilder.dataset.filter.DataSetFilter;
import org.dashbuilder.dataset.impl.DataColumnImpl;
import org.dashbuilder.dataset.impl.DataSetMetadataImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ODataDataSetProvider implements DataSetProvider, DataSetDefRegistryListener
{
	protected Logger logger = LoggerFactory.getLogger(ODataDataSetProvider.class);

	public static final DataSetProviderType<DataSetDef> TYPE = new DefaultProviderType("ODATA");
	
	private static ODataDataSetProvider SINGLETON = null;
	
    public ODataDataSetProvider() {
    }

	public static ODataDataSetProvider get() 
	{
		if (SINGLETON == null) {
            SINGLETON = new ODataDataSetProvider ();
        }
        return SINGLETON;
	}

	@Override
	public DataSetProviderType<DataSetDef> getType() 
	{
		return TYPE;
	}

	@Override
	public DataSetMetadata getDataSetMetadata(DataSetDef def) throws Exception 
	{
		return new DataSetMetadataImpl(
				def, "myProvider", 10, 3, 
				Arrays.asList("level","time","thread"), 
				Arrays.asList(ColumnType.LABEL,ColumnType.LABEL,ColumnType.LABEL), 3);
//		DataSet dataSet = lookupDataSet(def, null);
//        if (dataSet == null) {
//            return null;
//        }
//        return dataSet.getMetadata();
	}

	@Override
	public DataSet lookupDataSet(DataSetDef def, DataSetLookup lookup) throws Exception 
	{
		final boolean isTestMode = lookup != null && lookup.testMode();
        DataSetMetadata metadata = (DataSetMetadata) getDataSetMetadata(def, isTestMode);
		
		// Add the data set filter specified in the definition, if any.
        DataSetFilter dataSetFilter = def.getDataSetFilter();
        if (dataSetFilter != null) {
            lookup.addOperation(dataSetFilter);
        }
        
        List<DataColumn> columns = new ArrayList<DataColumn>();
        
        columns.add(new DataColumnImpl("level", ColumnType.LABEL));
        columns.add(new DataColumnImpl("time", ColumnType.LABEL));
        columns.add(new DataColumnImpl("thread", ColumnType.LABEL));
		
        // Perform the query & generate the resulting dataset.
        DataSet dataSet = DataSetFactory.newEmptyDataSet();
        dataSet.setColumns(columns);
        
        OlingoODataClient client = new OlingoODataClient(def);
        ClientEntitySet searchResponse = client.search(def, metadata);//, request);
        
        dataSet.setUUID(def.getUUID());

        // If there are no results, return an empty data set.
        // TODO Not implemented
//        if (searchResponse.getCount() != null && searchResponse.getCount() == 0) return dataSet;
        
        // There exist values. Populate the data set.
        fillDataSetValues(def, dataSet, searchResponse.getEntities());
        
		return dataSet;
	}
	
	/**
     * Fills the dataset values.
     *
     * @param dataSet The dataset instance to fill. Note that dataset columns must be added before calling this method.
     * @param entities The search result entities.
     *
     * @throws Exception
     */
	private void fillDataSetValues(DataSetDef def, DataSet dataSet, List<ClientEntity> entities) 
	{
		List<DataColumn> dataSetColumns = dataSet.getColumns();
        int position = 0;
        for (ClientEntity entity : entities) 
		{
        	int columnNumber = 0;
            for (DataColumn column : dataSetColumns) {
                String columnId = column.getId();
                ClientProperty property = entity.getProperty(columnId);
                ClientPrimitiveValue value = property.getPrimitiveValue();
                try {
					dataSet.setValueAt(position, columnNumber, value.toCastValue(String.class));
				} catch (EdmPrimitiveTypeException e) {
					logger.warn("Couldn't cast to String.class, retracting to toString()");
					logger.debug("Error while trying to cast to String", e);
				}
                columnNumber++;
            }
            position++;
		}
	}

	private DataSetMetadata getDataSetMetadata(DataSetDef def, boolean isTestMode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDataSetOutdated(DataSetDef def) {
		return false;
	}
	
	// Listen to changes on the data set definition registry, like for instance, when a dataset definition
    // is registered, removed, or updated.

    public void onDataSetDefStale(DataSetDef def) {
        if (this.getType().equals(def.getProvider())) {
            // Add your own logic in case you are interested in this type of events ...
        }
    }

    public void onDataSetDefModified(DataSetDef olDef, DataSetDef newDef) {
        if (this.getType().equals(olDef.getProvider())) {
            // Add your own logic in case you are interested in this type of events ...
        }
    }

    public void onDataSetDefRemoved(DataSetDef oldDef) {
        if (this.getType().equals(oldDef.getProvider())) {
            // Add your own logic in case you are interested in this type of events ...
        }
    }

    public void onDataSetDefRegistered(DataSetDef newDef) {
        if (this.getType().equals(newDef.getProvider())) {
            // Add your own logic in case you are interested in this type of events ...
        }
    }
}
