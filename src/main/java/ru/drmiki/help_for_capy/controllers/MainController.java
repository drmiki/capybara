package ru.drmiki.help_for_capy.controllers;

import jakarta.servlet.http.HttpSession;
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


    @GetMapping(path="/quiz")
    //public @ResponseBody List<Phrases> quiz(){
    public  String quiz(@RequestParam(required = false) String res, Model model){

        User serchUser = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (serchUser.getCountOfAllQuesitions() == null) serchUser.setCountOfAllQuesitions(0);
        if (serchUser.getCountOfRightQuestions() == null) serchUser.setCountOfRightQuestions(0);
        userRepository.save(serchUser);


        Integer newCount = serchUser.getCountOfAllQuesitions();
        Integer countOfRightQuestions = serchUser.getCountOfRightQuestions();




          //if (all == null) all ="sadasdasd";
//        if (all == null) all =Long.valueOf(0);
//           all = Long.valueOf(1);

//        System.out.println(all);


        //Adding 10 questions
        List<Phrases> listPhrases10 = new ArrayList<>();
        phrasesRepository.findAll().iterator().forEachRemaining(e -> listPhrases10.add(e));

        Phrases resultOfRandom = listPhrases10.get(new Random().nextInt(listPhrases10.size()));

        Map<Phrases, Boolean> question = new HashMap<>();

        listPhrases10.remove(resultOfRandom);
        question.put(resultOfRandom,true);

        for (Phrases phrase : listPhrases10) {
            if (question.size() > 3) break;
            question.put(phrase, false);
        }
        System.out.println(question);
        //return  listPhrases10;
        TreeMap<Phrases, Boolean> sorted = new TreeMap<>(question);
        List<Phrases> keys = new ArrayList(sorted.keySet());
        Collections.shuffle(keys);
        Map<Phrases, Boolean> shuffleMap = new LinkedHashMap<>();
        keys.forEach(k -> shuffleMap.put(k, sorted.get(k)));
        model.addAttribute("map",shuffleMap) ;
        model.addAttribute("all",newCount) ;
        model.addAttribute("counter",countOfRightQuestions) ;
        return "quiz";
    }

    @PostMapping(path = "/quiz")
    public String quizPost (@RequestParam(required = false) String res){

        //Initiation  elem of repo
        User serchUser = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        Integer newCount = serchUser.getCountOfAllQuesitions()+1;
        serchUser.setCountOfAllQuesitions(newCount);
        userRepository.save(serchUser);

        if (res != null && Boolean.valueOf(res)==true){
            if (serchUser.getCountOfRightQuestions() == null) serchUser.setCountOfRightQuestions(0);
            newCount = serchUser.getCountOfRightQuestions()+1;
            serchUser.setCountOfRightQuestions(newCount);
            userRepository.save(serchUser);
        }

        return "redirect:/quiz";
    }

    @PostMapping(path = "/add")
    public String addNewPhrases(@RequestParam String engName, @RequestParam String rusName, @RequestParam Long[] categories, Model model) {

        Phrases newPhrase = new Phrases();
        newPhrase.setEngName(engName);
        newPhrase.setRusName(rusName);
        List<Category> listCat = new ArrayList<>();
        for (Long category : categories) {
            listCat.add(categoryRepository.getReferenceById(category));
        }
        newPhrase.setCategory(listCat);
        User serchUser = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("categories", categoryRepository.findAll());
        //newPhrase.setUser((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        newPhrase.setUser(serchUser);
        phrasesRepository.save(newPhrase);
//        return "redirect:/";
        model.addAttribute("Message", "Новое слово добавлено!");
        return "addnew";
    }

    @PostMapping(path = "/addcategory")
    public String addNewCategory(@RequestParam String name,  Model model) {

        Category newCategory = new Category();
        newCategory.setName(name);
        categoryRepository.save(newCategory);
        model.addAttribute("Message", "Новое слово добавлено!");
        return "categories";

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
    public String cardController(@RequestParam(required = false) Long[] inputcategory, @RequestParam(required = false) Boolean showAll, Model model, HttpSession httpSession) {

        var map = phrasesRepository.findAll();

        //structure for checked category
        Map<Category, Boolean> newMap = new HashMap<>();

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
        //map is sorted by user

        if (showAll!=null && showAll){
            //list = new ArrayList<>();
            //list = (List<Phrases>) phrasesRepository.findAll();

            model.addAttribute("checkboxShowAllIsOn",true);
        }
        else {

            List<Phrases> found = new ArrayList<>();
            //filter by user
            User searchUser = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            Iterator<Phrases> iterator = map.iterator();

            while (iterator.hasNext()) {
                if (iterator.next().getUser() != searchUser) iterator.remove();
            };

            model.addAttribute("checkboxShowAllIsOn",false);
            List <Phrases> list = new ArrayList<>();
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
                                    };
                                });
                            } else {
                            }
                        }
                    }
            );
            if (list.isEmpty()) {
                String error = "Нет ничего с такой категорией Т_Т";
                model.addAttribute("message1", error);
                model.addAttribute("message2", error);
                model.addAttribute("categories", null);
                model.addAttribute("allCategories", categoryRepository.findAll());
                return "index";
            }else {
                var randomIndex = list.size() * Math.random();
                var el = list.get((int) randomIndex);
                model.addAttribute("message1", el.getEngName());
                model.addAttribute("message2", el.getRusName());
                //add category
                model.addAttribute("categories", el.getCategory());
                model.addAttribute("allCategories", categoryRepository.findAll());
                return "index";
            }
        }
        List <Phrases> list = new ArrayList<>();
        map.iterator().forEachRemaining(list::add);
        var randomIndex = list.size() * Math.random();
        var el = list.get((int) randomIndex);
        model.addAttribute("message1", el.getEngName());
        model.addAttribute("message2", el.getRusName());

        //add category
        model.addAttribute("categories", el.getCategory());



        model.addAttribute("allCategories", categoryRepository.findAll());
        return "index";
    }

    @GetMapping(value = "/test")
    public String testController() {
        return "test";
    }
}
