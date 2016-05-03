package source;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
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

	public static List<Publicacao> geraListaDePublicacoes(List<File> listaDeCurriculoXML) {
		try {

			for (File curriculo : listaDeCurriculoXML) {

				if (curriculo.isDirectory())
					continue;
				Document document = criaDocument(curriculo);

				XPathFactory xPathfactory = XPathFactory.newInstance();
				XPath xpath = xPathfactory.newXPath();
				// XPathExpression exprAchaArtigos =
				// xpath.compile("//ARTIGO-PUBLICADO");
				XPathExpression exprAchaArtigos = xpath
						.compile("/CURRICULO-VITAE/PRODUCAO-BIBLIOGRAFICA/ARTIGOS-PUBLICADOS/ARTIGO-PUBLICADO");
				NodeList nlArtigos = (NodeList) exprAchaArtigos.evaluate(document, XPathConstants.NODESET);

				XPathExpression exprAchaTitulo = xpath.compile("DADOS-BASICOS-DO-ARTIGO/@TITULO-DO-ARTIGO");
				XPathExpression exprAchaAno = xpath.compile("DADOS-BASICOS-DO-ARTIGO/@ANO-DO-ARTIGO");
				XPathExpression exprAchaAutores = xpath.compile("AUTORES");

				for (int i = 0; i < nlArtigos.getLength(); i++) {
					Node artigo = nlArtigos.item(i);

					// Node nodeTitulo = (Node)exprAchaTitulo.evaluate(artigo,
					// XPathConstants.NODE);
					// String titulo = nodeTitulo.getNodeValue();

					// escreve na tela o título e ano do artigo
					String titulo = (String) exprAchaTitulo.evaluate(artigo, XPathConstants.STRING);
					String ano = (String) exprAchaAno.evaluate(artigo, XPathConstants.STRING);
					// System.out.println("\n\n\n" + titulo + "-"+ ano);
					Publicacao publicacao = new Publicacao(titulo, ano);
					boolean estaNaLista = false;

					NodeList nlAutores = (NodeList) exprAchaAutores.evaluate(artigo, XPathConstants.NODESET);

					for (int j = 0; j < nlAutores.getLength(); j++) {
						Node autor = nlAutores.item(j);
						

						// este é um hashmap...
						NamedNodeMap atributosDoAutor = autor.getAttributes();

						// pega tag nome autor
						String nomeCompletoAutor = atributosDoAutor.getNamedItem("NOME-COMPLETO-DO-AUTOR")
								.getNodeValue();
						
//						System.out.println("Tratando autor " + nomeCompletoAutor);

						// if (atributosDoAutor.getNamedItem("NRO-ID-CNPQ") ==
						// null) {
						// idCNPQAutor = "";
						// } else {
						// idCNPQAutor =
						// atributosDoAutor.getNamedItem("NRO-ID-CNPQ").getNodeValue());
						// }
						// pega tag id autor
						String idCNPQAutor = (atributosDoAutor.getNamedItem("NRO-ID-CNPQ") == null ? ""
								: atributosDoAutor.getNamedItem("NRO-ID-CNPQ").getNodeValue());
						
						
						Pessoa pessoaux = pessoaExiste(nomeCompletoAutor, idCNPQAutor);
						if (pessoaux == null){
//							System.out.println("Incluindo autor " + nomeCompletoAutor);
							pessoaux = new Pessoa(nomeCompletoAutor, idCNPQAutor);
							mapaPessoas.put(nomeCompletoAutor, pessoaux);

						}
							
						// verifica se é autor
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

						// System.out.println(nomeCompletoAutor + " - " +
						// idCNPQAutor );

					}
					continue;

				}

			}
			return listaPublicacoes;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static List<Tupla> criaPares() {

		// varre a lista de publicacoes
		for (int i = 0; i < listaPublicacoes.size(); i++) {
			 Publicacao publicacao = listaPublicacoes.get(i);
//			System.out.println("\n\n"+publicacao.getTitulo());

			// varre a lista de coautores de cada publicacao
			for (int j = 0; j < publicacao.getCoautores().size(); j++) {

				Tupla par = existePar(publicacao.getAutor(),publicacao.getCoautores().get(j));
				
				if (par == null) {
					par = new Tupla();
					par.setP1(publicacao.getAutor());
					par.setP2(publicacao.getCoautores().get(j));
					par.getPublicacoes().add(publicacao);
					par.setPeso(1);
					listaDePares.add(par);
//					System.out.println("entrou no if");
//					System.out.println(par.getP1().getNome()+"--"+par.getP2().getNome()+"--"+par.getPeso());
				} else {
					if(!checaEAdicionaPublicacao(publicacao, par)){
						par.getPublicacoes().add(publicacao);
						par.setPeso(par.getPeso()+1);
//						System.out.println("entrou no else");
//						System.out.println(par.getP1().getNome()+"--"+par.getP2().getNome()+"--"+par.getPeso());
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
	public static Tupla existePar(Pessoa p1, Pessoa p2){
		
		for (Tupla tupla : listaDePares) {
			if (tupla.saoMembros(p1, p2)) {
				return tupla;
			}
//			if(tupla.getP1().getId().equals(tupla.getP2().getId())|| tupla.getP1().getNome().equals(tupla.getP2().getNome())){
//				//lancar excecao
//			}
//			if(p1.getId().equals(tupla.getP1().getId()) && p2.getId().equals(tupla.getP2().getId()) || p1.getNome().equals(tupla.getP1().getId()) && p2.getNome().equals(tupla.getP2().getNome())){
//				return tupla;
//			}
//			if(p1.getId().equals(tupla.getP2().getId()) && p2.getId().equals(tupla.getP1().getId()) || p1.getNome().equals(tupla.getP2().getNome()) && p2.getNome().equals(tupla.getP1().getNome())){
//				return tupla;
//			}
			
		}
		return null;
	}
	
	public static Pessoa pessoaExiste(String nome, String id){
		return mapaPessoas.get(nome);
		
		
//		for (String s : mapaPessoas.keySet()) {
//			if(s.equals(nome) || s.equals(id)){
//				Pessoa p = mapaPessoas.get(s);
//				return p;
//			}
//		}
//
//
//		return null;
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
