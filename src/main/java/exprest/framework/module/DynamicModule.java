package exprest.framework.module;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;

public class DynamicModule extends AbstractModule {
      
   @Override
   protected void configure() {
      DynamicModule.bind(this.binder(), this.getClass(), this);
   }
   
   static <T> void bind(Binder binder, Class<T> c, Object o) {
      binder.bind(c).toInstance(c.cast(o));
   }   
}
