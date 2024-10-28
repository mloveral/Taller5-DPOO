package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ComboTest {
	
	private ProductoMenu producto1;
	private ProductoMenu producto2;
	private ProductoMenu producto3;
	
	private Combo combo1;
	private Combo combo2;
	private Combo combo3;
	
	ArrayList<ProductoMenu> productosCombo1 = new ArrayList<ProductoMenu>();;
	ArrayList<ProductoMenu> productosCombo2 = new ArrayList<ProductoMenu>();;
	ArrayList<ProductoMenu> productosCombo3 = new ArrayList<ProductoMenu>();;
	
	int precio1;
	int precio2;
	int precio3;
	String factura;
	
	@BeforeEach
	void setUp( ) throws Exception
	{		
		// Crear los productos de los combos
		producto1 = new ProductoMenu("DPOO hamburguesa", 34000);
		producto2 = new ProductoMenu("DPOO papas", 7000);
		producto3 = new ProductoMenu("DPOO bebida", 6000);
		
		// Crear los combos con la lista de productos
		productosCombo1.add(producto1);
		productosCombo1.add(producto3);
		combo1 = new Combo("Hamburguesa + bebida", 0.2, productosCombo1);
		
		productosCombo2.add(producto1);
		productosCombo2.add(producto2);
		combo2 = new Combo("Hamburguesa + papas", 0.3, productosCombo2);
		
		productosCombo3.add(producto1);
		productosCombo3.add(producto2);
		productosCombo3.add(producto3);
		combo3 = new Combo("Happy DPOO meal", 0.5, productosCombo3);
		
		//Calcular los precios de los combos
		precio1 = (int) ((int) (producto1.getPrecio() + producto3.getPrecio())*0.2);
		precio2 = (int) ((int) (producto1.getPrecio() + producto2.getPrecio())*0.3);
		precio3 = (int) ((int) (producto1.getPrecio() + producto2.getPrecio() + producto3.getPrecio())*0.5);
		
		factura = "";
	}
	
	@AfterEach
	void tearDown( )
	{
		productosCombo1.clear();
		productosCombo2.clear();
		productosCombo3.clear();
		precio1 = 0; precio2 = 0; precio3 = 0;
	}
	
	@Test
	void testGetNombreCombo()
	{
		assertEquals("Hamburguesa + bebida", combo1.getNombre(), "El nombre del combo 1 no coincide con el esperado");
		assertEquals("Hamburguesa + papas", combo2.getNombre(), "El nombre del combo 2 no coincide con el esperado");
		assertEquals("Happy DPOO meal", combo3.getNombre(), "El nombre del combo 3 no coincide con el esperado");
	}
	
	@Test
	void testGetPrecioCombo()
	{
		assertEquals(precio1, combo1.getPrecio(), "El precio del combo 1 no coincide con el esperado");
		
		assertEquals(precio2, combo2.getPrecio(), "El precio del combo 2 no coincide con el esperado");
		
		assertEquals(precio3, combo3.getPrecio(), "El precio del combo 3 no coincide con el esperado");
	}
	
	@Test
	void testGenerarFacturaCombo()
	{
		factura = "Combo Hamburguesa + bebida\n";
		factura += " Descuento: " + 0.2 + "\n";
		factura += "            " + precio1 + "\n";
		assertEquals(factura, combo1.generarTextoFactura(), "La factura generada por el combo 1 no coincide con la esperada");
		
		factura = "Combo Hamburguesa + papas\n";
		factura += " Descuento: " + 0.3 + "\n";
		factura += "            " + precio2 + "\n";
		assertEquals(factura, combo2.generarTextoFactura(), "La factura generada por el combo 2 no coincide con la esperada");
		
		factura = "Combo Happy DPOO meal\n";
		factura += " Descuento: " + 0.5 + "\n";
		factura += "            " + precio3 + "\n";
		assertEquals(factura, combo3.generarTextoFactura(), "La factura generada por el combo 3 no coincide con la esperada");
	}
}
