package com.ntu.shvydkov.emerancy_bot.handler.message;

import com.ntu.shvydkov.emerancy_bot.conditions.BotCondition;
import com.ntu.shvydkov.emerancy_bot.telegram.keyboard.ReplyKeyboardMarkupBuilder;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.ntu.shvydkov.emerancy_bot.handler.ChatBotUtils.setDialogQuestionWithReturnToHome;

@Component
public class DictionaryMessageHandler implements MessageHandler {
    private String blogUrl = "https://patrul.in.ua/dovidnyk/";
    private final transient Map<String, List<DictionaryElement>> dictionary = initDictionary();
    private List<String> keysToDisplay = Collections.emptyList();

    @SneakyThrows
    private Map<String, List<DictionaryElement>> initDictionary() {
        Document dictionaryObject = Jsoup.connect(blogUrl).get();
        LinkedHashMap<String, List<DictionaryElement>> dictionaryMap = new LinkedHashMap<>();
        Elements items = dictionaryObject.select(".su-spoiler");
        for (Element item : items) {
            DictionaryElement dictionaryElement = new DictionaryElement();

            //key
            String itemname = item.select(".su-spoiler-title").text();

            Elements itemContent = item.select(".su-spoiler-content");

            List<String> numbers = Collections.emptyList();
            List<String> captions = Collections.emptyList();

            ArrayList<DictionaryElement> dictionaryElements = new ArrayList<>();

            Elements kinds = itemContent.select("h5");
            if (kinds.isEmpty()) {
                //no kinds = plain phones.
                captions = itemContent.get(0).childNodes().stream()
                        .filter(it -> it instanceof TextNode || it.hasAttr("href"))
                        .map(Node::toString)
                        .filter(it -> !Objects.equals(it, " "))
                        .filter(it -> !Objects.equals(it, " – "))
                        .collect(Collectors.toList());

                List<NumberAndDescription> numberAndDescriptions = new ArrayList<>();
                if (kinds.isEmpty()) {
                    dictionaryElement.setKind(itemname);
                } else {
                    dictionaryElement.setKind(kinds.get(0).text());
                }
                numbers = itemContent.get(0).select("b").stream().map(Element::text).collect(Collectors.toList());
                for (int i = 0; i < numbers.size(); i++) {
                    numberAndDescriptions.add(new NumberAndDescription(numbers.get(i), captions.get(i)));
                }
                dictionaryElement.setNumberAndDescriptions(numberAndDescriptions);
                dictionaryMap.put(itemname, Arrays.asList(dictionaryElement));
            } else {
                Elements numsRaw = itemContent.select("p");

                for (int i = 0; i < kinds.size(); i++) {
                    dictionaryElement = new DictionaryElement();
//                for (Element element : numsRaw) {
                    Element element = numsRaw.get(i);
                    //set kind
                    List<NumberAndDescription> numberAndDescriptions = new ArrayList<>();
                    numbers = element.select("b").stream().map(Element::text).collect(Collectors.toList());

                    captions = element.childNodes().stream().filter(it -> it instanceof TextNode).map(Object::toString).filter(it -> !Objects.equals(it, " "))
                            .collect(Collectors.toList());
//                    set num+ capt
                    for (int j = 0; j < numbers.size(); j++) {
                        numberAndDescriptions.add(new NumberAndDescription(numbers.get(j), captions.get(j)));
                    }
                    dictionaryElement.setKind(kinds.get(i).text());
                    dictionaryElement.setNumberAndDescriptions(numberAndDescriptions);
                    dictionaryElements.add(dictionaryElement);
                    System.out.println("");
                }
                dictionaryMap.put(itemname, dictionaryElements);
            }
        }
        return dictionaryMap;
    }

    @Override
    public boolean canHandle(BotCondition botCondition) {
        return botCondition.equals(BotCondition.DICTIONARY);
    }

    @SneakyThrows
    @Override
    public BotApiMethod<? extends Serializable> handle(Message message, AbsSender absSender) {

        if (message.getText().equals("Довідник")) {

            //modify 102 default answere
            List<DictionaryElement> defaultAnswer = dictionary.get("Екстрені телефони");
            List<NumberAndDescription> defaultAnswers = defaultAnswer.get(0).getNumberAndDescriptions().stream()
                    .map(it -> {
                        if (it.number.equals("102")) {
                            return new NumberAndDescription(it.number, " - Поліція");
                        }
                        return it;
                    }).collect(Collectors.toList());

            dictionary.remove("Екстрені телефони");
            SendMessage method = new SendMessage(message.getChatId().toString(), displayDictionaryElement(new DictionaryElement("Екстрені телефони", defaultAnswers)));
            method.setParseMode("html");
            absSender.execute(method);

            ReplyKeyboardMarkupBuilder response = ReplyKeyboardMarkupBuilder.create(message.getChatId());

            List<String> keys = new ArrayList<>(dictionary.keySet());

            for (String dictKey : keys) {
                if (dictKey.contains("Екстрені телефони")) {
                    List<DictionaryElement> valueToUpdate = dictionary.get(dictKey);
                    dictionary.remove(dictKey);
                    dictionary.put(dictKey.replace("Екстрені телефони (", "").replace(")", ""), valueToUpdate);
                }
            }
            response.setChatId(message.getChatId());
            response.setText("Або ви можете обрати екстренні телефони за містом:");

            response.row();
            int cntr = 0;
            keysToDisplay = dictionary.keySet().stream().map(String::valueOf).collect(Collectors.toList());
            while (cntr < keysToDisplay.size()) {
                response.button(keysToDisplay.get(cntr));
                if (cntr % 2 == 0) {
                    response.endRow();
                    response.row();
                }
                cntr++;
            }
            response.endRow();

            setDialogQuestionWithReturnToHome("", response);
            return response.build();
        } else if (keysToDisplay.contains(message.getText())) {
            ReplyKeyboardMarkupBuilder response = ReplyKeyboardMarkupBuilder.create(message.getChatId());

            List<DictionaryElement> dictionaryElements = dictionary.get(message.getText());

            for (DictionaryElement dictionaryElement : dictionaryElements) {
                SendMessage method = new SendMessage(message.getChatId().toString(), displayDictionaryElement(dictionaryElement));
                method.setParseMode("html");
                absSender.execute(method);
            }
            return response.build();
        }
        return new SendMessage(message.getChatId().toString(), "Можливо, ти помилився.");
    }

    private String displayDictionaryElement(DictionaryElement dictionaryElement) {
        StringBuilder response = new StringBuilder();
        response
                .append(Character.toChars(0x2B55))
                .append(" ")
                .append("<b>")
                .append(dictionaryElement.getKind())
                .append("</b>")
                .append("\n\n");
        for (NumberAndDescription numberAndDescription : dictionaryElement.getNumberAndDescriptions()) {
            response
                    .append(Character.toChars(0x25B6))
                    .append(" ")
                    .append(numberAndDescription.number)
                    .append(" ")
                    .append(numberAndDescription.description)
                    .append("\n");
        }
        return response.toString();
    }
}
