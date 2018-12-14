package com.test.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;
import com.googlecode.objectify.ObjectifyService;
import com.test.data.LibroBean;
import com.test.util.TokenUtil;

public class LibroDAO {

	private static final Logger LOGGER = Logger.getLogger(LibroDAO.class.getName());

	/**
	 * @return lista de libros
	 */
	public List<LibroBean> listado(String filtro)
	{
		LOGGER.info("Obteniendo listado de libros");
//		String queryString = "tituloAutor =  \"" + filtro + ""\"; //FORMATO QUERY: "tituloAutor = \"ejemp\"";
		
		
//    Build the Query and run the search
//	  Query query = Query.newBuilder().setOptions(options).build(queryString);
//	  Query query = Query.newBuilder().build("tituloAutor = \" ejemp \"");
	  IndexSpec indexSpec = IndexSpec.newBuilder().setName(TokenUtil.INDICE_TITULO_AUTOR).build();
	  Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
	  
	  
//	  Results<ScoredDocument> result = index.search(query);
	  Results<ScoredDocument> result = index.search(filtro);
	  
	  Iterator<ScoredDocument> it = (Iterator<ScoredDocument>) result.getResults().iterator();
	  
	  List<LibroBean> resultadoBusqueda = new ArrayList<LibroBean>();
	  
	  List<Long> listaId = new ArrayList<Long>();
	  
	  while (it.hasNext()) 
	  {
		  ScoredDocument doc = (ScoredDocument) it.next();
		  listaId.add(Long.valueOf(doc.getId()));
	  }

	  
	  Collection<LibroBean> lista = ObjectifyService.ofy().load().type(LibroBean.class).ids(listaId).values();
	  resultadoBusqueda = new ArrayList<LibroBean>(lista);
	  
	  return resultadoBusqueda;
	}
	
	
	
	public List<LibroBean> listado() {
		return ObjectifyService.ofy().load().type(LibroBean.class).list();
	}
	
	
	

	/**
	 * @param id
	 * @return obtiene libro a partir de id
	 */
	public LibroBean obtenerLibro(Long id) {
		LOGGER.info("Obteniendo libro: " + id);

		return ObjectifyService.ofy().load().type(LibroBean.class).id(id).now();
	}

	/**
	 * Guarda el libro pasado por par�metro
	 * 
	 * @param libro
	 */
	public void guardarLibro(LibroBean libro) {
		if (libro == null) {
			throw new IllegalArgumentException("El libro es nulo o vacío");
		}
		LOGGER.info("Guardando libro " + libro.getId());
		ObjectifyService.ofy().save().entity(libro).now();
	}

	/**
	 * Borra el libro pasado por par�metro
	 * 
	 * @param libro
	 */
	public void borrarLibro(LibroBean libro) {
		if (libro == null) {
			throw new IllegalArgumentException("El libro es nulo o vacío");
		}

		LOGGER.info("Borrando libro " + libro.getId());
		ObjectifyService.ofy().delete().entity(libro);
	}

}
