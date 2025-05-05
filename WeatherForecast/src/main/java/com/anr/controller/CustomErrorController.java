package com.anr.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomErrorController implements ErrorController {

	@GetMapping("/error")
	public ModelAndView handleError() {
		// Return a custom error view
		ModelAndView modelAndView = new ModelAndView("error");
		modelAndView.addObject("message", "An unexpected error occurred. Please try again later.");
		return modelAndView;
	}
}
