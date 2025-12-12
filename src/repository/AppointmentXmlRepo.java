package repository;

import domain.Appoitment;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import service.XmlListWrapper;

public class AppointmentXmlRepo extends AbstractXmlRepo<Integer, Appoitment> {

    public AppointmentXmlRepo(String fileName) {
        super(fileName, Appoitment.class);
    }

    @Override
    protected Class<? extends XmlListWrapper<Appoitment>> getWrapperClass() {
        return  XmlListWrapperAppointment.class;
    }

}


@XmlRootElement(name = "appointments")
@XmlAccessorType(XmlAccessType.FIELD)
class XmlListWrapperAppointment extends XmlListWrapper<Appoitment> {

    @jakarta.xml.bind.annotation.XmlElement(name = "Appointment")
    private java.util.List<Appoitment> list;

    @Override
    public java.util.List<Appoitment> getList() {
        return list;
    }

    @Override
    public void setList(java.util.List<Appoitment> list) {
        this.list = list;
    }
}
