package coriander.haarlem.controllers;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JLogSearchServlet extends HttpServlet {
	@Override
	protected void doGet(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse
	) throws javax.servlet.ServletException, java.io.IOException { 
		httpServletResponse.getWriter().write("CHUBBY RAIN");
	}
}