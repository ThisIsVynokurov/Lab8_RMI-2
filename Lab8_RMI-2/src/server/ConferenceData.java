package server;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ConferenceData {

    private ArrayList<UserRecord> arrList ;

    public ConferenceData(){
        arrList = new ArrayList<>();
    }

    public void exportToXML(String path){
        Transformer trf;
        DOMSource src;
        FileOutputStream file;

        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element element = document.createElement("RegisteredConferees");

            for (int i = 0; i < arrList.size(); i++) {
                Element user = document.createElement("Conferee");

                Element e1 = document.createElement("name");
                e1.setTextContent(arrList.get(i).name);
                Element e2 = document.createElement("surname");
                e2.setTextContent(arrList.get(i).surname);
                Element e3 = document.createElement("organization");
                e3.setTextContent(arrList.get(i).organization);
                Element e4 = document.createElement("report");
                e4.setTextContent(arrList.get(i).report);
                Element e5 = document.createElement("e-mail");
                e5.setTextContent(arrList.get(i).email);
                appendMultiple(user, e1, e2, e3, e4, e5);
                element.appendChild(user);

            }
            document.appendChild(element);
            trf = TransformerFactory.newInstance().newTransformer();
            trf.setOutputProperty(OutputKeys.ENCODING, "Windows-1251");
            trf.setOutputProperty(OutputKeys.INDENT, "yes");
            trf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            src = new DOMSource(document);
            file = new FileOutputStream(path);

            StreamResult result = new StreamResult(file);
            trf.transform(src, result);
        }
        catch (Exception e){
            System.err.println(e);
        }
    }

    private Element appendMultiple (Element body, Element...elements){
        for (Element e:elements) {
            body.appendChild(e);
        }
        return body;
    }

    public void addUser(UserRecord rec){

        arrList.add(rec);}
    public void loadStruct(String path) throws ParserConfigurationException, SAXException {
        arrList = new ArrayList<>();

        SAXParser saxParser = SAXParserFactory.newDefaultInstance().newSAXParser();
        DataHandler handler = new DataHandler();
        try {
            saxParser.parse(new File(path),handler);
            arrList.addAll(handler.getStruct());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<UserRecord> getStruct() {
        return arrList;
    }
}

class DataHandler extends DefaultHandler {

    private int curUser = 0;

    private String curTag;

    private ArrayList<UserRecord> dataStruct;

    public DataHandler() {
    }

    public ArrayList<UserRecord> getStruct() {
        System.err.println(dataStruct.size() + "" + dataStruct.get(1));
        return dataStruct;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        curTag = qName;
        if (qName.compareTo("List") == 0) {
            if (dataStruct != null) {
                return;
            }
            dataStruct = new ArrayList<>();
        } else if (qName.compareTo("User") == 0) {
            dataStruct.add(new UserRecord());
            curUser++;
        } else if (qName.compareTo("name") == 0) {
            dataStruct.get(curUser - 1).name = qName;
        } else if (qName.compareTo("surname") == 0) {
            dataStruct.get(curUser - 1).surname = qName;
        } else if (qName.compareTo("organization") == 0) {
            dataStruct.get(curUser - 1).organization = qName;
        } else if (qName.compareTo("report") == 0) {
            dataStruct.get(curUser - 1).report = qName;
        } else if (qName.compareTo("e-mail") == 0) {
            dataStruct.get(curUser - 1).email = qName;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String string;
        if (!(string = new String(ch, start, length)).isBlank()) {
            System.out.println(string);
            if (curTag.compareTo("name") == 0) {
                dataStruct.get(curUser - 1).name = string;
            } else if (curTag.compareTo("surname") == 0) {
                dataStruct.get(curUser - 1).surname = string;
            } else if (curTag.compareTo("organization") == 0) {
                dataStruct.get(curUser - 1).organization = string;
            } else if (curTag.compareTo("report") == 0) {
                dataStruct.get(curUser - 1).report = string;
            } else if (curTag.compareTo("e-mail") == 0) {
                dataStruct.get(curUser - 1).email = string;
            }
        }
    }
}