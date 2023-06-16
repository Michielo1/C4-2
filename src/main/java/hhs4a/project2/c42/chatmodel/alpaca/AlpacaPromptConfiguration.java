package hhs4a.project2.c42.chatmodel.alpaca;

import hhs4a.project2.c42.chatmodel.PromptConfiguration;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistory;
import hhs4a.project2.c42.utils.regex.EmojiFilter;
import hhs4a.project2.c42.utils.message.MessageInterface;

import java.util.*;

public class AlpacaPromptConfiguration extends PromptConfiguration {

    private static final int n_predict = 50;
    private static final int repeat_last_n = 64;
    private static final double repeat_penalty = 1.6;
    private static final int top_k = 40;
    private static final double top_p = 0.9;
    private static final double temp = 0.5;
    private static final int seed = 100;


    public AlpacaPromptConfiguration(ConversationHistory conversationHistory, int maxTokens, boolean supportsMultiThreading, int threadCounter) {
        super(conversationHistory, maxTokens, supportsMultiThreading, threadCounter);

        /*
            Prompt should be looking somewhat like

            User: ...
            Bot: ...
            User: ...

            Main important things:
            - user should always start
            - user should always end
         */

        StringBuilder builder = new StringBuilder();

        for (MessageInterface message : conversationHistory.getConversation()) {
            String text = EmojiFilter.removeEmojis(message.getText());
            if(conversationHistory.getChat().getLanguage().equalsIgnoreCase("Nederlands")){
                text = DeeplAPI.translate(text, "en");
            }
            if (builder.isEmpty() && message.isUser()) {
                // ensuring the first line is always user
                builder.append(message.getSender()).append(": ").append(text).append("\n");
            } else if (!builder.isEmpty()) {
                builder.append(message.getSender()).append(": ").append(text).append("\n");
            }
        }

        System.out.println("PROMPT: \n" + builder);
        super.setCompletePrompt(builder.toString());
    }

    public String getCompletePrompt() {
        /*
            Below is a dialog, where User interacts with AI. AI is helpful, kind, obedient, honest, and knows its own limits.  AI is an Alpaca chatmodel for 'Bedrijf 42'.

            Instruction
            Write the last AI response to complete the dialog.

            Dialog
            User: Hello, AI.
            AI: Hello! How can I assist you today?
            >PROMPT

            Response
            AI:
         */

        return "Below is a dialog, where User interacts with BOT. BOT is helpful, kind, obedient, honest, and knows its own limits. BOT is an Alpaca chatmodel for 'Bedrijf 42'. \n" +
                "\n" +
                " Instruction \n" +
                " Write the last BOT response to complete the dialog. \n" +
                "\n" +
                " Dialog \n" +
                " User: Hello, BOT. \n" +
                " BOT: Hello! How can I assist you today? \n" +
                super.getCompletePrompt() + "\n" +
                "\n" +
                " Response \n" +
                " BOT: ";
    }

    public List<Double> getWeights() {
        List<Double> weights = new ArrayList<>();
        weights.add((double)n_predict);
        weights.add((double)repeat_last_n);
        weights.add(repeat_penalty);
        weights.add((double)top_k);
        weights.add(top_p);
        weights.add(temp);
        weights.add((double)seed);
        return weights;
    }
}