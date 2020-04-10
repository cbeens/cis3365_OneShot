package GUI.Class;

public class NewMember {

    private String memberCount;
    private String memberSince;



    //getter and setter for member visits
    public String getMemberSince() {
        return memberSince;
    }
    public void setMemberSince(String since) {
        this.memberSince = since;
    }

    //getter and setter for member name
    public String getMemberCount() {
        return memberCount;
    }
    public void setMemberCount(String count) {
        this.memberCount = count;
    }


    @Override
    public String toString() {
        return "Member{" +
                ", Count ='" + memberCount + '\'' +
                ", Visits='" + memberSince + '\'' +
                '}';
    }


    //constructor...
    public NewMember(String count, String since) {
        this.memberCount = count;
        this.memberSince = since;
    }
}
