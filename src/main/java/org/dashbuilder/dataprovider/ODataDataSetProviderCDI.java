package org.dashbuilder.dataprovider;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.dashbuilder.DataSetCore;
import org.dashbuilder.dataprovider.backend.odata.ODataDataSetProvider;

@ApplicationScoped
public class ODataDataSetProviderCDI extends ODataDataSetProvider {

    public ODataDataSetProviderCDI() {
    }

    @Inject
    public ODataDataSetProviderCDI(StaticDataSetProviderCDI staticDataSetProvider) {

        super(staticDataSetProvider,
                DataSetCore.get().getIntervalBuilderLocator(),
                DataSetCore.get().getIntervalBuilderDynamicDate());
    }

    @PreDestroy
    public void destroy() {
        super.destroy();
    }

    /* Listen to changes on the data set definition registry

    private void onDataSetStaleEvent(@Observes DataSetStaleEvent event) {
        DataSetDef def = event.getDataSetDef();
        if (ODataDataSetProvider.TYPE.equals(def.getProvider())) {
            remove(def.getUUID());
        }
    }

    private void onDataSetDefRemovedEvent(@Observes DataSetDefRemovedEvent event) {
        DataSetDef def = event.getDataSetDef();
        if (ODataDataSetProvider.TYPE.equals(def.getProvider())) {
            remove(def.getUUID());
        }
    }

    private void onDataSetDefModifiedEvent(@Observes DataSetDefModifiedEvent event) {
        DataSetDef def = event.getOldDataSetDef();
        if (ODataDataSetProvider.TYPE.equals(def.getProvider())) {
            remove(def.getUUID());
        }
    }
    */
}