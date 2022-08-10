package cn.tuyucheng.taketoday.controller.parameterized;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class EmployeeRoleController {
    private static final Map<String, Role> userRoleCache = new HashMap<>();

    static {
        userRoleCache.put("John", Role.ADMIN);
        userRoleCache.put("Doe", Role.EMPLOYEE);
    }

    @ResponseBody
    @GetMapping(value = "/role/{name}", produces = "application/text;charset=UTF-8")
    public String getEmployeeRole(@PathVariable("name") String employeeName) {
        return userRoleCache.get(employeeName).toString();
    }

    private enum Role {
        ADMIN, EMPLOYEE
    }
}