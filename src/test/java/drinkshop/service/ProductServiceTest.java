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

import java.util.List;

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

    private Product validProduct(int id, String nume, double pret, CategorieBautura categorie, TipBautura tip) {
        return new Product(id, nume, pret, categorie, tip);
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

    // ---------------- WHITE-BOX for filterByCategorie ----------------
    // SC/DC/CC + APC path P1: allProducts.isEmpty() == true
    @Test
    void filterByCategorie_emptyRepository_returnsEmptyList() {
        List<Product> result = productService.filterByCategorie(CategorieBautura.JUICE);

        assertTrue(result.isEmpty());
    }

    // SC/DC/CC + APC path P2: allProducts.isEmpty() == false, categorie == ALL == true
    @Test
    void filterByCategorie_allCategory_returnsAllProducts() {
        Product p1 = validProduct(1, "Espresso", 10.0, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);
        Product p2 = validProduct(2, "Suc Portocale", 12.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        productService.addProduct(p1);
        productService.addProduct(p2);

        List<Product> result = productService.filterByCategorie(CategorieBautura.ALL);

        assertEquals(2, result.size());
        assertTrue(result.contains(p1));
        assertTrue(result.contains(p2));
    }

    // LC (1 iteratie) + APC path P3: loop intra o data, conditia interna true
    @Test
    void filterByCategorie_oneElement_matchingCategory_returnsThatElement() {
        Product p1 = validProduct(1, "Limonada", 11.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        productService.addProduct(p1);

        List<Product> result = productService.filterByCategorie(CategorieBautura.JUICE);

        assertEquals(1, result.size());
        assertEquals(p1, result.get(0));
    }

    // LC (>1 iteratii) + APC path P4/P5: conditia interna true si false in acelasi test
    @Test
    void filterByCategorie_multipleElements_mixedCategories_filtersOnlyRequestedCategory() {
        Product p1 = validProduct(1, "Americano", 13.0, CategorieBautura.CLASSIC_COFFEE, TipBautura.WATER_BASED);
        Product p2 = validProduct(2, "Smoothie Capsuni", 20.0, CategorieBautura.SMOOTHIE, TipBautura.PLANT_BASED);
        Product p3 = validProduct(3, "Ceai Verde", 9.0, CategorieBautura.TEA, TipBautura.WATER_BASED);
        productService.addProduct(p1);
        productService.addProduct(p2);
        productService.addProduct(p3);

        List<Product> result = productService.filterByCategorie(CategorieBautura.SMOOTHIE);

        assertEquals(1, result.size());
        assertTrue(result.contains(p2));
        assertFalse(result.contains(p1));
        assertFalse(result.contains(p3));
    }

    // DC/CC: conditia interna false pe toate iteratiile
    @Test
    void filterByCategorie_noMatchingCategory_returnsEmptyList() {
        Product p1 = validProduct(1, "Cappuccino", 15.0, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);
        Product p2 = validProduct(2, "Ceai", 8.0, CategorieBautura.TEA, TipBautura.WATER_BASED);
        productService.addProduct(p1);
        productService.addProduct(p2);

        List<Product> result = productService.filterByCategorie(CategorieBautura.JUICE);

        assertTrue(result.isEmpty());
    }
}