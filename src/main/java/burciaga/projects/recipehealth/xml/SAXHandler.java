package burciaga.projects.recipehealth.xml;


import burciaga.projects.recipehealth.xml.PubmedArticle;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;


/**
 * SAXHandler object is instantiated in XMLParser.java and is passed to a new SAXParser instance.
 * SAXHandler class overrides DefaultHandler methods to customize XML parsing.
 */
public class SAXHandler extends DefaultHandler {

    private List<PubmedArticle> pubmedArticleList = null;
    private PubmedArticle article = null;
    private HashSet<String> meshSet = null;
    private List<Integer> pmidList = null;

    private boolean pmid = false;
    private boolean title = false;
    private boolean abs = false;
    private boolean chemical = false;
    private boolean meshHeading = false;


    public List<PubmedArticle> getArticleList() {
        return pubmedArticleList;
    }

    public HashSet<String> meshSet() {
        return meshSet;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attribute)
            throws SAXException {
        switch(qName) {
            case "PubmedArticle":
                article = new PubmedArticle();
                meshSet = new HashSet<>();
                pmidList = new ArrayList<>();
                if (pubmedArticleList == null) {
                    pubmedArticleList = new ArrayList<>();
                    break;
                }
            case "PMID":
                pmid = true;
                break;
            case "ArticleTitle":
                title = true;
                break;
            case "AbstractText":
                abs = true;
                break;
            case "NameOfSubstance":
                chemical = true;
                break;
            case "DescriptorName":
                meshHeading = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("PubmedArticle")) {
            article.setMeshSet(meshSet);
            pubmedArticleList.add(article);
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        String newString = new String(ch, start, length);
        if (pmid) {
            article.setPMID(Integer.parseInt(newString));
            pmidList.add(Integer.parseInt(newString));
            pmid = false;
        } else if (title) {
            article.setTitle(newString);
            title = false;
        } else if (abs) {
            article.setAbs(newString);
            abs = false;
        } else if (chemical) {
            meshSet.add(newString);
            chemical = false;
        } else if (meshHeading) {
            meshSet.add(newString);
            meshHeading = false;
        }
    }


}
