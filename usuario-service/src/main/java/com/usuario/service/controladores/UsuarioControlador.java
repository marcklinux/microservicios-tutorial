package com.usuario.service.controladores;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usuario.service.entidades.Usuario;
import com.usuario.service.modelos.Carro;
import com.usuario.service.modelos.Moto;
import com.usuario.service.servicio.UsuarioServicio;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/usuario")
public class UsuarioControlador {

	@Autowired
	private UsuarioServicio usuarioServicio;

	@GetMapping
	public ResponseEntity<List<Usuario>> listarUsuarios() {
		List<Usuario> usuarios = usuarioServicio.getAll();
		if (usuarios.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(usuarios);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Usuario> obtenerUsuario(@PathVariable("id") int id) {
		Usuario usuario = usuarioServicio.getUsuarioById(id);
		if (usuario == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(usuario);
	}

	@PostMapping
	public ResponseEntity<Usuario> guardarUsuario(@RequestBody Usuario usuario) {
		Usuario nuevoUsuario = usuarioServicio.save(usuario);
		return ResponseEntity.ok(nuevoUsuario);
	}

	@CircuitBreaker(name = "carrosCB", fallbackMethod = "fallBackGetCarros")
	@GetMapping("/carros/{usuarioId}")
	public ResponseEntity<List<Carro>> listarCarros(@PathVariable("usuarioId") int id) {
		Usuario usuario = usuarioServicio.getUsuarioById(id);
		if (usuario == null) {
			return ResponseEntity.notFound().build();
		}

		List<Carro> carros = usuarioServicio.getCarros(id);
		return ResponseEntity.ok(carros);

	}

	@CircuitBreaker(name = "motosCB", fallbackMethod = "fallBackGetMotos")
	@GetMapping("/motos/{usuarioId}")
	public ResponseEntity<List<Moto>> listarMotos(@PathVariable("usuarioId") int id) {
		Usuario usuario = usuarioServicio.getUsuarioById(id);
		if (usuario == null) {
			return ResponseEntity.notFound().build();
		}

		List<Moto> motos = usuarioServicio.getMotos(id);
		return ResponseEntity.ok(motos);

	}

	@CircuitBreaker(name = "carrosCB", fallbackMethod = "fallBackSaveCarro")
	@PostMapping("/carro/{usuarioId}")
	public ResponseEntity<Carro> guardarCarro(@PathVariable("usuarioId") int usuarioId, @RequestBody Carro carro) {
		Carro nuevoCarro = usuarioServicio.saveCarro(usuarioId, carro);
		return ResponseEntity.ok(nuevoCarro);
	}

	@CircuitBreaker(name = "motosCB", fallbackMethod = "fallBackSaveMoto")
	@PostMapping("/moto/{usuarioId}")
	public ResponseEntity<Moto> guardarMoto(@PathVariable("usuarioId") int usuarioId, @RequestBody Moto moto) {
		Moto nuevaMoto = usuarioServicio.saveMoto(usuarioId, moto);
		return ResponseEntity.ok(nuevaMoto);
	}

	@CircuitBreaker(name = "todosCB", fallbackMethod = "fallBackGetTodos")
	@GetMapping("/todos/{usuarioId}")
	public ResponseEntity<Map<String, Object>> listarTodosLosVehiculos(@PathVariable("usuarioId") int usuarioId) {
		Map<String, Object> resultado = usuarioServicio.getUsuarioAndVehiculos(usuarioId);
		return ResponseEntity.ok(resultado);

	}

	private ResponseEntity<List<Carro>> fallBackGetCarros(@PathVariable("usuarioId") int id,
			RuntimeException excepcion) {
		return new ResponseEntity("El usuario :" + id + " tiene sus carros en el taller", HttpStatus.OK);
	}

	private ResponseEntity<List<Carro>> fallBackSaveCarro(@PathVariable("usuarioId") int id, @RequestBody Carro carro,
			RuntimeException excepcion) {
		return new ResponseEntity("El usuario :" + id + " no tiene dinero para los carros", HttpStatus.OK);
	}

	private ResponseEntity<List<Moto>> fallBackGetMotos(@PathVariable("usuarioId") int id, RuntimeException excepcion) {
		return new ResponseEntity("El usuario :" + id + " tiene sus motos en el taller", HttpStatus.OK);
	}

	private ResponseEntity<List<Moto>> fallBackSaveMoto(@PathVariable("usuarioId") int id, @RequestBody Moto moto,
			RuntimeException excepcion) {
		return new ResponseEntity("El usuario :" + id + " no tiene dinero para los mtos", HttpStatus.OK);
	}

	private ResponseEntity<List<Carro>> fallBackGetTodos(@PathVariable("usuarioId") int id,
			RuntimeException excepcion) {
		return new ResponseEntity("El usuario :" + id + " tiene los vehiculos en el taller", HttpStatus.OK);
	}
}
