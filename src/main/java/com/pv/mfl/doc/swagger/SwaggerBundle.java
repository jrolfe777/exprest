package com.pv.mfl.doc.swagger;


import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Environment;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import scala.Option;

import com.pv.mfl.module.system.auth.AuthParam;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.jaxrs.MutableParameter;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.model.Parameter;
import com.wordnik.swagger.reader.ClassReaders;

public class SwaggerBundle extends AssetsBundle {

    public static final String PATH = "/swagger-ui";

    public SwaggerBundle() {
        super(PATH);
    }

    @Override
    public void run(Environment environment) {
        environment.jersey().register(new ApiListingResourceJSON());
        environment.jersey().register(new ApiDeclarationProvider());
        environment.jersey().register(new ResourceListingProvider());
        environment.jersey().register(new SwaggerResource());
        ScannerFactory.setScanner(new DefaultJaxrsScanner());
        ClassReaders.setReader(new FilterOutAuthParamsApiReader());
        super.run(environment);
    }
    
    
    public class FilterOutAuthParamsApiReader extends DefaultJaxrsApiReader {
       public scala.Option<Parameter> processParamAnnotations(MutableParameter mutable, Annotation[] paramAnnotations) {
          if (Arrays.asList(paramAnnotations).stream().filter(e -> e.annotationType().equals(AuthParam.class)).count() > 0) {
             return Option.empty();
          } else {
             return super.processParamAnnotations(mutable, paramAnnotations);
          } 
       }       
    }
}