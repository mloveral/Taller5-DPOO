package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.Producto;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class PedidoTest {
	
	private static final double IVA = 0.19;
	
	private Pedido pedido1;
	private Pedido pedido2;
	private Pedido pedido3;
	
	private ProductoMenu producto1;
	private ProductoMenu producto2;
	
	private Producto productoParaAgregar;
	private String factura;
	
	@BeforeEach
	void setUp ( ) throws Exception
	{
		pedido1 = new Pedido("Ivan", "La Candelaria");
		pedido2 = new Pedido("Diego", "Villa de Leyva");
		pedido3 = new Pedido("Sofia", "Cartagena");
		producto1 = new ProductoMenu("DPOO hamburguesa", 34000);
		producto2 = new ProductoMenu("DPOO papas", 7000);
	}
	
	@AfterEach
	void tearDown( ) throws Exception
	{
		pedido1 = null;
		pedido2 = null;
		pedido3 = null;
		productoParaAgregar = null;
		producto1 = null;
		producto2 = null;
		factura = null;
	}
	
	@Test
	void testGetID()
	{
		assertEquals(0, pedido1.getIdPedido(), "El id del pedido 1 no coincide con el esperado");
		assertEquals(1, pedido2.getIdPedido(), "El id del pedido 2 no coincide con el esperado");
		assertEquals(2, pedido3.getIdPedido(), "El id del pedido 3 no coincide con el esperado");
	}
	
	@Test
	void testGetNombreCliente()
	{
		assertEquals("Ivan", pedido1.getNombreCliente(), "El nombre del cliente del pedido 1 no coincide con el esperado");
		assertEquals("Diego", pedido2.getNombreCliente(), "El nombre del cliente del pedido 2 no coincide con el esperado");
		assertEquals("Sofia", pedido3.getNombreCliente(), "El nombre del cliente del pedido 3 no coincide con el esperado");
	}
	
	@Test
	void testGetPrecioTotalPedidoVacio()
	{
		assertEquals(0, pedido1.getPrecioTotalPedido(), "El precio inicial del pedido 1 no coincide con el esperado");
		assertEquals(0, pedido2.getPrecioTotalPedido(), "El precio inicial del pedido 2 no coincide con el esperado");
		assertEquals(0, pedido3.getPrecioTotalPedido(), "El precio inicial del pedido 3 no coincide con el esperado");
	}
	
	/**
	 * Prueba para probar el getPrecio() de Pedido
	 * Se aprovecha esta prueba para comprobar que se añaden productos
	 * correctamente a un pedido
	 */
	@Test
	@DisplayName("Prueba get precio y agregar producto")
	void testGetPrecioTotalPedido()
	{
			
		// Se les añaden distintos productos a los pedidos para cambiar sus precios
		ArrayList<ProductoMenu> productosCombo = new ArrayList<ProductoMenu>();
		int precio = 0;
		
		//pedido 1
		productoParaAgregar = new ProductoAjustado(producto1);
		pedido1.agregarProducto(productoParaAgregar);
		pedido1.agregarProducto(producto2);
		precio = productoParaAgregar.getPrecio() + producto2.getPrecio();
		precio += ((int) precio*IVA);
		assertEquals(precio, pedido1.getPrecioTotalPedido(), "El precio del pedido 1 no coincide con el esperado");
		
		//pedido 2
		productosCombo.add(producto1);
		productosCombo.add(producto2);
		productoParaAgregar = new Combo("Hamburguesa + papas", 0.3, productosCombo);
		pedido2.agregarProducto(productoParaAgregar);
		precio = productoParaAgregar.getPrecio();
		precio += ((int) precio*IVA);
		assertEquals(precio, pedido2.getPrecioTotalPedido(), "El precio del pedido 2 no coincide con el esperado");
		
		// pedido 3
		pedido3.agregarProducto(producto1);
		pedido3.agregarProducto(productoParaAgregar);
		pedido3.agregarProducto(producto2);
		precio = productoParaAgregar.getPrecio() + producto1.getPrecio() + producto2.getPrecio();
		precio += ((int) precio*IVA);
		assertEquals(precio, pedido3.getPrecioTotalPedido(), "El precio del pedido 3 no coincide con el esperado");
	}
	
	@Test
	void testGenerarFacturaPedido()
	{
		int precioProductos = 0;
		// Prueba con un pedido vacío
		factura = "Cliente: Ivan\n";
		factura += "Dirección: La Candelaria\n";
		factura += "----------------\n" + "----------------\n";
		factura += "Precio Neto:  " + 0 + "\n";
		factura += "IVA:          " + 0 + "\n";
		factura += "Precio Total: " + 0 + "\n";
		assertEquals(factura, pedido1.generarTextoFactura(), "La factura del pedido 1 no coincide con la esperada");
		
		//Prueba con un pedido que tiene un producto añadido
		pedido2.agregarProducto(producto1);
		precioProductos = producto1.getPrecio();
		
		factura = "Cliente: Diego\n";
		factura += "Dirección: Villa de Leyva\n";
		factura += "----------------\n";
		factura += producto1.generarTextoFactura();
		factura += "----------------\n";
		factura += "Precio Neto:  " + precioProductos + "\n";
		factura += "IVA:          " + (int)((int) precioProductos*IVA) + "\n";
		factura += "Precio Total: " + (int) (precioProductos + ((int) precioProductos*IVA)) + "\n";
		assertEquals(factura, pedido2.generarTextoFactura(), "La factura del pedido 2 no coincide con la esperada");
		
		//Prueba con un pedido con varios productos
		ArrayList<ProductoMenu> productosCombo = new ArrayList<ProductoMenu>();
		productosCombo.add(producto1);
		productosCombo.add(producto2);
		productoParaAgregar = new Combo("Hamburguesa + papas", 0.3, productosCombo);
		pedido3.agregarProducto(productoParaAgregar);
		pedido3.agregarProducto(producto2);
		pedido3.agregarProducto(producto1);
		precioProductos = productoParaAgregar.getPrecio() + producto1.getPrecio() + producto2.getPrecio();
		
		factura = "Cliente: Sofia\n";
		factura += "Dirección: Cartagena\n";
		factura += "----------------\n";
		factura += productoParaAgregar.generarTextoFactura();
		factura += producto2.generarTextoFactura();
		factura += producto1.generarTextoFactura();
		factura += "----------------\n";
		factura += "Precio Neto:  " + precioProductos + "\n";
		factura += "IVA:          " + (int)((int) precioProductos*IVA) + "\n";
		factura += "Precio Total: " + (int)(precioProductos + ((int) precioProductos*IVA)) + "\n";
		assertEquals(factura, pedido3.generarTextoFactura(), "La factura del pedido 3 no coincide con la esperada");
	}
	
	@Test
	void testPedidoEscribeArchivoCorrectamente(@TempDir Path tempDir) throws IOException
	{
		String contenidoArchivo;
		File archivo = tempDir.resolve("factura1_test.txt").toFile();
		
		// Pedido vacio
		pedido1.guardarFactura(archivo);
		
		contenidoArchivo = Files.readString(archivo.toPath());
		
		assertEquals(pedido1.generarTextoFactura(), contenidoArchivo, "El contenido del archivo no coincide con la factura");
		
		// Pedido con un producto
		pedido2.agregarProducto(producto1);
		archivo = tempDir.resolve("factura2_test.txt").toFile();
		
		pedido2.guardarFactura(archivo);
		
		contenidoArchivo = Files.readString(archivo.toPath());
		
		assertEquals(pedido2.generarTextoFactura(), contenidoArchivo, "El contenido del archivo no coincide con la factura");
		
		// Pedido con varios productos
		ArrayList<ProductoMenu> productosCombo = new ArrayList<ProductoMenu>();
		productosCombo.add(producto1);
		productosCombo.add(producto2);
		productoParaAgregar = new Combo("Hamburguesa + papas", 0.3, productosCombo);
		pedido3.agregarProducto(productoParaAgregar);
		pedido3.agregarProducto(producto2);
		pedido3.agregarProducto(producto1);
		
		archivo = tempDir.resolve("factura3_test.txt").toFile();
		
		pedido3.guardarFactura(archivo);
		
		contenidoArchivo = Files.readString(archivo.toPath());
		
		assertEquals(pedido3.generarTextoFactura(), contenidoArchivo, "El contenido del archivo no coincide con la factura");
		
		archivo.deleteOnExit();
		
	}
	
	@Test
	void testGuardarFacturaLanzaExcepcion() throws IOException 
	{
		File archivoNoValido = File.createTempFile("factura_test_readonly", ".txt");
		archivoNoValido.setReadOnly();
		
		assertThrows(FileNotFoundException.class, () -> pedido1.guardarFactura(archivoNoValido));
		assertThrows(FileNotFoundException.class, () -> pedido2.guardarFactura(archivoNoValido));
		assertThrows(FileNotFoundException.class, () -> pedido3.guardarFactura(archivoNoValido));
		
		archivoNoValido.deleteOnExit();
	}
}
