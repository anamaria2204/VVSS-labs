package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.AbstractRepository;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration Step 3 - Scenario (1): V <--- S ---> R  (top-down breadth-first)
 *
 * S (ProductService) is integrated with the REAL V (ProductValidator)
 * AND the REAL R (AbstractRepository - in-memory).
 * E (Product) is used as a real object.
 * No mocks are used.
 */
class ProductServiceIntegrationStep3Test {

    // Real in-memory repository (R)
    private Repository<Integer, Product> productRepo;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepo = new AbstractRepository<Integer, Product>() {
            @Override
            protected Integer getId(Product entity) {
                return entity.getId();
            }
        };
        productService = new ProductService(productRepo, new ProductValidator());
    }

    // --- Test 1: valid product saved end-to-end in real repo ---
    @Test
    void addProduct_validProduct_persistedInRepo() {
        Product p = new Product(1, "Espresso", 10.0, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);

        productService.addProduct(p);

        assertEquals(p, productService.findById(1));
    }

    // --- Test 2: invalid product rejected by real validator; not persisted in real repo ---
    @Test
    void addProduct_invalidProduct_notSavedInRepo() {
        Product p = new Product(0, "ab", 3.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);

        assertThrows(ValidationException.class, () -> productService.addProduct(p));

        assertNull(productService.findById(0));
        assertTrue(productService.getAllProducts().isEmpty());
    }

    // --- Test 3: multiple valid products added and retrieved ---
    @Test
    void addProduct_multipleValidProducts_allFoundInRepo() {
        Product p1 = new Product(1, "Latte", 12.0, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);
        Product p2 = new Product(2, "Limonada", 8.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);

        productService.addProduct(p1);
        productService.addProduct(p2);

        List<Product> all = productService.getAllProducts();
        assertEquals(2, all.size());
        assertTrue(all.contains(p1));
        assertTrue(all.contains(p2));
    }

    // --- Test 4: deleteProduct removes product from real repo ---
    @Test
    void deleteProduct_existingProduct_removedFromRepo() {
        Product p = new Product(3, "Cappuccino", 15.0, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);
        productService.addProduct(p);
        assertNotNull(productService.findById(3));

        productService.deleteProduct(3);

        assertNull(productService.findById(3));
    }

    // --- Test 5: filterByCategorie with real repo and real validator ---
    @Test
    void filterByCategorie_onlyMatchingProductsReturned() {
        Product coffee = new Product(1, "Americano", 13.0, CategorieBautura.CLASSIC_COFFEE, TipBautura.WATER_BASED);
        Product juice  = new Product(2, "Limonada", 8.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        productService.addProduct(coffee);
        productService.addProduct(juice);

        List<Product> result = productService.filterByCategorie(CategorieBautura.JUICE);

        assertEquals(1, result.size());
        assertEquals(juice, result.get(0));
    }
}
