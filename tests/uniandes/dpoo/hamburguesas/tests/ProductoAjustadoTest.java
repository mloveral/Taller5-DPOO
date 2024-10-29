package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoAjustadoTest {
	
	private ProductoMenu producto1;
	private ProductoMenu producto2;
	private ProductoMenu producto3;
	
	private ProductoAjustado productoAjustado1;
	private ProductoAjustado productoAjustado2;
	private ProductoAjustado productoAjustado3;
	
	private String factura;
	
	@BeforeEach
	void setUp( ) throws Exception
	{
		producto1 = new ProductoMenu("DPOO hamburguesa", 34000);
		producto2 = new ProductoMenu("DPOO papas", 7000);
		producto3 = new ProductoMenu("DPOO bebida", 6000);
		
		productoAjustado1 = new ProductoAjustado(producto1);
		productoAjustado2 = new ProductoAjustado(producto2);
		productoAjustado3 = new ProductoAjustado(producto3);
		
		factura = null;
	}
	
	@AfterEach
	void tearDown( ) throws Exception 
	{
		producto1 = null;
		producto2 = null;
		producto3 = null;
		
		productoAjustado1 = null;
		productoAjustado2 = null;
		productoAjustado3 = null;
	}
	
	@Test
	void testGetNombreProductoAjustado( )
	{
		assertEquals(producto1.getNombre(), productoAjustado1.getNombre(), "El nombre del producto ajustado 1 no coincide con el esperado");
		
		assertEquals(producto2.getNombre(), productoAjustado2.getNombre(), "El nombre del producto ajustado 2 no coincide con el esperado");
		
		assertEquals(producto3.getNombre(), productoAjustado3.getNombre(), "El nombre del producto ajustado 3 no coincide con el esperado");
	}
	
	@Test
	void testGetPrecioProductoAjustado( )
	{
		assertEquals(producto1.getPrecio(), productoAjustado1.getPrecio(), "El precio del producto ajustado 1 no coincide con el esperado");
		
		assertEquals(producto2.getPrecio(), productoAjustado2.getPrecio(), "El precio del producto ajustado 2 no coincide con el esperado");
		
		assertEquals(producto3.getPrecio(), productoAjustado3.getPrecio(), "El precio del producto ajustado 3 no coincide con el esperado");
	}
	
	
	@Test
	void testGenerarFacturaProductoAjustadoSinModificaciones( )
	{
		factura = producto1.getNombre() + "            " + producto1.getPrecio() + "\n";
		assertEquals(factura, productoAjustado1.generarTextoFactura(), "La factura generada por el producto ajustado 1 no coincide con la esperada");
		
		factura = producto2.getNombre() + "            " + producto2.getPrecio() + "\n";
		assertEquals(factura, productoAjustado2.generarTextoFactura(), "La factura generada por el producto ajustado 2 no coincide con la esperada");
		
		factura = producto3.getNombre() + "            " + producto3.getPrecio() + "\n";
		assertEquals(factura, productoAjustado3.generarTextoFactura(), "La factura generada por el producto ajustado 3 no coincide con la esperada");
	}
	
	@Test
	void testGenerarFacturaProductoAjustadoModificado( )
	{
		//Agregando un ingrediente
		factura = producto1.getNombre();
		factura += "    +tomate";
		factura += "                1000";
		factura += "            " + (producto1.getPrecio()+1000) + "\n";
		Ingrediente ing = new Ingrediente("tomate", 1000);
		productoAjustado1.agregarIngrediente(ing);
		assertEquals(factura, productoAjustado1.generarTextoFactura(), "La factura generada por el producto ajustado 1 no coincide con la esperada");
		
		//Eliminando un ingrediente
		factura = producto2.getNombre();
		factura += "    -tomate";
		factura += "            " + producto2.getPrecio() + "\n";
		productoAjustado2.eliminarIngrediente(ing);
		assertEquals(factura, productoAjustado2.generarTextoFactura(), "La factura generada por el producto ajustado 2 no coincide con la esperada");
		
		//Añadiendo y eliminando un ingrediente 
		factura = producto3.getNombre();
		factura += "    +tomate";
		factura += "                1000";
		factura += "    -queso";
		factura += "            " + (producto3.getPrecio()+1000) + "\n";
		productoAjustado3.agregarIngrediente(ing);
		ing = new Ingrediente("queso", 1000);
		productoAjustado3.eliminarIngrediente(ing);
		assertEquals(factura, productoAjustado3.generarTextoFactura(), "La factura generada por el producto ajustado 3 no coincide con la esperada");
	}
}
