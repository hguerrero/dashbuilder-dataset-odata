package org.dashbuilder.dataprovider.backend.odata;

import static org.dashbuilder.dataset.date.Month.APRIL;
import static org.dashbuilder.dataset.date.Month.AUGUST;
import static org.dashbuilder.dataset.date.Month.DECEMBER;
import static org.dashbuilder.dataset.date.Month.FEBRUARY;
import static org.dashbuilder.dataset.date.Month.JANUARY;
import static org.dashbuilder.dataset.date.Month.JULY;
import static org.dashbuilder.dataset.date.Month.JUNE;
import static org.dashbuilder.dataset.date.Month.MARCH;
import static org.dashbuilder.dataset.date.Month.MAY;
import static org.dashbuilder.dataset.date.Month.NOVEMBER;
import static org.dashbuilder.dataset.date.Month.OCTOBER;
import static org.dashbuilder.dataset.date.Month.SEPTEMBER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dashbuilder.dataprovider.DataSetProvider;
import org.dashbuilder.dataprovider.DataSetProviderType;
import org.dashbuilder.dataprovider.StaticDataSetProvider;
import org.dashbuilder.dataset.ColumnType;
import org.dashbuilder.dataset.DataColumn;
import org.dashbuilder.dataset.DataSet;
import org.dashbuilder.dataset.DataSetFactory;
import org.dashbuilder.dataset.DataSetLookup;
import org.dashbuilder.dataset.DataSetMetadata;
import org.dashbuilder.dataset.IntervalBuilderDynamicDate;
import org.dashbuilder.dataset.def.DataSetDef;
import org.dashbuilder.dataset.def.ODataDataSetDef;
import org.dashbuilder.dataset.engine.group.IntervalBuilderLocator;
import org.dashbuilder.dataset.filter.DataSetFilter;
import org.dashbuilder.dataset.impl.DataColumnImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ODataDataSetProvider implements DataSetProvider
{
	protected Logger log = LoggerFactory.getLogger(ODataDataSetProvider.class);
    protected StaticDataSetProvider staticDataSetProvider;
    protected IntervalBuilderLocator intervalBuilderLocator;
    protected IntervalBuilderDynamicDate intervalBuilderDynamicDate;
	
    /** Backend cache map. **/
    protected final Map<String,DataSetMetadata> _metadataMap = new HashMap<String,DataSetMetadata>();

	public static final DataSetProviderType<DataSetDef> TYPE = () -> "ODATA";
	
	private static ODataDataSetProvider SINGLETON = null;
	
    public ODataDataSetProvider() {
    }

    public ODataDataSetProvider(StaticDataSetProvider staticDataSetProvider,
                                        IntervalBuilderLocator intervalBuilderLocator,
                                        IntervalBuilderDynamicDate intervalBuilderDynamicDate) {

        this.staticDataSetProvider = staticDataSetProvider;
        this.intervalBuilderLocator = intervalBuilderLocator;
        this.intervalBuilderDynamicDate = intervalBuilderDynamicDate;
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
		DataSet dataSet = lookupDataSet(def, null);
        if (dataSet == null) {
            return null;
        }
        return dataSet.getMetadata();
	}

	@Override
	public DataSet lookupDataSet(DataSetDef def, DataSetLookup lookup) throws Exception 
	{
		ODataDataSetDef odDef = (ODataDataSetDef)def;
		
		// Add the data set filter specified in the definition, if any.
        DataSetFilter dataSetFilter = odDef.getDataSetFilter();
        if (dataSetFilter != null) {
            lookup.addOperation(dataSetFilter);
        }
        
        List<DataColumn> columns = new ArrayList<DataColumn>();
        
        columns.add(new DataColumnImpl("level", ColumnType.LABEL));
		
//		DataSet dataSet = DataSetFactory.newEmptyDataSet();
//		
//		dataSet.setColumns(columns);
        
        DataSet dataSet = DataSetFactory.newDataSetBuilder()
		        .label("month")
		        .number("2012")
		        .number("2013")
		        .number("2014")
		        .row(JANUARY, 1000d, 2000d, 3000d)
		        .row(FEBRUARY, 1400d, 2300d, 2000d)
		        .row(MARCH, 1300d, 2000d, 1400d)
		        .row(APRIL, 900d, 2100d, 1500d)
		        .row(MAY, 1300d, 2300d, 1600d)
		        .row(JUNE, 1010d, 2000d, 1500d)
		        .row(JULY, 1050d, 2400d, 3000d)
		        .row(AUGUST, 2300d, 2000d, 3200d)
		        .row(SEPTEMBER, 1900d, 2700d, 3000d)
		        .row(OCTOBER, 1200d, 2200d, 3100d)
		        .row(NOVEMBER, 1400d, 2100d, 3100d)
		        .row(DECEMBER, 1100d, 2100d, 4200d)
		        .buildDataSet();
        
		return dataSet;
	}
	
	@Override
	public boolean isDataSetOutdated(DataSetDef def) {
		return false;
	}
	
    protected void remove(final String uuid) {
        _metadataMap.remove(uuid);
//        destroyClient(uuid);
        staticDataSetProvider.removeDataSet(uuid);
    }

    public void destroy() {
        // Destroy all clients.
//        for (ElasticSearchClient client : _clientsMap.values()) {
//            destroyClient(client);
//        }
    }

}
