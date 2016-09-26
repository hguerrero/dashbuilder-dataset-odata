package org.dashbuilder.dataprovider;

import org.dashbuilder.dataset.def.ODataDataSetDef;
import org.dashbuilder.dataset.json.DataSetDefJSONMarshallerExt;
import org.dashbuilder.dataset.json.ODataDefJSONMarshaller;

public class ODataProviderType extends AbstractProviderType<ODataDataSetDef>{

	@Override
	public String getName() {
		return "ODATA";
	}
	
	@Override
	public ODataDataSetDef createDataSetDef() {
		return new ODataDataSetDef();
	}
	
	@Override
	public DataSetDefJSONMarshallerExt<ODataDataSetDef> getJsonMarshaller() {
		return ODataDefJSONMarshaller.INSTANCE;
	}

}
