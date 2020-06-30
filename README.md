# Summary

When using the `audit-logging` plugin for Grails 4 a `NoSuchBeanDefinitionException` 
is thrown when accessing a domain `properties` collection in unit tests.

# Details

Domain unit tests are failing due to a missing bean, which should not be required.

This is required for two methods, `getLogURI` and `getLogCurrentUserName` as they 
require the `AuditRequestResolver` bean.  

The error is:
```
org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'grails.plugins.orm.auditable.resolvers.AuditRequestResolver' available

	at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBean(DefaultListableBeanFactory.java:348)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBean(DefaultListableBeanFactory.java:339)
	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1123)
	at grails.plugins.orm.auditable.Auditable$Trait$Helper.getLogCurrentUserName(Auditable.groovy:111)
	at groovy.lang.MetaBeanProperty.getProperty(MetaBeanProperty.java:59)
	at grails.beans.util.LazyMetaPropertyMap.get(LazyMetaPropertyMap.java:126)
	at audit.bean.in.tests.Vehicle.clone(Vehicle.groovy:16)
	at audit.bean.in.tests.VehicleSpec.Test cloning method(VehicleSpec.groovy:26)
```

# Reproducing
1. `./gradlew test` this project

# Workaround

## Variant 1
It is possible to work around the bean requirement by defining the bean per test class:
```groovy
    @Override
    Closure doWithSpring() {{->
            auditRequestResolver(DefaultAuditRequestResolver)
    }}
```
See commented out code in [VehicleSpec](src/test/groovy/audit/bean/in/tests/VehicleSpec.groovy)

## Variant 2
Explicitly removing the properties from the clone candidates also works around the issue as it does not call the methods 
in question
```groovy
    propertiesToClone.remove("logCurrentUserName")
    propertiesToClone.remove("logURI")
```

See commented out code in [Vehicle](grails-app/domain/audit/bean/in/tests/Vehicle.groovy#21)

## Running workarounds
Running the workarounds:
1. Uncomment the `doWithSpring` block in the tests
2. `./gradlew test` for a green run

# Proposed solution
By modifying the methods above the exception can be either caught or prevented.
See both proposed options in [AuditableModified](src/main/groovy/audit/bean/in/tests/AuditableModified.groovy)

Running the fixes:
1. Comment out `doWithSpring` changes from the above workaround
2. Change the trait used for `Vehicle` (change the commented out line)
3. `./gradlew test` for a green run




