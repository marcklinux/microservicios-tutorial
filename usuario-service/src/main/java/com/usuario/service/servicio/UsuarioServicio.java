package com.usuario.service.servicio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

import com.usuario.service.entidades.Usuario;
import com.usuario.service.feignclients.CarroFeignClient;
import com.usuario.service.feignclients.MotoFeignClient;
import com.usuario.service.modelos.Carro;
import com.usuario.service.modelos.Moto;
import com.usuario.service.repositorio.UsuarioRepositorio;

@Service
public class UsuarioServicio {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private CarroFeignClient carroFeignClient;
	
	@Autowired
	private MotoFeignClient motoFeignClient;
	
	

	
	public List<Usuario> getAll(){
		return usuarioRepositorio.findAll();
		
	}
	 
	public Usuario getUsuarioById(int id) {
		return usuarioRepositorio.findById(id).orElse(null);
	}

	
	public Usuario save(Usuario usuario) {
		Usuario nuevoUsuario = usuarioRepositorio.save(usuario);
		return nuevoUsuario;
	}
	
	public List<Carro> getCarros(int usuarioId){
		List<Carro> carros = restTemplate.getForObject("http://carro-servicio/carro/usuario/" + usuarioId, List.class);
		return carros;
	}
	
	public List<Moto> getMotos(int usuarioId){
		List<Moto> motos = restTemplate.getForObject("http://moto-servicio/moto/usuario/" + usuarioId, List.class);
		return motos;
	}
	
	
	public Carro saveCarro(int usuarioId,Carro carro) {
		carro.setUsuarioId(usuarioId);
		
		Carro nuevoCarro = carroFeignClient.save(carro);
		return nuevoCarro;
	}
	
	public Moto saveMoto(int usuarioId,Moto moto) {
		moto.setUsuarioId(usuarioId);
		
		Moto nuevaMoto = motoFeignClient.save(moto);
		return nuevaMoto;
	}
	
	public Map<String,Object> getUsuarioAndVehiculos(int usuarioId){
		Map<String,Object> resultado = new HashMap<>();
		Usuario usuario = usuarioRepositorio.findById(usuarioId).orElse(null);
		
		if(usuario == null) {
			resultado.put("Mensaje", "el usuario no existe");
			return resultado;
		}
		
		resultado.put("usuario", usuario);
		List<Carro> carros = carroFeignClient.getCarros(usuarioId);
		
		if(carros.isEmpty()) {
			resultado.put("Carros", "el usuario no tiene carros");
		}else {
			resultado.put("Carros", carros);
		}
		
		List<Moto> motos = motoFeignClient.getMotos(usuarioId);
		
		if(motos.isEmpty()) {
			resultado.put("Motos", "el usuario no tiene motos");
		}else {
			resultado.put("Motos", motos);
		}
		
		return resultado;
	}
}
