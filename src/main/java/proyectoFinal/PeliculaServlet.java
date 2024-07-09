package proyectoFinal;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.sql.SQLException;


@WebServlet("/PeliculaServlet/*")//probable error buscar aca
public class PeliculaServlet extends HttpServlet {

	private PeliculaService peliculaService;
	private ObjectMapper objectMapper;
	
	@Override
	public void init() throws ServletException
	{
		peliculaService = new PeliculaService();
		objectMapper = new ObjectMapper();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  
	{
		 String pathInfo = req.getPathInfo();
	        try {
	            if (pathInfo == null || pathInfo.equals("/")) 
	            {
	            	String queryPara = req.getParameter("genero"); 
	            	if (queryPara==null) {
	                	List<Pelicula> peliculas = peliculaService.getAllPeliculas();
		                String json = objectMapper.writeValueAsString(peliculas);
		                resp.setContentType("application/json");
		                resp.getWriter().write(json);
	                }else {
	                	List<Pelicula> peliculas = peliculaService.getAllPeliculasByGenero(queryPara);
	                	String json = objectMapper.writeValueAsString(peliculas);
		                resp.setContentType("application/json");
		                resp.getWriter().write(json);
	                }
	            } 
	            else 
	            {
	                String[] pathParts = pathInfo.split("/");
	                int id = Integer.parseInt(pathParts[1]);
	                Pelicula pelicula = peliculaService.getPelicula(id);
	                if (pelicula != null) 
	                {
	                    String json = objectMapper.writeValueAsString(pelicula);
	                    resp.setContentType("application/json");
	                    resp.getWriter().write(json);
	                } 
	                else 
	                {
	                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
	                }
	            }
	        } 
	        catch (SQLException | ClassNotFoundException e) 
	        {
	            throw new ServletException(e);
	        }
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		try 	
		{
			Pelicula pelicula = objectMapper.readValue(req.getReader(),Pelicula.class);
			peliculaService.addPelicula(pelicula);
			resp.setStatus(HttpServletResponse.SC_CREATED);
		}
		catch (SQLException | ClassNotFoundException e) 
		{
            throw new ServletException(e);
        }
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		try 
		{
			Pelicula pelicula = objectMapper.readValue(req.getReader(), Pelicula.class);
			peliculaService.updatePelicula(pelicula);
			resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
		}
		catch (SQLException | ClassNotFoundException e) 
		{
            throw new ServletException(e);
        }
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		try 
		{
			String pathInfo = req.getPathInfo();
			
			if (pathInfo!=null && pathInfo.split("/").length > 1) {
				int id = Integer.valueOf(pathInfo.split("/")[1]);
				peliculaService.deletePelicula(id);
				resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
			}
			else 
			{
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
			
		}
		catch (SQLException | ClassNotFoundException e) 
		{
            throw new ServletException(e);
        }
	}
	
	
	
	

}
