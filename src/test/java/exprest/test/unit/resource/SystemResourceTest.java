package exprest.test.unit.resource;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import test.exprest.module.system.resources.SystemResource;
import exprest.test.core.TestResourceFactory;

public class SystemResourceTest {

   @Test
   public void testUser() {
      SystemResource r = TestResourceFactory.getResource(SystemResource.class);
      Assert.assertEquals(r.user().getName(), "joel");
      
      Reporter.log("The logged in user is " + r.user().getName());
   }
   
   @Test
   public void testAuthorities() {
      SystemResource r = TestResourceFactory.getResource(SystemResource.class);
      Assert.assertEquals(r.user().getAuthorities().size(), 1);
   }
   
}
