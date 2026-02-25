package ac.lk.foe.uoj.ims.controller;

import ac.lk.foe.uoj.ims.dto.CapitalInventoryRequestDTO;
import ac.lk.foe.uoj.ims.service.CapitalInventoryRequestService;
import ac.lk.foe.uoj.ims.utill.StandardResponce;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/capitalrequest")
@RequiredArgsConstructor
public class CapitalInventoryRequestController {

    private final CapitalInventoryRequestService service;

    // CREATE
    @PostMapping("/create")
    public ResponseEntity<StandardResponce> create(@RequestBody CapitalInventoryRequestDTO dto){

        CapitalInventoryRequestDTO response = service.createRequest(dto);

        return new ResponseEntity<>(
                new StandardResponce(201,"Created",response,null),
                HttpStatus.CREATED);
    }

    // UPDATE STATUS (Approve Flow)
    @PutMapping("/update/{id}")
    public ResponseEntity<StandardResponce> updateStatus(@PathVariable Long id,
                                                         @RequestParam String email){
        try{
            CapitalInventoryRequestDTO response = service.updateStatus(id,email);
            return new ResponseEntity<>(
                    new StandardResponce(200,"Updated",response,null),
                    HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(
                    new StandardResponce(400,"Error",e.getMessage(),null),
                    HttpStatus.BAD_REQUEST);
        }
    }

    // REJECT
    @PutMapping("/reject/{id}")
    public ResponseEntity<StandardResponce> reject(@PathVariable Long id,
                                                   @RequestParam String email){
        CapitalInventoryRequestDTO response = service.rejectRequest(id,email);
        return new ResponseEntity<>(
                new StandardResponce(200,"Rejected",response,null),
                HttpStatus.OK);
    }
}