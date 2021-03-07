package io.app.blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author Arm Fahim
 *
 */
@RestController
public class Blog {

	@GetMapping("/")
	public String post() {
		return ("<h1> Welcome </h1>");
	}

	@GetMapping("/admin")
	public String admin() {
		return ("<h1> Welcome admin</h1>");
	}

	@GetMapping("/user")
	public String user() {
		return ("<h1> Welcome user</h1>");
	}

}
