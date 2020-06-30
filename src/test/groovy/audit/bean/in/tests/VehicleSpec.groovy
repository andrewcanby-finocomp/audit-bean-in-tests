package audit.bean.in.tests

import grails.plugins.orm.auditable.resolvers.DefaultAuditRequestResolver
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

/**
 * Simple test to expose bean issue when using grails-audit-plugin
 */
class VehicleSpec extends Specification implements DomainUnitTest<Vehicle> {

    // TODO add this block to demonstrate a workaround
//    /**
//     * Adding a bean that satisfies the `auditRequestResolver` requirement
//     * is an option to fix the tests
//     */
//    @Override
//    Closure doWithSpring() {{->
//            auditRequestResolver(DefaultAuditRequestResolver)
//    }}

    def "Test cloning method"() {
        given: "An existing vehicle"
        Vehicle vehicle = new Vehicle(make: "BMW", model: "X5", registration: "ABC123").save(failOnError: true)

        when: "Cloning"
        Vehicle clone = vehicle.clone()

        then: "It creates a new instance"
        clone != vehicle
        clone.make == vehicle.make
        clone.model == vehicle.model
        clone.registration == null
    }

    def "Test getting properties for Vehicle"() {
        given: "An existing vehicle"
        Vehicle vehicle = new Vehicle(make: "BMW", model: "X5", registration: "ABC123").save(failOnError: true)

        when: "Getting the properties"
        Map<?, ?> properties = vehicle.properties

        then: "No exception is thrown"
        noExceptionThrown()
    }
}
