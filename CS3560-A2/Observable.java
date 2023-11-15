public interface Observable extends Observer{
     public void follow(Observer follower);
     public void receive(String str);
 }