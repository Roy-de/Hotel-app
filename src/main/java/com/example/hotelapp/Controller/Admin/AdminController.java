package com.example.hotelapp.Controller.Admin;

import com.example.hotelapp.Service.AdminService.AdminServiceLayer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private AdminServiceLayer adminServiceLayer;

    public AdminController(AdminServiceLayer adminServiceLayer) {
        this.adminServiceLayer = adminServiceLayer;
    }
    @GetMapping("/get/{credentials}")
    public ResponseEntity<Boolean> get_admin(@PathVariable String credentials){
        boolean isValid = adminServiceLayer.isAdminExist(credentials);
        return ResponseEntity.ok(isValid);
    }
}
