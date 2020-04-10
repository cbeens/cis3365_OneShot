package GUI.Class;

public class ActiveMember {


        private String memberName;
        private String memberType;
        private String memberSince;


        //getter and setter for member since
        public String getMemberSince() {
            return memberSince;
        }
        public void setMemberSince(String since) {
            this.memberSince = since;
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
                    ", Visits='" + memberSince + '\'' +
                    '}';
        }


        //constructor...
        public ActiveMember(String name, String type, String since) {
            this.memberName = name;
            this.memberType = type;
            this.memberSince = since;
        }


}
