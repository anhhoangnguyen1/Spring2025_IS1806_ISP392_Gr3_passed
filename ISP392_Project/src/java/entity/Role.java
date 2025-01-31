package entity;



import Model.*;
import java.util.List;

public class Role {
    private int roleId;
    private String name;
    private List<Users> users;

    // Getters and Setters

    public Role() {
    }

    public Role(int roleId, String name, List<Users> users) {
        this.roleId = roleId;
        this.name = name;
        this.users = users;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }
    
}
