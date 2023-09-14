package ru.drmiki.help_for_capy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.drmiki.help_for_capy.classes.Phrases;
import ru.drmiki.help_for_capy.repo.PhrasesRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    PhrasesRepository phrasesRepository;

    @PostMapping(path="/add")
    public String addNewPhrases(@RequestParam String engName, @RequestParam String rusName){

        Phrases newPhrase = new Phrases();
        newPhrase.setEngName(engName);
        newPhrase.setRusName(rusName);
        phrasesRepository.save(newPhrase);
        return "redirect:/";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Phrases> showAll () {

        return phrasesRepository.findAll();
    }

    @GetMapping(path="/addnew")
    public String addnew(){
        return "addnew";
    }

    @RequestMapping(value = "/")
    public String cardController(Model model) {
        var map = phrasesRepository.findAll();
        List<Phrases> list = new ArrayList<>();
        map.iterator().forEachRemaining(list::add);

        var randomIndex = list.size() * Math.random();
        var el = list.get((int) randomIndex);
        model.addAttribute("message1", el.getEngName());
        model.addAttribute("message2", el.getRusName());
        return "index";
    }

    @GetMapping (value = "/test")
    public String testController() {
        return "test";
    }
}
