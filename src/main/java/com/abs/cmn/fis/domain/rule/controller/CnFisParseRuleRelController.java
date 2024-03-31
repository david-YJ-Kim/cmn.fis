package com.abs.cmn.fis.domain.rule.controller;

import com.abs.cmn.fis.domain.rule.model.CnFisParseRuleRel;
import com.abs.cmn.fis.domain.rule.service.CnFisParseRuleRelService;
import com.abs.cmn.fis.util.code.FisFileType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cn-fis-parse-rule-rels")
public class CnFisParseRuleRelController {

    private final CnFisParseRuleRelService service;

    @Autowired
    public CnFisParseRuleRelController(CnFisParseRuleRelService service) {
        this.service = service;
    }


    @PostMapping("/save")
    public CnFisParseRuleRel saveEntity(@RequestBody CnFisParseRuleRel entity) {
        return service.save(entity);
    }

    @GetMapping("/all")
    public List<CnFisParseRuleRel> getAllEntities() {
        return service.getAllEntities();
    }

    @GetMapping("/eqpId/{eqpId}/fileTyp/{fileTyp}")
    public List<CnFisParseRuleRel> findEntitiesByEqpIdAndFileTyp(
            @PathVariable String eqpId,
            @PathVariable String fileTyp) {
        return service.findCnFisParseRuleRelsByEqpIdAndFileTyp(eqpId, FisFileType.valueOf(fileTyp));
    }
}
