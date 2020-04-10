package GUI.Class;

public class PopularMember {

    private String memberCount;
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
    public String getMemberCount() {
        return memberCount;
    }
    public void setMemberCount(String count) {
        this.memberCount = count;
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
                ", Type='" + memberType + '\'' +
                ", Count ='" + memberCount + '\'' +
                ", Visits='" + memberVisits + '\'' +
                '}';
    }


    //constructor...
    public PopularMember(String type, String count, String visits) {
        this.memberType = type;
        this.memberCount = count;
        this.memberVisits = visits;
    }
}
