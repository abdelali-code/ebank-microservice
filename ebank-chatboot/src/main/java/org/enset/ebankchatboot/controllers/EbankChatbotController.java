package org.enset.ebankchatboot.controllers;


import lombok.RequiredArgsConstructor;
import org.enset.ebankchatboot.agents.EbankAiAgent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EbankChatbotController {

    private final EbankAiAgent ebankAiAgent;

    @GetMapping("/chat")
    public String chat(@RequestParam(name = "query", defaultValue = "Bonjour") String query) {
        return ebankAiAgent.chat(query);
    }
}
