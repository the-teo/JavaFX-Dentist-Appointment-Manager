package repository;

import domain.Patient;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import service.XmlListWrapper;

import java.util.List;

public class PatientXmlRepo extends AbstractXmlRepo<Integer, Patient> {
    public PatientXmlRepo(String fileName) {
        super(fileName, Patient.class);
    }

    @Override
    protected Class<? extends XmlListWrapper<Patient>> getWrapperClass() {
        return XmlListWrapperPatient.class;
    }

}

@XmlRootElement(name = "patients")
@XmlAccessorType(XmlAccessType.FIELD)
class XmlListWrapperPatient extends XmlListWrapper<Patient> {
    @XmlElement(name = "Patient")
    private List<Patient> list;

    @Override
    public List<Patient> getList() {
        return list;
    }

    @Override
    public void setList(List<Patient> list) {
        this.list = list;
    }
}





