package coriander.haarlem.controllers

import jetbrains.buildServer.controllers.BaseController
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView

class DilbertController extends BaseController {
	override protected def doHandle(
		httpServletRequest : HttpServletRequest ,
		httpServletResponse : HttpServletResponse
	) : ModelAndView  = {

		httpServletResponse.setContentType("text/html")
		httpServletResponse.getWriter().write(
			"<html>" +
				"<head></head>" +
				"<body>" +
				"	<img src=\"" + getLatestDilbert + "\" />" +
				"</body>" +
			"</html>"
		);

		null
	}

	private def getLatestDilbert = {
		// TODO: Get latest from: http://feeds.dilbert.com/DilbertDailyStrip?format=xml
		"http://dilbert.com/dyn/str_strip/000000000/" +
		"00000000/0000000/000000/70000/9000/500/79578/79578.strip.gif"
	}
}