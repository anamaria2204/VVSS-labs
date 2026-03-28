package drinkshop.repository.file;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileProductRepositoryTest {

    private FileProductRepository repo;
    private String testFile = "test_products.txt";

    @BeforeEach
    void setUp() {
        try {
            new File(testFile).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        repo = new FileProductRepository(testFile);
    }

    @AfterEach
    void tearDown() {
        new File(testFile).delete();
    }

    @Test
    void getId() {
        Product p = new Product(1, "Limonada", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        assertEquals(1, repo.getId(p));
    }

    @Test
    void extractEntity_valid() {
        String line = "1,Limonada,10.0,JUICE,WATER_BASED";
        Product p = repo.extractEntity(line);
        assertNotNull(p);
        assertEquals(1, p.getId());
        assertEquals("Limonada", p.getNume());
        assertEquals(10.0, p.getPret());
        assertEquals(CategorieBautura.JUICE, p.getCategorie());
        assertEquals(TipBautura.WATER_BASED, p.getTip());
    }

    @Test
    void extractEntity_invalid_id() {
        String line = "abc,Limonada,10.0,JUICE,WATER_BASED";
        Product p = repo.extractEntity(line);
        assertNull(p);
    }

    @Test
    void extractEntity_invalid_price() {
        String line = "1,Limonada,abc,JUICE,WATER_BASED";
        Product p = repo.extractEntity(line);
        assertNull(p);
    }

    @Test
    void extractEntity_invalid_categorie() {
        String line = "1,Limonada,10.0,INVALID,WATER_BASED";
        Product p = repo.extractEntity(line);
        assertNull(p);
    }

    @Test
    void extractEntity_invalid_tip() {
        String line = "1,Limonada,10.0,JUICE,INVALID";
        Product p = repo.extractEntity(line);
        assertNull(p);
    }

    @Test
    void extractEntity_too_few_fields() {
        String line = "1,Limonada,10.0,JUICE";
        Product p = repo.extractEntity(line);
        assertNull(p);
    }

    @Test
    void createEntityAsString() {
        Product p = new Product(1, "Limonada", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        String expected = "1,Limonada,10.0,JUICE,WATER_BASED";
        assertEquals(expected, repo.createEntityAsString(p));
    }
}