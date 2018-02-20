package br.com.alura.escolalura.escolalura.model;

public class Curso {

	private String nome;

	public Curso() {
	}
	
	public Curso(String string) {
		nome = string;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
