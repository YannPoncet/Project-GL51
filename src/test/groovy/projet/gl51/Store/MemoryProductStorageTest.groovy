package projet.gl51.Store

import spock.lang.Specification

import javax.validation.constraints.Null

class MemoryProductStorageTest extends Specification {

    ProductStorage store = new MemoryProductStorage()

    void "empty storage returns empty list"() {
        expect:
        store.all() == []
    }

    void "adding a product returns the product in the list"() {
        setup:
        store.save(new Product(name: "myProduct"))

        when:
        def all = store.all()

        then:
        all.size() == 1
        all.first().name == "myProduct"
    }

    void "adding a product will generate a new id"() {
        setup:
        Product myProduct = new Product(name: "myProduct")
        def uuid = store.save(myProduct)

        expect:
        myProduct.id != null
        myProduct.id == uuid
    }

    void "deleting a product will remove it from the list"() {
        setup:
        Product myProduct = new Product(name: "myProduct")
        def uuid = store.save(myProduct)

        when:
        store.delete(uuid)

        then:
        !store.all().contains(myProduct)
    }

    void "modifying a product will change it in the list"() {
        setup:
        Product myProduct = new Product(name: "myProduct")
        def uuid = store.save(myProduct)

        when:
        Product myUpdatedProduct = new Product(name: "myUpdatedProduct")
        store.update(uuid, myUpdatedProduct)

        then:
        myProduct != myUpdatedProduct
    }

    void "getting a product by its id will throw a NotExistingProductException if it doesn't exist"() {
        setup:
        def uuid = UUID.randomUUID().toString()

        when:
        store.getByID(uuid)

        then:
        thrown NotExistingProductException
    }

    void "getting a product by its id will return it if it does exist"() {
        setup:
        Product myProduct = new Product(name: "myProduct")
        def uuid = store.save(myProduct)

        when:
        def gettedProduct = store.getByID(uuid)

        then:
        myProduct == gettedProduct
    }
}
