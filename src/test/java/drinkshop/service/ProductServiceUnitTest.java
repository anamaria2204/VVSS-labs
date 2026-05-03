package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProductService (S) in isolation.
 * Scenario 1: V <--- S ---> R  (breadth-first, top-down integration)
 * Both ProductValidator (V) and Repository<Integer, Product> (R) are mocked.
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceUnitTest {

    @Mock
    private Repository<Integer, Product> productRepo;

    @Mock
    private ProductValidator validator;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepo, validator);
    }

    // --- Test 1: addProduct with a valid product ---
    // Verifies that validator.validate() and productRepo.save() are both called exactly once.
    @Test
    void addProduct_valid_callsValidateAndSave() {
        Product p = new Product(1, "Espresso", 10.0, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);
        doNothing().when(validator).validate(p);
        when(productRepo.save(p)).thenReturn(p);

        productService.addProduct(p);

        verify(validator, times(1)).validate(p);
        verify(productRepo, times(1)).save(p);
    }

    // --- Test 2: addProduct with an invalid product (validator throws) ---
    // Verifies that the exception propagates and productRepo.save() is never called.
    @Test
    void addProduct_invalid_validatorThrows_saveNotCalled() {
        Product p = new Product(0, "", -1.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        doThrow(new ValidationException("Product is invalid")).when(validator).validate(p);

        assertThrows(ValidationException.class, () -> productService.addProduct(p));

        verify(validator, times(1)).validate(p);
        verify(productRepo, never()).save(any());
    }

    // --- Test 3: getAllProducts delegates to repository ---
    @Test
    void getAllProducts_returnsRepoList() {
        Product p1 = new Product(1, "Latte", 12.0, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);
        Product p2 = new Product(2, "Limonada", 8.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        when(productRepo.findAll()).thenReturn(List.of(p1, p2));

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertTrue(result.contains(p1));
        assertTrue(result.contains(p2));
        verify(productRepo, times(1)).findAll();
    }

    // --- Test 4: deleteProduct delegates to repository ---
    @Test
    void deleteProduct_callsRepoDelete() {
        when(productRepo.delete(5)).thenReturn(null);

        productService.deleteProduct(5);

        verify(productRepo, times(1)).delete(5);
    }
}
