package nlp.com;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CleanXmlAnnotator;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import edu.stanford.nlp.util.CoreMap;

public class SampleTest {

	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException {

		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		File inputFile = new File(
				"C://Users/Savan Kumar/Desktop/Fall16/NLP/Project/dev/a8.crf");

		getCrfLstParser(inputFile);

		getSentenceTreeLst(pipeline, inputFile);

	}

	private static void getSentenceTreeLst(StanfordCoreNLP pipeline,
			File inputFile) throws IOException {
		String text = IOUtils.slurpFile(inputFile);
		Annotation doc1 = new Annotation(text.replaceAll("\\n\\n", "."));
		pipeline.annotate(doc1);
		int sentNo = 0;
		List<CoreMap> sentences = doc1
				.get(CoreAnnotations.SentencesAnnotation.class);

		for (CoreMap sentence : sentences) {
			List<CoreLabel> tokens = sentence
					.get(CoreAnnotations.TokensAnnotation.class);
			System.out.println("Sentence #" + ++sentNo + ": "
					+ sentence.get(CoreAnnotations.TextAnnotation.class).replaceAll("<.*?>",
							""));
			Annotation tempString = new Annotation(sentence.toString());
			/*System.out.println(tempString.replace("<[^>]+>", ""));
			System.out.println(tempString.replace("<.*?>", ""));
			System.out.println(tempString);*/

			for (CoreLabel token : tokens) {
				String word = token.get(TextAnnotation.class);
				String pos = token.get(PartOfSpeechAnnotation.class);
				String ne = token.get(NamedEntityTagAnnotation.class);
				// System.out.println(word + " " + pos + " " + ne);
			}
			CoreMap sentence1 =(CoreMap) tempString.get(CoreAnnotations.SentencesAnnotation.class);
			//System.out.println("++++"+sentence1.get(TreeAnnotation.class));
			Tree tree = sentence.get(TreeAnnotation.class);
			System.out.println("+" + tree.toString());
			List<String> childList = new ArrayList<>();
			Tree flatTree = tree.flatten();
			List<Tree> treeLst = tree.getLeaves();
			TregexPattern pattern = TregexPattern.compile("@NP");
			TregexMatcher matcher = pattern.matcher(tree);
			while (matcher.find()) {
				Tree match = matcher.getMatch();
				List<Tree> leaves = match.getLeaves();
				System.out.println(leaves);
			}

			for (Tree tree2 : treeLst) {

				//if (tree2.label().value().equals("NP"))
				//	System.out.println(tree2.nodeNumber(tree));
				// System.out.println(tree2.depth(tree));;
			}

		}
	}

	private static void getCrfLstParser(File inputFile)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		// doc.getDocumentElement().normalize();
		Element docEl = doc.getDocumentElement();
		Node childNode = docEl.getFirstChild();

		while (childNode.getNextSibling() != null) {
			childNode = childNode.getNextSibling();
			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				Element childElement = (Element) childNode;
				// System.out.println(childElement.getFirstChild().getTextContent()
				// + "\n");
			}
		}
	}

}
