package com.softdevsix.argos.handler;
import com.softdevsix.argos.domain.RulesRequestMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/argos/rules")
public class RulesHandler {
    private final RulesRequestMap rulesRequestMap;

    @Autowired
    public RulesHandler() {
        rulesRequestMap = new RulesRequestMap() ;
    }
    @PostMapping
    public ResponseEntity<Void> createRules(@RequestBody RulesRequestMap requestData) {
        rulesRequestMap.mapFromJson(requestData);
        return ResponseEntity.ok().build();
    }
}
