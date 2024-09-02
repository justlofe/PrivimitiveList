import pb.lofe.test.SafeList;


public class TestLength {

    public static void main(String[] args) {
        SafeList<String> list = new SafeList<>();
        list.add("test");
        list.addAll("val", "123");

        System.out.println(list.size());

        System.out.println("---");
        list.forEach(System.out::println);

        System.out.println("---");
        System.out.println(list.remove(1));

        System.out.println("---");
        list.forEach(System.out::println);
    }

}
