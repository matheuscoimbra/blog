package com.mc.blog.resources;


import com.mc.blog.security.JWTUtil;
import com.mc.blog.security.UserSS;
import com.mc.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

	@Autowired
	private JWTUtil jwtUtil;
	

	
	@RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSS user = UserService.authenticated();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer " + token);
		response.addHeader("access-control-expose-headers", "Authorization");
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/validateToken/{token}", method = RequestMethod.POST)
	public ResponseEntity<Boolean> validateToken(@PathVariable String token, HttpServletResponse response) {
		Boolean valido = false;
		if(token == null){
			valido = false;
		}else {
		 valido = jwtUtil.tokenValido(token);
		}

		return ResponseEntity.ok().body(valido);
	}
	

}
