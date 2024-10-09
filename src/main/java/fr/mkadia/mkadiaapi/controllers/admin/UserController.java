package fr.mkadia.mkadiaapi.controllers.admin;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/users")
@PreAuthorize("hasRole('USER')")
public class UserController {

    @GetMapping
    public String sayHello(){
        return "hello";
    }


}
