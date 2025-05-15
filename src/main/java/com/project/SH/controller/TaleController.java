package com.project.SH.controller;

import com.project.SH.model.impl.Tale;
import com.project.SH.service.impl.TaleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/Tales")

public class TaleController {

    private final TaleService service;

    public TaleController(TaleService service) {
        this.service = service;
    }

    @GetMapping
    public List<Tale> findAllTale(){
        return service.findAllTales();
    }

    @PostMapping("/save_Tale")
    public Tale saveTale(@RequestBody Tale tale) {
        return service.saveTale(tale);
    }

    @GetMapping("/{taleID}")
    public Tale findByID(@PathVariable int taleID) {
        return service.findByID(taleID);
    }

    @PutMapping("update_Tale")
    public Tale updateTale(@RequestBody Tale tale) {
        return service.updateTale(tale);
    }

    @DeleteMapping("delete_Tale/{taleID}")
    public void deleteTale(int taleID) {
        service.deleteTale(taleID);
    }
}

