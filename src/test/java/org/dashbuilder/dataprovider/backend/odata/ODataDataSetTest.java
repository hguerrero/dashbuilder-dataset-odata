package org.dashbuilder.dataprovider.backend.odata;
/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/


import static org.dashbuilder.dataset.filter.FilterFactory.*;
import static org.dashbuilder.dataset.group.AggregateFunctionType.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.dashbuilder.DataSetCore;
import org.dashbuilder.dataprovider.DataSetProviderRegistry;
import org.dashbuilder.dataprovider.DataSetProviderType;
import org.dashbuilder.dataset.DataSet;
import org.dashbuilder.dataset.DataSetLookup;
import org.dashbuilder.dataset.DataSetLookupFactory;
import org.dashbuilder.dataset.DataSetManager;
import org.dashbuilder.dataset.DataSetMetadata;
import org.dashbuilder.dataset.def.DataSetDef;
import org.dashbuilder.dataset.def.DataSetDefRegistry;
import org.dashbuilder.dataset.json.DataSetDefJSONMarshaller;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class ODataDataSetTest 
{
	private static final Logger logger = LoggerFactory.getLogger(ODataDataSetTest.class);
	
    protected ODataDataSetProvider odataDataSetProvider = spy(new ODataDataSetProvider());
    protected DataSetProviderRegistry dataSetProviderRegistry = DataSetCore.get().getDataSetProviderRegistry();

    protected DataSetDef odataDef = new DataSetDef();

    protected DataSetManager dataSetManager;
    protected DataSetDefRegistry dataSetDefRegistry;
    protected DataSetDefJSONMarshaller jsonMarshaller;
//    protected DataSetFormatter dataSetFormatter;

    @Before
    public void setUp() 
    {
        dataSetManager = DataSetCore.get().getDataSetManager();
        dataSetDefRegistry = DataSetCore.get().getDataSetDefRegistry();
        jsonMarshaller = DataSetCore.get().getDataSetDefJSONMarshaller();
//        dataSetFormatter = new DataSetFormatter();
        
        dataSetProviderRegistry.registerDataProvider(odataDataSetProvider);

        odataDef.setProvider(odataDataSetProvider.getType());
        odataDef.setProperty("serverURL", "http://localhost:8887");
        odataDef.setProperty("serviceName", "ODataInfinispanEndpoint.svc");
        odataDef.setProperty("segmentValue", "odataCache_get");
        odataDef.setUUID("test");
        
        dataSetDefRegistry.registerDataSetDef(odataDef);
    }

    @Test
    public void testRegistry() throws Exception 
    {
        DataSetProviderType type = dataSetProviderRegistry.getProviderTypeByName("ODATA");
        assertEquals(odataDataSetProvider.getType().getName(), "ODATA");
        assertEquals(type, odataDataSetProvider.getType());
    }

    @Test
    public void testJson() throws Exception 
    {
        String json = jsonMarshaller.toJsonString(odataDef);
        assertTrue(json.contains("\"serviceName\": \"ODataInfinispanEndpoint.svc\""));
        assertTrue(json.contains("\"segmentValue\": \"odataCache_get\""));
    }

    @Test
    public void testMetadata() throws Exception 
    {
        DataSetMetadata medatata = dataSetManager.getDataSetMetadata("test");

        verify(odataDataSetProvider).getDataSetMetadata(odataDef);
        
        assertEquals(medatata.getNumberOfColumns(), 3);
//        assertEquals(medatata.getColumnId(0), "prop1");
//        assertEquals(medatata.getColumnId(1), "prop2");
    }

    @Test
    public void testDataSetLookup() throws Exception 
    {
        DataSetLookup lookup = DataSetLookupFactory.newDataSetLookupBuilder()
        		.dataset("test")
        		.filter("level", equalsTo("WARN"))
        		.column(COUNT, "result")
                .buildLookup();

        DataSet dataSet = dataSetManager.lookupDataSet(lookup);

        verify(odataDataSetProvider).lookupDataSet(odataDef, lookup);
        assertEquals(dataSet.getRowCount(), 16);
//        assertEquals(dataSet.getValueAt(0, 0), "hello");
//        assertEquals(dataSet.getValueAt(0, 1), "world");
   }
}