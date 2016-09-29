package org.dashbuilder.dataset.def;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.dashbuilder.dataprovider.backend.odata.ODataDataSetProvider;
import org.dashbuilder.dataset.sort.ColumnSort;
import org.dashbuilder.dataset.validation.groups.ODataDataSetDefValidation;
import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class ODataDataSetDef extends DataSetDef {

    // Constants.

	@Portable
    public static enum ODataKeywords {
        ALL;

        private static final String KEYWORD_ALL = "_all";

        @Override
        public String toString() {
            if (this.equals(ALL)) return KEYWORD_ALL;
            return super.toString();
        }

    }

    // Data Set user parameters.
    @NotNull(message = "{dataSetApi_elDataSetDef_serverURL_notNull}", groups = {ODataDataSetDefValidation.class})
    @Size(min = 1, message = "{dataSetApi_elDataSetDef_serverURL_notNull}", groups = {ODataDataSetDefValidation.class})
    protected String serverURL;

    protected String clusterName;

    /**
     * Index/es to query. Can handle multiple values, comma separated.
     */
    @NotNull(message = "{dataSetApi_elDataSetDef_index_notNull}", groups = {ODataDataSetDefValidation.class})
    @Size(min = 1, message = "{dataSetApi_elDataSetDef_index_notNull}", groups = {ODataDataSetDefValidation.class})
    protected String index;

    /**
     * Type/es to query. Can handle multiple values, comma separated. Not mandatory.
     */
    protected String type;

    protected String query;
    protected String relevance;
    protected ColumnSort columnSort;

    public ODataDataSetDef() {
        super.setProvider(ODataDataSetProvider.TYPE);
    }

    public String getServerURL() {
        return serverURL;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getRelevance() {
        return relevance;
    }

    public void setRelevance(String relevance) {
        this.relevance = relevance;
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public Integer getCacheMaxRows() {
        return cacheMaxRows;
    }

    public void setCacheMaxRows(Integer cacheMaxRows) {
        this.cacheMaxRows = cacheMaxRows;
    }

    public ColumnSort getColumnSort() {
        return columnSort;
    }

    public void setColumnSort(ColumnSort columnSort) {
        this.columnSort = columnSort;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            ODataDataSetDef other = (ODataDataSetDef) obj;
            if (!super.equals(other)) {
                return false;
            }
            if (serverURL != null && !serverURL.equals(other.serverURL)) {
                return false;
            }
            if (clusterName != null && !clusterName.equals(other.clusterName)) {
                return false;
            }
            if (index != null && !index.equals(other.index)) {
                return false;
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public DataSetDef clone() {
        ODataDataSetDef def = new ODataDataSetDef();
        clone(def);
        def.setServerURL(getServerURL());
        def.setClusterName(getClusterName());
        def.setIndex(getIndex());
        def.setType(getType());
        return def;
    }
    
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("UUID=").append(UUID).append("\n");
        out.append("Provider=").append(provider).append("\n");
        out.append("Public=").append(isPublic).append("\n");
        out.append("Push enabled=").append(pushEnabled).append("\n");
        out.append("Push max size=").append(pushMaxSize).append(" Kb\n");
        out.append("Server URL=").append(serverURL).append("\n");
        out.append("Index=").append(index).append("\n");
        out.append("Type=").append(type).append("\n");
        out.append("Query=").append(query).append("\n");
        out.append("Get all columns=").append(allColumnsEnabled).append("\n");
        out.append("Cache enabled=").append(cacheEnabled).append("\n");
        out.append("Cache max rows=").append(cacheMaxRows).append(" Kb\n");
        return out.toString();
    }
}