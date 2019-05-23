package projet.gl51.Store

import io.micronaut.context.ApplicationContext
import io.micronaut.core.type.Argument
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class ProductControllerTest extends Specification {
  @Shared @AutoCleanup EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)
  @Shared @AutoCleanup RxHttpClient client = embeddedServer.applicationContext.createBean(RxHttpClient, embeddedServer.getURL())

  Product testProduct = new Product(name: "product", description: "desc", price: 0.0, idealTemperature: 0.0)

  void "when on the index page, we have an empty product list"() {
      given:
      def productList = client.toBlocking().retrieve(HttpRequest.GET("/store/products"), Argument.listOf(Product).type)

      expect:
      productList == []
  }

  void "when adding a new product, it returns the id and it is added into the product list" () {
      given:
      def id = client.toBlocking().retrieve(HttpRequest.POST("/store/products", testProduct))
      def productList = client.toBlocking().retrieve(HttpRequest.GET("/store/products"), Argument.listOf(Product).type)

      expect:
      id != ""
      productList.size() == 1
  }

  void "when on a product's id page, we get that product" () {
      setup:
      def id = client.toBlocking().retrieve(HttpRequest.POST("/store/products", testProduct))

      when:
      def retrievedProduct = client.toBlocking().retrieve(HttpRequest.GET("/store/products/"+id), Argument.of(Product).type)

      then:
      retrievedProduct.name == testProduct.name
      retrievedProduct.description == testProduct.description
      retrievedProduct.price == testProduct.price
      retrievedProduct.idealTemperature == testProduct.idealTemperature
  }

  void "when updating a product, the id of the product must have changed" () {
      setup:
      def id = client.toBlocking().retrieve(HttpRequest.POST("/store/products", testProduct))
      Product newProduct = new Product(name: "product2", description: "desc2", price: 0.1, idealTemperature: 0.1)

      when:
      client.toBlocking().retrieve(HttpRequest.PUT("/store/products/"+id, newProduct), Argument.of(HttpStatus).type)
      def productList = client.toBlocking().retrieve(HttpRequest.GET("/store/products"), Argument.listOf(Product).type)

      then:
      productList.last().name == newProduct.name
      productList.last().description == newProduct.description
      productList.last().price == newProduct.price
      productList.last().idealTemperature == newProduct.idealTemperature
  }

  void "when deleting a product, the list is empty" () {
    setup:
    def id = client.toBlocking().retrieve(HttpRequest.POST("/store/products", testProduct))
    def productList = client.toBlocking().retrieve(HttpRequest.GET("/store/products"), Argument.listOf(Product).type)
    def size = productList.size()

    when:
    client.toBlocking().retrieve(HttpRequest.DELETE("/store/products/"+id), Argument.of(HttpStatus).type)
    productList = client.toBlocking().retrieve(HttpRequest.GET("/store/products"), Argument.listOf(Product).type)

    then:
    size == productList.size()+1
  }
}
