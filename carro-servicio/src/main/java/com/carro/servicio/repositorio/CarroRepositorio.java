package com.carro.servicio.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carro.servicio.entidades.Carro;

@Repository
public interface CarroRepositorio extends JpaRepository<Carro,Integer> {

	List<Carro> findByUsuarioId(int usuarioId);
		
	
}
