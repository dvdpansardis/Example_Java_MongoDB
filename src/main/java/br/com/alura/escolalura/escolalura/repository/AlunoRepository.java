package br.com.alura.escolalura.escolalura.repository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import br.com.alura.escolalura.escolalura.codec.AlunoCodec;
import br.com.alura.escolalura.escolalura.model.Aluno;

@Repository
public class AlunoRepository {

	private MongoClient mongoClient;
	private MongoDatabase mongoDatabase;

	private void conectar() {
		Codec<Document> codec = MongoClient.getDefaultCodecRegistry().get(Document.class);
		AlunoCodec alunoCodec = new AlunoCodec(codec);
		CodecRegistry fromRegistries = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
				CodecRegistries.fromCodecs(alunoCodec));

		MongoClientOptions options = MongoClientOptions.builder().codecRegistry(fromRegistries).build();

		mongoClient = new MongoClient("localhost:27017", options);
		mongoDatabase = mongoClient.getDatabase("test");
	}

	public void salvar(Aluno aluno) {
		conectar();

		MongoCollection<Aluno> collectionAlunos = getCollectionAlunos();
		if (aluno.getId() == null) {
			collectionAlunos.insertOne(aluno);
		} else {
			collectionAlunos.updateOne(Filters.eq("_id", aluno.getId()), new Document("$set", aluno));
		}
		fecharConexao();
	}

	private void fecharConexao() {
		mongoClient.close();
	}

	public List<Aluno> obterTodosOsAlunos() {
		conectar();

		MongoCursor<Aluno> mongoCursor = getCollectionAlunos().find().iterator();
		fecharConexao();
		return mapperDocToObj(mongoCursor);
	}

	public Aluno obterAlunoPor(String id) {
		conectar();
		Aluno aluno = getCollectionAlunos().find(Filters.eq("_id", new ObjectId(id))).first();
		fecharConexao();
		return aluno;
	}

	public List<Aluno> pesquisarPorNome(String nome) {
		conectar();
		MongoCursor<Aluno> mongoCursor = getCollectionAlunos().find(Filters.regex("nome", nome + "."), Aluno.class)
				.iterator();
		fecharConexao();
		return mapperDocToObj(mongoCursor);
	}

	private MongoCollection<Aluno> getCollectionAlunos() {
		return mongoDatabase.getCollection("alunos", Aluno.class);
	}

	private List<Aluno> mapperDocToObj(MongoCursor<Aluno> mongoCursor) {
		List<Aluno> alunos = new ArrayList<>();
		while (mongoCursor.hasNext()) {
			alunos.add(mongoCursor.next());
		}
		return alunos;
	}

	public List<Aluno> obterAlunosPor(String classificacao, double nota) {
		conectar();
		MongoCursor<Aluno> resultados = null;
		if (classificacao.equals("reprovados")) {
			resultados = getCollectionAlunos().find(Filters.lt("notas", nota)).iterator();
		} else if (classificacao.equals("aprovados")) {
			resultados = getCollectionAlunos().find(Filters.gte("notas", nota)).iterator();
		}
		fecharConexao();
		return resultados != null ? mapperDocToObj(resultados) : new ArrayList<>();
	}

	public List<Aluno> pesquisaPorGeolocalizacao(Aluno aluno) {
		conectar();
		MongoCollection<Aluno> collectionAlunos = getCollectionAlunos();
		collectionAlunos.createIndex(Indexes.geo2dsphere("contato"));
		
		List<Double> coordinates = aluno.getContato().getCoordinates();
		
		Point point = new Point(new Position(coordinates.get(0), coordinates.get(1)));
		
		MongoCursor<Aluno> mongoCursor = collectionAlunos.find(Filters.nearSphere("contato", point, 600.0, 0.0)).limit(5).skip(1).iterator();
		
		List<Aluno> alunosProximos = new LinkedList<>();
		while(mongoCursor.hasNext()){
			alunosProximos.add(mongoCursor.next());
		}
		
		fecharConexao();
		return alunosProximos;
	}
}
