package org.dashbuilder.dataset.json;

import static org.dashbuilder.dataset.json.DataSetDefJSONMarshaller.ALL_COLUMNS;
import static org.dashbuilder.dataset.json.DataSetDefJSONMarshaller.CACHE_ENABLED;
import static org.dashbuilder.dataset.json.DataSetDefJSONMarshaller.CACHE_MAXROWS;
import static org.dashbuilder.dataset.json.DataSetDefJSONMarshaller.isBlank;

import org.dashbuilder.dataset.def.ODataDataSetDef;
import org.dashbuilder.json.JsonObject;

public class ODataDefJSONMarshaller implements DataSetDefJSONMarshallerExt<ODataDataSetDef> {

    public static ODataDefJSONMarshaller INSTANCE = new ODataDefJSONMarshaller();

    public static final String SERVER_URL = "serverURL";
    public static final String CLUSTER_NAME = "clusterName";
    public static final String INDEX = "index";
    public static final String TYPE = "type";
    public static final String QUERY = "query";
    public static final String RELEVANCE = "relevance";
    public static final String CACHE_SYNCED = "cacheSynced";

    @Override
    public void fromJson(ODataDataSetDef dataSetDef, JsonObject json) {
        String serverURL = json.getString(SERVER_URL);
        String clusterName = json.getString(CLUSTER_NAME);
        String index = json.getString(INDEX);
        String type = json.getString(TYPE);
        String query = json.getString(QUERY);
        String relevance = json.getString(RELEVANCE);
        String cacheEnabled = json.getString(CACHE_ENABLED);
        String cacheMaxRows = json.getString(CACHE_MAXROWS);
        String cacheSynced = json.getString(CACHE_SYNCED);

        // ServerURL parameter.
        if (isBlank(serverURL)) {
            throw new IllegalArgumentException("The serverURL property is missing.");
        } else {
            dataSetDef.setServerURL(serverURL);
        }

        // Cluster name parameter.
        if (isBlank(clusterName)) {
            throw new IllegalArgumentException("The clusterName property is missing.");
        } else {
            dataSetDef.setClusterName(clusterName);
        }

        // Index parameter
        if (isBlank(index)) {
            throw new IllegalArgumentException("The index property is missing.");
        } else {
            dataSetDef.setIndex(index);
        }

        // Type parameter.
        if (!isBlank(type)) {
            dataSetDef.setType(type);
        }

        // Query parameter.
        if (!isBlank(query)) {
            dataSetDef.setQuery(query);
        }

        // Relevance parameter.
        if (!isBlank(relevance)) {
            dataSetDef.setRelevance(relevance);
        }

        // Cache enabled parameter.
        if (!isBlank(cacheEnabled)) {
            dataSetDef.setCacheEnabled(Boolean.parseBoolean(cacheEnabled));
        }

        // Cache max rows parameter.
        if (!isBlank(cacheMaxRows)) {
            dataSetDef.setCacheMaxRows(Integer.parseInt(cacheMaxRows));
        }
    }

    @Override
    public void toJson(ODataDataSetDef dataSetDef, JsonObject json) {
        // Server URL.
        json.put(SERVER_URL, dataSetDef.getServerURL());

        // Cluster name.
        json.put(CLUSTER_NAME, dataSetDef.getClusterName());

        // Index.
        json.put(INDEX, dataSetDef.getIndex());

        // Type.
        json.put(TYPE, dataSetDef.getType());

        // All columns flag.
        json.put(ALL_COLUMNS, dataSetDef.isAllColumnsEnabled());
    }
}
