package source;

import static org.simmetrics.builders.StringMetricBuilder.with;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.simmetrics.StringMetric;
import org.simmetrics.metrics.OverlapCoefficient;
import org.simmetrics.simplifiers.Simplifiers;
import org.simmetrics.tokenizers.Tokenizers;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import model.Pessoa;
import model.Publicacao;
import model.Tupla;

public class ManipuladorXML {

	// static List<Documento> listaDeDocumentos = new ArrayList<Documento>();
	// public static List<Pessoa> listaDePessoasXML = new ArrayList<Pessoa>();
	static List<Publicacao> listaPublicacoes = new ArrayList<Publicacao>();
	static List<Tupla> listaDePares = new ArrayList<Tupla>();
	static Map<String, Pessoa> mapaPessoas = new HashMap<String, Pessoa>();
	static List<Publicacao> listaPublicacoesAutoresUFSC = new ArrayList<Publicacao>();
	static Map<String,List<Pessoa>> mapaAutoresUFSC = new HashMap<String,List<Pessoa>>();
	static StringMetric metric =
	            with(new OverlapCoefficient<String>())
	            .simplify(Simplifiers.toLowerCase(Locale.ENGLISH))
	            .simplify(Simplifiers.replaceNonWord())
	            .tokenize(Tokenizers.whitespace())
	            .build();

	public static Map<String, Pessoa> getMapaPessoas() {
		return mapaPessoas;
	}

	public static List<Publicacao> geraListaDePublicacoes(List<File> listaDeCurriculoXML) {
		try {

			for (File curriculo : listaDeCurriculoXML) {
				System.out.println("Analisando curriculo " + curriculo.getName());
				if (curriculo.isDirectory())
					continue;
				Document document = criaDocument(curriculo);

				XPathFactory xPathfactory = XPathFactory.newInstance();
				XPath xpath = xPathfactory.newXPath();
				XPathExpression exprAchaArtigos = xpath.compile("/CURRICULO-VITAE/PRODUCAO-BIBLIOGRAFICA/ARTIGOS-PUBLICADOS/ARTIGO-PUBLICADO|/CURRICULO-VITAE/PRODUCAO-BIBLIOGRAFICA/TRABALHOS-EM-EVENTOS/TRABALHO-EM-EVENTOS");
				NodeList nlArtigos = (NodeList) exprAchaArtigos.evaluate(document, XPathConstants.NODESET);
				XPathExpression exprAchaTitulo = xpath.compile("DADOS-BASICOS-DO-ARTIGO/@TITULO-DO-ARTIGO|DADOS-BASICOS-DO-TRABALHO/@TITULO-DO-TRABALHO");
				XPathExpression exprAchaAno = xpath.compile("DADOS-BASICOS-DO-ARTIGO/@ANO-DO-ARTIGO|DADOS-BASICOS-DO-TRABALHO/@ANO-DO-TRABALHO");
				XPathExpression exprAchaAutores = xpath.compile("AUTORES");
				XPathExpression exprAchaNomePrincipal = xpath.compile("/CURRICULO-VITAE/DADOS-GERAIS/@NOME-COMPLETO");

				for (int i = 0; i < nlArtigos.getLength(); i++) {
					Node artigo = nlArtigos.item(i);

					// consulta titulo e ano do artigo
					String titulo = (String) exprAchaTitulo.evaluate(artigo, XPathConstants.STRING);
					String ano = (String) exprAchaAno.evaluate(artigo, XPathConstants.STRING);
					String nomeDonoArtigo = (String) exprAchaNomePrincipal.evaluate(artigo, XPathConstants.STRING);
					// System.out.println("\n\n\n" + titulo + "-"+ ano);
					Publicacao publicacao = new Publicacao(titulo, ano);
					boolean estaNaLista = false;

					NodeList nlAutores = (NodeList) exprAchaAutores.evaluate(artigo, XPathConstants.NODESET);

					for (int j = 0; j < nlAutores.getLength(); j++) {
						Node autor = nlAutores.item(j);
						//hashmap...
						NamedNodeMap atributosDoAutor = autor.getAttributes();

						// pega elemento nome autor
						String nomeCompletoAutor = atributosDoAutor.getNamedItem("NOME-COMPLETO-DO-AUTOR")
								.getNodeValue();

						// pega tag id autor
						String idCNPQAutor = (atributosDoAutor.getNamedItem("NRO-ID-CNPQ") == null ? ""
								: atributosDoAutor.getNamedItem("NRO-ID-CNPQ").getNodeValue());

						String nomeTratadoAutor="";
						if(nomeCompletoAutor.contains(",")){
							String[] nomeSplit;
							nomeSplit = nomeCompletoAutor.split(",");
							for (int s =nomeSplit.length-1 ; s>=0;s--) {
								if(s==0){
									nomeTratadoAutor += nomeSplit[s];
								}else{
									nomeTratadoAutor += nomeSplit[s]+" ";
								}


							}

							nomeCompletoAutor ="";
							nomeCompletoAutor = nomeTratadoAutor.trim();
							System.out.println(nomeCompletoAutor);
						}



						//System.out.println("nome tratado "+ nomeCompletoAutor);
						Pessoa pessoaux = pessoaExiste(nomeCompletoAutor);
						if (pessoaux == null){
							//System.out.println("Incluindo autor " + nomeCompletoAutor);
							pessoaux = new Pessoa(nomeCompletoAutor, idCNPQAutor);
							mapaPessoas.put(nomeCompletoAutor, pessoaux);
						}

						// verifica se primeiro autor
						if (j == 0) {
							publicacao.setAutor(pessoaux);

						} else {
							publicacao.getCoautores().add(pessoaux);
						}


						if (listaPublicacoes.isEmpty()) {
							listaPublicacoes.add(publicacao);
						} else {
							for (int x = 0; x < listaPublicacoes.size(); x++) {
								if (listaPublicacoes.get(x).getTitulo().trim().equals(publicacao.getTitulo().trim())
										&& listaPublicacoes.get(x).getAno().trim().equals(publicacao.getAno().trim())) {
									estaNaLista = true;
									break;
								} else {
									estaNaLista = false;
								}
							}
							if (!estaNaLista) {
								listaPublicacoes.add(publicacao);
							}

						}

						//recupero elementos "area de conhecimento"
						XPathExpression exprAchaArea = xpath.compile("//*[starts-with(name(), 'AREA-DO-CONHECIMENTO')]");
						NodeList nlAreasConhecimento = (NodeList) exprAchaArea.evaluate(document, XPathConstants.NODESET);

						for (int x=0; x < nlAreasConhecimento.getLength() ;x++){
							Node nodeAreas = nlAreasConhecimento.item(x);
							NamedNodeMap atributosDoArea = nodeAreas.getAttributes();

							// pega elemento nome autor
							String nomeArea = atributosDoArea.getNamedItem("NOME-DA-AREA-DO-CONHECIMENTO")
									.getNodeValue();

							String nomeEspecialidade = atributosDoArea.getNamedItem("NOME-DA-ESPECIALIDADE")
									.getNodeValue();


							if(nomeArea != "" && nomeEspecialidade != "" && pessoaux.getNome().equals(nomeDonoArtigo)){
								pessoaux.getAreas().put(nomeEspecialidade, nomeArea);

							}

						}
					}
					continue;
				}
			}
			return listaPublicacoes;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Map<String,List<Pessoa>> geraListaDePublicacoesUFSC(List<File> listaDeCurriculoXML) {
		try {
			for (File curriculo : listaDeCurriculoXML) {
				System.out.println("Analisando curriculo " + curriculo.getName());
				if (curriculo.isDirectory())
					continue;
				Document document = criaDocument(curriculo);

				XPathFactory xPathfactory = XPathFactory.newInstance();
				XPath xpath = xPathfactory.newXPath();
				XPathExpression exprAchaArtigos = xpath.compile("/CURRICULO-VITAE/PRODUCAO-BIBLIOGRAFICA/ARTIGOS-PUBLICADOS/ARTIGO-PUBLICADO|/CURRICULO-VITAE/PRODUCAO-BIBLIOGRAFICA/TRABALHOS-EM-EVENTOS/TRABALHO-EM-EVENTOS");
				NodeList nlArtigos = (NodeList) exprAchaArtigos.evaluate(document, XPathConstants.NODESET);
				XPathExpression exprAchaTitulo = xpath.compile("DADOS-BASICOS-DO-ARTIGO/@TITULO-DO-ARTIGO|DADOS-BASICOS-DO-TRABALHO/@TITULO-DO-TRABALHO");
				XPathExpression exprAchaAno = xpath.compile("DADOS-BASICOS-DO-ARTIGO/@ANO-DO-ARTIGO|DADOS-BASICOS-DO-TRABALHO/@ANO-DO-TRABALHO");
				XPathExpression exprAchaAutores = xpath.compile("AUTORES");
				XPathExpression exprAchaNomePrincipal = xpath.compile("/CURRICULO-VITAE/DADOS-GERAIS/@NOME-COMPLETO");

				for (int i = 0; i < nlArtigos.getLength(); i++) {
					Node artigo = nlArtigos.item(i);

					// consulta titulo e ano do artigo
					String titulo = (String) exprAchaTitulo.evaluate(artigo, XPathConstants.STRING);
					String ano = (String) exprAchaAno.evaluate(artigo, XPathConstants.STRING);
					String nomeDonoArtigo = (String) exprAchaNomePrincipal.evaluate(artigo, XPathConstants.STRING);
					//System.out.println("\n\n" + titulo + "-"+ ano);

					NodeList nlAutores = (NodeList) exprAchaAutores.evaluate(artigo, XPathConstants.NODESET);

					for (int j = 0; j < nlAutores.getLength(); j++) {
						Node autor = nlAutores.item(j);
						//hashmap...
						NamedNodeMap atributosDoAutor = autor.getAttributes();

						// pega elemento nome autor
						String nomeCompletoAutor = atributosDoAutor.getNamedItem("NOME-COMPLETO-DO-AUTOR")
								.getNodeValue();

						// pega tag id autor
						String idCNPQAutor = (atributosDoAutor.getNamedItem("NRO-ID-CNPQ") == null ? ""
								: atributosDoAutor.getNamedItem("NRO-ID-CNPQ").getNodeValue());
						String nomeTratadoAutor="";
						if(nomeCompletoAutor.contains(",")){
							String[] nomeSplit;
							nomeSplit = nomeCompletoAutor.split(",");
							for (int s =nomeSplit.length-1 ; s>=0;s--) {
								if(s==0){
									nomeTratadoAutor += nomeSplit[s];
								}else{
									nomeTratadoAutor += nomeSplit[s]+" ";
								}

								nomeCompletoAutor = nomeTratadoAutor.trim();
							}
						}


						//System.out.println(nomeCompletoAutor);
						if(nomeCompletoAutor.equals(nomeDonoArtigo)){
							Pessoa pessoaux = pessoaExiste(nomeCompletoAutor);
							if (pessoaux == null){
								//System.out.println("criando pessoa"+nomeCompletoAutor);
								pessoaux = new Pessoa(nomeCompletoAutor, idCNPQAutor);
								mapaPessoas.put(nomeCompletoAutor, pessoaux);
							}
							Teste similaridade = new Teste();
							if(!mapaAutoresUFSC.isEmpty()){
//								double similaridadeValor = similaridade.verificaPalavras(mapaAutoresUFSC, titulo);
//								if(titulo.equals("A Similarity Search Method for Web Forms")){
//									System.out.println(similaridadeValor);
//								}								
								if(!mapaAutoresUFSC.containsKey(titulo.toUpperCase())){
									List<Pessoa> pessoasDaLista = new ArrayList<Pessoa>();
									pessoasDaLista.add(pessoaux);
									mapaAutoresUFSC.put(titulo.toUpperCase(), pessoasDaLista);
								}else{
									if(!verificaPessoaNoMapa(pessoaux,mapaAutoresUFSC, titulo)){
										mapaAutoresUFSC.get(titulo.toUpperCase()).add(pessoaux);
										//System.out.println("adicionando a pessoa "+nomeCompletoAutor);
									}
								}
								for (String file : mapaAutoresUFSC.keySet()) {
									double similaridadeTitulo = metric.compare(file, titulo.toUpperCase());
//									if(titulo.equals("A Similarity Search Approach for Web Forms")){
//										System.out.println(similaridadeTitulo);	
//									}
									if(similaridadeTitulo > 0.8 && !verificaPessoaNoMapa(pessoaux,mapaAutoresUFSC, titulo)){
										System.out.println(similaridadeTitulo+" adicionando "+pessoaux.getNome()+" no "+file +" como "+ titulo);
										mapaAutoresUFSC.get(file.toUpperCase()).add(pessoaux);
									}
								}
							}else{
								List<Pessoa> pessoasDaLista = new ArrayList<Pessoa>();
								pessoasDaLista.add(pessoaux);
								//	System.out.println("adicionando no pela primeira vez "+titulo+" e pessoa "+nomeCompletoAutor);

								mapaAutoresUFSC.put(titulo.toUpperCase(), pessoasDaLista);
							}

							//recupero elementos "area de conhecimento"
							XPathExpression exprAchaArea = xpath.compile("//*[starts-with(name(), 'AREA-DO-CONHECIMENTO')]");
							NodeList nlAreasConhecimento = (NodeList) exprAchaArea.evaluate(document, XPathConstants.NODESET);

							for (int x=0; x < nlAreasConhecimento.getLength() ;x++){
								Node nodeAreas = nlAreasConhecimento.item(x);
								NamedNodeMap atributosDoArea = nodeAreas.getAttributes();

								// pega elemento nome autor
								String nomeArea = atributosDoArea.getNamedItem("NOME-DA-AREA-DO-CONHECIMENTO")
										.getNodeValue();

								String nomeEspecialidade = atributosDoArea.getNamedItem("NOME-DA-ESPECIALIDADE")
										.getNodeValue();


								if(nomeArea != "" && nomeEspecialidade != "" && pessoaux.getNome().equals(nomeDonoArtigo)){
									pessoaux.getAreas().put(nomeEspecialidade, nomeArea);

								}

							}
						}
					}
					continue;
				}
			}
			return mapaAutoresUFSC;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	public static boolean verificaPessoaNoMapa(Pessoa pessoaux, Map<String, List<Pessoa>> mapaAutoresUFSC2, String titulo) {
		if(mapaAutoresUFSC2.containsKey(titulo.toUpperCase())){
			List<Pessoa> pessoas = mapaAutoresUFSC2.get(titulo.toUpperCase());
			for (Pessoa autores : pessoas) {
				if(autores.getNome().trim().equals(pessoaux.getNome().trim())){
					return true;
				}

			}
		}
		return false;
	}

	public static List<Tupla> criaPares() {
		// varre a lista de publicacoes
		for (int i = 0; i < listaPublicacoes.size(); i++) {
			Publicacao publicacao = listaPublicacoes.get(i);
			//			System.out.println("\n\n"+publicacao.getTitulo());

			// varre a lista de coautores de cada publicacao
			for (int j = 0; j < publicacao.getCoautores().size(); j++) {
				Tupla par = existePar(publicacao.getAutor(),publicacao.getCoautores().get(j));
				if (par == null) { //saoUFSC
					par = new Tupla();
					par.setP1(publicacao.getAutor());
					par.setP2(publicacao.getCoautores().get(j));
					par.getPublicacoes().add(publicacao);
					par.setPeso(1);
					listaDePares.add(par);
					//System.out.println(par.getP1().getNome()+"--"+par.getP2().getNome()+"--"+par.getPeso());
				} else {
					if(!checaEAdicionaPublicacao(publicacao, par)){
						par.getPublicacoes().add(publicacao);
						par.setPeso(par.getPeso()+1);
						//System.out.println(par.getP1().getNome()+"--"+par.getP2().getNome()+"--"+par.getPeso());
					}
				}
			}
		}
		return listaDePares;
	}

	public static List<Tupla> criaParesUFSC() {
		// varre a lista de publicacoes
		for (Entry<String, List<Pessoa>> mapa : mapaAutoresUFSC.entrySet()) {
			String publicacao = mapa.getKey();

			// varre a lista de coautores de cada publicacao
			for(int i=0; i< mapa.getValue().size();i++){
				for(int j=0; j< mapa.getValue().size();j++){
					if(!mapa.getValue().get(i).equals(mapa.getValue().get(j))){
						Tupla par = existePar(mapa.getValue().get(i),mapa.getValue().get(j));
						if (par == null) { //saoUFSC

							par = new Tupla();
							par.setP1(mapa.getValue().get(i));
							par.setP2(mapa.getValue().get(j));
							par.getPublicacoesUFSC().add(publicacao);
							par.setPeso(1);
							listaDePares.add(par);
							//System.out.println(par.getP1().getNome()+"--"+par.getP2().getNome()+"--"+par.getPeso());
						} else {
							if(!checaEAdicionaPublicacaoUFSC(publicacao, par)){
								par.getPublicacoesUFSC().add(publicacao);
								par.setPeso(par.getPeso()+1);
								//System.out.println(par.getP1().getNome()+"--"+par.getP2().getNome()+"--"+par.getPeso());
							}
						}
					}else{
						continue;
					}
				}
			}
		}
		return listaDePares;
	}


	public static boolean checaEAdicionaPublicacao(Publicacao publi, Tupla par){

		for(int i=0; i< par.getPublicacoes().size();i++){
			if(par.getPublicacoes().get(i).getTitulo().equals(publi.getTitulo()) && par.getPublicacoes().get(i).getAno().equals(publi.getAno())){
				return true;
			}
		}
		return false;
	}

	public static boolean checaEAdicionaPublicacaoUFSC(String publi, Tupla par){

		for(int i=0; i< par.getPublicacoesUFSC().size();i++){
			if(par.getPublicacoesUFSC().get(i).equals(publi)){
				return true;
			}
		}
		return false;
	}

	public static Tupla existePar(Pessoa p1, Pessoa p2){

		for (Tupla tupla : listaDePares) {
			if (tupla.saoMembros(p1, p2)) {
				return tupla;
			}

		}
		return null;
	}

	public static Pessoa pessoaExiste(String nome){
		return mapaPessoas.get(nome);
	}

	private static Document criaDocument(File curriculo) {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		Document document = null;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.parse(curriculo);
			document.getDocumentElement().normalize();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro na leitura dos currículos");
		} catch (SAXException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro na leitura dos currículos");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro na leitura dos currículos");
		}
		return document;
	}
}
