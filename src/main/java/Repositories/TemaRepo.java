package Repositories;

import Domain.Tema;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class TemaRepo extends AbstractRepository<Long, Tema> {

    public TemaRepo(String fileName, String tagname) {
        super(fileName, tagname);
    }

    @Override
    public Tema parseline(Element e) {

        String id=e.getAttribute("id");

        Node n1=e.getElementsByTagName("descriere").item(0);
        String descriere=n1.getTextContent();

        Node n2=e.getElementsByTagName("startweek").item(0);
        int startweekk=Integer.parseInt(n2.getTextContent());

        Node n3=e.getElementsByTagName("deadline").item(0);
        int deadline=Integer.parseInt(n3.getTextContent());

        Tema tema= new Tema(descriere,startweekk,deadline);
        tema.setId(Long.parseLong(id));
        return tema;
    }

    private static Node createTemaElement(Document doc, String name, String value)
    {
        Element node=doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }

    @Override
    public Node toxml(Document doc, Tema entity) {
        Element tema=doc.createElement("tema");

        tema.setAttribute("id",entity.getId().toString());
        tema.appendChild(createTemaElement(doc,"descriere",entity.getDescriere()));
        tema.appendChild(createTemaElement(doc,"startweek",Integer.toString(entity.getStartWeek())));
        tema.appendChild(createTemaElement(doc,"deadline",Integer.toString(entity.getDeadlineWeek())));
        return tema;
    }


}