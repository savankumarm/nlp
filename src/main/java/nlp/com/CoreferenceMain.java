package nlp.com;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class CoreferenceMain {

	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException {

		File inputFile = new File(
				"C://Users/Savan Kumar/Desktop/Fall16/NLP/Project/dev/a9.crf");

		List<CorefObj> corefObjLst = getCrfLstParser(inputFile);
		List<SentenceObj> sentenceObjLst = getSentenceTreeLst(inputFile);

		List<SentenceObj> nsentenceObjLst = matchCorefToSentence(
				sentenceObjLst, corefObjLst);
	}

	private static List<SentenceObj> matchCorefToSentence(
			List<SentenceObj> sentenceObjLst, List<CorefObj> corefObjLst) {
		List<SentenceObj> nsentenceObjs = new ArrayList<>();
		for (SentenceObj sentObj : sentenceObjLst) {

			for (CorefObj corefObj : corefObjLst) {
				List<CorefObj> ncorefObjLst = new ArrayList<>();
				if (sentObj.getSentenceXML().contains(
						"ID=" + '"' + corefObj.corefId + '"')) {
					// System.out.println(sentObj.getSentenceId()+"- "
					// + sentObj.sentence + "--"+ corefObj.coref );
					// System.out.println(sentObj.getTree().toString());
					findAntecedentLst(sentObj, corefObj);
					// add the coref list
					ncorefObjLst.add(corefObj);
				}

				sentObj.setCorefObjs(ncorefObjLst);
			}
			nsentenceObjs.add(sentObj);
		}
		return nsentenceObjs;
	}

	private static void findAntecedentLst(SentenceObj sentObj, CorefObj corefObj) {
		List<Tree> leavesLst = sentObj.getTree().getLeaves();

		for (Iterator<Tree> iterator = leavesLst.iterator(); iterator.hasNext();) {
			Tree leaf = (Tree) iterator.next();
			
			
			if (leaf.toString().contains("ID=" + '"' + corefObj.corefId + '"')) {
				StringBuilder NP = new StringBuilder();
				leaf = (Tree) iterator.next();
				NP.append(leaf);
				System.out.println(leaf.parent(sentObj.getTree()));
				System.out.println(leaf.depth(sentObj.getTree()));
			}
		}

		/*
		 * for (Tree leaf : leavesLst) { if (leaf.toString().contains("ID=" +
		 * '"' + corefObj.corefId + '"')) {
		 * System.out.println(leaf.parent(sentObj.getTree()).label().value());
		 * if (leaf.parent(sentObj.getTree()).label().value().equals("NP"))
		 * System.out.println(leaf.nodeNumber(sentObj.getTree())); //
		 * fetchParent(leaf,sentObj.getTree());
		 * 
		 * if (leaf.parent(sentObj.getTree()).parent(sentObj.getTree())
		 * .toString().contains("</COREF>")) {
		 * System.out.println(leaf.parent(sentObj.getTree())
		 * .parent(sentObj.getTree()).toString()); leaf.siblings(root); } else {
		 * // System.out.println(leaf.parent(sentObj.getTree()) //
		 * .parent(sentObj.getTree()).parent(sentObj.getTree()).toString ()); }
		 * 
		 * } }
		 */
	}

	private static List<SentenceObj> getSentenceTreeLst(File inputFile)
			throws IOException {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		String text = IOUtils.slurpFile(inputFile);
		// Annotation doc1 = new Annotation(text.replaceAll("<.*?>", ""));
		Annotation doc1 = new Annotation(text.replaceAll("\\n\\n", "."));
		pipeline.annotate(doc1);
		int sentNo = 0;
		List<CoreMap> sentences = doc1
				.get(CoreAnnotations.SentencesAnnotation.class);
		List<SentenceObj> sentenceObjs = new ArrayList<>();
		for (CoreMap sentence : sentences) {

			List<CoreLabel> tokens = sentence
					.get(CoreAnnotations.TokensAnnotation.class);
			SentenceObj obj = new SentenceObj();
			obj.setSentence(sentence.get(CoreAnnotations.TextAnnotation.class)
					.replace("<[^>]+>", ""));
			obj.setSentenceXML((sentence
					.get(CoreAnnotations.TextAnnotation.class)));
			obj.setSentenceId(++sentNo);
			obj.setTokens(tokens);
			List<TokenObj> tokenObjLst = new ArrayList<>();
			TokenObj tokenObj;
			for (CoreLabel token : tokens) {
				tokenObj = new TokenObj();
				tokenObj.setWord(token.get(TextAnnotation.class));
				tokenObj.setPos(token.get(PartOfSpeechAnnotation.class));
				tokenObj.setNe(token.get(NamedEntityTagAnnotation.class));
				// System.out.println(word + " " + pos + " " + ne);
				tokenObjLst.add(tokenObj);
			}
			obj.setTokenObjs(tokenObjLst);
			Tree tree = sentence.get(TreeAnnotation.class);
			obj.setTree(tree);
			sentenceObjs.add(obj);
			List<String> childList = new ArrayList<>();
			int depth = tree.numChildren();
			for (int j = 0; j < depth - 1; j++) {
				childList.add(tree.getChild(j).nodeString());
			}
		}
		return sentenceObjs;
	}

	private static List<CorefObj> getCrfLstParser(File inputFile)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		// doc.getDocumentElement().normalize();
		Element docEl = doc.getDocumentElement();
		Node childNode = docEl.getFirstChild();
		List<CorefObj> corefObjs = new ArrayList<>();
		// int corefId = 0;
		while (childNode.getNextSibling() != null) {
			CorefObj corefObj = new CorefObj();
			childNode = childNode.getNextSibling();
			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				Element childElement = (Element) childNode;
				corefObj.setCoref(childElement.getFirstChild().getTextContent());
				corefObj.setCorefId(new Integer(childElement.getAttributes()
						.item(0).getNodeValue()));
				corefObj.setCorefXML("<COREF ID = " + corefObj.getCorefId()
						+ ">" + corefObj.getCoref() + "</COREF>");
				corefObjs.add(corefObj);

				// System.out.println(childElement.getFirstChild().getTextContent()
				// + "\n");
			}
		}
		return corefObjs;
	}

}
