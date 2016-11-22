package wa3.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wa3.dao.Dao;
import wa3.dao.DocumentiDao;

@WebServlet(asyncSupported = true, name = "MyServlet", urlPatterns = { "/service" } )
public class MyServlet extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cmd = request.getParameter("cmd");
		
		DocumentiDao documentiDao = WebFactory.getDocumentiDao();
		if ("eliminadocumenti".equals(cmd)){
			String id = request.getParameter("id");
			if (id!=null && !id.isEmpty()){
				if (delete(documentiDao, id)){
					reply(response, "Ok");
				}
			}
			reply(response, "not found");
		}
		
		reply(response, "cmd "+cmd+" not found");
	}

	private <T> boolean delete(Dao<T> rigaDao, String id) {
		T riga = rigaDao.getById(id);
		if (riga!=null){
			rigaDao.remove(riga);
			return true;
		}
		return false;

	}

	private void reply(HttpServletResponse response, String string) throws IOException {
		response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			out.println("{result:'"+string+"'}");
		} finally {
			out.close();
		}

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	public String getServletInfo() {
		return "Short description";
	}
}