package com.softdevsix.argos.handler;

import com.softdevsix.argos.domain.Rules;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.softdevsix.argos.domain.RulesRequestMap;
import com.softdevsix.argos.service.RulesService;

@Controller
@RequestMapping("/argos/rules")
public class RulesHandler {
  private final RulesService service;

  public RulesHandler(RulesService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<Rules> createRules(@RequestParam Integer project, @RequestBody RulesRequestMap requestData) {
    Rules createdRules = service.handleRules(requestData, project);
    return ResponseEntity.ok(createdRules);
  }
}
