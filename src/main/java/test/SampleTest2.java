package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Text;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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

public class SampleTest2 {

	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException {

		File inputFile = new File(
				"C://Users/Savan Kumar/Desktop/Fall16/NLP/Project/dev/a9.crf");

		// getCrfLstParser(inputFile);
		findByID(inputFile, "COREF");
		// getSentenceTreeLst(inputFile);

	}

	

	private static void getCrfLstParser(File inputFile)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		doc.getDocumentElement().normalize();
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

	public static void findByID(File inputFile, String idName)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		doc.getDocumentElement().normalize();
		Element docEl = doc.getDocumentElement();
		   NodeList list = docEl.getElementsByTagName(idName);
		   
		   for (int temp = 0; temp < list.getLength(); temp++) {
	            Node nNode = list.item(temp);
	            System.out.println("\nCurrent Element :" 
	                    + nNode.getNodeName()+"  -- "+nNode.getAttributes().item(0).getNodeValue());
		   
		   
		Element name = doc.getElementById(idName);
		
			Text text = (Text) name;
			System.out.println("The ID " + idName + " locates the name "
					+ text.getData());
		}
	}

}
