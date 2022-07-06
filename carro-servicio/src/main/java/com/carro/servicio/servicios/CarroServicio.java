package com.carro.servicio.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carro.servicio.entidades.Carro;
import com.carro.servicio.repositorio.CarroRepositorio;

@Service
public class CarroServicio {
	
	@Autowired
	private CarroRepositorio carroRepositorio;
	
	public List<Carro> getAll(){
		return carroRepositorio.findAll();
	}
	
	public Carro getCarroById(int id) {
		return carroRepositorio.findById(id).orElse(null);
	}

	public Carro save(Carro carro) {
		Carro nuevoCarro = carroRepositorio.save(carro);
		return nuevoCarro;
	}
	
	public List<Carro> byUsuarioId(int usuarioId){
		return carroRepositorio.findByUsuarioId(usuarioId);
	}
}
