package com.AppRH.AppRH.controllers;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AppRH.AppRH.models.Dependente;
import com.AppRH.AppRH.models.Funcionario;
import com.AppRH.AppRH.repository.DependenteRepository;
import com.AppRH.AppRH.repository.FuncionarioRepository;

@Controller
public class FuncionarioController {
	
	@Autowired
	private FuncionarioRepository fr;

	@Autowired
	private DependenteRepository dr;
	
	// GET que chama o form para cadastrar funcionários
		@RequestMapping(value = "/cadastrarFuncionario", method = RequestMethod.GET)
		public String form() {
			return "funcionario/formFuncionario";
		}
		
		
		// POST que cadastra funcionários
		@RequestMapping(value = "/cadastrarFuncionario", method = RequestMethod.POST)
		public String form(@Valid Funcionario funcionario, BindingResult result, RedirectAttributes attributes) {

			if (result.hasErrors()) {
				attributes.addFlashAttribute("mensagem", "Verifique os campos");
				return "redirect:/cadastrarFuncionario";
			}

			fr.save(funcionario);
			attributes.addFlashAttribute("mensagem", "Funcionário cadastrado com sucesso!");
			return "redirect:/cadastrarFuncionario";
		}
		
		// GET que lista funcionários
		@RequestMapping("/funcionarios")
		public ModelAndView listaFuncionarios() {
			ModelAndView mv = new ModelAndView("funcionario/listaFuncionario");
			Iterable<Funcionario> funcionarios = fr.findAll();
			mv.addObject("funcionarios", funcionarios);
			return mv;
		}
		
		// GET que lista dependentes e detalhes dos funcionário
		@RequestMapping("/dependentes/{id}")
		public ModelAndView dependentes(@PathVariable("id") long id) {
			Funcionario funcionario = fr.findById(id);
			ModelAndView mv = new ModelAndView("funcionario/dependentes");
			mv.addObject("funcionarios", funcionario);

			// lista de dependentes baseada no id do funcionário
			Iterable<Dependente> dependentes = dr.findByFuncionario(funcionario);
			mv.addObject("dependentes", dependentes);

			return mv;
			}
		
		// POST que adiciona dependentes
		@RequestMapping(value="/dependentes/{id}", method = RequestMethod.POST)
		public String dependentesPost(@PathVariable("id") long id, Dependente dependentes, BindingResult result,
				RedirectAttributes attributes) {
			
			if(result.hasErrors()) {
				attributes.addFlashAttribute("mensagem", "Verifique os campos!");
				return "redirect:/dependentes/{id}";
			}
			
			if(dr.findByCpf(dependentes.getCpf()) != null) {
				attributes.addFlashAttribute("mensagem_erro", "CPF duplicado");
				return "redirect:/dependentes/{id}";
			}
			
			Funcionario funcionario = fr.findById(id);
			dependentes.setFuncionario(funcionario);
			dr.save(dependentes);
			attributes.addFlashAttribute("mensagem", "Dependente adicionado com sucesso");
			return "redirect:/dependentes/{id}";
			
		}
		
		//GET que deleta funcionário
		@RequestMapping("/deletarFuncionario")
		public String deletarFuncionario(long id) {
			Funcionario funcionario = fr.findById(id);
			fr.delete(funcionario);
			return "redirect:/funcionarios";

}
		// Métodos que atualizam funcionário
		// GET que chama o FORM de edição do funcionário
		@RequestMapping(value = "/editar-funcionario", method = RequestMethod.GET)
		public ModelAndView editarFuncionario(long id) {
			Funcionario funcionario = fr.findById(id);
			ModelAndView mv = new ModelAndView("funcionario/update-funcionario");
			mv.addObject("funcionario", funcionario);
			return mv;
		}	

		// POST que atualiza o funcionário
		@RequestMapping(value = "/editar-funcionario", method = RequestMethod.POST)
		public String updateFuncionario(@Valid Funcionario funcionario,  BindingResult result, RedirectAttributes attributes){
			
			fr.save(funcionario);
			attributes.addFlashAttribute("successs", "Funcionário alterado com sucesso!");
			//gambiarra enteder melhor
			long idLong = funcionario.getId();
			String id = "" + idLong;
			return "redirect:/dependentes/" + id;
			
		}
		
		
		// GET que deleta dependente
		@RequestMapping("/deletarDependente")
		public String deletarDependente(String cpf) {
			Dependente dependente = dr.findByCpf(cpf);
			
			Funcionario funcionario = dependente.getFuncionario();
			String codigo = "" + funcionario.getId();
			
			dr.delete(dependente);
			return "redirect:/dependentes/" + codigo;
		
		}

}
