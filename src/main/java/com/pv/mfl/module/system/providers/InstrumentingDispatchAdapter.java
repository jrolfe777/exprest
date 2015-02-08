package com.pv.mfl.module.system.providers;

import javax.inject.Inject;
import javax.ws.rs.ext.Provider;

import com.codahale.metrics.MetricRegistry;
import com.sun.jersey.spi.container.ResourceMethodDispatchAdapter;
import com.sun.jersey.spi.container.ResourceMethodDispatchProvider;


@Provider
public class InstrumentingDispatchAdapter implements ResourceMethodDispatchAdapter {
    
   protected final MetricRegistry _registry;

    @Inject
    public InstrumentingDispatchAdapter(MetricRegistry registry) {
        this._registry = registry;
    }


    @Override
    public ResourceMethodDispatchProvider adapt(ResourceMethodDispatchProvider provider) {
        return new InstrumentingDispatchProvider(provider, _registry);
    }
}
