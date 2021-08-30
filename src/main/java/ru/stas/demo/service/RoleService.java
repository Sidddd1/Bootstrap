package ru.stas.demo.service;


import ru.stas.demo.model.Role;
import ru.stas.demo.model.User;

import java.util.List;

public interface RoleService {
    List<Role> getRoles();
    void setRoles(User user, String[] role);
}
