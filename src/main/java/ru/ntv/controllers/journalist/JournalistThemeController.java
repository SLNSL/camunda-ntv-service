package ru.ntv.controllers.journalist;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ntv.dto.request.admin.CreateThemeRequest;
import ru.ntv.entity.Theme;
import ru.ntv.service.ThemesService;

@RestController
@RequestMapping("themes")
@Validated
public class JournalistThemeController {
    
    @Autowired
    private ThemesService themesService;
    
    @PostMapping()
    ResponseEntity<Theme> create(@Valid @RequestBody CreateThemeRequest req){
        final var theme = themesService.create(req);

        return ResponseEntity.ok(theme);
    }

    @DeleteMapping(params = "id")
    ResponseEntity<?> delete(@RequestParam int id){
        themesService.delete(id);

        return ResponseEntity.ok("OK");
    }
}