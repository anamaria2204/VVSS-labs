package drinkshop.service.validator;

import drinkshop.domain.Product;

public class ProductValidator implements Validator<Product> {

    @Override
    public void validate(Product product) {

        String errors = "";

        if (product.getId() < 1 || product.getId() > 9999)
            errors += "ID invalid! Trebuie sa fie intre 1 si 9999.\n";

        if (product.getNume() == null || product.getNume().length() < 3 || product.getNume().length() > 100)
            errors += "Numele nu poate fi gol si trebuie sa aiba lungimea intre 3 si 100 caractere!\n";

        if (product.getPret() < 5.0 || product.getPret() > 45.0)
            errors += "Pret invalid! Trebuie sa fie intre 5.0 si 45.0.\n";

        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
