package com.softdevsix.argos.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softdevsix.argos.domain.RulesRequestMap;
import com.softdevsix.argos.service.RulesService;

@RestController
@RequestMapping("/argos/rules")
public class RulesHandler {
  private final RulesService service;

  @Autowired
  public RulesHandler(RulesService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<Void> createRules(@RequestParam Integer project, @RequestBody RulesRequestMap requestData) {
    service.handleRules(requestData, project);
    return ResponseEntity.ok().build();
  }
}
