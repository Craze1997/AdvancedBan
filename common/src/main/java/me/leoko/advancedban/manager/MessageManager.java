package me.leoko.advancedban.manager;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import me.leoko.advancedban.AdvancedBan;
import me.leoko.advancedban.AdvancedBanCommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Leoko @ dev.skamps.eu on 13.07.2016.
 */
@RequiredArgsConstructor
public class MessageManager {
    private final AdvancedBan advancedBan;

    private static String replace(String str, Object... parameters) {
        for (int i = 0; i < parameters.length; i += 2) {
            str = str.replaceAll("%" + parameters[i].toString() + '%', parameters[i + 1].toString());
        }
        return str;
    }

    public String getMessage(String path, Object... parameters) {
        JsonNode message = advancedBan.getMessages().getMessage(path);
        String str;
        if (message.isTextual()) {
            str = replace(message.textValue(), parameters).replace('&', '§');
        } else {
            str = "Failed! See console for details!";
            advancedBan.getLogger().warn("Unregistered message used. Please check Message.yml");
        }
        return str;
    }

    public List<String> getMessageList(String path, Object... parameters) {
        JsonNode messages = advancedBan.getMessages().getMessage(path);
        if (messages.isArray()) {
            List<String> messageList = new ArrayList<>();
            messages.forEach(element -> messageList.add(replace(element.textValue(), parameters).replace('&', '§')));
            return messageList;
        }
        advancedBan.getLogger().warn("Unregistered message used. Please check Message.yml");
        return Collections.emptyList();
    }

    public List<String> getLayout(String path, Object... parameters) {
        JsonNode layout = advancedBan.getLayouts().getLayout(path);
        if (layout.isArray()) {
            List<String> messages = new ArrayList<>();
            layout.forEach(element -> messages.add(replace(element.textValue(), parameters).replace('&', '§')));
            return messages;
        }
        advancedBan.getLogger().warn("Unregistered layout used. Please check Layouts.yml");
        return Collections.emptyList();
    }

    public void sendMessage(AdvancedBanCommandSender sender, String path, boolean prefix, Object[] parameters) {
        StringBuilder builder = new StringBuilder();
        if (prefix && !advancedBan.getConfiguration().isPrefixDisabled()) {
            builder.append(getMessage("General.Prefix"));
            builder.append(' ');
        }
        builder.append(getMessage(path, parameters));
        sender.sendMessage(builder.toString());
    }

    public String getPrefix() {
        return advancedBan.getConfiguration().isPrefixDisabled() ? "" : advancedBan.getMessageManager().getMessage("General.Prefix");
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public String getReasonOrDefault(Optional<String> reason) {
        return reason.orElse(advancedBan.getConfiguration().getDefaultReason());
    }
}
