package br.com.alura.escolalura.escolalura.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.alura.escolalura.escolalura.model.Aluno;
import br.com.alura.escolalura.escolalura.model.Nota;
import br.com.alura.escolalura.escolalura.repository.AlunoRepository;

@Controller
public class NotaController {

	@Autowired
	private AlunoRepository alunoRepository;
	
	@GetMapping("/nota/cadastrar/{id}")
	public String cadastrar(@PathVariable String id, Model model) {
		model.addAttribute("aluno", alunoRepository.obterAlunoPor(id));
		model.addAttribute("notas", new Nota());
		return "nota/cadastrar";
	}
	
	@PostMapping("/nota/salvar/{id}")
	public String salvar(@PathVariable String id, @ModelAttribute Nota nota){
		Aluno aluno = alunoRepository.obterAlunoPor(id);
	  	alunoRepository.salvar(aluno.adicionar(aluno, nota));
		return "redirect:/aluno/listar";
	}
	
	@GetMapping("/nota/iniciarpesquisa")
	public String iniciarPesquisa(){
		return "nota/pesquisar";
	}
	
	@GetMapping("/nota/pesquisar")
	public String pesquisar(
			@RequestParam("classificacao") String classificacao, 
			@RequestParam("notacorte") String notaCorte,
			Model model){
		List<Aluno> alunos = alunoRepository.obterAlunosPor(classificacao, Double.parseDouble(notaCorte));
		model.addAttribute("alunos", alunos);
		return "nota/pesquisar";
	}

}
