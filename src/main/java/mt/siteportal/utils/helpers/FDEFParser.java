package mt.siteportal.utils.helpers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import nz.siteportal.objects.Score;

/**
 * A very Simple XML DOM Parser made specifically to parse Form .fdef files to
 * extract Groups, Question Texts and their Possible Answers.
 * 
 * @author Syed A. Zawad
 *
 */
public class FDEFParser {
	
	private Document doc;
	private XPath xPath;
	private String fileName;
	
	private HashMap<String, List<String>> groupsAndQuestions;
	private HashMap<String, String> groupsIdToText;
	private HashMap<String, List<String>> possibleAnswers;
	private List<Score> scores;
	
	public FDEFParser(String fileName){
		this.fileName = fileName;
		xPath = XPathFactory.newInstance().newXPath();
		groupsAndQuestions = new HashMap<String, List<String>>();
		possibleAnswers = new HashMap<String, List<String>>();
		parse();
	}
	
	public boolean parse(){
		System.out.println("Starting to parse file : " + fileName);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setIgnoringComments(true);
		dbf.setIgnoringElementContentWhitespace(true);
		InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
		try {
			System.out.println("Creating a new Document Builder...");
			DocumentBuilder db = dbf.newDocumentBuilder();
			System.out.println("...Done.");
			System.out.println("Creating Document object from xml file...");
			doc = db.parse(is);
			System.out.println("...Done.");
			System.out.println("Parsing the document...");
			generateAll();
		} catch (Exception e) {
			System.out.println("ERROR : There was an issue trying to parse the document.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean generateQuestionsList() throws XPathExpressionException{
		scores = new ArrayList<Score>();
		NodeList items = (NodeList) xPath.compile("/fieldDefinitions/items/item").evaluate(doc, XPathConstants.NODESET);
		for(int i = 0; i<items.getLength(); i++){
			Node attribute_id = items.item(i).getAttributes().getNamedItem("id");
			Node attribute_shortName = items.item(i).getAttributes().getNamedItem("shortName");
			Node attribute_scoreType = items.item(i).getAttributes().getNamedItem("scoreType");
			if(attribute_id != null){
				Score temp = new Score();
				temp.setScore_id(attribute_id.getNodeValue());
				temp.setScore_text(attribute_shortName.getNodeValue());
				temp.setScore_type((attribute_scoreType == null)? null : attribute_scoreType.getNodeValue());
				scores.add(temp);
			}
		}
		return false;
	}
	
	private void generateGroupsList(){
		groupsAndQuestions = new HashMap<String, List<String>>();
		groupsIdToText = new HashMap<String, String>();
		NodeList groups = doc.getElementsByTagName("group");
		for(int i = 0; i<groups.getLength(); i++){
			Element group = (Element)groups.item(i);
			String group_id = group.getAttribute("id");
			String group_text = group.getAttribute("text");
			NodeList group_items = group.getElementsByTagName("item");
			List<String> items = new ArrayList<String>();
			for(int j = 0; j<group_items.getLength(); j++){
				Element item = (Element)group_items.item(j);
				items.add(item.getAttribute("id"));
			}
			groupsAndQuestions.put(group_id, items);
			groupsIdToText.put(group_id, group_text);
		}
	}
	
	private void generatePossibleAnswers(){
		possibleAnswers = new HashMap<String, List<String>>();
		NodeList scoreTypes = doc.getElementsByTagName("scoreType");
		for(int i = 0; i<scoreTypes.getLength(); i++){
			Element scoreType = (Element)scoreTypes.item(i);
			NodeList value_items = scoreType.getElementsByTagName("value");
			List<String> temp_scoreTexts = new ArrayList<String>();
			for(int j = 0; j<value_items.getLength(); j++){
				String items_id = ((Element)value_items.item(j)).getAttribute("text");
				temp_scoreTexts.add(items_id);
			}
			possibleAnswers.put(scoreType.getAttribute("id"), temp_scoreTexts);
		}
	}
	
	private void generateAll() throws XPathExpressionException{
		generateQuestionsList();
		generateGroupsList();
		generatePossibleAnswers();
		for(String key : groupsAndQuestions.keySet()){
			List<String> question_item_ids = groupsAndQuestions.get(key);
			for(String item_id : question_item_ids){
				for(Score score : scores){
					if(score.getScore_id().equals(item_id)){
						score.setGroup_id(key);
						score.setGroup_text(groupsIdToText.get(key));
						break;
					}
				}
			}
		}
		for(Score score : scores){
			List<String> answers =  possibleAnswers.get(score.getScore_type());
			score.setPossibleAnswers(answers);
		}
	}
	
	public List<Score> getScores(){
		return scores;
	}
	
	public HashMap<String, List<String>> getPossibleAnswers(){
		return possibleAnswers;
	}
	
}