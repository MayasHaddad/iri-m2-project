package td.td2;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tools.FrenchStemmer;
import tools.FrenchTokenizer;
import tools.Normalizer;

/**
 * TD 2
 * @author mhadda
 *
 */
public class TD2 {
	/**
	 * Le répertoire du corpus
	 */
	// CHEMIN A CHANGER si nécessaire
	private static String DIRNAME = "/net/k14/u/enseignant/tannier/iri/lemonde/";
	/**
	 * Un fichier de ce répertoire
	 */
	private static String FILENAME = DIRNAME + "texte.95-1.txt";

	/**
	 * Créez une méthode \emph{main} permettant de 
	 * raciniser le texte d'un fichier du corpus.
	 * @param fileName
	 * @throws IOException
	 */
	private static void stemming(String fileName) throws IOException {
		// TODO !
		ArrayList<String> words = (new FrenchStemmer()).normalize(new File(fileName));
		System.out.println(words);
	}
	
	
	
	/**
	 * Une méthode renvoyant le nombre d'occurrences
	 * de chaque mot dans un fichier.
	 * @param fileName le fichier à analyser
	 * @param normalizer la classe de normalisation utilisée
	 * @throws IOException
	 */
	public static HashMap<String, Integer>  getTermFrequencies(String fileName, Normalizer normalizer, boolean removeStopWords) throws IOException {
		// Création de la table des mots
		HashMap<String, Integer> hits = new HashMap<String, Integer>();
		
		// TODO !
		// Appel de la méthode de normalisation
		ArrayList<String> words = normalizer.normalize(fileName, removeStopWords);
		Integer number;
		// Pour chaque mot de la liste, on remplit un dictionnaire
		// du nombre d'occurrences pour ce mot
		for (String word : words) {
			word = word.toLowerCase();
			// on récupère le nombre d'occurrences pour ce mot
			number = hits.get(word);
			// Si ce mot n'était pas encore présent dans le dictionnaire,
			// on l'ajoute (nombre d'occurrences = 1)
			if (number == null) {
				hits.put(word, 1);
			}
			// Sinon, on incrémente le nombre d'occurrence
			else {
				hits.put(word, ++number);
			}
		}

//		// Affichage du résultat
		for (Map.Entry<String, Integer> hit : hits.entrySet()) {
			System.out.println(hit.getKey() + "\t" + hit.getValue());
		}
		return hits;
	}
	/**
	 * Une méthode permettant d'afficher le nombre d'occurrences
	 * de chaque mot pour l'ensemble du corpus.
	 * @param dirName le répertoire à analyser
	 * @param normalizer la classe de normalisation utilisée
	 * @throws IOException
	 */
	private static HashMap<String, Integer> getCollectionFrequency(String dirName, Normalizer normalizer) throws IOException {
		// Création de la table des mots
		HashMap<String, Integer> hits = new HashMap<String, Integer>();
		File dir = new File(dirName);
		String wordLC;
		if (dir.isDirectory()) {
			// Liste des fichiers du répertoire
			// ajouter un filtre (FileNameFilter) sur les noms
			// des fichiers si nécessaire
			String[] fileNames = dir.list();
			
			// Parcours des fichiers et remplissage de la table
			
			// TODO !
			Integer number;
			for (String fileName : fileNames) {
				System.err.println("Analyse du fichier " + fileName);
				// Appel de la méthode de normalisation
				ArrayList<String> words = normalizer.normalize(new File(dirName + File.separator + fileName));
				// Pour chaque mot de la liste, on remplit un dictionnaire
				// du nombre d'occurrences pour ce mot
				for (String word : words) {
					wordLC = word;
					wordLC = wordLC.toLowerCase();
					number = hits.get(wordLC);
					// Si ce mot n'était pas encore présent dans le dictionnaire,
					// on l'ajoute (nombre d'occurrences = 1)
					if (number == null) {
						hits.put(wordLC, 1);
					}
					// Sinon, on incrémente le nombre d'occurrence
					else {
							hits.put(wordLC, ++number);
					}
				}
			}
		}
		return hits;
	}
	
	/**
	 * Une méthode permettant d'afficher le nombre de documents contenant
	 * chaque mot du corpus.
	 * @param dirName le répertoire à analyser
	 * @param normalizer la classe de normalisation utilisée
	 * @throws IOException
	 */
	private static HashMap<String, Integer> getDocumentFrequency(String dirName, Normalizer normalizer, boolean removeStopWords) throws IOException {
		// Création de la table des mots
		HashMap<String, Integer> hits = new HashMap<String, Integer>();
		File dir = new File(dirName);
		String wordLC;
		if (dir.isDirectory()) {
			// Liste des fichiers du répertoire
			// ajouter un filtre (FileNameFilter) sur les noms
			// des fichiers si nécessaire
			String[] fileNames = dir.list();
			
			// Parcours des fichiers et remplissage de la table
			
			// TODO !
			Integer number;
			ArrayList<String> alreadySeenInTheCurrentFile = new ArrayList<String>();
			for (String fileName : fileNames) {
				alreadySeenInTheCurrentFile.clear();
				System.err.println("Analyse du fichier " + fileName);
				// Appel de la méthode de normalisation
				ArrayList<String> words = normalizer.normalize(dirName + File.separator + fileName, removeStopWords);
				// Pour chaque mot de la liste, on remplit un dictionnaire
				// du nombre d'occurrences pour ce mot
				for (String word : words) {
					wordLC = word;
					wordLC = wordLC.toLowerCase();
					number = hits.get(wordLC);
					// Si ce mot n'était pas encore présent dans le dictionnaire,
					// on l'ajoute (nombre d'occurrences = 1)
					if (number == null) {
						hits.put(wordLC, 1);
					}
					// Sinon, on incrémente le nombre d'occurrence
					else {
						if(!alreadySeenInTheCurrentFile.contains(wordLC)){
							hits.put(wordLC, ++number);
						}
					}
					alreadySeenInTheCurrentFile.add(wordLC);
				}
			}
		}

		// Affichage du résultat (avec la fréquence)	
		for (Map.Entry<String, Integer> hit : hits.entrySet()) {
			System.out.println(hit.getKey() + "\t" + hit.getValue());
		}
		return hits;
	}
	
	public static HashMap<String, Double> getTfIdf(String fileName, HashMap<String, Integer> dfs,
			int documentNumber, Normalizer normalizer, boolean removeStopWords) throws IOException{
		HashMap<String, Double> tfIdfs = new HashMap<String, Double>();
		for(Map.Entry<String,Integer> entry : getTermFrequencies(fileName, normalizer, removeStopWords).entrySet()){
			String word = entry.getKey();
			Integer tf = entry.getValue();
			Double idf = Math.log(documentNumber/dfs.get(word));
			Double tfIdf = tf * idf;
			System.out.println(word + "\t" + tfIdf);
			tfIdfs.put(word, tfIdf);
		}
		return tfIdfs;
	}
	
	public static void getWeightFiles(String inDirName, String outDirName, Normalizer normalizer,
			boolean removeStopWords) throws IOException{
		File dir = new File(inDirName);
		if (dir.isDirectory()) {
			String [] fileNames= dir.list();
			int numberDocuments = fileNames.length;
			HashMap<String, Integer> dfs = getDocumentFrequency(inDirName, normalizer, removeStopWords);
			for(String file : fileNames){
				HashMap<String, Double> tfidfs = getTfIdf(inDirName + file, dfs, numberDocuments, normalizer, true);

				PrintWriter writer = new PrintWriter(outDirName + "/" + file + ".poids", "UTF-8");
				
				for (Map.Entry<String, Double> tfidf : tfidfs.entrySet()) {
						writer.println(tfidf.getKey() + "\t" + tfidf.getValue());
				}
						
				writer.println("The second line");
				writer.close();
			}
		}
	}
	
	/**
	 * Main, appels de toutes les méthodes des exercices du TD1. 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO !
		try {
			//stemming(FILENAME);
			Normalizer stemmer = new FrenchStemmer();
			Normalizer tokenizer = new FrenchTokenizer();
			Normalizer[] normalizers = {tokenizer};
			for (Normalizer normalizer : normalizers) {
				//getTermFrequencies(FILENAME, normalizer, true);
				//HashMap<String, Integer> dfs = getDocumentFrequency(DIRNAME, normalizer, false);
				//getTfIdf(FILENAME, dfs, 107, normalizer, true);
				getWeightFiles(DIRNAME, "/net/k3/u/etudiant/mhadda1/IRI/weights", normalizer, true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
