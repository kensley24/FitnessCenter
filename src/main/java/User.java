
abstract class User extends Main{

    int id;
    int name;
    public User() {
        this.id = id;
        this.name = name;
    }

    public abstract int login(int id, String name);
    public abstract void display();
    public abstract void signup();

}
