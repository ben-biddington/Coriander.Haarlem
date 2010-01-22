package coriander.haarlem.controllers;

import jetbrains.buildServer.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JLogSearchController extends BaseController {
	@Override
	protected ModelAndView doHandle(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse
	) throws Exception {
		httpServletResponse.getWriter().write("CHUBBY_RAIN");
		return null;
	}
}
