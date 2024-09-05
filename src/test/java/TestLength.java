import pb.lofe.test.SafeList;


public class TestLength {

    public static void main(String[] args) {
        SafeList<String> list = new SafeList<>();
        list.add("test");
        list.addAll("val", "123");

        val("size", list.size());
        ln();

        System.out.println(list.remove(1));
        ln();

        list.forEach(System.out::println);
        ln();

        ln("Array: ");
        for (Object string: list.toArray(new String[0])) {
            System.out.println(string);
        }
    }

    public static void ln() {
        System.out.println();
    }

    public static void ln(String ln) {
        System.out.println(ln);
    }

    public static void val(String name, Object val) {
        System.out.printf("%s: %s\n", name, val);
    }

}
