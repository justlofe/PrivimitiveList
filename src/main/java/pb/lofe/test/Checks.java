package pb.lofe.test;

public class Checks {

    public static void check(boolean bool, String message) {
        if(!bool) throw new RuntimeException(message);
    }

}
