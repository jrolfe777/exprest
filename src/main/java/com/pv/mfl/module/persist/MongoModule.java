package com.pv.mfl.module.persist;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pv.mfl.framework.module.DynamicModule;
import com.pv.mfl.framework.module.DynamicModuleConfiguration;

@DynamicModuleConfiguration("mongo.yml")
public class MongoModule extends DynamicModule {
   
   @JsonProperty @NotEmpty public String hostName = "localhost";   
   @JsonProperty @NotEmpty public String database = "mydb";
   @JsonProperty @Min(1) @Max(65535) public Integer port = 27017;
}