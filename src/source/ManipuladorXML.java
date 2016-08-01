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
				System.out.println("Analisando curriculo " + curriculo.getName());
				if (curriculo.isDirectory())
					continue;
				Document document = criaDocument(curriculo);

				XPathFactory xPathfactory = XPathFactory.newInstance();
				XPath xpath = xPathfactory.newXPath();
				XPathExpression exprAchaArtigos = xpath
						.compile("/CURRICULO-VITAE/PRODUCAO-BIBLIOGRAFICA/ARTIGOS-PUBLICADOS/ARTIGO-PUBLICADO");
				NodeList nlArtigos = (NodeList) exprAchaArtigos.evaluate(document, XPathConstants.NODESET);
				XPathExpression exprAchaTitulo = xpath.compile("DADOS-BASICOS-DO-ARTIGO/@TITULO-DO-ARTIGO");
				XPathExpression exprAchaAno = xpath.compile("DADOS-BASICOS-DO-ARTIGO/@ANO-DO-ARTIGO");
				XPathExpression exprAchaAutores = xpath.compile("AUTORES");
				XPathExpression exprAchaAreasConhecimento = xpath.compile("//AREAS-DO-CONHECIMENTO");
				XPathExpression exprAchaNomeAreaConhecimento = xpath.compile("@NOME-DA-AREA-DO-CONHECIMENTO");
				XPathExpression exprAchaNomeEspecialidade = xpath.compile("@NOME-DA-ESPECIALIDADE");
				XPathExpression exprAchaUniversidade = xpath.compile("/CURRICULO-VITAE/DADOS-GERAIS/ENDERECO/ENDERECO-PROFISSIONAL/@CODIGO-INSTITUICAO-EMPRESA");
				XPathExpression exprAchaNomeCurriculo = xpath.compile("/CURRICULO-VITAE/DADOS-GERAIS/@NOME-COMPLETO");
				String codUniversidade = (String) exprAchaUniversidade.evaluate(document, XPathConstants.STRING);
				String nomeCurriculo = (String) exprAchaNomeCurriculo.evaluate(document, XPathConstants.STRING);

				for (int i = 0; i < nlArtigos.getLength(); i++) {
					Node artigo = nlArtigos.item(i);

					// consulta titulo e ano do artigo
					String titulo = (String) exprAchaTitulo.evaluate(artigo, XPathConstants.STRING);
					String ano = (String) exprAchaAno.evaluate(artigo, XPathConstants.STRING);
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


						Pessoa pessoaux = pessoaExiste(nomeCompletoAutor, idCNPQAutor);
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

						//adiciono codigo da univesidade no autor dono do curriculo
						if(pessoaux.getNome().equals(nomeCurriculo)){
							pessoaux.setCodUniversidade(codUniversidade);
						}else{
							pessoaux.setCodUniversidade("Não é da UFSC");;
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

						//recupero elementos "areas de conhecimento"
						NodeList nlAreasConhecimento = (NodeList) exprAchaAreasConhecimento.evaluate(document, XPathConstants.NODESET);
						XPathExpression exprAchaArea = xpath.compile("//*[starts-with(name(), 'AREA-DO-CONHECIMENTO')]");

						for (int x=0; x < nlAreasConhecimento.getLength() ;x++){
							Node nodeAreas = nlAreasConhecimento.item(x);
							NodeList nlAreasConhecimentoNome = (NodeList) exprAchaArea.evaluate(nodeAreas, XPathConstants.NODESET);

							//recupero elemento "area de conhecimento-x"
							for(int s=0;s < nlAreasConhecimentoNome.getLength();s++){
								Node nodeNomeAreaConhecimento = nlAreasConhecimentoNome.item(s);
								String nomeArea = (String) exprAchaNomeAreaConhecimento.evaluate(nodeNomeAreaConhecimento, XPathConstants.STRING);
								String nomeEspecialidade = (String) exprAchaNomeEspecialidade.evaluate(nodeNomeAreaConhecimento, XPathConstants.STRING);
								if(nomeArea != "" && nomeEspecialidade != ""){
									pessoaux.getAreas().put(nomeEspecialidade, nomeArea);

								}
								//System.out.println("nome da area "+ s +": "+nomeArea);
								//System.out.println("especialidade "+ s +": "+nomeEspecialidade);
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

	public static boolean saoUFSC(Pessoa p1,Pessoa p2){
		return p1.getCodUniversidade().equals("004300000009") && p1.getCodUniversidade().equals("004300000009");
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

		}
		return null;
	}

	public static Pessoa pessoaExiste(String nome, String id){
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
			throw new RuntimeException("Erro na leitura dos currÃ­culos");
		} catch (SAXException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro na leitura dos currÃ­culos");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro na leitura dos currÃ­culos");
		}
		return document;
	}
}
