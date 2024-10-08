package com.nicolaspaiva.finance_vault.admin;

import com.nicolaspaiva.finance_vault.admin.dto.UserNotFound;
import com.nicolaspaiva.finance_vault.admin.service.AdminService;
import com.nicolaspaiva.finance_vault.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@Slf4j
public class AdminController {

    private final AdminService adminService;

    // TODO: Implement pagination

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(){
        return new ResponseEntity<>(adminService.getDashboard(), HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@RequestParam (defaultValue = "asc") String sort){
        return ResponseEntity.ok(adminService.getAllUsers(sort));
    }

    @GetMapping("/users/search")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email){

        Optional<UserEntity> user = adminService.getUserByEmail(email);

        if(user.isPresent()){
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new UserNotFound(), HttpStatus.NOT_FOUND);
        }
    }

}
