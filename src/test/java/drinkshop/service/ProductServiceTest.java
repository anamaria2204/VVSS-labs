package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.AbstractRepository;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private ProductService productService;
    private Repository<Integer, Product> repo;

    @BeforeEach
    void setUp() {
        repo = new AbstractRepository<Integer, Product>() {
            @Override
            protected Integer getId(Product entity) {
                return entity.getId();
            }
        };
        productService = new ProductService(repo);
    }

    @AfterEach
    void tearDown() {
        repo = null;
        productService = null;
    }

    @Test
    void addProduct_validId_max() {
        Product p = new Product(9999, "Limonada", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        productService.addProduct(p);
        assertEquals(p, productService.findById(9999));
    }

    // ECP and BVA for id (1-9999)
    @Test
    void addProduct_invalidId_zero() {
        Product p = new Product(0, "Limonada", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    @Test
    void addProduct_invalidId_negative() {
        Product p = new Product(-1, "Limonada", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    @Test
    void addProduct_invalidId_tooHigh() {
        Product p = new Product(10000, "Limonada", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    // ECP and BVA for nume (lungime 3-100 caractere)
    @Test
    void addProduct_invalidNume_null() {
        Product p = new Product(1, null, 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    @Test
    void addProduct_invalidNume_empty() {
        Product p = new Product(1, "", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    @Test
    void addProduct_invalidNume_tooShort() {
        Product p = new Product(1, "ab", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED); // 2 caractere
        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    @Test
    void addProduct_invalidNume_tooLong() {
        String longName = "a".repeat(101); // 101 caractere
        Product p = new Product(1, longName, 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    @Test
    void addProduct_validNume_minLength() {
        Product p = new Product(1, "abc", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED); // 3 caractere
        productService.addProduct(p);
        assertEquals(p, productService.findById(1));
    }

    @Test
    void addProduct_validNume_maxLength() {
        String maxName = "a".repeat(100); // 100 caractere
        Product p = new Product(1, maxName, 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        productService.addProduct(p);
        assertEquals(p, productService.findById(1));
    }

    // ECP and BVA for pret (5.0-45.0)
    @Test
    void addProduct_invalidPret_tooLow() {
        Product p = new Product(1, "Limonada", 4.99, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    @Test
    void addProduct_invalidPret_tooHigh() {
        Product p = new Product(1, "Limonada", 45.01, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    @Test
    void addProduct_invalidPret_zero() {
        Product p = new Product(1, "Limonada", 0.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    @Test
    void addProduct_invalidPret_negative() {
        Product p = new Product(1, "Limonada", -1.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    @Test
    void addProduct_validPret_min() {
        Product p = new Product(1, "Limonada", 5.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        productService.addProduct(p);
        assertEquals(p, productService.findById(1));
    }

    @Test
    void addProduct_validPret_max() {
        Product p = new Product(1, "Limonada", 45.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        productService.addProduct(p);
        assertEquals(p, productService.findById(1));
    }

    @Test
    void findById() {
        Product p = new Product(1, "Limonada", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        productService.addProduct(p);
        assertEquals(p, productService.findById(1));
        assertNull(productService.findById(2));
    }
}