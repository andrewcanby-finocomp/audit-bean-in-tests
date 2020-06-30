package audit.bean.in.tests

import grails.plugins.orm.auditable.Auditable

/**
 * TODO change the implements to AuditableModified and see more general changes
 */
//class Vehicle implements AuditableModified {
class Vehicle implements Auditable {
    String make
    String model
    String registration

    @Override
    Vehicle clone() {
        // don't clone the registration
        Set propertiesToClone = this.properties.keySet()
        propertiesToClone.remove('registration')

        /*TODO by removing these properties the issue can also be worked around*/
//        propertiesToClone.remove("logCurrentUserName")
//        propertiesToClone.remove("logURI")

        new Vehicle(this.properties.subMap(propertiesToClone))
    }
}