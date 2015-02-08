package com.pv.mfl.test.unit.resource;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import test.com.pv.mfl.module.system.resources.SystemResource;

import com.pv.mfl.test.core.TestResourceFactory;

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
