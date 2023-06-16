package hhs4a.project2.c42.chatmodel.alpaca;

import hhs4a.project2.c42.enums.DriverEnum;
import hhs4a.project2.c42.chatmodel.PromptConfiguration;
import hhs4a.project2.c42.chatmodel.PromptGenerator;
import hhs4a.project2.c42.driver.Driver;
import hhs4a.project2.c42.driver.DriverUtil;
import hhs4a.project2.c42.utils.account.LoggedInAccountHolder;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistoryHolder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.Select;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("BusyWait")
public class AlpacaPromptGenerator implements PromptGenerator {

    private boolean isRunning;
    private String lastOutput;
    private String prompt;
    private WebDriver webDriver;
    private long lastCancel;

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    // The synchronized keyword ensures that only one thread can execute this method at a time.
    // If a thread attempts to execute this method while another thread is already executing it,
    // it will block until the other thread is finished.
    @Override
    public synchronized String generatePrompt(PromptConfiguration config) {
        System.out.println("Setting up driver...");

        // check if any driver exists
        if (!DriverUtil.getInstance().hasCompatibleMethod()) {
            // attempt download
            DriverUtil.getInstance().downloadAnyDriver();
            // check if download worked, if not -> throw error
            if (!DriverUtil.getInstance().hasCompatibleMethod()) {
                return "ERROR! No compatible driver has been found.";
            }
        }

        System.out.println("Compatible driver found!");

        // get driver, using ternary operator to check if specific driver is available
        Driver specificDriver = null;
        if(LoggedInAccountHolder.getInstance().getAccount().getSettings().getDiverOption() != null && LoggedInAccountHolder.getInstance().getAccount().getSettings().getDiverOption() != DriverEnum.AUTOMATISCH) {
            specificDriver = DriverUtil.getInstance().useSpecificDriver(LoggedInAccountHolder.getInstance().getAccount().getSettings().getDiverOption());
        }
        Driver driver = specificDriver == null ? DriverUtil.getInstance().getAnyDriver() : specificDriver;

        return generatePrompt(config, driver);
    }

    @Override
    public String generatePrompt(PromptConfiguration config, Driver driver) {
        System.out.println("Generating alpaca prompt...");

        // running prompt, very bad, won't fix
        if (new Date(lastCancel).after(new Date(System.currentTimeMillis() - 4000))) {
            return "CANCELLED";
        } else {
            isRunning = true;
        }

        // set prompt
        prompt = config.getCompletePrompt();


        try {
            //setting up the driver
            setUpDriver(driver, config);

            // check if not running
            if (!isRunning) {
                return "CANCELLED";
            }

            //reading the output
            String output1 = readOutput();

            System.out.println("Calling janitor... where did he go??");
            System.out.println("Oh there he is! Cleaning up!");

            // clean up driver
            driver.destroy();

            if (!isRunning) {
                // cancelled
                return "CANCELLED";
            }

            if(detectError(output1)){
                return "ERROR! Could not generate an output. Please try again later!";
            }

            System.out.println("Processing output...");

            // split output
            assert output1 != null;
            String aiResponse = output1.split("BOT:")[output1.split("BOT:").length - 1];

            System.out.println("All done!");

            // translate if dutch is selected
            if(ConversationHistoryHolder.getInstance().getConversationHistory().getChat().getLanguage().equalsIgnoreCase("Nederlands")){
                System.out.println("Translating output...");
                return DeeplAPI.translate(aiResponse, "nl");
            }

            // return output
            return aiResponse;
        } catch (WebDriverException e) {
            System.out.println("Bot not available!");
            e.printStackTrace();
            return "ERROR! Could not generate an output. Please try again later!";
        } catch (Exception e) {
            // TODO handle error
            e.printStackTrace();
            return "ERROR! Could not generate an output. Please try again later!";
        } finally {
            isRunning = false;
        }
    }

    private void inputOptions(PromptConfiguration config) {
        // set config options
        List<String> options = new ArrayList<>();
        options.add("n_predict");
        options.add("repeat_last_n");
        options.add("repeat_penalty");
        options.add("top_k");
        options.add("top_p");
        options.add("temp");
        options.add("seed");

        for (int i = 0; i < options.size(); i++) {
            WebElement inputElement = webDriver.findElement(By.name(options.get(i)));
            inputElement.clear();
            inputElement.sendKeys(String.valueOf(config.getWeights().get(i)));
        }

        // set threads
        if (config.supportsMultiThreading() && config.getThreadCount() >= 1) {
            WebElement inputElement = webDriver.findElement(By.name("threads"));
            inputElement.clear();
            inputElement.sendKeys(String.valueOf(config.getThreadCount()));
        }

        System.out.println("Setting prompt...");

        // set prompt
        WebElement input = webDriver.findElement(By.id("input"));
        input.clear();
        // translate prompt into something useful
        // split the prompt at every \n
        String[] parts = prompt.split("\n");
        // use a StringBuilder to append the parts with Keys.ENTER
        StringBuilder sb = new StringBuilder();
        // loop through the parts
        for (int i = 0; i < parts.length; i++) {
            // append the part
            sb.append(parts[i]);
            // if it is not the last part, append Keys.ENTER
            if (i < parts.length - 1) {
                sb.append(Keys.ENTER);
            }
        }
        String newPrompt = sb.toString();
        input.sendKeys(newPrompt);
    }

    public void cancelPrompt() {
        try {
            WebElement button = webDriver.findElement(By.id("prompt-cancel"));
            button.click();
        } catch (NullPointerException ignored) { }
        isRunning = false;
        lastCancel = System.currentTimeMillis();
    }

    public String getLastOutput() {
        if (lastOutput != null) {
            String[] splitted = lastOutput.split("\n");
            int responseLine = -1;
            int botLine = -1;
            for (int i = 0; i < splitted.length; i++) {
                if (splitted[i].contains("RESPONSE")) {
                    responseLine = i;
                    continue;
                }
                if (splitted[i].contains("BOT:")) {
                    botLine = i;
                }
            }
            if (botLine > responseLine && !(responseLine == -1)) {
                return splitted[botLine].split("BOT:")[lastOutput.split("BOT:").length - 1];
            }
        }
        return "Loading...";
    }

    @Override
    public String getModelName() {
        return "ALPACA13B";
    }

    private void setUpDriver(Driver driver, PromptConfiguration config) {
        /*
                Setting up driver
             */

        webDriver = DriverUtil.getInstance().prepareAlpacaWebdriver(driver);

        System.out.println("Setting options...");

        // set model
        WebElement selectElement = webDriver.findElement(By.name("model"));
        Select select = new Select(selectElement);
        select.selectByValue("alpaca.13B");

        // set options
        inputOptions(config);

        WebElement button = webDriver.findElement(By.id("prompt-run"));
        button.click();

        System.out.println("Awaiting answer!");
    }

    private String readOutput(){
        /*
                Reading output
             */

        String output1;
        try {
            // initialize output and lastOutput to null
            output1 = null;
            this.lastOutput = null;
            // delay
            Thread.sleep(10000);
            // check if not running
            if (!isRunning) {
                return "CANCELLED";
            }
            // loop until output is not null and not equal to lastOutput
            while (isRunning) {
                // check if not running
                Thread.sleep(7000);
                if (!isRunning) {
                    return "CANCELLED";
                }
                // check if it matches last output
                // if it does, the AI isn't generating any more content and we can stop the repeating task
                String output = webDriver.findElement(By.cssSelector("li[data-id^='TS']")).getText();
                if (this.lastOutput != null) {
                    if (this.lastOutput.equalsIgnoreCase(output)) {
                        output1 = this.lastOutput;
                        break; // exit the loop
                    }
                }
                System.out.println("Current output: " + output);
                // update lastOutput with the current output
                this.lastOutput = output;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return output1;
    }

    private boolean detectError(String output1){
        // error detection
        if (output1 != null && !output1.contains("BOT:")) {
            System.out.println("Output is missing the bot response????");
            System.out.println("Current output: " + output1);
            return true;
        }
        return false;
    }

}
