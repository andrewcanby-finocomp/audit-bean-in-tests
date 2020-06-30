package audit.bean.in.tests

import grails.plugins.orm.auditable.Auditable
import grails.plugins.orm.auditable.resolvers.AuditRequestResolver
import grails.util.Holders
import org.springframework.beans.factory.NoSuchBeanDefinitionException

/**
 * Proposed modifications for Auditable to handle tests where the bean context may not be available
 */
trait AuditableModified implements Auditable {
    /**
     * ALWAYS check for the bean before using it.
     * Likley many redundant calls in most use cases as the plugin will register a candidate bean
     */
    @Override
    String getLogURI() {
        Holders.applicationContext.containsBean('auditRequestResolver') ?
                Holders.applicationContext.getBean(AuditRequestResolver)?.currentURI :
                null
    }

    /**
     * Do not check for the bean, but catch the exception and handle as if it does not exist
     */
    @Override
    String getLogCurrentUserName() {
        try {
            Holders.applicationContext.getBean(AuditRequestResolver)?.currentActor ?: 'N/A'
        } catch (NoSuchBeanDefinitionException nsbde) {
            'N/A'
        }
    }
}
