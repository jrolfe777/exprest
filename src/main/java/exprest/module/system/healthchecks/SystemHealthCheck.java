package exprest.module.system.healthchecks;

public class SystemHealthCheck extends com.codahale.metrics.health.HealthCheck {

   public SystemHealthCheck() {
   }

   @Override
   protected Result check() throws Exception {
      return Result.healthy();
   }
}
