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
import br.com.alura.escolalura.escolalura.repository.AlunoRepository;
import br.com.alura.escolalura.escolalura.service.GeolocalizacaoService;

@Controller
public class AlunoController {

	@Autowired
	private AlunoRepository alunoRepository;
	
	@Autowired
	private GeolocalizacaoService geoSerivce;
	
	@GetMapping("/aluno/pesquisar")
	public String pesquisar(@RequestParam("nome") String nome, Model model){
		List<Aluno> alunos = alunoRepository.pesquisarPorNome(nome);
		model.addAttribute("alunos", alunos);
		return "aluno/pesquisarnome";
	}
	
	@GetMapping("/aluno/pesquisarnome")
	public String pesquisarNome(){
		return "aluno/pesquisarnome";
	}
	
	@GetMapping("/aluno/visualizar/{id}")
	public String visualizar(@PathVariable String id, Model model){
		model.addAttribute("aluno", alunoRepository.obterAlunoPor(id));
		return "aluno/visualizar";
	}
	
	@GetMapping("/aluno/cadastrar")
	public String cadastrar(Model model){
		model.addAttribute("aluno", new Aluno());
		return "aluno/cadastrar";
	}
	
	@PostMapping("/aluno/salvar")
	public String salvar(@ModelAttribute Aluno aluno) throws Exception{
		aluno.getContato().setCoordinates(geoSerivce.obterLatLongPor(aluno.getContato()));
		alunoRepository.salvar(aluno);
		return "redirect:/";
	}
	
	@GetMapping("/aluno/listar")
	public String listar(Model model){
		List<Aluno> alunos = alunoRepository.obterTodosOsAlunos();
		model.addAttribute("alunos", alunos);
		return "aluno/listar";
	}
}
