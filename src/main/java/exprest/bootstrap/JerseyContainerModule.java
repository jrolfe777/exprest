package exprest.bootstrap;

import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.spi.container.WebApplication;

public class JerseyContainerModule extends JerseyServletModule {
   private final GuiceContainer _container;

   public JerseyContainerModule(final GuiceContainer container) {
      _container = container;
   }

   @Override
   protected void configureServlets() {
      bind(GuiceContainer.class).toInstance(_container);
   }

   @Override
   public WebApplication webApp(com.sun.jersey.guice.spi.container.servlet.GuiceContainer guiceContainer) {
      return _container.getWebApplication();
   }
}