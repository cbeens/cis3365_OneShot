package GUI.Class;

public class CountMember {

    private String memberCount;
    private String memberLocation;



    //getter and setter for member visits
    public String getMemberLocation() {
        return memberLocation;
    }
    public void setMemberLocation(String location) {
        this.memberLocation = location;
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
                ", Location ='" + memberLocation + '\'' +
                ", Count='" + memberCount + '\'' +
                '}';
    }


    //constructor...
    public CountMember(String count, String location) {
        this.memberCount = count;
        this.memberLocation = location;
    }
}
