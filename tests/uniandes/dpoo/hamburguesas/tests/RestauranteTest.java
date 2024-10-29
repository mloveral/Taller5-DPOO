package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import uniandes.dpoo.hamburguesas.excepciones.HamburguesaException;
import uniandes.dpoo.hamburguesas.excepciones.IngredienteRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoFaltanteException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

public class RestauranteTest {
	
	Restaurante rest1;
	Pedido mockPedido1;
	Pedido mockPedido2;
	Pedido pedidoEnCurso;
	String archivoFactura1;
	String archivoFactura2;
	private static final String CARPETA_FACTURAS = "data/facturas/";
	private static final String PREFIJO_FACTURAS = "factura_";
	
	//Para la prueba de cargar Información
	ArrayList<ProductoMenu> menuEsperado;
	ArrayList<Combo> combosEsperados;
	ArrayList<Ingrediente> ingredientesEsperados;
	
	
	@BeforeEach
	void setUp( ) throws Exception
	{
		rest1 = new Restaurante();
		mockPedido1 = new Pedido("Juan", "Uniandes");
		mockPedido2 = new Pedido("Juana", "Javeriana");
		pedidoEnCurso = null;
		archivoFactura1 = null;
		archivoFactura2 = null;
	}
	
	@AfterEach
	void tearDown( ) throws Exception
	{
		rest1 = null;
		menuEsperado = null;
		combosEsperados = null;
		ingredientesEsperados = null;
	}
	
	@Test
	@DisplayName("Probar la excepción si ya hay un pedido en curso")
	void testIniciarPedidoYaHayUnPedidoEnCursoException( ) throws YaHayUnPedidoEnCursoException 
	{
		rest1.iniciarPedido("Juan", "Uniandes");
		assertThrows(YaHayUnPedidoEnCursoException.class, ()-> rest1.iniciarPedido("Carlos", "Cogua"));
	}
	
	@Test
	void testGetPedidoEnCursoVacio( )
	{
		//Mirar cuando no hay ningun pedido en curso
		assertEquals(null, rest1.getPedidoEnCurso());
	}
	
	@Test
	void testGetPedidoEnCurso( ) throws YaHayUnPedidoEnCursoException
	{
		rest1.iniciarPedido("Juan", "Uniandes");
		
		//Se verifica que los nombres coincidan
		assertEquals(mockPedido1.getNombreCliente(), rest1.getPedidoEnCurso().getNombreCliente());
		
		//Se verifica que (tenientdo en cuenta que los pedidos no tienen productos)
		//que coincidan en precio total
		assertEquals(mockPedido1.getPrecioTotalPedido(), rest1.getPedidoEnCurso().getPrecioTotalPedido());
	}
	
	@Test
	void testCerrarYGuardarPedido() throws YaHayUnPedidoEnCursoException, IOException, NoHayPedidoEnCursoException
	{
		String contenidoArchivo;
		int idPedido;
		
		rest1.iniciarPedido("Juan", "Uniandes");
		pedidoEnCurso = rest1.getPedidoEnCurso();
		idPedido =  rest1.getPedidoEnCurso().getIdPedido();
		
		//Guardar y cerrar pedido
		rest1.cerrarYGuardarPedido();
		
		contenidoArchivo = Files.readString(Paths.get(CARPETA_FACTURAS+PREFIJO_FACTURAS+idPedido+".txt"));
		
		//Comparandolo con el pedido dentro del restaurante:
		assertEquals(pedidoEnCurso.generarTextoFactura(), contenidoArchivo, "La factura del pedido guardado no coincide con la esperada");
		
		//Comparandolo con un pedido identico, pero externo:
		assertEquals(mockPedido1.generarTextoFactura(), contenidoArchivo, "La factura del pedido guardado no coincide con la esperada");
		
	}
	
	@Test
	void testCerrarYGuardarPedidoNoHayPedidoEnCursoException()
	{
		assertThrows(NoHayPedidoEnCursoException.class, ()->rest1.cerrarYGuardarPedido());
	}
	
	@Test
	void testGetPedidos() throws YaHayUnPedidoEnCursoException, NoHayPedidoEnCursoException, IOException
	{
		ArrayList<Pedido> pedidosEsperados = new ArrayList<Pedido>();
		
		
		//Prueba sin haber añadido ningun pedido
		assertEquals(pedidosEsperados, rest1.getPedidos(), "Los pedidos cerrados obtenidos no coinciden con los esperados");
		
		//Prueba añadiendo un pedido, pero no cerrandolo
		rest1.iniciarPedido("Juan", "Uniandes");
		assertEquals(pedidosEsperados, rest1.getPedidos(), "Los pedidos cerrados obtenidos no coinciden con los esperados");
		
		//Prueba cerrando un pedido
		pedidoEnCurso = rest1.getPedidoEnCurso();
		pedidosEsperados.add(pedidoEnCurso);
		rest1.cerrarYGuardarPedido();
		
		//Compara el tamaño de las listas de pedidos
		assertEquals(pedidosEsperados.size(), rest1.getPedidos().size(), "Los pedidos cerrados obtenidos no coinciden con los esperados");
		
		//Compara cada elemento de la lista de pedidos
		for (int i = 0; i < pedidosEsperados.size(); i++)
		{
			assertEquals(pedidosEsperados.get(i), rest1.getPedidos().get(i), "Los pedidos cerrados obtenidos no coinciden con los esperados");
		}
		
		//Prueba cerrando más de un pedido
		rest1.iniciarPedido("Juana", "Javeriana");
		pedidoEnCurso = rest1.getPedidoEnCurso();
		pedidosEsperados.add(pedidoEnCurso);
		rest1.cerrarYGuardarPedido();
		assertEquals(pedidosEsperados, rest1.getPedidos(), "Los pedidos cerrados obtenidos no coinciden con los esperados");
	}
	
	@Test
	void testGetMenuBaseVacio()
	{
		 menuEsperado = new ArrayList<ProductoMenu>( );
		 assertEquals(menuEsperado, rest1.getMenuBase(), "El menu base obtenido no coinciden con el esperado");
	}
	
	@Test
	void testGetMenuCombosVacio()
	{
		 combosEsperados = new ArrayList<Combo>( );
		 assertEquals(combosEsperados, rest1.getMenuCombos(), "El menu base obtenido no coinciden con el esperado");
	}
	
	@Test
	void testGetIngredientesVacio()
	{
		 ingredientesEsperados = new ArrayList<Ingrediente>( );
		 assertEquals(ingredientesEsperados, rest1.getIngredientes(), "El menu base obtenido no coinciden con el esperado");
	}
	
	/**
	 * Set up específico para la prueba de cargar información desde los archivos
	 * de texto.
	 * Crea listas con los objetos esperados que tiene que cargar desde el archivo
	 * para luego hacer las assertions en los test de cargar información
	 */
	private void setUpCargarInfo()
	{
		menuEsperado = new ArrayList<ProductoMenu>( );
		combosEsperados = new ArrayList<Combo>( );
		ingredientesEsperados = new ArrayList<Ingrediente>( );
		
		//Crea cada item y lo añade a su lista respectiva
		ArrayList<ProductoMenu> productosCombo1 = new ArrayList<ProductoMenu>();
		ArrayList<ProductoMenu> productosCombo2 = new ArrayList<ProductoMenu>();
		Combo combo1;
		Combo combo2;
		ProductoMenu producto;
		Ingrediente ingrediente;
		
		//Productos del menu
		producto = new ProductoMenu("corral", 14000);
		productosCombo1.add(producto);
		menuEsperado.add(producto);
		
		producto = new ProductoMenu("corral queso", 16000);
		productosCombo2.add(producto);
		menuEsperado.add(producto);
		
		producto = new ProductoMenu("papas medianas", 5500);
		productosCombo1.add(producto);
		productosCombo2.add(producto);
		menuEsperado.add(producto);
		
		producto = new ProductoMenu("gaseosa", 5000);
		productosCombo1.add(producto);
		productosCombo2.add(producto);
		menuEsperado.add(producto);
		
		//Combos
		combo1 = new Combo("combo corral", 0.1, productosCombo1);
		combo2 = new Combo("combo corral queso", 0.1, productosCombo2);
		combosEsperados.add(combo1);
		combosEsperados.add(combo2);
		
		//Ingredientes
		ingrediente = new Ingrediente("lechuga", 1000);
		ingredientesEsperados.add(ingrediente);
		
		ingrediente = new Ingrediente("tomate", 1000);
		ingredientesEsperados.add(ingrediente);
		
		ingrediente = new Ingrediente("cebolla", 1000);
		ingredientesEsperados.add(ingrediente);
		
		ingrediente = new Ingrediente("queso mozzarella", 2500);
		ingredientesEsperados.add(ingrediente);
		
		ingrediente = new Ingrediente("huevo", 2500);
		ingredientesEsperados.add(ingrediente);
	}
	
	@Test
	void testCargarInformacionTotalRestaurante() throws NumberFormatException, HamburguesaException, IOException
	{
		setUpCargarInfo();
		
		File archivoIngredientes = new File("tests/uniandes/dpoo/hamburguesas/tests/datosPrueba/ingredientes_test.txt");
		File archivoMenu = new File("tests/uniandes/dpoo/hamburguesas/tests/datosPrueba/menu_test.txt");
		File archivoCombos = new File("tests/uniandes/dpoo/hamburguesas/tests/datosPrueba/combos_test.txt");
		
		rest1.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoCombos);
		
		ArrayList<ProductoMenu> menu = rest1.getMenuBase();
		ArrayList<Combo> combos = rest1.getMenuCombos();
		ArrayList<Ingrediente> ingredientes = rest1.getIngredientes();
		
		//Para ingredientes
		
		// Para mirar si el tamaño de la lista esperada es el mismo que el de la obtenida
		assertEquals(ingredientes.size(), ingredientesEsperados.size(), "La cantidad de ingredientes cargados no coincide con el esperado");
		
		Ingrediente i1;
		Ingrediente i2;
		for (int i = 0; i < ingredientes.size(); i++)
		{
			i1 = ingredientes.get(i);
			i2 = ingredientesEsperados.get(i);
			assertEquals(i2.getNombre(), i1.getNombre(), "Los ingredientes cargados no coinciden con los esperados");
			assertEquals(i2.getCostoAdicional(), i1.getCostoAdicional(), "Los ingredientes cargados no coinciden con los esperados");
		}
		
		//Para productos del menu
		
		// Para mirar si el tamaño de la lista esperada es el mismo que el de la obtenida
		assertEquals(menuEsperado.size(), menu.size(), "El tamaño del menu cargados no coincide con el esperado");
		
		
		ProductoMenu p1;
		ProductoMenu p2;
		for (int i = 0; i < menu.size(); i++)
		{
			p1 = menu.get(i);
			p2 = menuEsperado.get(i);
			assertEquals(p2.getNombre(), p1.getNombre(), "El menu cargados no coincide con el esperado");
			assertEquals(p2.getPrecio(), p1.getPrecio(), "El menu cargados no coincide con el esperado");
		}
		
		// Para combos;
		
		// Para mirar si el tamaño de la lista esperada es el mismo que el de la obtenida
		assertEquals(combosEsperados.size(), combos.size(), "La cantidad de combos cargados no coinciden con los esperados");
		
		Combo c1;
		Combo c2;
		for (int i = 0; i < combos.size(); i++)
		{
			c1 = combos.get(i);
			c2 = combosEsperados.get(i);
			assertEquals(c2.getNombre(), c1.getNombre(), "Los combos cargados no coinciden con los esperados");
			assertEquals(c2.getPrecio(), c1.getPrecio(), "Los combos cargados no coinciden con los esperados");
		}
		
	}
	
	@Test
	void testCargarIngredientesIngredienteRepetidoException() throws NumberFormatException, HamburguesaException, IOException
	{
		setUpCargarInfo();
		
		File archivoIngredientes = new File("tests/uniandes/dpoo/hamburguesas/tests/datosPrueba/ingredientes_test.txt");
		File archivoMenu = new File("tests/uniandes/dpoo/hamburguesas/tests/datosPrueba/menu_test.txt");
		File archivoCombos = new File("tests/uniandes/dpoo/hamburguesas/tests/datosPrueba/combos_test.txt");
		
		File archivoTemporal = File.createTempFile("ingredientes_temp", ".txt");
		archivoTemporal.deleteOnExit();
		
		Files.copy(archivoIngredientes.toPath(), archivoTemporal.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		
		// Repite un ingrediente en el archivo temporal
		try (FileWriter writer = new FileWriter(archivoTemporal, true))
		{
			writer.write("\ntomate;1000");
		}
		
		assertThrows(IngredienteRepetidoException.class, 
				()-> rest1.cargarInformacionRestaurante(archivoTemporal, archivoMenu, archivoCombos));
		
	}
	
	@Test
	void testCargarMenuProductoRepetidoException() throws NumberFormatException, HamburguesaException, IOException
	{
		setUpCargarInfo();
		
		File archivoIngredientes = new File("tests/uniandes/dpoo/hamburguesas/tests/datosPrueba/ingredientes_test.txt");
		File archivoMenu = new File("tests/uniandes/dpoo/hamburguesas/tests/datosPrueba/menu_test.txt");
		File archivoCombos = new File("tests/uniandes/dpoo/hamburguesas/tests/datosPrueba/combos_test.txt");
		
		File archivoTemporal = File.createTempFile("menu_temp", ".txt");
		archivoTemporal.deleteOnExit();
		
		// Repite un producto del menu en el archivo temporal
		Files.copy(archivoMenu.toPath(), archivoTemporal.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		
		try (FileWriter writer = new FileWriter(archivoTemporal, true))
		{
			writer.write("\ncorral queso;16000");
		}
		
		assertThrows(ProductoRepetidoException.class, 
				()-> rest1.cargarInformacionRestaurante(archivoIngredientes, archivoTemporal, archivoCombos));
		
	}
	
	@Test
	void testCargarCombosProductoRepetidoException() throws NumberFormatException, HamburguesaException, IOException
	{
		setUpCargarInfo();
		
		File archivoIngredientes = new File("tests/uniandes/dpoo/hamburguesas/tests/datosPrueba/ingredientes_test.txt");
		File archivoMenu = new File("tests/uniandes/dpoo/hamburguesas/tests/datosPrueba/menu_test.txt");
		File archivoCombos = new File("tests/uniandes/dpoo/hamburguesas/tests/datosPrueba/combos_test.txt");
		
		File archivoTemporal = File.createTempFile("combos_temp", ".txt");
		archivoTemporal.deleteOnExit();
		
		Files.copy(archivoCombos.toPath(), archivoTemporal.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		
		// Repite un combo en el archivo temporal
		try (FileWriter writer = new FileWriter(archivoTemporal, true))
		{
			writer.write("\ncombo corral;10%;corral;papas medianas;gaseosa");
		}
		
		assertThrows(ProductoRepetidoException.class, 
				()-> rest1.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoTemporal));
		
	}
	
	@Test
	void testCargarCombosProductoFaltanteException() throws NumberFormatException, HamburguesaException, IOException
	{
		setUpCargarInfo();
		
		File archivoIngredientes = new File("tests/uniandes/dpoo/hamburguesas/tests/datosPrueba/ingredientes_test.txt");
		File archivoMenu = new File("tests/uniandes/dpoo/hamburguesas/tests/datosPrueba/menu_test.txt");
		File archivoCombos = new File("tests/uniandes/dpoo/hamburguesas/tests/datosPrueba/combos_test.txt");
		
		File archivoTemporal = File.createTempFile("combos_temp", ".txt");
		archivoTemporal.deleteOnExit();
		
		Files.copy(archivoCombos.toPath(), archivoTemporal.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		
		// Añade un producto que no existe en el menu temporal al último combo
		try (FileWriter writer = new FileWriter(archivoTemporal, true))
		{
			writer.write(";papas grandes");
		}
		
		assertThrows(ProductoFaltanteException.class, 
				()-> rest1.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoTemporal));
		
	}
}
