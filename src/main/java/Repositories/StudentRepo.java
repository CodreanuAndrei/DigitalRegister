package Repositories;


import Domain.Student;
import Validator.ValidatorContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class StudentRepo extends AbstractRepository<Long, Student> {

    public StudentRepo(String fileName, String tagname) {
        super(fileName, tagname);
    }


    @Override
    public Student parseline(Element e) {
        String id=e.getAttribute("id");

        Node n1=e.getElementsByTagName("nume").item(0);
        String nume=n1.getTextContent();

        Node n2=e.getElementsByTagName("prenume").item(0);
        String prenume=n2.getTextContent();

        Node n3=e.getElementsByTagName("grupa").item(0);
        String grupa=n3.getTextContent();

        Node n4=e.getElementsByTagName("mail").item(0);
        String mail=n4.getTextContent();

        Node n5=e.getElementsByTagName("indrumator").item(0);
        String indrumator=n5.getTextContent();

        Student st= new Student(nume,prenume,grupa,mail,indrumator);
        st.setId(Long.parseLong(id));
        return st;
    }

    private static Node createStElement(Document doc,String name, String value)
    {
        Element node=doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }

    @Override
    public Node toxml(Document doc, Student student) {

        Element st=doc.createElement("student");

        st.setAttribute("id",student.getId().toString());
        st.appendChild(createStElement(doc,"nume",student.getName()));
        st.appendChild(createStElement(doc,"prenume",student.getPrenume()));
        st.appendChild(createStElement(doc,"grupa",student.getGrupa()));
        st.appendChild(createStElement(doc,"mail",student.getEmail()));
        st.appendChild(createStElement(doc,"indrumator",student.getIndrumator()));
        return st;
    }


}
