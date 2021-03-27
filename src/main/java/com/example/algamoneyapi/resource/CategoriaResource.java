package com.example.algamoneyapi.resource;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.algamoneyapi.model.Categoria;
import com.example.algamoneyapi.repository.CategoriaRepository;

@RestController // Com isso já retorna um JSON da representação solicitada
@RequestMapping("/categorias")
public class CategoriaResource {
	
	// Ache uma implementação de Categoria e injete
	@Autowired 
	private CategoriaRepository categoriaRepository;
	
	/* É possível eu ter outro método @GetMapping,
	porém como filho de /categoria ex: @GetMapping("/outros") 
	Neste casos seria um subdiretório de /categoria */
	
	/* Um modo de retornar outros status quando a categoria
	 * vier sem valores
	 * 	@GetMapping 
		public ResponseEntity<?> listar(){
			List<Categoria> categorias = categoriaRepository.findAll();
			return !categorias.isEmpty() ? ResponseEntity.ok(categorias) : ResponseEntity.noContent().build();
	}
	 */
		
	@GetMapping 
	public List<Categoria> listar(){
		return categoriaRepository.findAll();
	}
	
	@PostMapping // Para realizar a operação de criação
	/*
	@ResponseStatus(HttpStatus.CREATED) // Digo que ao terminar de salvar para retornar um status equivalente
	public void criar(@RequestBody Categoria categoria) {
		categoriaRepository.save(categoria);
	}
	*/
	public ResponseEntity<Categoria> criar(@RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
				  .buildAndExpand(categoriaSalva.getCodigo()).toUri();
		response.setHeader("Location", uri.toASCIIString());
		return ResponseEntity.created(uri).body(categoriaSalva);
	}
	
	/* Selecionando uma categoria
	@GetMapping("/{codigo}")
	public Categoria buscarPeloCodigo(@PathVariable Long codigo) {
		return categoriaRepository.findById(codigo).orElse(null);
	}
	*/
	
	//Selecionando e validando existência de uma categoria
	@GetMapping("/{codigo}")
	public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo) {
	  return this.categoriaRepository.findById(codigo)
	      .map(categoria -> ResponseEntity.ok(categoria))
	      .orElse(ResponseEntity.notFound().build());
	}

}
