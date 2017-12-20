package org.launchcode.controllers;


import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;



    @RequestMapping(value = "")
    public String index(Model model){

        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "Available Menus");
        return "menu/index";

    }

    @RequestMapping(value="add", method = RequestMethod.GET)
    public String displayAddMenuForm(Model model){

        model.addAttribute(new Menu());
        model.addAttribute("title", "Add Menu");

        return "menu/add";
    }

    @RequestMapping(value="add", method = RequestMethod.POST)
    public String processAddMenuForm(@ModelAttribute @Valid Menu newMenu, Errors errors, Model model){

        if(errors.hasErrors()){
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }

        menuDao.save(newMenu);
        return "redirect:view/" + newMenu.getId();
    }

    @RequestMapping(value = "view/{menuId}", method = RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable int menuId){

        model.addAttribute("menu", menuDao.findOne(menuId));


        return "menu/view";
    }


    @RequestMapping(value = "add-item/{menuId}", method = RequestMethod.GET)
    public String displayAddItemForm(Model model, @PathVariable int menuId){

        AddMenuItemForm form = new AddMenuItemForm(menuDao.findOne(menuId), cheeseDao.findAll());

        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("form", form);
        model.addAttribute("title", "Add item to menu: " + menuDao.findOne(menuId).getName());
        model.addAttribute("menu", menuDao.findOne(menuId));

        return "menu/add-item";
    }


    @RequestMapping(value = "add-item/{menuId}", method = RequestMethod.POST)
    public String processAddItemForm(Model model,
                                     @PathVariable int menuId,
                                     @ModelAttribute @Valid AddMenuItemForm form,
                                     Errors errors,
                                     @RequestParam int cheeseId){


        if(errors.hasErrors()){
            model.addAttribute("cheeses", cheeseDao.findAll());
            model.addAttribute("form", form);
            model.addAttribute("title", "Add item to menu: " + menuDao.findOne(menuId).getName());
            model.addAttribute("menu", menuDao.findOne(menuId));
            return "menu/add-item";
        }

    Cheese newItem = cheeseDao.findOne(cheeseId);
    Menu newMenu = menuDao.findOne(menuId);
    newMenu.addItem(newItem);
    menuDao.save(newMenu);

    model.addAttribute("menu", newMenu);
    model.addAttribute("menuId", menuId);
    return "menu/view";
    }


}
