package com.laioffer.twitch.item;

import com.laioffer.twitch.model.TypeGroupedItemList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/*the ItemController class is responsible for exposing an HTTP endpoint (/search) that accepts a "game_id" parameter.
When a request is made to this endpoint, the controller delegates the item search operation to the injected ItemService instance.
The controller is used to handle incoming HTTP requests and manage the interaction between the client and the ItemService
for item search functionality.*/
@RestController
public class ItemController {

    private final ItemService itemService;   //itemController depends on itemService

    public ItemController(ItemService itemService) {  //SpringBoot dependency injection 去new一个新的object出来用
        this.itemService = itemService;
    }

    /*
    This method is the actual endpoint handler for the "/search" URL. It takes a request parameter named "game_id"
    (provided in the URL) of type String. The return type of the method appears to be TypeGroupedItemList.*/
    @GetMapping("/search")
    public TypeGroupedItemList search(@RequestParam("game_id") String gameId) {
        return itemService.getItems(gameId);
    }
}