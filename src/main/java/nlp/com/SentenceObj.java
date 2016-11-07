package nlp.com;

import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.Tree;

public class SentenceObj {
	int sentenceId;
	String sentence;
	String sentenceXML;
	List<CorefObj> corefObjs;
	Tree tree;
	List<TokenObj> tokenObjs;
	
	
	
	List<CoreLabel> tokens;
	String grammer;
	
	
	
	public String getSentenceXML() {
		return sentenceXML;
	}

	public void setSentenceXML(String sentenceXML) {
		this.sentenceXML = sentenceXML;
	}

	public List<TokenObj> getTokenObjs() {
		return tokenObjs;
	}

	public void setTokenObjs(List<TokenObj> tokenObjs) {
		this.tokenObjs = tokenObjs;
	}

	public List<CoreLabel> getTokens() {
		return tokens;
	}

	public void setTokens(List<CoreLabel> tokens) {
		this.tokens = tokens;
	}

	public String getGrammer() {
		return grammer;
	}

	public void setGrammer(String grammer) {
		this.grammer = grammer;
	}

	public int getSentenceId() {
		return sentenceId;
	}

	public void setSentenceId(int sentenceId) {
		this.sentenceId = sentenceId;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public List<CorefObj> getCorefObjs() {
		return corefObjs;
	}

	public void setCorefObjs(List<CorefObj> corefObjs) {
		this.corefObjs = corefObjs;
	}

	public Tree getTree() {
		return tree;
	}

	public void setTree(Tree tree) {
		this.tree = tree;
	}

}
