package server;

import java.io.Serializable;

public class UserRecord implements Serializable {

    String name;
    String surname;
    String organization;
    String report;
    String email;

    public UserRecord (String name, String surname, String organization, String report, String email){
        this.name = name;
        this.surname = surname;
        this.organization = organization;
        this.report = report;
        this.email = email;
    }

    public UserRecord(){
    }

    @Override
    public String toString(){
        return "RegisteredConferee\n" + "name: " + name + ", surname: " + surname + ", organization: " + organization + ", report: " + report + ", email: " + email;
    }
}