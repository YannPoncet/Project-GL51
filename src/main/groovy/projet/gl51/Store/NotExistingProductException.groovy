package projet.gl51.Store

class NotExistingProductException extends Exception {
    NotExistingProductException(){
        super()
    }

    NotExistingProductException(String message){
        super(message)
    }
}
