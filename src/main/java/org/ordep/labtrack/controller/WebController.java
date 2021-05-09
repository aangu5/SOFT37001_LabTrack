package org.ordep.labtrack.controller;

import org.ordep.labtrack.service.CardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class WebController {

    private final CardService cardService;

    public WebController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("allCards", cardService.getAllCards());
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/card/{type}")
    public String chemical(@RequestParam UUID id, @PathVariable String type, Model model) {
        if (type.equals("ChemicalHazardCard")) {
            model.addAttribute("chemicalHazardCard", cardService.findOneChemicalHazardCard(id));
            return "cards/chemical";
        }
        if (type.equals("PhysicalHazardCard")) {
            model.addAttribute("physicalHazardCard", cardService.findOnePhysicalHazardCard(id));
            return "cards/physical";
        }
        if (type.equals("BiologicalHazardCard")) {
            model.addAttribute("biologicalHazardCard", cardService.findOneBiologicalHazardCard(id));
            return "cards/biological";
        }

        return null;
    }
}
