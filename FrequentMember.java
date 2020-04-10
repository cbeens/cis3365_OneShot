package GUI.Class;

public class FrequentMember   {

    private String memberName;
    private String memberType;
    private String memberVisits;


    //getter and setter for member visits
    public String getMemberVisits() {
        return memberVisits;
    }
    public void setMemberVisits(String since) {
        this.memberVisits = since;
    }

    //getter and setter for member name
    public String getMemberName() {
        return memberName;
    }
    public void setMemberName(String name) {
        this.memberName = name;
    }

    //getter and setter for member type
    public String getMemberType() {
        return memberType;
    }
    public void setMemberType(String type) {
        this.memberType = type;
    }


    @Override
    public String toString() {
        return "Member{" +
                ", Name='" + memberName + '\'' +
                ", Type ='" + memberType + '\'' +
                ", Visits='" + memberVisits + '\'' +
                '}';
    }


    //constructor...
    public FrequentMember(String name, String type, String visits) {
        this.memberName = name;
        this.memberType = type;
        this.memberVisits = visits;
    }
}
