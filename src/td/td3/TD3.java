package td.td3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import tools.FrenchStemmer;
import tools.FrenchTokenizer;
import tools.Normalizer;

public class TD3 {
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
		System.out.println(fileName);
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
			//	System.out.println(hit.getKey() + "\t" + hit.getValue());
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
					//		writer.println(tfidf.getKey() + "\t" + tfidf.getValue());
				}
				writer.close();
			}
		}
	}

	public static TreeMap<String, TreeSet<String>> getInvertedFile(String dirName,
			Normalizer normalizer, boolean removeStopWords) throws IOException{
		TreeMap<String, TreeSet<String>> invertedFileTreeMap = new TreeMap<String, TreeSet<String>>();

		// Création de la table des mots
		File dir = new File(dirName);
		String wordLC;
		if (dir.isDirectory()) {
			// Liste des fichiers du répertoire
			// ajouter un filtre (FileNameFilter) sur les noms
			// des fichiers si nécessaire
			String[] fileNames = dir.list();

			// Parcours des fichiers et remplissage de la table

			ArrayList<String> alreadySeenInTheCurrentFile = new ArrayList<String>();
			for (String fileName : fileNames) {
				alreadySeenInTheCurrentFile.clear();
				System.err.println("Analyse du fichier " + fileName);
				// Appel de la méthode de normalisation
				ArrayList<String> words = normalizer.normalize(dirName + File.separator + fileName, removeStopWords);
				// Pour chaque mot de la liste, on remplit le dictionnaire de fichier inverse
				// du nom du fichier actuel
				for (String word : words) {
					wordLC = word;
					wordLC = wordLC.toLowerCase();
					TreeSet<String> content = new TreeSet<String>();
					content = invertedFileTreeMap.get(wordLC);

					// Si ce mot n'était pas encore présent dans le dictionnaire,
					// on l'ajoute (liste des fichiers le contenant = nom fichier actuel)
					if (content == null) {
						TreeSet<String> newContent = new TreeSet<String>();
						newContent.add(fileName);
						invertedFileTreeMap.put(wordLC, newContent);
					}
					// Sinon, on ajoute � la liste existante, le nom de fichier actuel
					else {
						if(!alreadySeenInTheCurrentFile.contains(wordLC)){
							content.add(fileName);
							invertedFileTreeMap.put(wordLC, content);
						}
					}
					alreadySeenInTheCurrentFile.add(wordLC);
				}
			}
		}
		return invertedFileTreeMap;
	}

	/*
	 * Question 2
	 * TreeSet remplac� par ArrayList (Sous r�serve, prof)
	 */
	public static void saveInvertedFile(TreeMap<String, TreeSet<String>> invertedFile, File outFile)
			throws IOException{
		PrintWriter writer = new PrintWriter(outFile + ".inverted", "UTF-8");

		for (Map.Entry<String, TreeSet<String>> line : invertedFile.entrySet()) {
			writer.println(line.getKey() + "\t" + line.getValue().size() + "\t" + line.getValue());
		}
		writer.close();
	}

	/*
	 * Question 3
	 */

	public static TreeMap<String, TreeMap<String, Double>> getInvertedFileWithWeights(
			String dirName, Normalizer normalizer, boolean removeStopWords) throws IOException{

		//TreeMap<String, ArrayList<String>> invertedFile = getInvertedFile(DIRNAME, normalizer, removeStopWords);

		TreeMap<String, TreeMap<String, Double>> invertedFileWithWeightsMap = new TreeMap<String, TreeMap<String, Double>>();

		HashMap<String, Integer> dfs = getDocumentFrequency(dirName, normalizer, removeStopWords);

		File dir = new File(dirName);

		if (dir.isDirectory()) {
			String[] fileNames = dir.list();
			for (String fileName : fileNames) {
				for(Map.Entry<String, Double> tfIdfEntry : getTfIdf(dirName + "/" + fileName, dfs, 107, normalizer, removeStopWords).entrySet()){
					TreeMap<String, Double> myNewFileNameWeightTreeMap = new TreeMap<String, Double>();

					if(invertedFileWithWeightsMap.get(tfIdfEntry.getKey()) == null){
						myNewFileNameWeightTreeMap.put(fileName, tfIdfEntry.getValue());
					}else{

						myNewFileNameWeightTreeMap = invertedFileWithWeightsMap.get(tfIdfEntry.getKey());
						myNewFileNameWeightTreeMap.put(fileName, tfIdfEntry.getValue());
					}
					System.out.println(myNewFileNameWeightTreeMap);
					invertedFileWithWeightsMap.put(tfIdfEntry.getKey(), myNewFileNameWeightTreeMap);
				}
			}
		}
		return invertedFileWithWeightsMap;
	}
	// L'int�r�t de cette m�thode est simplement d'ordonner la liste des fichiers pour obtenir un null en applicant diff aux deux fichiers
	public static TreeSet<String> getFilesListOrdered(String filesList) throws IOException {
	
		TreeSet<String> listOrdered = new TreeSet<String>();
		
		filesList = filesList.replace("[","");
		filesList = filesList.replace("]",",");
		
		for(String element : filesList.split(",")){
			listOrdered.add(element.trim());
		}
	
		return listOrdered;
	}
	/*
	 * FUSION DES INDEXES
	 */
	public static void mergeInvertedFiles(File invertedFile1, File invertedFile2,
			File mergedInvertedFile) throws IOException {
		PrintWriter writer = new PrintWriter(mergedInvertedFile + ".inverted.merged", "ISO-8859-1");

		InputStream ipsInvertedFile1 = new FileInputStream(invertedFile1);
		InputStream ipsInvertedFile2 = new FileInputStream(invertedFile2);

		InputStreamReader ipsrInvertedFile1 = new InputStreamReader(ipsInvertedFile1);
		InputStreamReader ipsrInvertedFile2 = new InputStreamReader(ipsInvertedFile2);

		BufferedReader brInvertedFile1 = new BufferedReader(ipsrInvertedFile1);
		BufferedReader brInvertedFile2 = new BufferedReader(ipsrInvertedFile2);

		String lineInvertedFile1 = brInvertedFile1.readLine();
		String lineInvertedFile2 = brInvertedFile2.readLine();

		String wordInvertedFile1 = new String();
		String wordInvertedFile2 = new String();

		while (lineInvertedFile1 != null && lineInvertedFile2 != null){

			wordInvertedFile1 = lineInvertedFile1.split(new String("\t"))[0];
			wordInvertedFile2 = lineInvertedFile2.split(new String("\t"))[0];
			String outputLine = new String();
			if (wordInvertedFile1.equals(wordInvertedFile2)){

				Integer sumOfDfs = new Integer(lineInvertedFile1.split(new String("\t"))[1]);
				sumOfDfs += new Integer(lineInvertedFile2.split(new String("\t"))[1]);
				String firstListOfFiles = lineInvertedFile1.split(new String("\t"))[2];
				String secondListOfFiles = lineInvertedFile2.split(new String("\t"))[2];

				String listOfFiles = firstListOfFiles + secondListOfFiles;
				outputLine = wordInvertedFile1 + "\t" + sumOfDfs + "\t" + getFilesListOrdered(listOfFiles);

				lineInvertedFile1 = brInvertedFile1.readLine();
				lineInvertedFile2 = brInvertedFile2.readLine();
			}

			if(wordInvertedFile1.compareTo(wordInvertedFile2) < 0){
				outputLine = lineInvertedFile1;
				lineInvertedFile1 = brInvertedFile1.readLine();
			}

			if(wordInvertedFile1.compareTo(wordInvertedFile2) > 0){
				outputLine = lineInvertedFile2;
				lineInvertedFile2 = brInvertedFile2.readLine();
			}
			writer.println(outputLine);
		}

		if(lineInvertedFile1 == null){
			while(lineInvertedFile2 != null){
				writer.println(lineInvertedFile2);
				lineInvertedFile2 = brInvertedFile2.readLine();
			}
		}else{
			if(lineInvertedFile2 == null){
				while(lineInvertedFile1 != null){
					writer.println(lineInvertedFile1);
					lineInvertedFile1 = brInvertedFile1.readLine();
				}
			}
		}
		brInvertedFile1.close();
		brInvertedFile2.close();
		writer.close();


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
			Normalizer[] normalizers = {stemmer};
			for (Normalizer normalizer : normalizers) {

				/*// Premiere partie : Fichier Inverse
				// Q1
				System.out.println(getInvertedFile(DIRNAME, normalizer, true));
				// Q2
				saveInvertedFile(getInvertedFile(DIRNAME, normalizer, true), (new File("/net/k3/u/etudiant/mhadda1/IRI/invertedFile.txt")));
				// Q3
				System.out.println(getInvertedFileWithWeights(DIRNAME, normalizer, true));*/
				saveInvertedFile(getInvertedFile("/net/k14/u/enseignant/tannier/iri/lemonde-sub1/", normalizer, true), (new File("/net/k3/u/etudiant/mhadda1/IRI/invertedFile_1.txt")));
				saveInvertedFile(getInvertedFile("/net/k14/u/enseignant/tannier/iri/lemonde-sub2/", normalizer, true), (new File("/net/k3/u/etudiant/mhadda1/IRI/invertedFile_2.txt")));
				mergeInvertedFiles(new File("/net/k3/u/etudiant/mhadda1/IRI/invertedFile_1.txt.inverted"), 
						new File("/net/k3/u/etudiant/mhadda1/IRI/invertedFile_2.txt.inverted"), 
						new File("/net/k3/u/etudiant/mhadda1/IRI/mergedInvertedFile.txt"));
				//System.out.println(getFilesListOrdered("[ b,c,a,d ][ z,y,x ]"));

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
