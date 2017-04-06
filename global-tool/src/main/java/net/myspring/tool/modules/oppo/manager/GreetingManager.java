package net.myspring.tool.modules.oppo.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by liuj on 2017/4/6.
 */
@EnableBinding(Source.class)
@Component
public class GreetingManager {

    @Autowired
    @Output(Source.OUTPUT)
    private MessageChannel channel;

    public void greet() {
        channel.send(MessageBuilder.withPayload("hello world").build());
    }
}
