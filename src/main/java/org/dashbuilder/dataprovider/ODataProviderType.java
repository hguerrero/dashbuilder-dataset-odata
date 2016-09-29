package org.dashbuilder.dataprovider;

import org.dashbuilder.dataset.def.ODataDataSetDef;
import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class ODataProviderType extends DefaultProviderType
{
	@Override
	public String getName() {
		return "ODATA";
	}
	
	@Override
	public ODataDataSetDef createDataSetDef() {
		return new ODataDataSetDef();
	}
	
//	@Override
//	public DataSetDefJSONMarshallerExt<DataSetDef> getJsonMarshaller() {
//		return ODataDefJSONMarshaller.INSTANCE;
//	}

}
