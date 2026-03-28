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
    void addProduct() {
        // Valid product
        Product p = new Product(1, "Limonada", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        productService.addProduct(p);
        assertEquals(p, productService.findById(1));
    }

    // ECP and BVA for id
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

    // ECP and BVA for nume
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
    void addProduct_invalidNume_blank() {
        Product p = new Product(1, "   ", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    // ECP and BVA for pret
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

    // Valid boundary
    @Test
    void addProduct_validPret_min() {
        Product p = new Product(1, "Limonada", 0.01, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        productService.addProduct(p);
        assertEquals(p, productService.findById(1));
    }

    @Test
    void addProduct_validId_min() {
        Product p = new Product(1, "Limonada", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        productService.addProduct(p);
        assertEquals(p, productService.findById(1));
    }

    @Test
    void addProduct_validNume_min() {
        Product p = new Product(1, "a", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
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