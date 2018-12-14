package com.test.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.SearchServiceFactory;
import com.test.dao.LibroDAO;
import com.test.data.LibroBean;
import com.test.util.TokenUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Path("/libro")
@Produces("application/json;charset=utf-8")
@Api(value="libro", description = "Libro service")
public class LibroResource {

	private LibroDAO libroDAO;
	
	public LibroResource()
	{
		this.libroDAO = new LibroDAO();
	}
	
	
	@GET
	@ApiOperation("listado de libros")
	/**
	 * Obtiene lista completa de libros.
	 * @return Response
	 */
	public Response listado()
	{
		return Response.ok(this.libroDAO.listado()).build();
	}
	
	@GET
	@Path("/busquedaLibros/{filtro}")
	@ApiOperation("búsqueda libros")
	/**
	 * Obtiene lista de libros según el filtro recibido por parámetro.
	 * @param String filtro
	 * @return Response
	 */
	public Response listadoFiltrado(@PathParam("filtro") String filtro)
	{
		return Response.ok(this.libroDAO.listado(filtro)).build();
	}
	
	
	@GET
	@Path("/{id}")
	@ApiOperation("Obtener libro")
	/**
	 * Obtiene libro a partir de su identificador para cargar el detalle.
	 * @param Long id
	 * @return Response
	 */
	public Response obtenerLibro(@PathParam("id") Long id)
	{
		LibroBean libro = this.libroDAO.obtenerLibro(id);
		if (libro == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		return Response.ok(libro).build();
	}
	
	
	@POST
	@Consumes("application/json;charset=utf-8")
	@ApiOperation("Guardar libro")
	/**
	 * Guarda libro según los datos introducidos por el usuario en el formulario.
	 * @param LibroBean libro
	 * @return Response
	 */
	public Response guardarLibro(LibroBean libro)
	{
		this.libroDAO.guardarLibro(libro);
		
		LibroBean libroGuardado = this.libroDAO.obtenerLibro(libro.getId());
		
		generarNuevoIndice(libroGuardado);
		
		return Response.ok().build();
	}
	

	@DELETE
	@Path("/{id}")
	@ApiOperation("Borrar libro")
	/**
	 * Borra el libro seleccionado por el usuario
	 * @param Long id
	 * @return Response
	 */
	public Response eliminarLibro(@PathParam("id") Long id)
	{	
		LibroBean libro = this.libroDAO.obtenerLibro(id);
		if (libro == null)
		{
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		this.libroDAO.borrarLibro(libro);
		
		eliminarIndice(id);
		
		return Response.ok().build();
	}
	
	
	
	private Document construyeDocumento(String tituloAutorToken, String idLibro)
	{
		return Document.newBuilder()
	        // Setting the document identifer is optional.
	        // If omitted, the search service will create an identifier.
	        .setId(idLibro)
	        .addField(Field.newBuilder().setName("tituloAutor").setText(tituloAutorToken))
	        .build();
	}
	
	
	private void indexarDocumento(Document documento)
	{
		IndexSpec indexSpec = IndexSpec.newBuilder().setName(TokenUtil.INDICE_TITULO_AUTOR).build();
		com.google.appengine.api.search.Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);

		index.put(documento);
	}
	
	/**
	 * Se almacena un nuevo índice al guardar un libro
	 * @param libro
	 */
	private void generarNuevoIndice(LibroBean libro)
	{
		TokenUtil tokenUtil = new TokenUtil();
		String autorTituloTokenizado = tokenUtil.getSearchTokens(libro.getAutor() + libro.getTitulo());
		
		Document documento = construyeDocumento(autorTituloTokenizado, libro.getId().toString());
		indexarDocumento(documento);
	}
	
	/**
	 * Se elimina el indice asociado al libro que se ha eliminado
	 * @param idLibro
	 */
	private void eliminarIndice(Long idLibro)
	{
		IndexSpec indexSpec = IndexSpec.newBuilder().setName(TokenUtil.INDICE_TITULO_AUTOR).build();
		com.google.appengine.api.search.Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
		
		List<String> listaIdDocumentos = new ArrayList<String>();
		listaIdDocumentos.add(idLibro.toString());
		
		index.delete(listaIdDocumentos);
	}
	
	
}