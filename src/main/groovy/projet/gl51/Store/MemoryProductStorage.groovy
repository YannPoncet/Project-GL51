package projet.gl51.Store

class MemoryProductStorage implements ProductStorage {
    List<Product> productList = []

    @Override
    String save(Product p) {
        p.id = UUID.randomUUID().toString()
        productList.add(p)

        return p.id
    }

    @Override
    void update(String id, Product p) {
        def product = getByID(id)
        def productIndex = productList.indexOf(product)

        p.id = UUID.randomUUID().toString()
        productList.add(productIndex, p)
        productList.remove(product)
    }

    @Override
    Product getByID(String id) {
        def product = productList.find { it.id == id }
        if(product == null)
        {
          throw new NotExistingProductException("The wanted product has not been found!")
        }
        return product
    }

    @Override
    void delete(String id) {
        def product = getByID(id)
        productList.remove(product)
    }

    @Override
    List<Product> all() {
        return productList
    }
}
