package br.com.alura.escolalura.escolalura.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

public class Aluno {

	private ObjectId id;

	private String nome;

	private Date dataNascimento;

	private Curso curso;

	private List<Nota> notas;

	private List<Habilidade> habilidades;

	private Contato contato;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public List<Nota> getNotas() {
		return this.notas == null ? new ArrayList<Nota>() : this.notas;
	}

	public void setNotas(List<Nota> notas) {
		this.notas = notas;
	}

	public List<Habilidade> getHabilidades() {
		if (habilidades == null)
			habilidades = new ArrayList<Habilidade>();
		return habilidades;
	}

	public void setHabilidades(List<Habilidade> habilidades) {
		this.habilidades = habilidades;
	}

	public Aluno criarId() {
		setId(new ObjectId());
		return this;
	}

	public Aluno adicionar(Aluno aluno, Habilidade habilidade) {
		List<Habilidade> habilidadesAluno = aluno.getHabilidades();
		habilidadesAluno.add(habilidade);
		aluno.setHabilidades(habilidadesAluno);
		return aluno;
	}

	public Aluno adicionar(Aluno aluno, Nota nota) {
		List<Nota> notasAluno = aluno.getNotas();
		notasAluno.add(nota);
		aluno.setNotas(notasAluno);
		return aluno;
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

}
