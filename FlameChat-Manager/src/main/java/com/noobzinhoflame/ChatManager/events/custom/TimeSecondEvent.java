package com.noobzinhoflame.ChatManager.events.custom;

import com.noobzinhoflame.ChatManager.events.CustomEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TimeSecondEvent extends CustomEvent {

    private TimeType type;

    public enum TimeType {

        MILLISECONDS
    }
}
