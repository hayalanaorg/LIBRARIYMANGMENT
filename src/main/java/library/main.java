package library;
public class main {
    public static void main(String[] args) {
        libraryy library = new libraryy();

        // إضافة كتب (US1.3)
        library.addBook(new Book("Java Basics", "John Doe", "123"));
        library.addBook(new Book("Python 101", "Sara Lee", "456"));
        library.addBook(new Book("C++ for Beginners", "Ali Ahmad", "789"));

        // البحث عن كتاب (US1.4)
        System.out.println("Search results for 'python':");
        for (Book b : library.searchBook("Python")) {
            System.out.println(b);
        }
    }
}
