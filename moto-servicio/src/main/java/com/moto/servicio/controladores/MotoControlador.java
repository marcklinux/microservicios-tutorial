package com.moto.servicio.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moto.servicio.entidades.Moto;
import com.moto.servicio.servicios.MotoServicio;

@RestController
@RequestMapping("/moto")
public class MotoControlador {

	@Autowired
	private MotoServicio motoServicio;
	
	@GetMapping
	public ResponseEntity<List<Moto>> listarMotos(){
		List<Moto> motos = motoServicio.getAll();
		if(motos.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.ok(motos);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Moto> obtenerMoto(@PathVariable("id") int id){
		Moto moto = motoServicio.getMotoById(id);
		if(moto == null) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(moto);
	}
	
	@PostMapping
	public ResponseEntity<Moto> guardarMoto(@RequestBody Moto moto){
		Moto nuevaMoto = motoServicio.save(moto);
		return ResponseEntity.ok(nuevaMoto);
	}
	
	@GetMapping("/usuario/{usuarioId}")
	public ResponseEntity<List<Moto>> ListarMotoPorUsuarioId(@PathVariable("usuarioId")int id){
		List<Moto> motos = motoServicio.byUsuarioId(id);
		if(motos.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.ok(motos);
		
	}
}
