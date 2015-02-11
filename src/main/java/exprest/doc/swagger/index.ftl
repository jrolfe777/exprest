<#-- @ftlvariable name="" type="com.federecio.dropwizard.swagger.SwaggerView" -->
<!DOCTYPE html>
<html>
<head>
  <title>MediaFusion Lite - API Reference</title>
  <link href='//fonts.googleapis.com/css?family=Droid+Sans:400,700' rel='stylesheet' type='text/css'/>
  <link href='${swaggerStaticPath}/css/highlight.default.css' media='screen' rel='stylesheet' type='text/css'/>
  <link href='${swaggerStaticPath}/css/screen.css' media='screen' rel='stylesheet' type='text/css'/>
  <script type="text/javascript" src="${swaggerStaticPath}/lib/shred.bundle.js"></script>
  <script src='${swaggerStaticPath}/lib/jquery-1.8.0.min.js' type='text/javascript'></script>
  <script src='${swaggerStaticPath}/lib/jquery.slideto.min.js' type='text/javascript'></script>
  <script src='${swaggerStaticPath}/lib/jquery.wiggle.min.js' type='text/javascript'></script>
  <script src='${swaggerStaticPath}/lib/jquery.ba-bbq.min.js' type='text/javascript'></script>
  <script src='${swaggerStaticPath}/lib/handlebars-1.0.0.js' type='text/javascript'></script>
  <script src='${swaggerStaticPath}/lib/underscore-min.js' type='text/javascript'></script>
  <script src='${swaggerStaticPath}/lib/backbone-min.js' type='text/javascript'></script>
  <script src='${swaggerStaticPath}/lib/swagger.js' type='text/javascript'></script>
  <script src='${swaggerStaticPath}/swagger-ui.js' type='text/javascript'></script>
  <script src='${swaggerStaticPath}/lib/highlight.7.3.pack.js' type='text/javascript'></script>
  <script type="text/javascript">
    $(function () {
      window.swaggerUi = new SwaggerUi({
      url: "api-docs",
      dom_id: "swagger-ui-container",
      supportedSubmitMethods: ['get', 'post', 'put', 'delete'],
      onComplete: function(swaggerApi, swaggerUi){
        if(console) {
          console.log("Loaded SwaggerUI")
        }
        $('pre code').each(function(i, e) {hljs.highlightBlock(e)});
      },
      onFailure: function(data) {
        if(console) {
          console.log("Unable to Load SwaggerUI");
          console.log(data);
        }
      },
      docExpansion: "none"
    });

    window.swaggerUi.load();
  });

  </script>
</head>

    <div align="center">
      <a href="http://www.pv.com"><img src="${swaggerStaticPath}/images/cloud.png"/></a>
    </div>
    
<div id="message-bar" class="swagger-ui-wrap">
  &nbsp;
</div>

<div id="swagger-ui-container" class="swagger-ui-wrap">

</div>

</body>

</html>
