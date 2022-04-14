package sg.edu.rp.c346.id20007649.contacts;

public class ContactModel {

    // variables for our user name
    // and contact number.
    private String name;
    private String contactNumber;

    private String email;

    // constructor
    public ContactModel(String name, String contactNumber, String houseNumber, String email) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
    }

    public ContactModel(String name, String contactNumber) {

        this.name = name;
        this.contactNumber = contactNumber;

    }


    // on below line we have
    // created getter and setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }


    public String getEmail() {
        return email;

    }

    public void setEmail(String email) {
        this.email = email;

    }
}

