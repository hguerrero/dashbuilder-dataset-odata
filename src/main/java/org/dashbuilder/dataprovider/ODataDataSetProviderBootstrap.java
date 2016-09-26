package org.dashbuilder.dataprovider;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.dashbuilder.dataset.DataSetDefRegistryCDI;
import org.dashbuilder.dataset.def.ODataDataSetDef;
import org.uberfire.commons.services.cdi.Startup;

@Startup
@ApplicationScoped
public class ODataDataSetProviderBootstrap 
{
	@Inject
    private DataSetProviderRegistryCDI providerRegistry;
	
	@Inject
	private DataSetDefRegistryCDI dataSetDefRegistry;
	
	@Inject
	private ODataDataSetProviderCDI odataDataSetProviderCDI;
	
	@PostConstruct
    public void init() 
	{
		ODataDataSetDef odDataSetDef = new ODataDataSetDef();
		odDataSetDef.setUUID("odata_report");
		odDataSetDef.setServerURL("localhost:9300");
		odDataSetDef.setClusterName("elasticsearch");
		odDataSetDef.setIndex("expensereports");
		odDataSetDef.setType("expense");
		odDataSetDef.setAllColumnsEnabled(true);
		
    	providerRegistry.registerDataProvider(odataDataSetProviderCDI);
    	dataSetDefRegistry.registerDataSetDef(odDataSetDef);
    }

}
