module kz.btsd.bot.scenario.sdk {
    requires spring.beans;
    requires kotlin.stdlib;
    requires spring.context;
    requires kotlinx.coroutines.core;
    requires transitive kz.btsd.bot.botsdk;
    requires bot.api.contract;
    requires kotlin.logging;
    requires com.fasterxml.jackson.module.kotlin;
    requires com.fasterxml.jackson.databind;
    requires okhttp3;
    requires spring.boot;
    requires spring.web;
    requires spring.boot.autoconfigure;
    requires com.fasterxml.jackson.core;
    requires java.annotation;
    uses kz.btsd.bot.botscenariosdk.Dispatcher;

    exports kz.btsd.bot.botscenariosdk;
    exports kz.btsd.bot.botscenariosdk.scenario;
    exports kz.btsd.bot.botscenariosdk.operations;
    exports kz.btsd.bot.botscenariosdk.config to spring.core, spring.beans, spring.context, kotlin.reflect;
    opens kz.btsd.bot.botscenariosdk;
    opens kz.btsd.bot.botscenariosdk.scenario;
    opens kz.btsd.bot.botscenariosdk.operations;
}
