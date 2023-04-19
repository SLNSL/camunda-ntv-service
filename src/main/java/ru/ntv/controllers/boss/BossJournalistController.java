package ru.ntv.controllers.boss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ntv.dto.response.boss.JournalistListResponse;
import ru.ntv.dto.response.boss.JournalistResponse;
import ru.ntv.exception.NotRightRoleException;
import ru.ntv.service.UserService;

import java.util.List;

@RestController
@Validated
@RequestMapping("journalists")
public class BossJournalistController {
    
    @Autowired
    UserService userService;

    @GetMapping(params = "id")
    ResponseEntity<JournalistResponse> getJournalistById(@RequestParam int id){
        final JournalistResponse journalist = userService.getJournalistById(id);
        
        return ResponseEntity.ok(journalist);
    }

    @GetMapping
    ResponseEntity<JournalistListResponse> getAllJournalists(){
        final List<JournalistResponse> journalists = userService.getAllJournalists();

        return ResponseEntity.ok(new JournalistListResponse(journalists));
    }

    @DeleteMapping
    ResponseEntity<?> dismissJournalist(@RequestParam int id) throws NotRightRoleException {

        return ResponseEntity.ok(userService.dismissJournalist(id));
    }
}