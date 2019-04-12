package projet.gl51

import io.micronaut.context.ApplicationContext
import io.micronaut.core.type.Argument
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class StudentControllerSpec extends Specification {
    @Shared @AutoCleanup EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)
    @Shared @AutoCleanup RxHttpClient client = embeddedServer.applicationContext.createBean(RxHttpClient, embeddedServer.getURL())

    void "test index"() {
        given:
        def response = client.toBlocking().exchange("/student", Argument.listOf(Student).type)

        expect:
        response.status == HttpStatus.OK
        response.body()[0].firstName == 'Boris'
        response.body()[0].lastName == 'Brogle'
        response.body()[1].firstName == 'Yann'
        response.body()[1].lastName == 'Poncet'
        response.body()[2].firstName == 'Rodolphe'
        response.body()[2].lastName == 'Ponthon'
    }
}