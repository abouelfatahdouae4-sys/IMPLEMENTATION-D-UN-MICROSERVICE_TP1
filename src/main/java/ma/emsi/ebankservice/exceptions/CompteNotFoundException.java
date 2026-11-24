package ma.emsi.ebankservice.exceptions;

public class CompteNotFoundException extends RuntimeException {
    public CompteNotFoundException(String id) {
        super(String.format("Compte %s introuvable", id));
    }
}
