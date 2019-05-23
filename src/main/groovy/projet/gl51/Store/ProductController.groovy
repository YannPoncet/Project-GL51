package projet.gl51.Store

import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Patch
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.HttpStatus


@Controller("/store/products")
class ProductController {

  MemoryProductStorage storage = new MemoryProductStorage()

  @Get("/")
  List<Product> index() {
    // When on the index page, we show all the products in the storage
    storage.all()
  }

  @Get("/{id}")
  Product get(String id) {
    storage.getByID(id)
  }

  @Post("/")
  String save(@Body Product productToSave) {
    storage.save(productToSave)
  }

  @Put("/{id}")
  HttpStatus update(String id, @Body Product replacingProduct) {
    storage.update(id, replacingProduct)
    HttpStatus.OK
  }

  @Delete("/{id}")
  HttpStatus delete(String id) {
    storage.delete(id)
    HttpStatus.OK
  }
}
