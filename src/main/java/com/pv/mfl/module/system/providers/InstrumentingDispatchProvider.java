package com.pv.mfl.module.system.providers;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.model.AbstractResourceMethod;
import com.sun.jersey.spi.container.ResourceMethodDispatchProvider;
import com.sun.jersey.spi.dispatch.RequestDispatcher;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.Timed;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.ExceptionMetered;

import static com.codahale.metrics.MetricRegistry.name;

class InstrumentingDispatchProvider implements ResourceMethodDispatchProvider {

   protected final ResourceMethodDispatchProvider _provider;
   protected final MetricRegistry _registry;

   public InstrumentingDispatchProvider(ResourceMethodDispatchProvider provider, MetricRegistry registry) {
      _provider = provider;
      _registry = registry;
   }

   /**
    * We always want our resource methods timed and metered, so we use this to enforce this behavior.  However,
    * if someone has explicitly made use of the metrics annotations, that will override this default behavior.
    */
   @Override
   public RequestDispatcher create(AbstractResourceMethod method) {
      
      final RequestDispatcher originalDispatcher = _provider.create(method);
      if (originalDispatcher == null) {
         return null;
      }
      
      if (method.getMethod().isAnnotationPresent(Timed.class) || 
            method.getMethod().isAnnotationPresent(Metered.class) || 
            method.getMethod().isAnnotationPresent(ExceptionMetered.class)) {
         
         return originalDispatcher;
         
      }

      String name = name(name(method.getDeclaringResource().getResourceClass(), method.getMethod().getName()));
      
      final Timer timer = _registry.timer(name);
      final Meter exceptMeter = _registry.meter(name + "_exceptions");

      final RequestDispatcher timedDispatcher = (Object resource, HttpContext httpContext) -> {
         final Timer.Context context = timer.time();
         try {
            originalDispatcher.dispatch(resource, httpContext);
         } finally {
            context.stop();
         }
      };

      final RequestDispatcher exceptionMetricDispatcher = (Object resource, HttpContext httpContext) -> {
         try {
            timedDispatcher.dispatch(resource, httpContext);
         } catch (Exception e) {
            exceptMeter.mark();
            InstrumentingDispatchProvider.<RuntimeException> throwUnchecked(e);
         }
      };

      return exceptionMetricDispatcher;
   }

   @SuppressWarnings("unchecked")
   private static <T extends Exception> void throwUnchecked(Throwable e)
         throws T {
      throw (T) e;
   }
}
