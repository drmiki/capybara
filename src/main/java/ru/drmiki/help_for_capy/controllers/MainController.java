package ru.drmiki.help_for_capy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.drmiki.help_for_capy.classes.Category;
import ru.drmiki.help_for_capy.classes.Phrases;
import ru.drmiki.help_for_capy.classes.User;
import ru.drmiki.help_for_capy.repo.CategoryRepository;
import ru.drmiki.help_for_capy.repo.PhrasesRepository;
import ru.drmiki.help_for_capy.repo.UserRepository;

import java.util.*;

@Controller
public class MainController {
    @Autowired
    PhrasesRepository phrasesRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping(path = "/add")
    public String addNewPhrases(@RequestParam String engName, @RequestParam String rusName, @RequestParam Long[] categories) {

        Phrases newPhrase = new Phrases();
        newPhrase.setEngName(engName);
        newPhrase.setRusName(rusName);
        List<Category> listCat = new ArrayList<>();
        for (Long category : categories) {
            listCat.add(categoryRepository.getReferenceById(category));
        }
        newPhrase.setCategory(listCat);
        User serchUser = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        //newPhrase.setUser((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        newPhrase.setUser(serchUser);
        phrasesRepository.save(newPhrase);
        return "redirect:/";
    }

    @PostMapping(path = "/addcategory")
    public String addNewCategory(@RequestParam String name) {

        Category newCategory = new Category();
        newCategory.setName(name);
        categoryRepository.save(newCategory);
        return "redirect:/";
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Phrases> showAll() {

        return phrasesRepository.findAll();
    }

    @GetMapping(path = "/addnew")
    public String addnew(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "addnew";
    }

    @GetMapping(path = "/addcategory")
    public String addcategory(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "categories";
    }

    @RequestMapping(value = "/")
    public String cardController(@RequestParam(required = false) Long[] inputcategory, Model model) {

        var map = phrasesRepository.findAll();
        List<Phrases> found = new ArrayList<>();
        //filter by user
        User searchUser = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

//        map.forEach(phrases -> {
//            if (phrases.getUser() != searchUser) found.add(phrases);
//        });
        Iterator<Phrases> iterator = map.iterator();

        while (iterator.hasNext()) {
            if (iterator.next().getUser() != searchUser) iterator.remove();
        };


        List<Phrases> list = new ArrayList<>();
        map.iterator().forEachRemaining(x -> {
                    if (inputcategory == null || inputcategory.length == 0) {
                        list.add(x);
                    } else {

                        List<Category> catList = new ArrayList<>();
                        for (Long category : inputcategory) {
                            catList.add(categoryRepository.getReferenceById(category));
                        }
                        if (x.getCategory().size() != 0) {
                            catList.forEach(category -> {
                                if (x.getCategory().stream().anyMatch(category1 -> category1 == category)) {
                                    list.add(x);
                                    return;
                                }
                                ;

                            });

                        } else {
//                            list.add(x);
                        }
                    }

                }

//                if(x.)

//                list::add
        );
        if (list.isEmpty()) {
            String error = "Нет ничего с такой категорией Т_Т";
            model.addAttribute("message1", error);
            model.addAttribute("message2", error);
            model.addAttribute("categories", null);
            model.addAttribute("allCategories", categoryRepository.findAll());
            return "index";
        }
        var randomIndex = list.size() * Math.random();
        var el = list.get((int) randomIndex);
        model.addAttribute("message1", el.getEngName());
        model.addAttribute("message2", el.getRusName());

        //add category
        model.addAttribute("categories", el.getCategory());

        //structure for checked category
        Map<Category, Boolean> newMap = new HashMap<>();
        List<Category> catList = new ArrayList<>();
        List<Category> fullList = categoryRepository.findAll();
        if (inputcategory == null || inputcategory.length == 0) {
            fullList.forEach(category -> {
                newMap.put(category, true);
            });
        } else {
            fullList.forEach(category -> {

                newMap.put(category, Arrays.asList(inputcategory).indexOf(Long.valueOf(category.getId())) != -1);

            });
        }

        TreeMap<Category, Boolean> sorted = new TreeMap<>(newMap);

        model.addAttribute("checkedCategories", sorted);

        model.addAttribute("allCategories", categoryRepository.findAll());
        return "index";
    }

    @GetMapping(value = "/test")
    public String testController() {
        return "test";
    }
}
