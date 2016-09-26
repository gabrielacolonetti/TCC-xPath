package source;

import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Pessoa;

public class Teste {

    protected static float checkSimilarity(String sString1, String sString2) throws Exception {

        // Se as strings têm tamanho distinto, obtêm a similaridade de todas as
        // combinações em que tantos caracteres quanto a diferença entre elas são
        // inseridos na string de menor tamanho. Retorna a similaridade máxima
        // entre todas as combinações, descontando um percentual que representa
        // a diferença em número de caracteres.
        if(sString1.length() != sString2.length()) {
            int iDiff = Math.abs(sString1.length() - sString2.length());
            int iLen = Math.max(sString1.length(), sString2.length());
            String sBigger, sSmaller, sAux;

            if(iLen == sString1.length()) {
                sBigger = sString1;
                sSmaller = sString2;
            }
            else {
                sBigger = sString2;
                sSmaller = sString1;
            }

            float fSim, fMaxSimilarity = Float.MIN_VALUE;
            for(int i = 0; i <= sSmaller.length(); i++) {
                sAux = sSmaller.substring(0, i) + sBigger.substring(i, i+iDiff) + sSmaller.substring(i);
                fSim = checkSimilaritySameSize(sBigger,  sAux);
                if(fSim > fMaxSimilarity)
                    fMaxSimilarity = fSim;
            }
            return fMaxSimilarity - (1f * iDiff) / iLen;

        // Se as strings têm o mesmo tamanho, simplesmente compara-as caractere
        // a caractere. A similaridade advém das diferenças em cada posição.
        } else
            return checkSimilaritySameSize(sString1, sString2);
    }

    protected static float checkSimilaritySameSize(String sString1, String sString2) throws Exception {

        if(sString1.length() != sString2.length())
            throw new Exception("Strings devem ter o mesmo tamanho!");

        int iLen = sString1.length();
        int iDiffs = 0;

        // Conta as diferenças entre as strings
        for(int i = 0; i < iLen; i++)
            if(sString1.charAt(i) != sString2.charAt(i))
                iDiffs++;

        // Calcula um percentual entre 0 e 1, sendo 0 completamente diferente e
        // 1 completamente igual
        return 1f - (float) iDiffs / iLen;
    }


    
    public double verificaPalavras(Map<String,List<Pessoa>> mapaAutoresUFSC, String titulo) throws Exception{
    	double similaridadeTitulo = 0.0;
    	for (String chaveTitulo : mapaAutoresUFSC.keySet()) {
			if(chaveTitulo.length() == titulo.length()){
				similaridadeTitulo = (double) checkSimilaritySameSize(chaveTitulo.toUpperCase(), titulo.toUpperCase());
			}else{
				similaridadeTitulo = (double) checkSimilarity(chaveTitulo.toUpperCase(), titulo.toUpperCase());

			}
		}
    	return similaridadeTitulo;
    	
    }
  }