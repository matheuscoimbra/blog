package com.mc.blog.services;

import com.mc.blog.services.exceptions.*;
import com.mc.blog.repositories.UsuarioRepository;
import com.mc.blog.domain.Usuario;
import com.mc.blog.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthService {

	@Autowired
	private UsuarioRepository UsuarioRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	

	
	private Random rand = new Random();
	
	public void sendNewPassword(String email) {
		
		Usuario Usuario = UsuarioRepository.findByEmailAndIsAtivoTrue(email);
		if (Usuario == null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		
		String newPass = newPassword();
		Usuario.setSenha(pe.encode(newPass));
		
		UsuarioRepository.save(Usuario);
	}

	private String newPassword() {
		char[] vet = new char[10];
		for (int i=0; i<10; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}

	private char randomChar() {
		int opt = rand.nextInt(3);
		if (opt == 0) { // gera um digito
			return (char) (rand.nextInt(10) + 48);
		}
		else if (opt == 1) { // gera letra maiuscula
			return (char) (rand.nextInt(26) + 65);
		}
		else { // gera letra minuscula
			return (char) (rand.nextInt(26) + 97);
		}
	}
}
