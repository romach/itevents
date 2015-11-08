package org.itevents.util.mail;
import org.itevents.model.Event;
import org.itevents.model.Otp;
import org.itevents.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.List;

@Component
public class MailBuilderUtil {
    @Value("classpath:utils/mailBuilder/mail.xsl")
    private Resource emailTemplateXslResource;
    @Value("classpath:utils/mailBuilder/UserOtpMail.xsl")
    private Resource EmailUserOtpTemplateXslResource;

    public String buildHtmlFromEventsList(List<Event> events) throws ParseException, JAXBException, IOException,
            TransformerException {
        return buildMailFromXmlEvents(buildXmlFromEventList(events));
    }
    // ����� 48
    public String buildHtmlFromUserOtp(User user, Otp otp)  throws ParseException, JAXBException, IOException,
            TransformerException {
        return buildMailFromXmlUserOtp(BuildXmlFromUserOtp(user, otp));
    }

    private String buildXmlFromEventList(List<Event> events) throws JAXBException {
        EventsXmlNodeWrapper eventsXmlNodeWrapper = new EventsXmlNodeWrapper();
        eventsXmlNodeWrapper.setEvents(events);

        Marshaller marshaller = JAXBContext.newInstance(EventsXmlNodeWrapper.class)
                .createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

        StringWriter eventsInXmlStringWriter = new StringWriter();
        marshaller.marshal(eventsXmlNodeWrapper, eventsInXmlStringWriter);
        return eventsInXmlStringWriter.toString();
    }

    private String buildMailFromXmlEvents(String eventsInXml) throws IOException, TransformerException {
        Transformer transformer =
                TransformerFactory.newInstance().newTransformer(
                        new StreamSource(emailTemplateXslResource.getFile())
                );

        StringWriter mailStringWriter = new StringWriter();
        transformer.transform(
                new StreamSource(new StringReader(eventsInXml)),
                new StreamResult(mailStringWriter)
        );

        return mailStringWriter.toString();
    }

    //    ����� 48
    private String BuildXmlFromUserOtp(User user, Otp otp) throws JAXBException {
        UserOtpXmlWrapper userOtpXmlWrapper = new UserOtpXmlWrapper();
        userOtpXmlWrapper.setUser(user);
        userOtpXmlWrapper.setOtp(otp);
        userOtpXmlWrapper.setURL();
        Marshaller marshaller = JAXBContext.newInstance(UserOtpXmlWrapper.class).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

        StringWriter UserOtpInXmlStringWriter = new StringWriter();
        marshaller.marshal(userOtpXmlWrapper, UserOtpInXmlStringWriter);
        return UserOtpInXmlStringWriter.toString();
    }

    //    ����� 48
    private String buildMailFromXmlUserOtp(String userOtp)  throws IOException, TransformerException {
        Transformer transformer =
                TransformerFactory.newInstance().newTransformer(
                        new StreamSource(EmailUserOtpTemplateXslResource.getFile())
                );
        StringWriter mailStringWriter = new StringWriter();
        transformer.transform(
                new StreamSource(new StringReader(userOtp)),
                new StreamResult(mailStringWriter)
        );
        return mailStringWriter.toString();
    }

    @XmlRootElement(name = "events")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class EventsXmlNodeWrapper {

        @XmlElement(name = "event")
        private List<Event> events;

        public EventsXmlNodeWrapper() {
        }

        public List<Event> getEvents() {
            return events;
        }

        public void setEvents(List<Event> events) {
            this.events = events;
        }
    }

    @XmlRootElement(name ="userOtp")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class UserOtpXmlWrapper {
        @XmlElement(name = "url")
        private String url;
        @XmlElement(name = "user")
        private User user;
        @XmlElement(name = "otp")
        private Otp otp;

        public UserOtpXmlWrapper() {
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Otp getOtp() {
            return otp;
        }

        public void setOtp(Otp otp) {
            this.otp = otp;
        }

        public String getURL() {
            return url;
        }

        public void setURL() {
            InetAddress ip;
            String hostname;
            try {
                ip = InetAddress.getLocalHost();
                hostname = ip.getHostName();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }
}