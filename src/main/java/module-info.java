module hhs4a.project.c {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires jb;
    requires org.slf4j;
    requires org.apache.commons.lang3;
    requires org.apache.commons.io;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires webmagic.core;
    requires selenium.api;
    requires selenium.chrome.driver;
    requires webmagic.selenium;
    requires io.github.bonigarcia.webdrivermanager;
    requires java.sql;
    requires selenium.firefox.driver;
    requires selenium.edge.driver;
    requires msedge.selenium.tools.java;
    requires selenium.opera.driver;
    requires selenium.safari.driver;
    requires selenium.support;
    requires java.net.http;
    requires org.json;

    opens hhs4a.project2.c42 to javafx.fxml;
    exports hhs4a.project2.c42.chatmodel;
    exports hhs4a.project2.c42.driver;
    exports hhs4a.project2.c42;
    exports hhs4a.project2.c42.utils.chatscreen;
    exports hhs4a.project2.c42.chatmodel.alpaca;
    exports hhs4a.project2.c42.scenecontrollers;
    opens hhs4a.project2.c42.scenecontrollers to javafx.fxml;
    opens hhs4a.project2.c42.utils.chatscreen to javafx.fxml;
    exports hhs4a.project2.c42.utils.message;
    opens hhs4a.project2.c42.utils.message to javafx.fxml;
    exports hhs4a.project2.c42.utils.historyoverlay;
    opens hhs4a.project2.c42.utils.historyoverlay to javafx.fxml;
    exports hhs4a.project2.c42.utils.account;
    opens hhs4a.project2.c42.utils.account to javafx.fxml;
    exports hhs4a.project2.c42.enums;
    opens hhs4a.project2.c42.enums to javafx.fxml;
    exports hhs4a.project2.c42.utils.chatscreen.messagecell;
    opens hhs4a.project2.c42.utils.chatscreen.messagecell to javafx.fxml;
    exports hhs4a.project2.c42.utils.fx;
    opens hhs4a.project2.c42.utils.fx to javafx.fxml;

}