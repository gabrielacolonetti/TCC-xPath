package source;


import java.util.List;
import main.ClusteringProcess;
import utility.datatypes.TextFilesUtility;

public class TextFilesUtilityCALC extends TextFilesUtility {
	
	public static void addDataObjects(ClusteringProcess process, List<JaccardSimilaridade> listaDeCoeficientes) {
	
			for (JaccardSimilaridade jaccardSimilaridade : listaDeCoeficientes) {
				int index = 0;
				String title = jaccardSimilaridade.getP1();
				String content =  jaccardSimilaridade.getP2();
				TextFileTCC dataObject = new TextFileTCC(title, content, index);
			
				dataObject.setListaDeCoeficientes(listaDeCoeficientes);
				
				process.addDataObject(dataObject);
				index++;
		
			}
		
	}
}
