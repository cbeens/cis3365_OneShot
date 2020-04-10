package GUI.Class;

public class membershipPts {


        private String memberName;
        private String memberType;
        private String memberPts;


        //getter and setter for member points
        public String getMemberPts() {
            return memberPts;
        }
        public void setMemberPts(String memberPts) {
            this.memberPts = memberPts;
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
                    ", Points='" + memberPts + '\'' +
                    '}';
        }


        //constructor...
        public membershipPts(String name, String type, String points) {
            this.memberName = name;
            this.memberType = type;
            this.memberPts = points;
        }


}
