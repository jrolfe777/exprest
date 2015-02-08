package com.pv.mfl.doc.swagger;

import io.dropwizard.views.View;

import com.google.common.base.Charsets;

public class SwaggerView extends View {

    public SwaggerView() {
       super("index.ftl", Charsets.UTF_8);
    }
    
   public String getSwaggerStaticPath() {
       return SwaggerBundle.PATH;
   }
}
