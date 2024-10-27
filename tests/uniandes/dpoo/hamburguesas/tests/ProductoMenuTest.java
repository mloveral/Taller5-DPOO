package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoMenuTest {
	
	private ProductoMenu producto1;
	private String factura;
	
	@BeforeEach
	void setUp( ) throws Exception {
		producto1 = new ProductoMenu("DPOO hamburguesa", 34000);
		factura = null;
	}
	
	@AfterEach
    void tearDown( ) throws Exception
    {
		producto1 = null;
    }
	
	@Test
	void testGetNombre( ) {
		assertEquals("DPOO hamburguesa", producto1.getNombre(), "El nombre del producto no coincide con el esperado");
	}
	
	@Test
	void testGetPrecio( ) {
		assertEquals(34000, producto1.getPrecio(), "El precio del producto no coincide con el esperado");
	}
	
	@Test
	@DisplayName("Generar el texto de la factura del producto")
	void testGenerarFactura( ) {
		factura = "DPOO hamburguesa\n" + "            " + 34000 + "\n";
		assertEquals(factura, producto1.generarTextoFactura(), "La factura generada por el producto no coincide con la esperada");
	}
}
