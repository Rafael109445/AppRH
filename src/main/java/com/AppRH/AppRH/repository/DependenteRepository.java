package com.AppRH.AppRH.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.AppRH.AppRH.models.Dependente;
import com.AppRH.AppRH.models.Funcionario;


public interface DependenteRepository extends CrudRepository<Dependente, String> {

	Iterable<Dependente> findByFuncionario(Funcionario funcionario);

	// para o método delete por CPF tambem, serve para outras coisas
	Dependente findByCpf(String cpf);
	Dependente findById(long id);
	// Query para a busca
	List<Dependente> findByNome(String nome);
	
	// Query para a busca
		@Query(value = "select u from Dependente u where u.nome like %?1%")
		List<Dependente> findByNomesDependentes(String nome);

	

}
