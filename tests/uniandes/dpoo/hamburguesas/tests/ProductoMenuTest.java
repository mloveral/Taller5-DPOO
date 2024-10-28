package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoMenuTest {
	
	private ProductoMenu producto1;
	private ProductoMenu producto2;
	private ProductoMenu producto3;
	private String factura;
	
	@BeforeEach
	void setUp( ) throws Exception {
		producto1 = new ProductoMenu("DPOO hamburguesa", 34000);
		producto2 = new ProductoMenu("DPOO papas", 7000);
		producto3 = new ProductoMenu("DPOO bebida", 6000);
		factura = null;
	}
	
	@AfterEach
    void tearDown( ) throws Exception
    {
		producto1 = null;
		producto2 = null;
		producto3 = null;
    }
	
	@Test
	void testGetNombreProductoMenu( ) 
	{
		assertEquals("DPOO hamburguesa", producto1.getNombre(), "El nombre del producto 1 no coincide con el esperado");
		
		assertEquals("DPOO papas", producto2.getNombre(), "El nombre del producto 2 no coincide con el esperado");
		
		assertEquals("DPOO bebida", producto3.getNombre(), "El nombre del producto 3 no coincide con el esperado");
	}
	
	@Test
	void testGetPrecioProductoMenu( ) 
	{
		assertEquals(34000, producto1.getPrecio(), "El precio del producto 1 no coincide con el esperado");
		
		assertEquals(7000, producto2.getPrecio(), "El precio del producto 2 no coincide con el esperado");
		
		assertEquals(6000, producto3.getPrecio(), "El precio del producto 3 no coincide con el esperado");
	}
	
	@Test
	void testGenerarFacturaProductoMenu( ) 
	{
		factura = "DPOO hamburguesa\n" + "            " + 34000 + "\n";
		assertEquals(factura, producto1.generarTextoFactura(), "La factura generada por el producto 1 no coincide con la esperada");
		
		factura = "DPOO papas\n" + "            " + 7000 + "\n";
		assertEquals(factura, producto2.generarTextoFactura(), "La factura generada por el producto 2 no coincide con la esperada");
		
		factura = "DPOO bebida\n" + "            " + 6000 + "\n";
		assertEquals(factura, producto3.generarTextoFactura(), "La factura generada por el producto 3 no coincide con la esperada");
	}
}
