package com.tdm.tdm.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyErrorController implements ErrorController {

	@Override
	public String getErrorPath() {
		return "/error";
	}

	@RequestMapping("/error")
	public String handleError(HttpServletRequest request) {
		// get error status
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		// TODO: log error details here

		if (status != null) {
			int statusCode = Integer.parseInt(status.toString());

			// display specific error page
			if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
				return "error/401Unaut";
			} else if (statusCode == HttpStatus.NOT_FOUND.value()) {
				return "error/404Pagenotfound";
			} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				return "error/500";
			} else if (statusCode == HttpStatus.FORBIDDEN.value()) {
				return "error/403forbin";
			}
		}

		// display generic error
		return "error";
	}

	

	@GetMapping("403")
	public String accessDenied(Model model) {

		return "403";
	}

}
